package neomcfly.jsoupmapper;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.AccessibleObject;
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

    public <T> T map(InputStream body, Class<T> clazz) {

        return map(body, (Type) clazz);
    }

    public <T> List<T> mapToList(InputStream body, Class<T> class1) {
        return map(body, (Type) class1);
    }

    public <T> T map(InputStream body, Type type) {

        log.debug(type.getClass().getCanonicalName());

        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;

            // Handle type parameter class
            Class<?> classArgument = (Class<?>) parameterizedType
                    .getActualTypeArguments()[0];

            // Handle type class
            Class<?> rowClass = (Class<?>) parameterizedType.getRawType();

            if (Collection.class.isAssignableFrom(rowClass)) {

                Collection o = new ArrayList<Object>();

                o.addAll(parseList(body, classArgument));

                return (T) o;

            } else {

                log.debug(rowClass.getCanonicalName() + " is not a collection");

                throw new RuntimeException("Parameterized type not handled: "
                        + rowClass.getCanonicalName());
            }

        } else {

            Class<?> typeClass = (Class<?>) type;

            log.debug(typeClass.getCanonicalName());

            return (T) parseElement(body, typeClass);

        }

    }

    protected <T> T parseElement(InputStream in, Class<T> classArgument) {
        try {
            Document parse = Jsoup.parse(in, "UTF8", "/");

            return map(parse.body(), classArgument);

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    protected Collection parseList(InputStream in, Class<?> classArgument) {

        try {

            Document parse = Jsoup.parse(in, encoding, "/");

            log.debug(parse.toString());

            JSoupListSelect selector = classArgument
                    .getAnnotation(JSoupListSelect.class);

            log.debug(selector.value());

            Collection result = new ArrayList();

            Elements select = parse.select(selector.value());
            for (Element element : select) {

                result.add(map(element, classArgument));
            }

            return result;

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    protected <T> T map(Element element, Class<T> classTarget) {
        try {
            return (T) map(element, classTarget.newInstance());
        } catch (InstantiationException e) {
            log.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    protected Object map(Element element, Object target)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {

        List<Field> allFields = getAllFields(target.getClass());
        for (Field field : allFields) {
            evaluate(element, field, target);
        }

        Method[] methods = target.getClass().getMethods();
        for (Method method : methods) {
            evaluate(element, method, target);
        }

        return target;
    }

    protected void evaluate(Element source, AccessibleObject accessibleObject,
            Object target) throws IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {

        Object value = null;

        // Element JSoup en cours de parcours
        Element currentElement = source;
        Boolean optional = false;

        JSoupOptional optionalAnnotation = accessibleObject
                .getAnnotation(JSoupOptional.class);
        if (optionalAnnotation != null) {
            optional = optionalAnnotation.value();
        }

        JSoupSelect selector = accessibleObject
                .getAnnotation(JSoupSelect.class);
        if (selector != null) {
            log.debug("{}, select [{},{}]", getName(accessibleObject),
                    source.nodeName(), selector.value());

            Elements select = source.select(selector.value());

            log.debug("{}, select [{},{} => {}]", getName(accessibleObject),
                    source.nodeName(), selector.value(), select.size());

            currentElement = select.first();

            if (!select.isEmpty()) {
                log.debug("{}, select [{},{} => {}]", getName(accessibleObject),
                        source.nodeName(), selector.value(),
                        currentElement.nodeName());
            }
        }

        if (currentElement == null && !optional) {

            log.debug("{}]", source.toString());

            throw new RuntimeException(
                    "Select returns none for non optional field: "
                            + getName(accessibleObject));
        }

        if (currentElement == null) {
            return;
        }

        JSoupAttr attr = accessibleObject.getAnnotation(JSoupAttr.class);
        if (attr != null) {

            log.debug("{} attr", getName(accessibleObject));

            value = currentElement.attr(attr.value());
        }

        JSoupText text = accessibleObject.getAnnotation(JSoupText.class);
        if (text != null) {

            log.debug("{} text", getName(accessibleObject));

            value = currentElement.text();
        }

        if (value != null) {
            setValue(target, accessibleObject, value);
        }

    }

    protected Object getName(AccessibleObject accessibleObject) {
        if (accessibleObject instanceof Member) {
            return ((Member) accessibleObject).getName();
        }
        return "__unknown__";
    }

    protected void setValue(Object target, AccessibleObject accessibleObject,
            Object value) throws IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {
        if (accessibleObject instanceof Field) {
            setValue(target, (Field) accessibleObject, value);
        } else if (accessibleObject instanceof Method) {
            setValue(target, (Method) accessibleObject, value);
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

    public static void setValue(Object target, Field field, Object value)
            throws IllegalArgumentException, IllegalAccessException {

        log.debug("set value [{} => {} ]", value, field.getName());

        field.setAccessible(true);

        field.set(target, value);
    }

    public static void setValue(Object target, Method method, Object value)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {

        log.debug("set value [{}({}) ]", method.getName(), value);

        method.invoke(target, value);
    }

}
