package estivate.core.ast.parser;

import java.lang.annotation.Annotation;

import estivate.annotations.Convert;
import estivate.core.ast.EstivateAST;
import estivate.core.ast.ExpressionAST;
import estivate.core.ast.lang.CustomConverterAST;
import estivate.core.ast.parser.EstivateParser.AnnotationParser;
import estivate.utils.AnnotationsUtils;

public class CustomConvertorParser implements AnnotationParser {

    public static final CustomConvertorParser INSTANCE = new CustomConvertorParser();

    public void parseAnnotation(EstivateAST ast, Annotation[] annotations) {
    }

    public void parseAnnotation(ExpressionAST ast, Annotation[] annotations) {
        Convert annotation = AnnotationsUtils.find(annotations, Convert.class);
        if (annotation != null) {
            CustomConverterAST convertAst = new CustomConverterAST();

            convertAst.setConverterClass(annotation.value());
            convertAst.setFormat(annotation.format());

            ast.setConverter(convertAst);
        }
    }

}
