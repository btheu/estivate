package estivate.core.impl;

import java.util.ArrayList;
import java.util.List;

import estivate.core.Converter;

public class DefaultConverter implements Converter {

	public static final List<Converter> converters = new ArrayList<Converter>();
	static {
		converters.add(new PrimitiveConverter());
		converters.add(new SimpleConverter());
	}

	public boolean canConvert(Object value, Class<?> targetType) {
		for (Converter typeConverter : converters) {
			if (typeConverter.canConvert(value, targetType)) {
				return true;
			}
		}
		return false;
	}

	public Object convert(Object value, Class<?> targetType) {
		for (Converter typeConverter : converters) {
			if (typeConverter.canConvert(value, targetType)) {
				return typeConverter.convert(value, targetType);
			}
		}
		return null;
	}

}
