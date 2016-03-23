package neomcfly.jsoupmapper.core;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Handles primitives types or their respective object definition class type
 * 
 * @author theunissenb
 *
 */
@SuppressWarnings("rawtypes")
public class PrimitiveTypeConvertor implements TypeConvertor {

	@Override
	public boolean canHandle(Type from, Type to) {
		if (!ClassUtils.isAssignableFrom(from, String.class)) {
			return false;
		}
		return primitivesAndBoxes.contains(to);
	}

	@Override
	public Object convert(Document document, Elements elements, Object value, Type targetType) {
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
	}
	
	private static final Set<Type> primitivesAndBoxes = mLBD.keySet();
	
}
