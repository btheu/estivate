package neomcfly.jsoupmapper.core;

import java.lang.reflect.Type;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public interface TypeConvertor<I, O> {

    public boolean canHandle(Type from, Type to);

    public O convert(Document document, Elements elements, Object value,
            Type targetType);

}
