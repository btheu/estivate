package estivate.core;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public abstract class ClassUtils {

    /**
     * 
     * Determine if the given type is assignable from the given value, assuming
     * setting by reflection. Considers primitive wrapper classes as assignable
     * to the corresponding primitive types.
     * 
     * @author Spring Framework
     * 
     * @param type
     *            the target type
     * @param value
     *            the value that should be assigned to the type
     * @return if the type is assignable from the value
     */
    public static boolean isAssignableValue(Class<?> type, Object value) {
        return (value != null ? isAssignable(type, value.getClass())
                : !type.isPrimitive());
    }

    /**
     * Check if the right-hand side type may be assigned to the left-hand side
     * type, assuming setting by reflection. Considers primitive wrapper classes
     * as assignable to the corresponding primitive types.
     * 
     * @author Spring Framework
     * 
     * @param lhsType
     *            the target type
     * @param rhsType
     *            the value type that should be assigned to the target type
     * @return if the target type is assignable from the value type
     */
    public static boolean isAssignable(Class<?> lhsType, Class<?> rhsType) {
        if (lhsType.isAssignableFrom(rhsType)) {
            return true;
        }
        if (lhsType.isPrimitive()) {
            Class<?> resolvedPrimitive = primitiveWrapperTypeMap.get(rhsType);
            if (lhsType == resolvedPrimitive) {
                return true;
            }
        } else {
            Class<?> resolvedWrapper = primitiveTypeToWrapperMap.get(rhsType);
            if (resolvedWrapper != null
                    && lhsType.isAssignableFrom(resolvedWrapper)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Map with primitive wrapper type as key and corresponding primitive type
     * as value, for example: Integer.class -> int.class.
     */
    private static final Map<Class<?>, Class<?>> primitiveWrapperTypeMap = new HashMap<Class<?>, Class<?>>(
            8);

    /**
     * Map with primitive type as key and corresponding wrapper type as value,
     * for example: int.class -> Integer.class.
     */
    private static final Map<Class<?>, Class<?>> primitiveTypeToWrapperMap = new HashMap<Class<?>, Class<?>>(
            8);

    static {
        primitiveWrapperTypeMap.put(Boolean.class, boolean.class);
        primitiveWrapperTypeMap.put(Byte.class, byte.class);
        primitiveWrapperTypeMap.put(Character.class, char.class);
        primitiveWrapperTypeMap.put(Double.class, double.class);
        primitiveWrapperTypeMap.put(Float.class, float.class);
        primitiveWrapperTypeMap.put(Integer.class, int.class);
        primitiveWrapperTypeMap.put(Long.class, long.class);
        primitiveWrapperTypeMap.put(Short.class, short.class);

        for (Map.Entry<Class<?>, Class<?>> entry : primitiveWrapperTypeMap
                .entrySet()) {
            primitiveTypeToWrapperMap.put(entry.getValue(), entry.getKey());
        }
    }

    public static Class<?> rawType(Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            return (Class<?>) parameterizedType.getRawType();
        }
        return (Class<?>) type;
    }

    public static Object getName(AnnotatedElement member) {
        if (member instanceof Member) {
            return ((Member) member).getName();
        }
        if (member instanceof Class) {
            return ((Class<?>) member).getSimpleName();
        }
        return "__unknown__";
    }

    public static <T> T newInstance(Class<? extends T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Cant create bean", e);
        }

    }
}
