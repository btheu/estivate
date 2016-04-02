package estivate.core;

import org.jsoup.select.Elements;

import estivate.TypeConverter;

public class StandardTypeConverter implements TypeConverter {

    @Override
    public boolean canConvert(Class<?> targetType, Object value) {
        return ClassUtils.isAssignableValue(targetType, value)
                && !Elements.class.equals(value.getClass());
    }

    @Override
    public Object convert(Class<?> targetType, Object value) {
        if (ClassUtils.isAssignableValue(targetType, value)) {
            return value;
        }
        return null;
    }

}
