package estivate;

public interface TypeConvertor {

    public boolean canConvert(Class<?> targetType, Object value);

    public Object convert(Class<?> targetType, Object value);

    public static final class VOID implements TypeConvertor {
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
