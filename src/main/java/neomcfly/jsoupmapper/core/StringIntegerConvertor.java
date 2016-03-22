package neomcfly.jsoupmapper.core;

import java.lang.reflect.Type;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class StringIntegerConvertor extends AbstractStringConvertor<Integer> {

    @Override
    public Integer convert(Document document, Elements elements, Object value,
            Type targetType) {
        return Integer.parseInt((String) value);
    }

}
