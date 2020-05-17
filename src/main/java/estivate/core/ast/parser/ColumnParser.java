package estivate.core.ast.parser;

import java.lang.annotation.Annotation;
import java.util.regex.Pattern;

import estivate.annotations.Column;
import estivate.annotations.Select;
import estivate.core.ast.EstivateAST;
import estivate.core.ast.ExpressionAST;
import estivate.core.ast.QueryAST;
import estivate.core.ast.lang.ColumnQueryAST;
import estivate.core.ast.parser.EstivateParser.AnnotationParser;
import estivate.utils.AnnotationsUtils;
import estivate.utils.StringUtil;

/**
 * Parse {@link Select} annotation
 * 
 * @author Benoit Theunissen
 *
 */
public class ColumnParser implements AnnotationParser {

    public static final ColumnParser INSTANCE = new ColumnParser();

    public void parseAnnotation(EstivateAST ast, Annotation[] annotations) {
        Column annotation = AnnotationsUtils.find(annotations, Column.class);
        if (annotation != null) {
            ast.addQuery(parse(annotation));
        }
    }

    public void parseAnnotation(ExpressionAST ast, Annotation[] annotations) {
        Column annotation = AnnotationsUtils.find(annotations, Column.class);
        if (annotation != null) {
            ast.setOptional(annotation.optional());
            ast.addQuery(parse(annotation));
        }
    }

    public static QueryAST parse(Column annotation) {
        ColumnQueryAST query = new ColumnQueryAST();

        String value = or(annotation.name(), annotation.value());
        query.setColumnName(value);
        if (annotation.regex()) {
            query.setRegex(Pattern.compile(value));
        }

        if (StringUtil.isNotBlank(annotation.thClass())) {
            query.setHavingExpr("." + annotation.thClass());
        }
        if (StringUtil.isNotBlank(annotation.thHaving())) {
            query.setHavingExpr(annotation.thHaving());
        }

        return query;
    }

    private static String or(String value1, String value2) {
        return StringUtil.isBlank(value1) ? value2 : value1;
    }

}
