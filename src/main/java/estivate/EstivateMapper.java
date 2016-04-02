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
import estivate.core.Convertor;
import estivate.core.ConvertorContext;
import estivate.core.MembersFinder;
import estivate.core.PrimitiveTypeConvertor;
import estivate.core.Reductor;
import estivate.core.SelectEvaluator;
import estivate.core.Selector;
import estivate.core.StandardTypeConvertor;
import estivate.core.impl.DefaultConvertor;
import estivate.core.impl.DefaultMembersFinder;
import estivate.core.impl.DefaultReductor;
import estivate.core.impl.DefaultSelector;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * <li>browse members and call tasks implementing speciifc interfaces
 * <li>get members ordered to evaluate
 * <li>evaluate select elements
 * <li>evaluate reduce value
 * <li>converts value
 * <li>sets value to target
 * 
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

    protected static MembersFinder membersFinder = new DefaultMembersFinder();
    protected static Selector selector = new DefaultSelector();
    protected static Reductor reductor = new DefaultReductor();
    protected static Convertor convertor = new DefaultConvertor();

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
                elementsCurr = SelectEvaluator.select(aSelect, elements, clazz);
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

    private static <T> void map(Document document, Elements elements,
            AccessibleObject member, T target) {

        if (hasOneAnnotationMapper(member)) {

            // select
            Elements elementsCurr = selector.select(document, elements, member);

            // reduce
            Object value = reductor.reduce(document, elementsCurr, member);

            // Handle optional on member scope
            Boolean optional = false;

            Optional aOptional = member.getAnnotation(Optional.class);
            if (aOptional != null) {
                optional = aOptional.value();
            }
            if (!optional && value == null) {
                throw new IllegalStateException(
                        "No value matched and not optional for "
                                + getName(member));
            }

            // convert
            // value = convertor.convert(member, value);

            // set value to target
            setValueToTarget(document, elementsCurr, target, member, value);
        }

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

    protected static Object getName(AnnotatedElement member) {
        return ClassUtils.getName(member);
    }

    protected static void setValueToTarget(Document document, Elements elements,
            Object target, AccessibleObject member, Object value) {
        try {
            // Get target Type of
            // - Field
            // - Method arguments
            // Evaluate target type, trigger recursive evaluation if necessary
            // set the value to the target
            Type[] memberType = getMemberType(member);

            List<TypeConvertor> lConvertors = new ArrayList<>();
            lConvertors.add(getConverter(member));
            lConvertors.addAll(convertors);

            Object[] values = evaluateArguments(document, elements, value,
                    lConvertors, memberType);

            if (member instanceof Field) {
                Field field = (Field) member;

                setValue(document, elements, target, field, values[0]);

            } else if (member instanceof Method) {
                Method method = (Method) member;

                setValue(document, elements, target, method, values);
            }

        } catch (IllegalArgumentException | IllegalAccessException
                | InvocationTargetException e) {
            throw new IllegalArgumentException(
                    "Cannot set value [" + value + "|" + value.getClass()
                            + "] for member [" + getName(member) + "]",
                    e);
        }

    }

    private static TypeConvertor getConverter(AccessibleObject member) {

        Class<? extends TypeConvertor> converterClass = TypeConvertor.VOID.class;

        Convert aConvert = member.getAnnotation(Convert.class);
        if (aConvert != null) {
            converterClass = aConvert.value();
        }
        try {
            return converterClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    public static Type[] getMemberType(AccessibleObject member) {
        if (member instanceof Field) {
            Field field = (Field) member;

            return new Type[] { field.getGenericType() };

        } else if (member instanceof Method) {
            Method method = (Method) member;

            return method.getGenericParameterTypes();
        }
        return null;
    }

    public static final List<TypeConvertor> convertors = new ArrayList<>();
    static {
        convertors.add(new PrimitiveTypeConvertor());
        convertors.add(new StandardTypeConvertor());
        convertors.add(new RecursiveMappingTypeConvertor());
    }

    /**
     * Prepares futures method arguments (or field value) depending of ordered
     * expected types.
     * 
     * @param document
     * @param elements
     * @param value
     *            The current value of the DOM after selects and reduces
     * @param convertors
     *            Convertors to use for this evaluation
     * @param targetsType
     * 
     * @return Arguments ordered giving method signature aka <code>argumentsType
     *         </code>
     */
    protected static Object[] evaluateArguments(Document document,
            Elements elements, Object value, List<TypeConvertor> convertors,
            Type... targetsType) {
        Object[] arguments = new Object[targetsType.length];

        for (int i = 0; i < targetsType.length; i++) {
            Type targetType = targetsType[i];

            Class<?> targetRawType = ClassUtils.rawType(targetType);

            for (TypeConvertor convertor : convertors) {
                if (convertor instanceof ConvertorContext) {
                    ConvertorContext convertorContext = (ConvertorContext) convertor;
                    convertorContext.setDocument(document);
                    convertorContext.setElements(elements);
                    convertorContext.setGenericTargetType(targetType);
                }
                if (convertor.canConvert(targetRawType, value)) {
                    arguments[i] = convertor.convert(targetRawType, value);
                    break;
                }
            }
        }
        return arguments;
    }

    protected static void setValue(Document document, Elements element,
            Object target, Field field, Object value)
            throws IllegalArgumentException, IllegalAccessException {

        log.debug("set value on field ['{}' => '{}']", value, field.getName());

        // If both types matchs
        if (ClassUtils.isAssignableValue(field.getType(), value)) {

            boolean accessibleBack = field.isAccessible();

            field.setAccessible(true);

            field.set(target, value);

            field.setAccessible(accessibleBack);

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

    protected Document parseDocument(InputStream document) {
        try {
            return Jsoup.parse(document, encoding, baseURI);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static class RecursiveMappingTypeConvertor
            implements TypeConvertor, ConvertorContext {

        @Setter
        private Document document;
        @Setter
        private Elements elements;
        @Setter
        private Type genericTargetType;

        @Override
        public boolean canConvert(Class<?> targetType, Object value) {
            return true;
        }

        @Override
        public Object convert(Class<?> targetType, Object value) {
            log.debug("evaluate argument by recursive mapping");
            return map(document, elements, genericTargetType);
        }

    }
}
