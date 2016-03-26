package btheu.jsoupmapper.core;

import java.lang.reflect.Type;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lombok.Setter;

public class StandardTypeConvertor implements TypeConvertor, ConvertorContext {

    @Setter
    private Elements elements;
    @Setter
    private Document document;
    @Setter
    private Type genericTargetType;

    @Override
    public boolean canConvert(Class<?> targetType, Object value) {
        return Document.class.equals(targetType)
                || Element.class.equals(targetType)
                || Elements.class.equals(targetType)
                || (ClassUtils.isAssignableValue(targetType, value)
                        && !Elements.class.equals(value.getClass()));
    }

    @Override
    public Object convert(Class<?> targetType, Object value) {
        if (Elements.class.equals(targetType)) {
            return elements;
        }
        if (Element.class.equals(targetType)) {
            if (elements.size() == 1) {
                return elements.first();
            } else {
                throw new IllegalArgumentException(
                        "Cant set 'Element' object with Elements of size:"
                                + elements.size());
            }
        }
        if (Document.class.equals(targetType)) {
            return document;
        }
        if (ClassUtils.isAssignableValue(targetType, value)) {
            return value;
        }
        return null;
    }

}
