package estivate.annotations.ast.parser;

import java.lang.annotation.Annotation;

import estivate.annotations.Text;
import estivate.annotations.ast.EstivateParserAST.QueryParser;
import estivate.annotations.ast.EstivateParserAST.ReduceParser;
import estivate.annotations.ast.QueryAST;
import estivate.annotations.ast.ReduceAST;
import estivate.utils.AnnotationsUtils;

/**
 * Parse {@link Text} annotation
 * 
 * @author Benoit Theunissen
 *
 */
public class TextParser implements QueryParser, ReduceParser {

	public static final Class<? extends Annotation> TYPE = Text.class;

	public QueryAST parseQuery(Annotation[] annotations) {

		Text aAttr = (Text) AnnotationsUtils.find(annotations, TYPE);

		SelectQueryAST query = new SelectQueryAST();

		query.setUnique(aAttr.unique());
		query.setIndex(aAttr.index());
		query.setFirst(aAttr.first());
		query.setLast(aAttr.last());
		query.setQueryString(aAttr.select());

		return query;
	}

	public ReduceAST parseReduce(Annotation[] annotations) {
		return new TextReduceAST();
	}

	public static QueryParser.Factory queryFactory = new QueryParser.Factory() {
		@Override
		public QueryParser queryParser(Annotation[] annotations) {
			if (AnnotationsUtils.contains(annotations, TYPE)) {
				return new TextParser();
			}
			return super.queryParser(annotations);
		}
	};

	public static ReduceParser.Factory reduceFactory = new ReduceParser.Factory() {
		@Override
		public ReduceParser reduceParser(Annotation[] annotations) {
			if (AnnotationsUtils.contains(annotations, TYPE)) {
				return new TextParser();
			}
			return super.reduceParser(annotations);
		}
	};

}
