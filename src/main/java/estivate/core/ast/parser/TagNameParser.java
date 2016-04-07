package estivate.core.ast.parser;

import java.lang.annotation.Annotation;

import estivate.annotations.TagName;
import estivate.core.ast.QueryAST;
import estivate.core.ast.ReduceAST;
import estivate.core.ast.lang.TagNameReduceAST;
import estivate.core.ast.parser.EstivateParser.QueryParser;
import estivate.core.ast.parser.EstivateParser.ReduceParser;
import estivate.utils.AnnotationsUtils;

/**
 * Parse {@link TagName} annotation
 * 
 * @author Benoit Theunissen
 *
 */
public class TagNameParser implements QueryParser, ReduceParser {

	public static final Class<? extends Annotation> TYPE = TagName.class;

	public QueryAST parseQuery(Annotation[] annotations) {

		TagName annotation = (TagName) AnnotationsUtils.find(annotations, TYPE);

		return SelectParser.parse(annotation);
	}

	public ReduceAST parseReduce(Annotation[] annotations) {

		TagName aAttr = (TagName) AnnotationsUtils.find(annotations, TYPE);

		TagNameReduceAST reduce = new TagNameReduceAST();

		return reduce;
	}

	public static QueryParser.Factory queryFactory = new QueryParser.Factory() {
		@Override
		public QueryParser queryParser(Annotation[] annotations) {
			if (AnnotationsUtils.contains(annotations, TYPE)) {
				return new TagNameParser();
			}
			return super.queryParser(annotations);
		}
	};

	public static ReduceParser.Factory reduceFactory = new ReduceParser.Factory() {
		@Override
		public ReduceParser reduceParser(Annotation[] annotations) {
			if (AnnotationsUtils.contains(annotations, TYPE)) {
				return new TagNameParser();
			}
			return super.reduceParser(annotations);
		}
	};

}
