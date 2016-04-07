package estivate.core;

import java.lang.reflect.AccessibleObject;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import estivate.annotations.Attr;
import estivate.annotations.TagName;
import estivate.annotations.Text;
import estivate.annotations.Title;
import estivate.annotations.Val;

/**
 * <p>
 * Handle annotation of type 'reduce', aka. returns a final result like text()
 * or attr().
 * 
 * <p>
 * Thoses reductors will be called after {@link Selecter}
 * 
 * @see Selecter
 * 
 * @author Benoit Theunissen
 *
 */
@Deprecated
public interface Reducter {

    /**
     * Apply all rules (annotations) of type reduce.
     * 
     * @see Attr
     * @see Text
     * @see Title
     * @see TagName
     * @see Val
     * 
     * @param document
     * @param elementsIn
     * @param member
     * @param isTargetList
     * @return
     */
    Object reduce(Document document, Elements elementsIn,
            AccessibleObject member, boolean isTargetList);

}
