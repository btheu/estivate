package estivate.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Get the value of a form element (input, textarea, etc).
 * 
 * @author Benoit Theunissen
 *
 */
@Target({ FIELD, METHOD })
@Retention(RUNTIME)
public @interface Val {

    String select() default "";

    /**
     * <p>
     * 1 based element index
     * <p>
     * -1 means no access by index to an item of Elements collection.
     * 
     * @return element position selection
     */
    int index() default -1;

    boolean unique() default false;

    boolean first() default false;

    boolean last() default false;

    boolean optional() default false;
}
