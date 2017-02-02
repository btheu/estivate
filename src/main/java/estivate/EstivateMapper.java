package estivate;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import estivate.annotations.Select;
import estivate.core.ast.EstivateAST;
import estivate.core.ast.parser2.EstivateParser2;
import estivate.core.eval.EstivateEvaluator2;

/**
 * <ul>
 * <li>parse members and call registered implementation</li>
 * <li>use dialect JSoup by default</li>
 * <li>get members ordered to evaluate</li>
 * <li>evaluate select elements</li>
 * <li>evaluate reduce value</li>
 * <li>converts value</li>
 * <li>sets value to target</li>
 * </ul>
 * 
 * @author Benoit Theunissen
 *
 */
@Slf4j
public class EstivateMapper {

    @Getter
    @Setter
    protected String encoding = "UTF-8";

    @Getter
    @Setter
    protected String baseURI = "/";

    protected static final String PACKAGE_NAME = Select.class.getPackage().getName();

    protected static final List<Class<?>> STANDARD_TARGET_TYPES = new ArrayList<Class<?>>();
    static {
        STANDARD_TARGET_TYPES.add(Document.class);
        STANDARD_TARGET_TYPES.add(Elements.class);
        STANDARD_TARGET_TYPES.add(Element.class);
    }

    public <T> T map(InputStream document, Class<T> clazz) throws IOException {
        Document doc = Jsoup.parse(document, this.encoding, this.baseURI);
        return this.map(doc, clazz);
    }

    @SuppressWarnings("unchecked")
    public <T> T map(Document document, Class<T> clazz) {

        EstivateAST ast = EstivateParser2.parse(clazz);

        return (T) EstivateEvaluator2.eval(document, ast);
    }

    public <T> List<T> mapToList(InputStream document, Class<T> clazz) throws IOException {
        Document doc = Jsoup.parse(document, this.encoding, this.baseURI);
        return this.mapToList(doc, clazz);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> mapToList(Document document, Class<T> clazz) {

        EstivateAST ast = EstivateParser2.parse(clazz);

        return (List<T>) EstivateEvaluator2.evalToList(document, ast);
    }

    public Object map(InputStream document, Type type) throws IOException {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;

            // Handle type parameter class
            Class<?> classArgument = (Class<?>) parameterizedType.getActualTypeArguments()[0];

            // Handle type class
            Class<?> rowClass = (Class<?>) parameterizedType.getRawType();

            if (Collection.class.isAssignableFrom(rowClass)) {

                return this.mapToList(document, classArgument);

            } else {
                log.debug(rowClass.getCanonicalName() + " is not a collection");

                throw new IllegalArgumentException("Parameterized type not handled: " + rowClass.getCanonicalName());
            }
        } else {
            return this.map(document, (Class<?>) type);
        }
    }

}
