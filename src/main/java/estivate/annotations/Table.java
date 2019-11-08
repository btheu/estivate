package estivate.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <p>
 * Perform &lt;Table&gt; request
 * 
 * @see Column
 * 
 * @author Benoit Theunissen
 *
 */
@Target({ TYPE, FIELD, METHOD })
@Retention(RUNTIME)
public @interface Table {

    String value() default "";

    String select() default "";

    String rowSelect() default "tbody tr";

    /**
     * <p>
     * 1 based element index
     * <p>
     * -1 means no access by index to an item of Elements collection.
     * 
     * @return element position selection
     */
    int index() default -1;

    boolean first() default false;

    boolean last() default false;

    boolean optional() default false;

}
