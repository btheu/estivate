package neomcfly.jsoupmapper;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <p>
 * Get an attribute's value by its key. To get an absolute URL from an attribute
 * that may be a relative URL, prefix the key with abs, which is a shortcut to
 * the absUrl method. E.g.:
 * <p>
 * <code>String url = a.attr("abs:href");<code>
 * 
 * @author NeoMcFly
 *
 */
@Target({ FIELD, METHOD })
@Retention(RUNTIME)
public @interface JsoupAttr {

    String value();

}
