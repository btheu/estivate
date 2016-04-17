package estivate.core.impl;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import estivate.core.MembersFinder;

/**
 * 
 * @author Benoit Theunissen
 *
 */
public class DefaultMembersFinder implements MembersFinder {

	public List<Field> listFields(Class<?> clazz) {
		return getAllFields(clazz);
	}

	public List<Method> listMethods(Class<?> clazz) {
		return Arrays.asList(clazz.getDeclaredMethods());
	}

	public List<AccessibleObject> list(Class<?> clazz) {
		List<AccessibleObject> members = new ArrayList<AccessibleObject>();

		members.addAll(listFields(clazz));
		members.addAll(listMethods(clazz));

		return members;
	}

	public static List<Field> getAllFields(Class<?> clazz) {

		List<Field> res = new ArrayList<Field>();

		Class<?> index = clazz;
		while (index != Object.class && index != null) {
			res.addAll(Arrays.asList(index.getDeclaredFields()));

			index = index.getSuperclass();
		}

		return res;
	}

}
