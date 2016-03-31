package estivate.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Gets the combined text of this element and all its children. Whitespace is
 * normalized and trimmed.
 * <p>
 * For example, given HTML {@code 
 * 
<p>
 * Hello  <b>there</b> now! 
 * 
</p>
 * }, {@code p.text()} returns {@code "Hello there now!"}
 *
 */
@Target({ FIELD, METHOD })
@Retention(RUNTIME)
public @interface Text {

    /**
     * When <code>true</code>, equivalent to JSoup's element.ownText()
     * 
     */
    boolean own() default false;

    String select() default "";

    int index() default -1;

    boolean unique() default false;

    boolean first() default false;

    boolean last() default false;

    boolean optional() default false;

}
