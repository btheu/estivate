package estivate.core.ast.parser;

import java.lang.annotation.Annotation;

import estivate.annotations.Is;
import estivate.core.ast.EstivateAST;
import estivate.core.ast.ExpressionAST;
import estivate.core.ast.lang.IsReduceAST;
import estivate.core.ast.parser.EstivateParser.AnnotationParser;
import estivate.utils.AnnotationsUtils;

public class IsParser implements AnnotationParser {

    public static final IsParser INSTANCE = new IsParser();

    public static final Class<? extends Annotation> TYPE = Is.class;

    public void parseAnnotation(EstivateAST ast, Annotation[] annotations) {
    }

    public void parseAnnotation(ExpressionAST ast, Annotation[] annotations) {
        Is annotation = (Is) AnnotationsUtils.find(annotations, TYPE);
        if (annotation != null) {
            ast.setOptional(annotation.optional());
            ast.addQuery(SelectParser.parse(annotation));

            IsReduceAST reduce = new IsReduceAST();

            reduce.setIs(annotation.value());

            ast.setReduce(reduce);
        }

    }

}
