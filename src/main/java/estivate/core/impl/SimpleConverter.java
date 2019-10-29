package estivate.core.impl;

import org.jsoup.select.Elements;

import estivate.core.ClassUtils;
import estivate.core.Converter;

/**
 * 
 * 
 * @author Benoit Theunissen
 *
 */
public class SimpleConverter implements Converter {

    public boolean canConvert(Object value, Class<?> targetType) {
        return ClassUtils.isAssignableValue(targetType, value) && !Elements.class.equals(value.getClass());
    }

    public Object convert(Object value, Class<?> targetType, String format) {
        if (ClassUtils.isAssignableValue(targetType, value) && !Elements.class.equals(value.getClass())) {
            return value;
        }
        return null;
    }

}
