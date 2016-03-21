package neomcfly.jsoupmapper;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JSoupMapper {

    @Getter
    @Setter
    protected String encoding = "UTF-8";

    @Getter
    @Setter
    protected String baseURI = "/";

    public Object map(InputStream document, Type type) {
        return map(parseDocument(document), type);
    }

    public <T> List<T> mapToList(InputStream document, Class<T> clazz) {
        return mapToList(parseDocument(document), clazz);
    }

    public <T> T map(InputStream document, Class<T> clazz) {
        return map(parseDocument(document), clazz);
    }

    public Object map(Elements document, Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;

            // Handle type parameter class
            Class<?> classArgument = (Class<?>) parameterizedType
                    .getActualTypeArguments()[0];

            // Handle type class
            Class<?> rowClass = (Class<?>) parameterizedType.getRawType();

            if (Collection.class.isAssignableFrom(rowClass)) {

                return mapToList(document, classArgument);

            } else {
                log.debug(rowClass.getCanonicalName() + " is not a collection");

                throw new IllegalArgumentException(
                        "Parameterized type not handled: "
                                + rowClass.getCanonicalName());
            }
        } else {
            return map(document, (Class<?>) type);
        }
    }

    public <T> List<T> mapToList(Elements element, Class<T> clazz) {
        List<T> result = new ArrayList<T>();

        Elements elementsCur = element;

        log.debug(element.toString());

        JSoupListSelect selector = clazz.getAnnotation(JSoupListSelect.class);
        if (selector != null) {
            log.debug(selector.value());

            elementsCur = element.select(selector.value());
        }

        for (Element elt : elementsCur) {
            result.add(map(new Elements(elt), clazz));
        }

        return result;
    }

    public <T> T map(Elements element, Class<T> clazz) {
        try {
            Elements elementsCur = select(element, clazz);

            return map(elementsCur, clazz, clazz.newInstance());
        } catch (InstantiationException e) {
            log.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public <T> T map(Elements element, Class<T> clazz, T target) {

        List<Field> allFields = getAllFields(target.getClass());
        for (Field field : allFields) {
            map(element, field, target);
        }

        Method[] methods = target.getClass().getMethods();
        for (Method method : methods) {
            map(element, method, target);
        }

        return target;
    }

    private <T> void map(Elements element, AccessibleObject member, T target) {

        if (hasOneAnnotationMapper(member)) {
            Boolean optional = false;

            JSoupOptional aOptional = member.getAnnotation(JSoupOptional.class);
            if (aOptional != null) {
                optional = aOptional.value();
            }

            // FIXME Handle here member of collection type
            Elements elementsSelected = select(element, member);

            Object value = reduce(elementsSelected, member);

            if (!optional && value == null) {
                throw new IllegalStateException(
                        "No value matched and not optional for "
                                + getName(member));
            } else {

                setValue(elementsSelected, target, member, value);
            }
        }

    }

    private boolean hasOneAnnotationMapper(AccessibleObject member) {
        Annotation[] annotations = member.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().getCanonicalName()
                    .startsWith("neomcfly.jsoupmapper")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Apply all rules (annotations) of type select
     * 
     * @see JSoupSelect
     * 
     * @param element
     * @param member
     * @return
     */
    private Elements select(Elements elements, AnnotatedElement member) {

        Elements elementsCurr = elements;

        JSoupSelect aSelect = member.getAnnotation(JSoupSelect.class);
        if (aSelect != null) {
            log.debug("{} select {}", getName(member), aSelect.value());
            elementsCurr = elements.select(aSelect.value());
        } else {
            log.debug("no JSoupSelect found, using root element");
        }

        JSoupFirst aFirst = member.getAnnotation(JSoupFirst.class);
        if (aFirst != null) {
            elementsCurr = new Elements(elementsCurr.first());
        }

        JSoupLast aLast = member.getAnnotation(JSoupLast.class);
        if (aLast != null) {
            elementsCurr = new Elements(elementsCurr.last());
        }

        return elementsCurr;
    }

    /**
     * Apply all rules (annotations) of type reduce.
     * 
     * @see JSoupAttr
     * @see JSoupText
     * @see JSoupTitle
     * @see JSoupTagName
     * @see JSoupVal
     * 
     * @param elementSelected
     * @param member
     * @return
     */
    private Object reduce(Elements element, AnnotatedElement member) {

        Object value = element.first();

        JSoupAttr aAttr = member.getAnnotation(JSoupAttr.class);
        if (aAttr != null) {

            log.debug("{} attr", getName(member));

            log.debug("using attr()", getName(member));

            value = element.attr(aAttr.value());
        }

        JSoupVal aVal = member.getAnnotation(JSoupVal.class);
        if (aVal != null) {

            log.debug("{} val", getName(member));

            log.debug("using val()", getName(member));

            value = element.val();
        }

        JSoupText aText = member.getAnnotation(JSoupText.class);
        if (aText != null) {
            log.debug("{} text", getName(member));

            if (aText.value() || aText.own()) {
                log.debug("using first().owntext()");
                value = element.first().ownText();
            } else {
                log.debug("using text()");
                value = element.text();
            }
        }

        return value;
    }

    protected Object getName(AnnotatedElement member) {
        if (member instanceof Member) {
            return ((Member) member).getName();
        }
        if (member instanceof Class) {
            return ((Class<?>) member).getSimpleName();
        }
        return "__unknown__";
    }

    protected void setValue(Elements element, Object target, Field field,
            Object value)
            throws IllegalArgumentException, IllegalAccessException {

        log.debug("set value on field [{} => {} ]", value, field.getName());

        // If both types matchs
        if (field.getType().isAssignableFrom(value.getClass())) {

            log.debug("set value by field");

            // TODO call setter first if exists
            field.setAccessible(true);

            field.set(target, value);

        } else {

            log.debug("set value by recursive mapping");

            setValue(element, target, field,
                    map(element, field.getGenericType()));
        }

    }

    protected void setValue(Elements element, Object target, Method method,
            Object value) throws IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {

        log.debug("set value by method [{}({}) ]", method.getName(), value);

        method.invoke(target, value);
    }

    protected void setValue(Elements element, Object target,
            AccessibleObject member, Object value) {
        try {
            if (member instanceof Field) {
                setValue(element, target, (Field) member, value);
            } else if (member instanceof Method) {
                setValue(element, target, (Method) member, value);
            }
        } catch (IllegalArgumentException | IllegalAccessException
                | InvocationTargetException e) {
            throw new IllegalArgumentException("Cannot set value [" + value
                    + "] for [" + getName(member) + "]", e);
        }

    }

    public static List<Field> getAllFields(Class<?> clazz) {

        List<Field> res = new ArrayList<Field>();

        Class<?> index = clazz;
        while (index != Object.class) {
            res.addAll(Arrays.asList(index.getDeclaredFields()));

            index = index.getSuperclass();

        }

        return res;
    }

    protected Elements parseDocument(InputStream document) {
        try {
            return new Elements(Jsoup.parse(document, encoding, baseURI));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

}
