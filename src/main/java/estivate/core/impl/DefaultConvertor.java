package estivate.core.impl;

import java.util.ArrayList;
import java.util.List;

import estivate.TypeConvertor;
import estivate.core.Convertor;
import estivate.core.PrimitiveTypeConvertor;
import estivate.core.StandardTypeConvertor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultConvertor implements Convertor {

    public static final List<TypeConvertor> convertors = new ArrayList<>();
    static {
        convertors.add(new PrimitiveTypeConvertor());
        convertors.add(new StandardTypeConvertor());
    }

    @Override
    public boolean canConvert(Object value, Class<?> targetType) {
        for (TypeConvertor typeConvertor : convertors) {
            if (typeConvertor.canConvert(targetType, value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object convert(Object value, Class<?> targetType) {
        for (TypeConvertor typeConvertor : convertors) {
            if (typeConvertor.canConvert(targetType, value)) {
                return typeConvertor.convert(targetType, value);
            }
        }
        return null;
    }

}
