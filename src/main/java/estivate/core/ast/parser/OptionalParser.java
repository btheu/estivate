package estivate.core.ast.parser;

import java.lang.annotation.Annotation;

import estivate.annotations.Optional;
import estivate.core.ast.EstivateAST;
import estivate.core.ast.ExpressionAST;
import estivate.core.ast.parser.EstivateParser.AnnotationParser;
import estivate.utils.AnnotationsUtils;

/**
 * Parse {@link Optional} annotation
 * 
 * @author Benoit Theunissen
 *
 */
public class OptionalParser implements AnnotationParser {

    public static final OptionalParser INSTANCE = new OptionalParser();

    public static final Class<? extends Annotation> TYPE = Optional.class;

    public void parseAnnotation(EstivateAST ast, Annotation[] annotations) {
        Optional annotation = (Optional) AnnotationsUtils.find(annotations, TYPE);
        if (annotation != null) {
            ast.setOptional(annotation.value());
        }
    }

    public void parseAnnotation(ExpressionAST ast, Annotation[] annotations) {
        Optional annotation = (Optional) AnnotationsUtils.find(annotations, TYPE);
        if (annotation != null) {
            ast.setOptional(annotation.value());
        }
    }

}
