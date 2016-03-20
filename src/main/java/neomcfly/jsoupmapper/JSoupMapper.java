package neomcfly.jsoupmapper;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JSoupMapper extends JSoupMapperOLD {

    @Getter
    @Setter
    protected String encoding = "UTF-8";

    @Getter
    @Setter
    protected String baseURI = "/";

    @Override
    public Object map(InputStream document, Type type) {
        return map(parseDocument(document), type);
    }

    public <T> List<T> mapToList(InputStream document, Class<T> clazz) {
        return mapToList(parseDocument(document), clazz);
    }

    public <T> T map(InputStream document, Class<T> clazz) {
        return map(parseDocument(document), clazz);
    }

    public Object map(Element document, Type type) {
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

    public <T> List<T> mapToList(Element element, Class<T> clazz) {
        List<T> result = new ArrayList<T>();

        Elements elementsCur = new Elements(element);

        log.debug(element.toString());

        JSoupListSelect selector = clazz.getAnnotation(JSoupListSelect.class);
        if (selector != null) {
            log.debug(selector.value());

            elementsCur = element.select(selector.value());
        }

        for (Element elt : elementsCur) {
            result.add(map(elt, clazz));
        }

        return result;
    }

    @Override
    public <T> T map(Element element, Class<T> clazz) {
        try {
            return map(element, clazz, clazz.newInstance());
        } catch (InstantiationException e) {
            log.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public <T> T map(Element element, Class<T> clazz, T target) {

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

    private <T> void map(Element element, AccessibleObject member, T target) {

        if (hasOneAnnotationMapper(member)) {
            Boolean optional = false;

            JSoupOptional aOptional = member.getAnnotation(JSoupOptional.class);
            if (aOptional != null) {
                optional = aOptional.value();
            }

            // FIXME Handle here member of collection type
            Element elementSelected = select(element, member).first();

            Object value = reduce(elementSelected, member);

            if (!optional && value == null) {
                throw new IllegalStateException(
                        "No value matched and not optional for "
                                + getName(member));
            } else {
                setValue(elementSelected, target, member, value);
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
    private Elements select(Element element, AccessibleObject member) {

        JSoupSelect selector = member.getAnnotation(JSoupSelect.class);
        if (selector == null) {
            log.debug("no JSoupSelect found, using root element");
            return new Elements(element);
        }

        log.debug("{} select {}", getName(member), selector.value());

        return element.select(selector.value());
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
    private Object reduce(Element element, AccessibleObject member) {

        Object value = element;

        JSoupAttr aAttr = member.getAnnotation(JSoupAttr.class);
        if (aAttr != null) {

            log.debug("{} attr", getName(member));

            log.debug("using attr()", getName(member));

            value = element.attr(aAttr.value());
        }

        JSoupText aText = member.getAnnotation(JSoupText.class);
        if (aText != null) {
            log.debug("{} text", getName(member));

            if (aText.value() || aText.own()) {
                log.debug("using owntext()");
                value = element.ownText();
            } else {
                log.debug("using text()");
                value = element.text();
            }
        }

        // TODO throw exeption if not optional and no result
        return value;
    }

    protected void setValue(Element element, Object target, Field field,
            Object value)
            throws IllegalArgumentException, IllegalAccessException {

        log.debug("set value by field [{} => {} ]", value, field.getName());

        if (field.getType().isAssignableFrom(value.getClass())) {
            // TODO call setter first if exists
            field.setAccessible(true);

            field.set(target, value);
        } else {
            setValue(element, target, field,
                    map(element, field.getGenericType()));
        }

    }

    protected void setValue(Element element, Object target, Method method,
            Object value) throws IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {

        log.debug("set value by method [{}({}) ]", method.getName(), value);

        method.invoke(target, value);
    }

    protected void setValue(Element element, Object target,
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

    protected Element parseDocument(InputStream document) {
        try {
            return Jsoup.parse(document, encoding, baseURI);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

}
