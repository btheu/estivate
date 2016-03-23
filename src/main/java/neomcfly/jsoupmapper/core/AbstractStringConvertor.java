package neomcfly.jsoupmapper.core;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class AbstractStringConvertor<T>
        implements TypeConvertor<String, T> {

    private Type inputType;
    private Type outputType;

    public AbstractStringConvertor() {

        // Handle type parameter class
        ParameterizedType parameterizedTypeOut = (ParameterizedType) this
                .getClass().getGenericSuperclass();
        outputType = parameterizedTypeOut.getActualTypeArguments()[0];

        // Handle type parameter class
        ParameterizedType parameterizedType = (ParameterizedType) this
                .getClass().getSuperclass().getGenericInterfaces()[0];
        inputType = parameterizedType.getActualTypeArguments()[0];

    }

    @Override
    public boolean canHandle(Type input, Type output) {
        return ClassUtils.isAssignableFrom(input, inputType)
                && ClassUtils.isAssignableFrom(output, outputType);
    }

}
