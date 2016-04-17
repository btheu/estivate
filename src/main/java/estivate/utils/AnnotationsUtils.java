package estivate.utils;

import java.lang.annotation.Annotation;

public abstract class AnnotationsUtils {

	public static boolean contains(Annotation[] array, Class<? extends Annotation> type) {
		for (Annotation annotation : array) {
			if (annotation.annotationType().equals(type)) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public static <T> T find(Annotation[] array, Class<T> type) {
		for (Annotation annotation : array) {
			if (annotation.annotationType().equals(type)) {
				return (T) annotation;
			}
		}
		return null;
	}

}
