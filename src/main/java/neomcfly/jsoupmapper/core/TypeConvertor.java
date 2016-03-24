package neomcfly.jsoupmapper.core;

public interface TypeConvertor {

    public boolean canConvert(Class<?> targetType, Object value);

    public Object convert(Class<?> targetType, Object value);

}
