package estivate.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 
 * @author Benoit Theunissen
 *
 */
@Target({ TYPE, FIELD, METHOD })
@Retention(RUNTIME)
public @interface Cell {

    String value() default "";

    String name() default "";

    boolean regex() default false;

    boolean optional() default false;
}