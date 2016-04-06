package estivate;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import estivate.annotations.Select;
import estivate.core.ast.EstivateAST;
import estivate.core.ast.parser.EstivateParser;
import estivate.core.eval.EstivateEvaluator;
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
public class EstivateMapper2 {

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

	@SuppressWarnings("unchecked")
	public <T> T map(Document document, Class<T> clazz) {

		EstivateAST ast = EstivateParser.parse(clazz);

		log.debug("AST {}", ast.toString());

		return (T) EstivateEvaluator.eval(document, ast);
	}

	

}
