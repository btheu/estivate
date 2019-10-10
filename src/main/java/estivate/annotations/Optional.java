package estivate.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Indicate that EstivateMapper wont throw a exception if the mapping of this
 * field or method is not satisfied.
 * 
 * @author Benoit Theunissen
 *
 */
@Target({ TYPE, FIELD, METHOD })
@Retention(RUNTIME)
public @interface Optional {

    boolean value() default true;

}
