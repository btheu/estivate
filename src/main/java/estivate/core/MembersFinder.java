package estivate.core;

import java.lang.reflect.AccessibleObject;
import java.util.List;

/**
 * 
 * @author Benoit Theunissen
 *
 */
public interface MembersFinder {

    List<AccessibleObject> list(Class<?> clazz);

}
