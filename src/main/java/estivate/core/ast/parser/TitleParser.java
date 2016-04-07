package estivate.core.ast.parser;

import java.lang.annotation.Annotation;

import estivate.annotations.Title;
import estivate.core.ast.ReduceAST;
import estivate.core.ast.lang.TitleReduceAST;
import estivate.core.ast.parser.EstivateParser.ReduceParser;
import estivate.utils.AnnotationsUtils;

/**
 * Parse {@link Title} annotation
 * 
 * @author Benoit Theunissen
 *
 */
public class TitleParser implements ReduceParser {

	public static final Class<? extends Annotation> TYPE = Title.class;

	public ReduceAST parseReduce(Annotation[] annotations) {

		Title annotation = (Title) AnnotationsUtils.find(annotations, TYPE);

		TitleReduceAST reduce = new TitleReduceAST();
		reduce.setOptional(annotation.optional());

		return reduce;
	}

	public static ReduceParser.Factory reduceFactory = new ReduceParser.Factory() {
		@Override
		public ReduceParser reduceParser(Annotation[] annotations) {
			if (AnnotationsUtils.contains(annotations, TYPE)) {
				return new TitleParser();
			}
			return super.reduceParser(annotations);
		}
	};

}
