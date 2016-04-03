package estivate;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import estivate.annotations.Convert;
import estivate.annotations.Optional;
import estivate.annotations.Select;
import estivate.core.ClassUtils;
import estivate.core.Converter;
import estivate.core.MembersFinder;
import estivate.core.Reducter;
import estivate.core.SelectEvaluater;
import estivate.core.Selecter;
import estivate.core.impl.DefaultConverter;
import estivate.core.impl.DefaultMembersFinder;
import estivate.core.impl.DefaultReducter;
import estivate.core.impl.DefaultSelecter;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * <ul>
 * <li>browse members and call tasks implementing speciifc interfaces</li>
 * <li>get members ordered to evaluate</li>
 * <li>evaluate select elements</li>
 * <li>evaluate reduce value</li>
 * <li>converts value</li>
 * <li>sets value to target</li>
 * </ul>
 * 
 * @author Benoit Theunissen
 *
 */
@Slf4j
public class EstivateMapper {

    @Getter
    @Setter
    protected String encoding = "UTF-8";

    @Getter
    @Setter
    protected String baseURI = "/";

    protected static final String PACKAGE_NAME = Select.class.getPackage()
            .getName();

    protected static final List<Class<?>> STANDARD_TARGET_TYPES = new ArrayList<>();
    static {
        STANDARD_TARGET_TYPES.add(Document.class);
        STANDARD_TARGET_TYPES.add(Elements.class);
        STANDARD_TARGET_TYPES.add(Element.class);
    }

    protected static MembersFinder membersFinder = new DefaultMembersFinder();
    protected static Selecter selecter = new DefaultSelecter();
    protected static Reducter reducter = new DefaultReducter();
    protected static Converter converter = new DefaultConverter();

    public <T> T map(InputStream document, Class<T> clazz) {
        Document parseDocument = parseDocument(document);
        return map(parseDocument, new Elements(parseDocument), clazz);
    }

    public <T> List<T> mapToList(InputStream document, Class<T> clazz) {
        Document parseDocument = parseDocument(document);
        return mapToList(parseDocument, new Elements(parseDocument), clazz);
    }

    /**
     * Entry Point of Recursive
     * 
     * @param document
     * @param elements
     * @param type
     * @return
     */
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

        Select selector = clazz.getAnnotation(Select.class);
        if (selector != null) {
            log.debug(selector.value());

            elementsCur = element.select(selector.value());
        }

        for (Element elt : elementsCur) {
            result.add(map(document, new Elements(elt), clazz));
        }

        return result;
    }

    public static <T> T map(Document document, Elements elements,
            Class<T> clazz) {
        try {

            Elements elementsCurr = elements;

            Select aSelect = clazz.getAnnotation(Select.class);
            if (aSelect != null) {
                elementsCurr = SelectEvaluater.select(aSelect, elements, clazz);
            } else {
                log.debug("no Select found, using root element");
            }

            return map(document, elementsCurr, clazz, clazz.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            log.error(e.getMessage(), e);

            throw new IllegalArgumentException(
                    "Cant create new instance of " + clazz.getCanonicalName(),
                    e);

        }
    }

    /**
     * Entry Point of core mapping.
     * 
     * @param document
     * @param element
     * @param clazz
     * @param target
     * @return
     */
    public static <T> T map(Document document, Elements element, Class<T> clazz,
            T target) {

        List<AccessibleObject> members = membersFinder.list(clazz);
        for (AccessibleObject member : members) {
            map(document, element, member, target);
        }

        return target;
    }

    protected static <T> void map(Document document, Elements elementsIn,
            AccessibleObject member, T target) {

        if (hasOneAnnotationMapper(member)) {

            // find all types arguments for method member
            Type[] valueTypes = getMemberTypes(member);

            Type valueTypeTarget = getValueTypeTarget(valueTypes);

            boolean isValueTypeList = checkListNature(valueTypeTarget);

            // select
            Elements elementsOut = selecter.select(document, elementsIn,
                    member);

            // reduce
            Object valueIn = reducter.reduce(document, elementsOut, member,
                    isValueTypeList);

            // Handle optional on member scope
            Boolean optional = false;

            Optional aOptional = member.getAnnotation(Optional.class);
            if (aOptional != null) {
                optional = aOptional.value();
            }
            if (!optional && valueIn == null) {
                throw new IllegalStateException(
                        "No value matched and not optional for "
                                + getName(member));
            }

            List<Object> valuesOut = new ArrayList<>();

            for (Type valueType : valueTypes) {

                Class<?> valueClass = ClassUtils.rawType(valueType);

                // find custom converter
                Converter customConverter = findCustomConverter(member);

                if (customConverter.canConvert(valueIn, valueClass)) {
                    // custom converter
                    log.debug("Convert with custom converter");
                    valuesOut.add(customConverter.convert(valueIn, valueClass));
                } else if (isStandardType(valueIn, valueClass)) {
                    // standard converter
                    log.debug("Convert with standard converter");
                    valuesOut.add(standardConverter(document, elementsOut,
                            valueIn, valueClass));
                } else if (converter.canConvert(valueIn, valueClass)) {
                    // inner converter
                    log.debug("Convert with inner converter");
                    valuesOut.add(converter.convert(valueIn, valueClass));
                } else {
                    // recursive converter (fallback)
                    log.debug("Convert by recursive mapping");
                    valuesOut.add(map(document, elementsOut, valueType));
                }
            }

            // set value to target
            setValueToTarget(document, elementsOut, target, member, valuesOut);
        }

    }

    private static Type getValueTypeTarget(Type[] valueTypes) {

        for (Type type : valueTypes) {
            if (!STANDARD_TARGET_TYPES.contains(type)) {
                return type;
            }
        }

        return null;
    }

    private static boolean checkListNature(Type valueType) {
        return valueType == null ? false
                : ClassUtils.rawType(valueType).equals(List.class);
    }

    private static Object standardConverter(Document document,
            Elements elementsIn, Object valueIn, Class<?> valueClass) {
        if (valueClass.equals(Document.class)) {
            return document;
        }
        if (valueClass.equals(Elements.class)) {
            return elementsIn;
        }
        if (valueClass.equals(Element.class)) {
            if (elementsIn.size() == 1) {
                return elementsIn.first();
            } else {
                throw new IllegalArgumentException(
                        "Cant set 'Element' object with Elements of size:"
                                + elementsIn.size());
            }
        }
        return null;
    }

    private static boolean isStandardType(Object valueIn, Class<?> valueClass) {
        return STANDARD_TARGET_TYPES.contains(valueClass);
    }

    private static <T> void setValueToTarget(Document document,
            Elements elementsIn, T target, AccessibleObject member,
            List<Object> valuesIn) {
        try {

            Object[] values = prepareArgumentValues(document, elementsIn,
                    member, valuesIn);

            if (member instanceof Field) {
                Field field = (Field) member;

                setValue(document, elementsIn, target, field, values[0]);

            } else if (member instanceof Method) {
                Method method = (Method) member;

                setValue(document, elementsIn, target, method, values);
            }

        } catch (IllegalArgumentException | IllegalAccessException
                | InvocationTargetException e) {
            throw new IllegalArgumentException("Cannot set values [" + valuesIn
                    + "] for member [" + getName(member) + "]", e);
        }

    }

    private static Object[] prepareArgumentValues(Document document,
            Elements elementsIn, AccessibleObject member,
            List<Object> valuesIn) {
        Type[] memberTypes = getMemberTypes(member);

        Object[] arguments = new Object[memberTypes.length];

        for (int i = 0; i < memberTypes.length; i++) {
            Type targetType = memberTypes[i];

            Class<?> targetRawType = ClassUtils.rawType(targetType);

            for (Object valueIn : valuesIn) {
                if (ClassUtils.isAssignableValue(targetRawType, valueIn)) {
                    arguments[i] = valueIn;
                }
            }
            // if [i] is already filled: next
            if (arguments[i] != null) {
                continue;
            }

            if (ClassUtils.isAssignableValue(targetRawType, elementsIn)) {
                arguments[i] = elementsIn;
            } else if (ClassUtils.isAssignableValue(targetRawType, document)) {
                arguments[i] = document;
            }
        }

        return arguments;
    }

    private static Converter findCustomConverter(AccessibleObject member) {

        Class<? extends Converter> custom = Converter.VOID.class;

        Convert aConvert = member.getAnnotation(Convert.class);
        if (aConvert != null) {
            custom = aConvert.value();
        }

        return ClassUtils.newInstance(custom);
    }

    private static boolean hasOneAnnotationMapper(AccessibleObject member) {
        Annotation[] annotations = member.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().getPackage().getName()
                    .equals(PACKAGE_NAME)) {
                return true;
            }
        }
        return false;
    }

    public static Type[] getMemberTypes(AccessibleObject member) {
        if (member instanceof Field) {
            Field field = (Field) member;

            return new Type[] { field.getGenericType() };

        } else if (member instanceof Method) {
            Method method = (Method) member;

            return method.getGenericParameterTypes();
        }
        return null;
    }

    protected Document parseDocument(InputStream document) {
        try {
            return Jsoup.parse(document, encoding, baseURI);
        } catch (IOException e) {
            throw new RuntimeException("Cant parse document.", e);
        }
    }

    /**
     * 
     * 
     * @param document
     * @param element
     * @param target
     * @param field
     * @param value
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    protected static void setValue(Document document, Elements element,
            Object target, Field field, Object value)
            throws IllegalArgumentException, IllegalAccessException {

        log.debug("set value on field ['{}' => '{}']", value, field.getName());

        if (ClassUtils.isAssignableValue(field.getType(), value)) {

            boolean accessibleBack = field.isAccessible();

            field.setAccessible(true);

            field.set(target, value);

            field.setAccessible(accessibleBack);

        } else {
            log.error("set value is not assignable with field '{}'",
                    field.getName());
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

    protected static Object getName(AnnotatedElement member) {
        return ClassUtils.getName(member);
    }
}
