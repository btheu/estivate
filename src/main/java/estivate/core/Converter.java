package estivate.core;

/**
 * <p>
 * Handle convertion from select/reduce to target object.
 * 
 * @author Benoit Theunissen
 *
 */
public interface Converter {

	boolean canConvert(Object value, Class<?> targetType);

	/**
	 * Convert value to target type.
	 * 
	 * @param value
	 *            The value to be converted
	 * @param targetType
	 *            The target type to which the value have to be converted
	 * @return the value converted
	 */
	Object convert(Object value, Class<?> targetType);

	public static final class VOID implements Converter {
		public boolean canConvert(Object value, Class<?> targetType) {
			return false;
		}

		public Object convert(Object value, Class<?> targetType) {
			return value;
		}
	}

}
