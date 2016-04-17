package estivate.core;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 
 * @author Benoit Theunissen
 *
 */
public interface MembersFinder {

	List<AccessibleObject> list(Class<?> clazz);

	List<Field> listFields(Class<?> clazz);

	List<Method> listMethods(Class<?> clazz);

}
