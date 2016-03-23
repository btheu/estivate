package neomcfly.jsoupmapper.core;

import java.lang.reflect.Type;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@SuppressWarnings("rawtypes")
public class StandardTypeConvertor implements TypeConvertor {

    @Override
    public boolean canHandle(Type from, Type to) {
        return ClassUtils.isAssignableFrom(to,
                Document.class.getGenericSuperclass())
                || ClassUtils.isAssignableFrom(to,
                        Element.class.getGenericSuperclass())
                || ClassUtils.isAssignableFrom(to,
                        Elements.class.getGenericSuperclass())
                || ClassUtils.isAssignableFrom(to, from);
    }

    @Override
    public Object convert(Document document, Elements elements, Object value,
            Type targetType) {
        if (ClassUtils.isAssignableFrom(targetType, Elements.class)) {
            return elements;
        }
        if (ClassUtils.isAssignableFrom(targetType, Element.class)) {
            if (elements.size() == 1) {
                return elements.first();
            } else {
                throw new IllegalArgumentException(
                        "Cant set 'Element' object with Elements of size:"
                                + elements.size());
            }
        }
        if (ClassUtils.isAssignableFrom(targetType, Document.class)) {
            return document;
        }
        if (ClassUtils.isAssignableFrom(targetType, value.getClass())) {
            return value;
        }
        return null;
    }

}
