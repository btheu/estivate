package estivate.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import estivate.core.eval.EstivateEvaluator.QueryEvaluator;

/**
 * Find elements that match the {@link QueryEvaluator} CSS query, with this
 * element as the starting context. Matched elements may include this element,
 * or any of its children.
 * <p>
 * This method is generally more powerful to use than the DOM-type
 * {@code getElementBy*} methods, because multiple filters can be combined,
 * e.g.:
 * </p>
 * <ul>
 * <li>{@code el.select("a[href]")} - finds links ({@code a} tags with
 * {@code href} attributes)
 * <li>{@code el.select("a[href*=example.com]")} - finds links pointing to
 * example.com (loosely)
 * </ul>
 * <p>
 * See the query syntax documentation in {@link org.jsoup.select.Selector}.
 * </p>
 * 
 */
@Target({ TYPE, FIELD, METHOD })
@Retention(RUNTIME)
public @interface Select {

    String value() default "";

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
