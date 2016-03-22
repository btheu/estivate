package neomcfly.jsoupmapper.core;

import java.lang.reflect.ParameterizedType;

public abstract class AbstractStringConvertor<T> implements TypeConvertor<String, T> {

	private Class<?> classInput;
	private Class<?> classOutput;

	public AbstractStringConvertor() {

		// Handle type parameter class
		ParameterizedType parameterizedTypeOut = (ParameterizedType) this.getClass().getGenericSuperclass();
		classOutput = (Class<?>) parameterizedTypeOut.getActualTypeArguments()[0];
		
		// Handle type parameter class
		ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getSuperclass().getGenericInterfaces()[0];
		classInput = (Class<?>) parameterizedType.getActualTypeArguments()[0];

	}

	@Override
	public boolean canHandle(Class<?> input, Class<?> output) {
		return ClassUtils.isAssignable(input, classInput)  && ClassUtils.isAssignable(output, classOutput) ;
	}

}
