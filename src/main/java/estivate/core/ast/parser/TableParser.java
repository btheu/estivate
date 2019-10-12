package estivate.core.ast.parser;

import java.lang.annotation.Annotation;

import estivate.annotations.Table;
import estivate.core.ast.EstivateAST;
import estivate.core.ast.ExpressionAST;
import estivate.core.ast.QueryAST;
import estivate.core.ast.lang.TableQueryAST;
import estivate.core.ast.parser.EstivateParser.AnnotationParser;
import estivate.utils.AnnotationsUtils;

/**
 * Parse {@link Table} annotation
 * 
 * @author Benoit Theunissen
 *
 */
public class TableParser implements AnnotationParser {

    public static final TableParser INSTANCE = new TableParser();

    public static final Class<Table> TYPE = Table.class;

    public void parseAnnotation(EstivateAST ast, Annotation[] annotations) {
        Table annotation = AnnotationsUtils.find(annotations, TYPE);
        if (annotation != null) {
            ast.setQuery(parse(annotation));
        }
    }

    public void parseAnnotation(ExpressionAST ast, Annotation[] annotations) {
        Table annotation = AnnotationsUtils.find(annotations, TYPE);
        if (annotation != null) {
            ast.setOptional(annotation.optional());
            ast.setQuery(parse(annotation));
        }
    }

    public static QueryAST parse(Table annotation) {
        TableQueryAST query = new TableQueryAST();

        query.setUnique(true);
        query.setIndex(annotation.index());
        query.setFirst(annotation.first());
        query.setLast(annotation.last());
        query.setQueryString(SelectParser.or(annotation.select(), annotation.value()));

        SelectParser.validate(query);

        return query;
    }
}
