package estivate.core;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Handles primitives types or their respective object definition class type
 * 
 * @author Benoit Theunissen
 *
 */
public class PrimitiveTypeConvertor implements TypeConvertor {

	@Override
	public boolean canConvert(Class<?> targetType, Object value) {
		if (!value.getClass().isAssignableFrom(String.class)) {
			return false;
		}
		return primitivesAndBoxes.contains(targetType);
	}

	@Override
	public Object convert(Class<?> targetType, Object value) {
		return mLBD.get(targetType).apply((String) value);
	}
	
	public static Function<String, Object> parseInt = new Function<String, Object>() {
		@Override
		public Object apply(String s) {
			return Integer.parseInt(s);
		}
	};
	public static Function<String, Object> parseChar = new Function<String, Object>() {
		@Override
		public Object apply(String s) {
			return s.charAt(0);
		}
	};
	public static Function<String, Object> parseByte = new Function<String, Object>() {
		@Override
		public Object apply(String s) {
			return Byte.parseByte(s);
		}
	};
	public static Function<String, Object> parseBool = new Function<String, Object>() {
		@Override
		public Object apply(String s) {
			return Boolean.parseBoolean(s);
		}
	};
	public static Function<String, Object> parseFloat = new Function<String, Object>() {
		@Override
		public Object apply(String s) {
			return Float.parseFloat(s);
		}
	};
	public static Function<String, Object> parseDouble = new Function<String, Object>() {
		@Override
		public Object apply(String s) {
			return Double.parseDouble(s);
		}
	};
	public static Function<String, Object> parseLong = new Function<String, Object>() {
		@Override
		public Object apply(String s) {
			return Long.parseLong(s);
		}
	};
	public static Function<String, Object> parseShort = new Function<String, Object>() {
		@Override
		public Object apply(String s) {
			return Short.parseShort(s);
		}
	};
	public static Function<String, Object> parseBigDecimal = new Function<String, Object>() {
		@Override
		public Object apply(String s) {
			return new BigDecimal(s);
		}
	};
	public static Function<String, Object> parseBigInteger = new Function<String, Object>() {
		@Override
		public Object apply(String s) {
			return new BigInteger(s);
		}
	};
	
	private static final Map<Type, Function<String,Object>> mLBD = new HashMap<>();
	static{
		mLBD.put(int.class, 		parseInt);
		mLBD.put(Integer.class, 	parseInt);
		mLBD.put(Number.class, 		parseInt);
		mLBD.put(char.class, 		parseChar);
		mLBD.put(Character.class, 	parseChar);
		mLBD.put(byte.class, 		parseByte);
		mLBD.put(Byte.class, 		parseByte);	
		mLBD.put(boolean.class, 	parseBool);
		mLBD.put(Boolean.class, 	parseBool);
		mLBD.put(float.class, 		parseFloat);
		mLBD.put(Float.class, 		parseFloat);
		mLBD.put(double.class, 		parseDouble);
		mLBD.put(Double.class, 		parseDouble);
		mLBD.put(long.class, 		parseLong);
		mLBD.put(Long.class, 		parseLong);
		mLBD.put(short.class, 		parseShort);
		mLBD.put(Short.class, 		parseShort);
		
		mLBD.put(BigDecimal.class, 		parseBigDecimal);
		mLBD.put(BigInteger.class, 		parseBigInteger);
	}
	
	private static final Set<Type> primitivesAndBoxes = mLBD.keySet();


	
}
