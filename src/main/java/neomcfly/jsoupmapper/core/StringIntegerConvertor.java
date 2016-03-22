package neomcfly.jsoupmapper.core;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class StringIntegerConvertor extends AbstractStringConvertor<Integer> {

	@Override
	public Integer convert(Document document, Elements elements, Object value) {
		return Integer.parseInt((String)value);
	}

}
