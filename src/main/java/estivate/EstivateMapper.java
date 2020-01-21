package estivate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

import estivate.annotations.Select;
import estivate.core.ast.EstivateAST;
import estivate.core.ast.parser.EstivateParser;
import estivate.core.eval.EstivateEvaluator;
import estivate.core.eval.EstivateEvaluator.EvalContext;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

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

    public <T> T map(InputStream stream, Class<T> clazz) throws IOException {
        Document doc = parseStream(stream);
        log.debug("{}", doc.toString());
        return this.map(doc, clazz);
    }

    public <T> List<T> mapToList(InputStream stream, Class<T> clazz) throws IOException {
        Document doc = parseStream(stream);
        log.debug("{}", doc.toString());
        return this.mapToList(doc, clazz);
    }

    public Object map(InputStream stream, Type type) throws IOException {
        Document doc = parseStream(stream);
        log.debug("{}", doc.toString());
        return map(doc, type);
    }

    private Document parseStream(InputStream stream) throws IOException {
        return Jsoup.parse(bufferize(stream), this.encoding, this.baseURI);
    }

    /**
     * Workaround of JSoup stream parsing that fail on very rare byte configuration
     * 
     * @param inputStream
     * @return new InputStream with datas get fetched
     * @throws IOException
     */
    private InputStream bufferize(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        byte[] byteArray = buffer.toByteArray();

        return new ByteArrayInputStream(byteArray);
    }

    @SuppressWarnings("unchecked")
    public <T> T map(Document document, Class<T> clazz) {

        EstivateAST ast = EstivateParser.parse(clazz);

        EvalContext context = EstivateEvaluator.buildEvalContext(document, new Elements(document), ast);

        return (T) EstivateEvaluator.eval(context, ast);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> mapToList(Document document, Class<T> clazz) {

        EstivateAST ast = EstivateParser.parse(clazz);

        EvalContext context = EstivateEvaluator.buildEvalContext(document, new Elements(document), ast);

        return (List<T>) EstivateEvaluator.evalToList(context, ast);
    }

    public Object map(Document document, Type type) throws IOException {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;

            // Handle type parameter class
            Class<?> classArgument = (Class<?>) parameterizedType.getActualTypeArguments()[0];

            // Handle type class
            Class<?> rowClass = (Class<?>) parameterizedType.getRawType();

            if (Collection.class.isAssignableFrom(rowClass)) {
                log.debug(rowClass.getCanonicalName() + " is a Collection type");

                return this.mapToList(document, classArgument);
            } else {
                log.error(rowClass.getCanonicalName() + " is not a Collection type");

                throw new IllegalArgumentException("Parameterized type not handled: " + rowClass.getCanonicalName());
            }
        } else {
            return this.map(document, (Class<?>) type);
        }
    }
}
