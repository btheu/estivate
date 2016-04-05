package estivate.utils;

public abstract class ArraysUtils {

	public static boolean contains(Object[] array, Object element) {
		for (Object object : array) {
			if (object.equals(element)) {
				return true;
			}
		}
		return false;
	}

	public static <T> T find(T[] array, T element) {
		for (T object : array) {
			if (object.equals(element)) {
				return object;
			}
		}
		return null;
	}

}
