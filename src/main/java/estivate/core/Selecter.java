package estivate.core;

import java.lang.reflect.AccessibleObject;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * <p>
 * Handle annotation of type 'select', aka. returns a list of elements.
 * 
 * <p>
 * Thoses selectors will be called before {@link Reducter}
 * 
 * @see Reducter
 * 
 * @author Benoit Theunissen
 *
 */
public interface Selecter {

    Elements select(Document document, Elements elements,
            AccessibleObject member);

}
