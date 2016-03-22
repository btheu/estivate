package neomcfly.jsoupmapper.core;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public interface TypeConvertor<I,O> {

	public boolean canHandle(Class<?> input, Class<?> output);
	
	public O convert(Document document, Elements elements, Object value);
	
	public static final TypeConvertor VOID = new TypeConvertor() {
		@Override
		public boolean canHandle(Class input, Class output) {
			return input.isAssignableFrom(output);
		}
		@Override
		public Object convert(Document document, Elements elements, Object value) {
			return value;
		}
	};
}
