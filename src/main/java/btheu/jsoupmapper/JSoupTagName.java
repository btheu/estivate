package btheu.jsoupmapper;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Get the name of the tag for this element. E.g. div
 * 
 * @author NeoMcFly
 *
 */
@Target({ FIELD, METHOD })
@Retention(RUNTIME)
public @interface JSoupTagName {

}
