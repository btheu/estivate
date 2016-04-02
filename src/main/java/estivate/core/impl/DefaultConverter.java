package estivate.core.impl;

import java.util.ArrayList;
import java.util.List;

import estivate.TypeConverter;
import estivate.core.Converter;
import estivate.core.PrimitiveTypeConverter;
import estivate.core.StandardTypeConverter;

public class DefaultConverter implements Converter {

    public static final List<TypeConverter> convertors = new ArrayList<>();
    static {
        convertors.add(new PrimitiveTypeConverter());
        convertors.add(new StandardTypeConverter());
    }

    @Override
    public boolean canConvert(Object value, Class<?> targetType) {
        for (TypeConverter typeConverter : convertors) {
            if (typeConverter.canConvert(targetType, value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object convert(Object value, Class<?> targetType) {
        for (TypeConverter typeConverter : convertors) {
            if (typeConverter.canConvert(targetType, value)) {
                return typeConverter.convert(targetType, value);
            }
        }
        return null;
    }

}
