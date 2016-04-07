package estivate.core.ast.parser;

import java.lang.annotation.Annotation;

import estivate.annotations.Attr;
import estivate.core.ast.EstivateAST;
import estivate.core.ast.ExpressionAST;
import estivate.core.ast.QueryAST;
import estivate.core.ast.ReduceAST;
import estivate.core.ast.lang.AttrReduceAST;
import estivate.core.ast.parser.EstivateParser.QueryParser;
import estivate.core.ast.parser.EstivateParser.ReduceParser;
import estivate.core.ast.parser2.EstivateParser2.AnnotationParser;
import estivate.utils.AnnotationsUtils;

/**
 * Parse {@link Attr} annotation
 * 
 * @author Benoit Theunissen
 *
 */
public class AttrParser implements AnnotationParser, QueryParser, ReduceParser {

	public static final Class<? extends Annotation> TYPE = Attr.class;

    public void parseAnnotation(EstivateAST ast, Annotation[] annotations) {}

    public void parseAnnotation(ExpressionAST ast, Annotation[] annotations) {
        
        Attr annotation = (Attr) AnnotationsUtils.find(annotations, TYPE);
        
        ast.setQuery(SelectParser.parse(annotation));
    }
    
	@Deprecated
	public QueryAST parseQuery(Annotation[] annotations) {

		Attr aAttr = (Attr) AnnotationsUtils.find(annotations, TYPE);

		return SelectParser.parse(aAttr);
	}

	@Deprecated
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
