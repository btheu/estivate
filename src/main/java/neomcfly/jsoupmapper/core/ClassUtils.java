package neomcfly.jsoupmapper.core;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class ClassUtils {

    public static boolean isAssignableFrom(Type type1, Type type2) {

        boolean isParam1 = type1 instanceof ParameterizedType;
        boolean isParam2 = type2 instanceof ParameterizedType;

        if (isParam1 && isParam2) {
            ParameterizedType pType1 = (ParameterizedType) type1;
            ParameterizedType pType2 = (ParameterizedType) type2;
            return isAssignableFrom(pType1, pType2);
        }
        if (isParam1 != isParam2) {
            return false;
        }

        Class<?> c1 = (Class<?>) type1;
        Class<?> c2 = (Class<?>) type2;

        try {
            if (c1.isPrimitive() && c1.equals(c2.getField("TYPE").get(null))) {
                return true;
            }
        } catch (IllegalArgumentException | IllegalAccessException
                | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        try {
            if (c2.isPrimitive() && c2.equals(c1.getField("TYPE").get(null))) {
                return true;
            }
        } catch (IllegalArgumentException | IllegalAccessException
                | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }

        return c1.isAssignableFrom(c2);
    }

    public static boolean isAssignableFrom(ParameterizedType type1,
            ParameterizedType type2) {

        Type raw1 = type1.getRawType();
        Type raw2 = type2.getRawType();

        if (isAssignableFrom(raw1, raw2)) {

            Type[] ata1 = type1.getActualTypeArguments();
            Type[] ata2 = type2.getActualTypeArguments();

            if (ata1.length == ata2.length) {

                for (int i = 0; i < ata2.length; i++) {
                    Type atype1 = ata1[i];
                    Type atype2 = ata2[i];
                    if (!isAssignableFrom(atype1, atype2)) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
        return false;
    }
}
