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
import org.jsoup.nodes.Document;
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
        Document parseDocument = parseDocument(document);
        return map(parseDocument, new Elements(parseDocument), type);
    }

    public <T> List<T> mapToList(InputStream document, Class<T> clazz) {
        Document parseDocument = parseDocument(document);
        return mapToList(parseDocument, new Elements(parseDocument), clazz);
    }

    public <T> T map(InputStream document, Class<T> clazz) {
        Document parseDocument = parseDocument(document);
        return map(parseDocument, new Elements(parseDocument), clazz);
    }

    public Object map(Document document, Elements elements, Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;

            // Handle type parameter class
            Class<?> classArgument = (Class<?>) parameterizedType
                    .getActualTypeArguments()[0];

            // Handle type class
            Class<?> rowClass = (Class<?>) parameterizedType.getRawType();

            if (Collection.class.isAssignableFrom(rowClass)) {

                return mapToList(document, elements, classArgument);

            } else {
                log.debug(rowClass.getCanonicalName() + " is not a collection");

                throw new IllegalArgumentException(
                        "Parameterized type not handled: "
                                + rowClass.getCanonicalName());
            }
        } else {
            return map(document, elements, (Class<?>) type);
        }
    }

    public <T> List<T> mapToList(Document document, Elements element,
            Class<T> clazz) {
        List<T> result = new ArrayList<T>();

        Elements elementsCur = element;

        log.debug(element.toString());

        JSoupListSelect selector = clazz.getAnnotation(JSoupListSelect.class);
        if (selector != null) {
            log.debug(selector.value());

            elementsCur = element.select(selector.value());
        }

        for (Element elt : elementsCur) {
            result.add(map(document, new Elements(elt), clazz));
        }

        return result;
    }

    public <T> T map(Document document, Elements element, Class<T> clazz) {
        try {
            Elements elementsCur = select(document, element, clazz);

            return map(document, elementsCur, clazz, clazz.newInstance());
        } catch (InstantiationException e) {
            log.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public <T> T map(Document document, Elements element, Class<T> clazz,
            T target) {

        List<Field> allFields = getAllFields(target.getClass());
        for (Field field : allFields) {
            map(document, element, field, target);
        }

        Method[] methods = target.getClass().getMethods();
        for (Method method : methods) {
            map(document, element, method, target);
        }

        return target;
    }

    private <T> void map(Document document, Elements element,
            AccessibleObject member, T target) {

        if (hasOneAnnotationMapper(member)) {
            Boolean optional = false;

            JSoupOptional aOptional = member.getAnnotation(JSoupOptional.class);
            if (aOptional != null) {
                optional = aOptional.value();
            }

            Elements elementsSelected = select(document, element, member);

            Object value = reduce(document, elementsSelected, member);

            if (!optional && value == null) {
                throw new IllegalStateException(
                        "No value matched and not optional for "
                                + getName(member));
            } else {

                setValue(document, elementsSelected, target, member, value);
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
    private Elements select(Document document, Elements elements,
            AnnotatedElement member) {

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
    private Object reduce(Document document, Elements element,
            AnnotatedElement member) {

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

        JSoupTagName aTagName = member.getAnnotation(JSoupTagName.class);
        if (aTagName != null) {

            log.debug("{} tagName", getName(member));

            log.debug("using tagName()", getName(member));

            value = element.first().tagName();
        }

        JSoupTitle aTitle = member.getAnnotation(JSoupTitle.class);
        if (aTitle != null) {

            log.debug("{} title", getName(member));

            log.debug("using title()", getName(member));

            value = document.title();
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

    protected void setValue(Document document, Elements element, Object target,
            Field field, Object value)
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

            setValue(document, element, target, field,
                    map(document, element, field.getGenericType()));
        }

    }

    protected void setValue(Document document, Elements element, Object target,
            Method method, Object value) throws IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {

        log.debug("set value by method [{}({}) ]", method.getName(), value);

        method.invoke(target, value);
    }

    protected void setValue(Document document, Elements element, Object target,
            AccessibleObject member, Object value) {
        try {
            if (member instanceof Field) {
                setValue(document, element, target, (Field) member, value);
            } else if (member instanceof Method) {
                setValue(document, element, target, (Method) member, value);
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

    protected Document parseDocument(InputStream document) {
        try {
            return Jsoup.parse(document, encoding, baseURI);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

}
