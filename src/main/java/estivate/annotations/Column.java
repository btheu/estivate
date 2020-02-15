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
public @interface Column {

    /**
     * <p>
     * For multiple row header, path can be specified with a slash (/)
     * separator.
     * 
     */
    String value() default "";

    /**
     * <p>
     * For multiple row header, path can be specified with a slash (/)
     * separator.
     * 
     */
    String name() default "";

    /**
     * <p>
     * Class name to match the column from &lt;th&gt; tag
     */
    String thClass() default "";

    boolean regex() default false;

    boolean optional() default false;

    boolean trim() default true;

    boolean ignoreCase() default true;

    boolean ignoreSpace() default true;

}
