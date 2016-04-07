package estivate.core;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ClassUtils {

    /**
     * 
     * @param field
     * @param target
     * @param value
     */
    public static void setValue(Field field, Object target, Object value){

        log.debug("set value on field ['{}' => '{}']", value, field.getName());
        
        try {

            if (ClassUtils.isAssignableValue(field.getType(), value)) {

                boolean accessibleBack = field.isAccessible();

                field.setAccessible(true);

                field.set(target, value);

                field.setAccessible(accessibleBack);

            } else {
                log.error("set value is not assignable with field '{}'", field.getName());
                throw new IllegalArgumentException("Cant set " + value.toString() + " to " + field.getName());
            }
        } catch (Exception e) {
            log.error("set value is not assignable with field ",e);
            throw new RuntimeException("Cant set " + value.toString() + " to " + field.getName(),e);
        }

    }

    public static void setValue(Method method, Object target, Object... values){

        log.debug("set value by method [{} ({})]", method.getName(), values);

        try {
            method.invoke(target, values);
        } catch (Exception e) {
            throw new RuntimeException("Cant set " + values.toString() + " to " + method.getName()+"()",e);
        }
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
        return (value != null ? isAssignable(type, value.getClass()) : !type.isPrimitive());
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
            if (resolvedWrapper != null && lhsType.isAssignableFrom(resolvedWrapper)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Map with primitive wrapper type as key and corresponding primitive type
     * as value, for example: Integer.class -> int.class.
     */
    private static final Map<Class<?>, Class<?>> primitiveWrapperTypeMap = new HashMap<Class<?>, Class<?>>(8);

    /**
     * Map with primitive type as key and corresponding wrapper type as value,
     * for example: int.class -> Integer.class.
     */
    private static final Map<Class<?>, Class<?>> primitiveTypeToWrapperMap = new HashMap<Class<?>, Class<?>>(8);

    static {
        primitiveWrapperTypeMap.put(Boolean.class, boolean.class);
        primitiveWrapperTypeMap.put(Byte.class, byte.class);
        primitiveWrapperTypeMap.put(Character.class, char.class);
        primitiveWrapperTypeMap.put(Double.class, double.class);
        primitiveWrapperTypeMap.put(Float.class, float.class);
        primitiveWrapperTypeMap.put(Integer.class, int.class);
        primitiveWrapperTypeMap.put(Long.class, long.class);
        primitiveWrapperTypeMap.put(Short.class, short.class);

        for (Map.Entry<Class<?>, Class<?>> entry : primitiveWrapperTypeMap.entrySet()) {
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

    public static Class<?>[] typeArguments(Type type) {
        
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] typeArguments = parameterizedType.getActualTypeArguments();
            Class<?>[] types = new Class<?>[typeArguments.length];
            for (int i = 0; i < typeArguments.length; i++) {
               types[i] = rawType(typeArguments[i]);
            }
            return types;
        }
        
        return null;
    }
    
    public static String getName(AnnotatedElement member) {
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
        } catch (Exception e) {
            throw new RuntimeException("Cant create bean", e);
        }

    }
}
