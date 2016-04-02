package estivate.core;

import java.lang.reflect.AccessibleObject;

/**
 * 
 * Handle convertion from select/reduce to target object.
 * 
 * @author Benoit Theunissen
 *
 */
public interface Convertor {

    Object convert(AccessibleObject member, Object value);

}
