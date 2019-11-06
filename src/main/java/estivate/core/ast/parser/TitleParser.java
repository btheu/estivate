package estivate.core.ast.parser;

import java.lang.annotation.Annotation;

import estivate.annotations.Title;
import estivate.core.ast.EstivateAST;
import estivate.core.ast.ExpressionAST;
import estivate.core.ast.lang.TitleReduceAST;
import estivate.core.ast.parser.EstivateParser.AnnotationParser;
import estivate.utils.AnnotationsUtils;

/**
 * Parse {@link Title} annotation
 * 
 * @author Benoit Theunissen
 *
 */
public class TitleParser implements AnnotationParser {

    public static final TitleParser INSTANCE = new TitleParser();

    public static final Class<? extends Annotation> TYPE = Title.class;

    public void parseAnnotation(EstivateAST ast, Annotation[] annotations) {
    }

    public void parseAnnotation(ExpressionAST ast, Annotation[] annotations) {

        Title annotation = (Title) AnnotationsUtils.find(annotations, TYPE);
        if (annotation != null) {
            ast.setOptional(annotation.optional());

            TitleReduceAST reduce = new TitleReduceAST();

            ast.setReduce(reduce);
        }
    }

}
