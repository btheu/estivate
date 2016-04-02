package estivate;

public interface TypeConverter {

    public boolean canConvert(Class<?> targetType, Object value);

    public Object convert(Class<?> targetType, Object value);

    public static final class VOID implements TypeConverter {
        @Override
        public boolean canConvert(Class<?> targetType, Object value) {
            return false;
        }

        @Override
        public Object convert(Class<?> targetType, Object value) {
            return value;
        }
    }

}
