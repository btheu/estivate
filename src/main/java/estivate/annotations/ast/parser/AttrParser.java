package estivate.annotations.ast.parser;

import java.lang.annotation.Annotation;

import estivate.annotations.Attr;
import estivate.annotations.ast.EstivateParserAST.QueryParser;
import estivate.annotations.ast.EstivateParserAST.ReduceParser;
import estivate.annotations.ast.QueryAST;
import estivate.annotations.ast.ReduceAST;
import estivate.utils.AnnotationsUtils;

/**
 * Parse {@link Attr} annotation
 * 
 * @author Benoit Theunissen
 *
 */
public class AttrParser implements QueryParser, ReduceParser {

	public QueryAST parseQuery(Annotation[] annotations) {

		Attr aAttr = AnnotationsUtils.find(annotations, Attr.class);

		SelectQueryAST query = new SelectQueryAST();

		query.setUnique(aAttr.unique());
		query.setIndex(aAttr.index());
		query.setFirst(aAttr.first());
		query.setLast(aAttr.last());
		query.setQueryString(aAttr.select());

		return query;
	}

	public ReduceAST parseReduce(Annotation[] annotations) {
		Attr aAttr = AnnotationsUtils.find(annotations, Attr.class);

		AttrReduceAST reduce = new AttrReduceAST();

		reduce.setAttr(aAttr.value());

		return reduce;
	}

	public static QueryParser.Factory queryFactory = new QueryParser.Factory() {
		@Override
		public QueryParser queryParser(Annotation[] annotations) {
			if (AnnotationsUtils.contains(annotations, Attr.class)) {
				return new AttrParser();
			}
			return super.queryParser(annotations);
		}
	};

	public static ReduceParser.Factory reduceFactory = new ReduceParser.Factory() {
		@Override
		public ReduceParser reduceParser(Annotation[] annotations) {
			if (AnnotationsUtils.contains(annotations, Attr.class)) {
				return new AttrParser();
			}
			return super.reduceParser(annotations);
		}
	};

}
