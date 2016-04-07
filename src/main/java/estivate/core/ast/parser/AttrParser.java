package estivate.core.ast.parser;

import java.lang.annotation.Annotation;

import estivate.annotations.Attr;
import estivate.core.ast.QueryAST;
import estivate.core.ast.ReduceAST;
import estivate.core.ast.lang.AttrReduceAST;
import estivate.core.ast.lang.SelectQueryAST;
import estivate.core.ast.parser.EstivateParser.QueryParser;
import estivate.core.ast.parser.EstivateParser.ReduceParser;
import estivate.utils.AnnotationsUtils;

/**
 * Parse {@link Attr} annotation
 * 
 * @author Benoit Theunissen
 *
 */
public class AttrParser implements QueryParser, ReduceParser {

    public static final Class<? extends Annotation> TYPE = Attr.class;
    
	public QueryAST parseQuery(Annotation[] annotations) {

		Attr aAttr = (Attr) AnnotationsUtils.find(annotations, TYPE);

		SelectQueryAST query = new SelectQueryAST();

		query.setUnique(aAttr.unique());
		query.setIndex(aAttr.index());
		query.setFirst(aAttr.first());
		query.setLast(aAttr.last());
		query.setQueryString(aAttr.select());

		return query;
	}

	public ReduceAST parseReduce(Annotation[] annotations) {
	    
		Attr aAttr = (Attr) AnnotationsUtils.find(annotations, TYPE);

		AttrReduceAST reduce = new AttrReduceAST();

		reduce.setAttr(aAttr.value());

		return reduce;
	}

	public static QueryParser.Factory queryFactory = new QueryParser.Factory() {
		@Override
		public QueryParser queryParser(Annotation[] annotations) {
			if (AnnotationsUtils.contains(annotations, TYPE)) {
				return new AttrParser();
			}
			return super.queryParser(annotations);
		}
	};

	public static ReduceParser.Factory reduceFactory = new ReduceParser.Factory() {
		@Override
		public ReduceParser reduceParser(Annotation[] annotations) {
			if (AnnotationsUtils.contains(annotations, TYPE)) {
				return new AttrParser();
			}
			return super.reduceParser(annotations);
		}
	};

}
