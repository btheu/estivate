package estivate.core.ast.parser;

import java.lang.annotation.Annotation;

import estivate.annotations.Text;
import estivate.core.ast.EstivateAST;
import estivate.core.ast.ExpressionAST;
import estivate.core.ast.QueryAST;
import estivate.core.ast.lang.TextReduceAST;
import estivate.core.ast.parser.EstivateParser.AnnotationParser;
import estivate.utils.AnnotationsUtils;

/**
 * Parse {@link Text} annotation
 * 
 * @author Benoit Theunissen
 *
 */
public class TextParser implements AnnotationParser {

    public static final TextParser INSTANCE = new TextParser();

    public void parseAnnotation(EstivateAST ast, Annotation[] annotations) {
    }

    public void parseAnnotation(ExpressionAST ast, Annotation[] annotations) {
        Text annotation = AnnotationsUtils.find(annotations, Text.class);
        if (annotation != null) {
            ast.setOptional(annotation.optional());

            QueryAST query = SelectParser.parse(annotation);
            ast.addQuery(query);

            TextReduceAST reduce = new TextReduceAST();
            reduce.setOwn(annotation.own());
            ast.setReduce(reduce);
        }
    }

}
