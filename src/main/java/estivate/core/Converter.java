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
     * @param target
     *            The target type to which the value have to be converted
     * @return the value converted
     */
    Object convert(Object value, Class<?> targetType);

    public static final class VOID implements Converter {
        @Override
        public boolean canConvert(Object value, Class<?> targetType) {
            return false;
        }

        @Override
        public Object convert(Object value, Class<?> targetType) {
            return value;
        }
    }

}
