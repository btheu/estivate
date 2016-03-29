package btheu.jsoupmapper;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import btheu.jsoupmapper.core.TypeConvertor;

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
	
    int index() default -1;
    
    boolean unique() default false;
    
    boolean first() default false;

    boolean last() default false;
    
    boolean optional() default false;
	
    Class<? extends TypeConvertor> converter() default TypeConvertor.VOID.class;
}
