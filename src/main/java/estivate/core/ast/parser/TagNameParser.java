package estivate.core.ast.parser;

import java.lang.annotation.Annotation;

import estivate.annotations.TagName;
import estivate.core.ast.EstivateAST;
import estivate.core.ast.ExpressionAST;
import estivate.core.ast.lang.TagNameReduceAST;
import estivate.core.ast.parser.EstivateParser.AnnotationParser;
import estivate.utils.AnnotationsUtils;

/**
 * Parse {@link TagName} annotation
 * 
 * @author Benoit Theunissen
 *
 */
public class TagNameParser implements AnnotationParser {

    public static final TagNameParser INSTANCE = new TagNameParser();

    public static final Class<? extends Annotation> TYPE = TagName.class;

    public void parseAnnotation(EstivateAST ast, Annotation[] annotations) {
    }

    public void parseAnnotation(ExpressionAST ast, Annotation[] annotations) {

        TagName annotation = (TagName) AnnotationsUtils.find(annotations, TYPE);
        if (annotation != null) {
            ast.setOptional(annotation.optional());
            ast.addQuery(SelectParser.parse(annotation));

            TagNameReduceAST reduce = new TagNameReduceAST();

            ast.setReduce(reduce);

        }
    }

}
