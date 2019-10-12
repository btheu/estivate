package estivate.core.ast.parser;

import java.lang.annotation.Annotation;

import estivate.annotations.Attr;
import estivate.core.ast.EstivateAST;
import estivate.core.ast.ExpressionAST;
import estivate.core.ast.lang.AttrReduceAST;
import estivate.core.ast.parser.EstivateParser.AnnotationParser;
import estivate.utils.AnnotationsUtils;

/**
 * Parse {@link Attr} annotation
 * 
 * @author Benoit Theunissen
 *
 */
public class AttrParser implements AnnotationParser {

    public static final AttrParser INSTANCE = new AttrParser();

    public static final Class<? extends Annotation> TYPE = Attr.class;

    public void parseAnnotation(EstivateAST ast, Annotation[] annotations) {
    }

    public void parseAnnotation(ExpressionAST ast, Annotation[] annotations) {

        Attr annotation = (Attr) AnnotationsUtils.find(annotations, TYPE);
        if (annotation != null) {
            ast.addQuery(SelectParser.parse(annotation));

            AttrReduceAST reduce = new AttrReduceAST();

            reduce.setAttr(annotation.value());

            ast.setReduce(reduce);

        }
    }

}
