package estivate.annotations.ast.parser;

import java.lang.annotation.Annotation;

import estivate.annotations.Text;
import estivate.annotations.ast.QueryAST;
import estivate.annotations.ast.ReduceAST;
import estivate.annotations.ast.parser.EstivateParser.QueryParser;
import estivate.annotations.ast.parser.EstivateParser.ReduceParser;
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

		Text aText = (Text) AnnotationsUtils.find(annotations, TYPE);

		SelectQueryAST query = new SelectQueryAST();

		query.setUnique(aText.unique());
		query.setIndex(aText.index());
		query.setFirst(aText.first());
		query.setLast(aText.last());
		query.setQueryString(aText.select());

		return query;
	}

	public ReduceAST parseReduce(Annotation[] annotations) {
	    
	    Text aText = (Text) AnnotationsUtils.find(annotations, TYPE);
	    
	    TextReduceAST reduce = new TextReduceAST();
	    reduce.setOwn(aText.own());
	    
		return reduce;
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
