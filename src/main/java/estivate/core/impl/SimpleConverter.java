package estivate.core.impl;

import org.jsoup.select.Elements;

import estivate.core.ClassUtils;
import estivate.core.Converter;

public class StandardConverter implements Converter {

	public boolean canConvert(Object value, Class<?> targetType) {
		return ClassUtils.isAssignableValue(targetType, value) && !Elements.class.equals(value.getClass());
	}

	public Object convert(Object value, Class<?> targetType) {
		if (ClassUtils.isAssignableValue(targetType, value)) {
			return value;
		}
		return null;
	}

}
