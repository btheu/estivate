package estivate.core.ast.parser;

import java.lang.annotation.Annotation;

import estivate.annotations.Val;
import estivate.core.ast.QueryAST;
import estivate.core.ast.ReduceAST;
import estivate.core.ast.lang.ValReduceAST;
import estivate.core.ast.parser.EstivateParser.QueryParser;
import estivate.core.ast.parser.EstivateParser.ReduceParser;
import estivate.utils.AnnotationsUtils;

/**
 * Parse {@link Val} annotation
 * 
 * @author Benoit Theunissen
 *
 */
public class ValParser implements QueryParser, ReduceParser {

	public static final Class<? extends Annotation> TYPE = Val.class;

	public QueryAST parseQuery(Annotation[] annotations) {

		Val aVal = (Val) AnnotationsUtils.find(annotations, TYPE);

		return SelectParser.parse(aVal);
	}

	public ReduceAST parseReduce(Annotation[] annotations) {

		Val aAttr = (Val) AnnotationsUtils.find(annotations, TYPE);

		ValReduceAST reduce = new ValReduceAST();

		return reduce;
	}

	public static QueryParser.Factory queryFactory = new QueryParser.Factory() {
		@Override
		public QueryParser queryParser(Annotation[] annotations) {
			if (AnnotationsUtils.contains(annotations, TYPE)) {
				return new ValParser();
			}
			return super.queryParser(annotations);
		}
	};

	public static ReduceParser.Factory reduceFactory = new ReduceParser.Factory() {
		@Override
		public ReduceParser reduceParser(Annotation[] annotations) {
			if (AnnotationsUtils.contains(annotations, TYPE)) {
				return new ValParser();
			}
			return super.reduceParser(annotations);
		}
	};

}
