package estivate.core.impl;

import java.util.ArrayList;
import java.util.List;

import estivate.core.Converter;

public class DefaultConverter implements Converter {

    public static final List<Converter> convertors = new ArrayList<>();
    static {
        convertors.add(new PrimitiveConverter());
        convertors.add(new StandardConverter());
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
