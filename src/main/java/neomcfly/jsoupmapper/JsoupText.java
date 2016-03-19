package neomcfly.jsoupmapper;

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
 * @return unencoded text, or empty string if none.
 * @see #ownText()
 * @see #textNodes()
 */
@Target({ FIELD, METHOD })
@Retention(RUNTIME)
public @interface JsoupText {

    /**
     * When <code>true</code>, equivalent to JSoup's element.ownText()
     * 
     */
    @AliasFor("own")
    boolean value() default false;

    /**
     * When <code>true</code>, equivalent to JSoup's element.ownText()
     * 
     */
    @AliasFor("value")
    boolean own() default false;

}
