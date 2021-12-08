package estivate.core.impl;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import estivate.core.Converter;
import estivate.core.Function;
import lombok.SneakyThrows;

/**
 * Handles primitives types or their respective object definition class type
 * 
 * @author Benoit Theunissen
 *
 */
public class PrimitiveConverter implements Converter {

	public boolean canConvert(Object value, Class<?> targetType) {
		if (!value.getClass().isAssignableFrom(String.class)) {
			return false;
		}
		return primitivesAndBoxes.contains(targetType);
	}

	public Object convert(Object value, Class<?> targetType, String format) {
		return mLBD.get(targetType).apply((String) value);
	}

	public static Function<String, Object> parseInt = new Function<String, Object>() {
		public Object apply(String s) {
			return Integer.parseInt(s.replaceAll(" ", ""));
		}
	};
	public static Function<String, Object> parseChar = new Function<String, Object>() {
		public Object apply(String s) {
			return s.charAt(0);
		}
	};
	public static Function<String, Object> parseByte = new Function<String, Object>() {
		public Object apply(String s) {
			return Byte.parseByte(s);
		}
	};
	public static Function<String, Object> parseBool = new Function<String, Object>() {
		public Object apply(String s) {
			return Boolean.parseBoolean(s);
		}
	};
	public static Function<String, Object> parseFloat = new Function<String, Object>() {
		public Object apply(String s) {
			return Float.parseFloat(s.replaceAll(" ", ""));
		}
	};
	public static Function<String, Object> parseDouble = new Function<String, Object>() {
		public Object apply(String s) {
			return Double.parseDouble(s.replaceAll(" ", ""));
		}
	};
	public static Function<String, Object> parseLong = new Function<String, Object>() {
		public Object apply(String s) {
			return Long.parseLong(s.replaceAll(" ", ""));
		}
	};
	public static Function<String, Object> parseShort = new Function<String, Object>() {
		public Object apply(String s) {
			return Short.parseShort(s.replaceAll(" ", ""));
		}
	};
	public static Function<String, Object> parseBigDecimal = new Function<String, Object>() {
		public Object apply(String s) {
			return new BigDecimal(s.replaceAll(" ", ""));
		}
	};
	public static Function<String, Object> parseBigInteger = new Function<String, Object>() {
		public Object apply(String s) {
			return new BigInteger(s.replaceAll(" ", ""));
		}
	};
	public static Function<String, Object> parseURL = new Function<String, Object>() {
	    @SneakyThrows
	    public Object apply(String s) {
	        return new URL(s.replaceAll(" ", ""));
	    }
	};

	private static final Map<Type, Function<String, Object>> mLBD = new HashMap<Type, Function<String, Object>>();
	static {
		mLBD.put(int.class, parseInt);
		mLBD.put(Integer.class, parseInt);
		mLBD.put(Number.class, parseInt);
		mLBD.put(char.class, parseChar);
		mLBD.put(Character.class, parseChar);
		mLBD.put(byte.class, parseByte);
		mLBD.put(Byte.class, parseByte);
		mLBD.put(boolean.class, parseBool);
		mLBD.put(Boolean.class, parseBool);
		mLBD.put(float.class, parseFloat);
		mLBD.put(Float.class, parseFloat);
		mLBD.put(double.class, parseDouble);
		mLBD.put(Double.class, parseDouble);
		mLBD.put(long.class, parseLong);
		mLBD.put(Long.class, parseLong);
		mLBD.put(short.class, parseShort);
		mLBD.put(Short.class, parseShort);

		mLBD.put(BigDecimal.class, parseBigDecimal);
		mLBD.put(BigInteger.class, parseBigInteger);

		mLBD.put(URL.class, parseURL);
	}

	private static final Set<Type> primitivesAndBoxes = mLBD.keySet();

	public boolean isPrimitive(Type type) {
		return primitivesAndBoxes.contains(type);
	}

}
