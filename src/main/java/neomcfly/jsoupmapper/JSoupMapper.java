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
import neomcfly.jsoupmapper.core.ClassUtils;
import neomcfly.jsoupmapper.core.StandardTypeConvertor;
import neomcfly.jsoupmapper.core.StringIntegerConvertor;
import neomcfly.jsoupmapper.core.TypeConvertor;

@Slf4j
public class JSoupMapper {

    @Getter
    @Setter
    protected String encoding = "UTF-8";

    @Getter
    @Setter
    protected String baseURI = "/";

    protected static final String PACKAGE_NAME = JSoupSelect.class.getPackage()
            .getName();

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

    public static Object map(Document document, Elements elements, Type type) {
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

    public static <T> List<T> mapToList(Document document, Elements element,
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

    public static <T> T map(Document document, Elements element,
            Class<T> clazz) {
        try {
            Elements elementsCur = select(document, element, clazz);

            return map(document, elementsCur, clazz, clazz.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            log.error(e.getMessage(), e);

            throw new IllegalArgumentException(
                    "Cant create new instance of " + clazz.getCanonicalName(),
                    e);

        }
    }

    public static <T> T map(Document document, Elements element, Class<T> clazz,
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

    private static <T> void map(Document document, Elements elements,
            AccessibleObject member, T target) {

        if (hasOneAnnotationMapper(member)) {
            Boolean optional = false;

            JSoupOptional aOptional = member.getAnnotation(JSoupOptional.class);
            if (aOptional != null) {
                optional = aOptional.value();
            }

            Elements elementsSelected = select(document, elements, member);

            Object value = reduce(document, elementsSelected, member);

            if (!optional && value == null) {
                throw new IllegalStateException(
                        "No value matched and not optional for "
                                + getName(member));
            } else {
                mapFieldOrArgument(document, elementsSelected, target, member,
                        value);
            }
        }

    }

    private static boolean hasOneAnnotationMapper(AccessibleObject member) {
        Annotation[] annotations = member.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().getCanonicalName()
                    .startsWith(PACKAGE_NAME)) {
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
    private static Elements select(Document document, Elements elements,
            AnnotatedElement member) {

        Elements elementsCurr = elements;

        JSoupSelect aSelect = member.getAnnotation(JSoupSelect.class);
        if (aSelect != null) {
            log.debug("select '{}' for '{}'", aSelect.value(), getName(member));
            log.trace("select in  '{}'", elementsCurr.toString());
            elementsCurr = elements.select(aSelect.value());
            log.trace("select out '{}'", elementsCurr.toString());
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
    private static Object reduce(Document document, Elements elements,
            AnnotatedElement member) {

        Object value = elements;

        JSoupAttr aAttr = member.getAnnotation(JSoupAttr.class);
        if (aAttr != null) {

            log.debug("'{}' attr", getName(member));

            log.debug("using attr()", getName(member));

            value = elements.attr(aAttr.value());
        }

        JSoupVal aVal = member.getAnnotation(JSoupVal.class);
        if (aVal != null) {

            log.debug("'{}' val", getName(member));

            log.debug("using val()", getName(member));

            value = elements.val();
        }

        JSoupTagName aTagName = member.getAnnotation(JSoupTagName.class);
        if (aTagName != null) {

            log.debug("'{}' tagName", getName(member));

            log.debug("using tagName()", getName(member));

            value = elements.first().tagName();
        }

        JSoupTitle aTitle = member.getAnnotation(JSoupTitle.class);
        if (aTitle != null) {

            log.debug("'{}' title", getName(member));

            log.debug("using title()", getName(member));

            value = document.title();
        }

        JSoupText aText = member.getAnnotation(JSoupText.class);
        if (aText != null) {
            log.debug("'{}' text", getName(member));

            log.trace("text in  '{}'", elements);

            if (aText.value() || aText.own()) {
                log.debug("using first().owntext()");
                value = elements.first().ownText();
            } else {
                log.debug("using text()");
                value = elements.text();
            }

            log.trace("text out  '{}'", value);
        }

        return value;
    }

    protected static Object getName(AnnotatedElement member) {
        if (member instanceof Member) {
            return ((Member) member).getName();
        }
        if (member instanceof Class) {
            return ((Class<?>) member).getSimpleName();
        }
        return "__unknown__";
    }

    protected static void mapFieldOrArgument(Document document,
            Elements elements, Object target, AccessibleObject member,
            Object value) {
        try {
            // Get target Type of
            // - Field
            // - Method arguments
            // Evaluate target type, trigger recursive evaluation if necessary
            // set the value to the target
            if (member instanceof Field) {
                Field field = (Field) member;

                Type fieldType = field.getGenericType();

                Object values = evaluateArguments(document, elements, value,
                        fieldType)[0];

                setValue(document, elements, target, field, values);

            } else if (member instanceof Method) {
                Method method = (Method) member;

                Type[] parameterTypes = method.getGenericParameterTypes();

                Object[] values = evaluateArguments(document, elements, value,
                        parameterTypes);

                setValue(document, elements, target, method, values);
            }

        } catch (IllegalArgumentException | IllegalAccessException
                | InvocationTargetException e) {
            throw new IllegalArgumentException("Cannot set value [" + value
                    + "] for [" + getName(member) + "]", e);
        }

    }

    public static final List<TypeConvertor<?, ?>> convertors = new ArrayList<>();
    static {
        convertors.add(new StringIntegerConvertor());
        convertors.add(new StandardTypeConvertor());
        convertors.add(new RecursiveMappingTypeConvertor());
    }

    protected static Object[] evaluateArguments(Document document,
            Elements elements, Object value, Type... argumentsType) {
        Object[] arguments = new Object[argumentsType.length];

        for (int i = 0; i < argumentsType.length; i++) {
            Type argumentType = argumentsType[i];

            for (TypeConvertor<?, ?> convertor : convertors) {
                if (convertor.canHandle(value.getClass(), argumentType)) {

                    arguments[i] = convertor.convert(document, elements, value,
                            argumentType);
                    break;
                }
            }
        }

        return arguments;
    }

    /**
     * Prepares futures method arguments (or field value) depending of ordered
     * expected types.
     * 
     * @param document
     * @param elements
     * @param value
     *            The current value of the DOM after selects and reduces
     * @param argumentsType
     * 
     * @return Arguments ordered giving method signature aka <code>argumentsType
     *         <code>
     */
    protected static Object[] evaluateArgumentsOLD(Document document,
            Elements elements, Object value, Type... argumentsType) {
        Object[] arguments = new Object[argumentsType.length];

        for (int i = 0; i < argumentsType.length; i++) {
            Type argumentType = argumentsType[i];

            if (ClassUtils.isAssignableFrom(argumentType, value.getClass())) {
                arguments[i] = value;
            } else if (ClassUtils.isAssignableFrom(argumentType,
                    Elements.class)) {
                arguments[i] = elements;
            } else if (ClassUtils.isAssignableFrom(argumentType,
                    Element.class)) {
                if (elements.size() == 1) {
                    arguments[i] = elements.first();
                } else {
                    throw new IllegalArgumentException(
                            "Cant set 'Element' object with Elements of size:"
                                    + elements.size());
                }
            } else if (ClassUtils.isAssignableFrom(argumentType,
                    Document.class)) {
                arguments[i] = document;
            } else {
                log.debug("evaluate argument by recursive mapping");
                arguments[i] = map(document, elements, argumentType);
            }
        }
        return arguments;
    }

    protected static void setValue(Document document, Elements element,
            Object target, Field field, Object value)
            throws IllegalArgumentException, IllegalAccessException {

        log.debug("set value on field ['{}' => '{}']", value, field.getName());

        // If both types matchs
        if (ClassUtils.isAssignableFrom(field.getType(), value.getClass())) {

            // TODO call setter first if exists
            field.setAccessible(true);

            field.set(target, value);

        } else {
            log.error("set value is not assignable with field");
            throw new IllegalArgumentException(
                    "Cant set " + value.toString() + " to " + field.getName());
        }

    }

    protected static void setValue(Document document, Elements element,
            Object target, Method method, Object... values)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {

        log.debug("set value by method [{} ({})]", method.getName(), values);

        method.invoke(target, values);
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

    @SuppressWarnings("rawtypes")
    public static class RecursiveMappingTypeConvertor implements TypeConvertor {

        @Override
        public boolean canHandle(Type from, Type to) {
            return true;
        }

        @Override
        public Object convert(Document document, Elements elements,
                Object value, Type targetType) {

            log.debug("evaluate argument by recursive mapping");
            return map(document, elements, targetType);
        }

    }
}
