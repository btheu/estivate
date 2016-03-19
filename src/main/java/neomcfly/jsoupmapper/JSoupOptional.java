package neomcfly.jsoupmapper;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Indicate that JSoupMapper wont throw a exception if the mapping of this field
 * or method is not satisfied.
 * 
 * @author NeoMcFly
 *
 */
@Target({ FIELD })
@Retention(RUNTIME)
public @interface JSoupOptional {

    boolean value() default true;

}
