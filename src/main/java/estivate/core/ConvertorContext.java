package estivate.core;

import java.lang.reflect.Type;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

@Deprecated
public interface ConvertorContext {

    public void setDocument(Document document);

    public void setElements(Elements elements);

    public void setGenericTargetType(Type type);

}
