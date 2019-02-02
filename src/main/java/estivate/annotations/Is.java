package estivate.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <p>
 * Check if this element matches the given Selector CSS query.
 * <p>
 * <code>
 * &#64;Is(".activate")<br>
 * Boolean activated;
 * </code>
 * <p>
 * <code>Boolean activated = a.is(".activate");</code>
 * 
 * @author Benoit Theunissen
 *
 */
@Target({ FIELD, METHOD })
@Retention(RUNTIME)
public @interface Is {

    String value();

    String select() default "";

    int index() default -1;

    boolean unique() default false;

    boolean first() default false;

    boolean last() default false;

    boolean optional() default false;

}
