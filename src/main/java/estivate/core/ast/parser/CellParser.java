package estivate.core.ast.parser;

import java.lang.annotation.Annotation;
import java.util.regex.Pattern;

import estivate.annotations.Cell;
import estivate.annotations.Select;
import estivate.core.ast.EstivateAST;
import estivate.core.ast.ExpressionAST;
import estivate.core.ast.QueryAST;
import estivate.core.ast.lang.CellQueryAST;
import estivate.core.ast.parser.EstivateParser.AnnotationParser;
import estivate.utils.AnnotationsUtils;
import estivate.utils.StringUtil;

/**
 * Parse {@link Select} annotation
 * 
 * @author Benoit Theunissen
 *
 */
public class CellParser implements AnnotationParser {

    public static final CellParser INSTANCE = new CellParser();

    public void parseAnnotation(EstivateAST ast, Annotation[] annotations) {
        Cell annotation = AnnotationsUtils.find(annotations, Cell.class);
        if (annotation != null) {
            ast.setQuery(parse(annotation));
        }
    }

    public void parseAnnotation(ExpressionAST ast, Annotation[] annotations) {
        Cell annotation = AnnotationsUtils.find(annotations, Cell.class);
        if (annotation != null) {
            ast.setQuery(parse(annotation));
        }
    }

    public static QueryAST parse(Cell annotation) {
        CellQueryAST query = new CellQueryAST();

        String value = or(annotation.name(), annotation.value());
        query.setColumnName(value);
        if (annotation.regex()) {
            query.setRegex(Pattern.compile(value));
        }

        return query;
    }

    private static String or(String value1, String value2) {
        return StringUtil.isBlank(value1) ? value2 : value1;
    }

}
