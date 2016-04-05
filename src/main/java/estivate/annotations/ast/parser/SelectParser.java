package estivate.annotations.ast.parser;

import java.lang.annotation.Annotation;

import org.jsoup.helper.StringUtil;

import estivate.annotations.Attr;
import estivate.annotations.Select;
import estivate.annotations.ast.EstivateParserAST.QueryParser;
import estivate.annotations.ast.QueryAST;
import estivate.utils.AnnotationsUtils;

/**
 * Parse {@link Attr} annotation
 * 
 * @author Benoit Theunissen
 *
 */
public class SelectParser implements QueryParser {

	public QueryAST parseQuery(Annotation[] annotations) {

		Select aSelect = AnnotationsUtils.find(annotations, Select.class);

		SelectQueryAST query = new SelectQueryAST();

		query.setUnique(aSelect.unique());
		query.setIndex(aSelect.index());
		query.setFirst(aSelect.first());
		query.setLast(aSelect.last());
		query.setQueryString(or(aSelect.select(), aSelect.value()));

		return query;
	}

	public static QueryParser.Factory queryFactory = new QueryParser.Factory() {
		@Override
		public QueryParser queryParser(Annotation[] annotations) {
			if (AnnotationsUtils.contains(annotations, Select.class)) {
				return new SelectParser();
			}
			return super.queryParser(annotations);
		}
	};

	private String or(String value1, String value2) {
		return StringUtil.isBlank(value1) ? value2 : value1;
	}

}
