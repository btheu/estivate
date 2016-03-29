package btheu.jsoupmapper;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Get the name of the tag for this element. E.g. div
 * 
 * @author Benoit Theunissen
 *
 */
@Target({ FIELD, METHOD })
@Retention(RUNTIME)
public @interface TagName {

    String select() default "";
    
    int index() default -1;
    
    boolean unique() default false;
    
    boolean first() default false;

    boolean last() default false;
    
    boolean optional() default false;
    
    
}
