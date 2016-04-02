package estivate.core.impl;

import java.util.ArrayList;
import java.util.List;

import estivate.core.Converter;
import estivate.core.PrimitiveTypeConverter;
import estivate.core.StandardTypeConverter;

public class DefaultConverter implements Converter {

    public static final List<Converter> convertors = new ArrayList<>();
    static {
        convertors.add(new PrimitiveTypeConverter());
        convertors.add(new StandardTypeConverter());
    }

    @Override
    public boolean canConvert(Object value, Class<?> targetType) {
        for (Converter typeConverter : convertors) {
            if (typeConverter.canConvert(value, targetType)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object convert(Object value, Class<?> targetType) {
        for (Converter typeConverter : convertors) {
            if (typeConverter.canConvert(value, targetType)) {
                return typeConverter.convert(value, targetType);
            }
        }
        return null;
    }

}
