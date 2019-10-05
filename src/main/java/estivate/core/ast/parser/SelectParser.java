package estivate.core.ast.parser;

import java.lang.annotation.Annotation;

import estivate.annotations.Attr;
import estivate.annotations.Is;
import estivate.annotations.Select;
import estivate.annotations.TagName;
import estivate.annotations.Text;
import estivate.annotations.Val;
import estivate.core.ast.EstivateAST;
import estivate.core.ast.ExpressionAST;
import estivate.core.ast.QueryAST;
import estivate.core.ast.lang.SelectQueryAST;
import estivate.core.ast.parser.EstivateParser.AnnotationParser;
import estivate.utils.AnnotationsUtils;
import estivate.utils.StringUtil;

/**
 * Parse {@link Select} annotation
 * 
 * @author Benoit Theunissen
 *
 */
public class SelectParser implements AnnotationParser {

    public static final SelectParser INSTANCE = new SelectParser();

    public void parseAnnotation(EstivateAST ast, Annotation[] annotations) {
        Select annotation = AnnotationsUtils.find(annotations, Select.class);
        if (annotation != null) {
            ast.setQuery(parse(annotation));
        }
    }

    public void parseAnnotation(ExpressionAST ast, Annotation[] annotations) {
        Select annotation = AnnotationsUtils.find(annotations, Select.class);
        if (annotation != null) {
            ast.setQuery(parse(annotation));
        }
    }

    public static QueryAST parse(Select annotation) {
        SelectQueryAST query = new SelectQueryAST();

        query.setUnique(annotation.unique());
        query.setIndex(annotation.index());
        query.setFirst(annotation.first());
        query.setLast(annotation.last());
        query.setQueryString(or(annotation.select(), annotation.value()));

        validate(query);

        return query;
    }

    public static QueryAST parse(Val annotation) {
        SelectQueryAST query = new SelectQueryAST();

        query.setUnique(annotation.unique());
        query.setIndex(annotation.index());
        query.setFirst(annotation.first());
        query.setLast(annotation.last());
        query.setQueryString(annotation.select());

        validate(query);

        return query;
    }

    public static QueryAST parse(Text annotation) {
        SelectQueryAST query = new SelectQueryAST();

        query.setUnique(annotation.unique());
        query.setIndex(annotation.index());
        query.setFirst(annotation.first());
        query.setLast(annotation.last());
        query.setQueryString(annotation.select());

        validate(query);

        return query;
    }

    public static QueryAST parse(Is annotation) {
        SelectQueryAST query = new SelectQueryAST();

        query.setUnique(annotation.unique());
        query.setIndex(annotation.index());
        query.setFirst(annotation.first());
        query.setLast(annotation.last());
        query.setQueryString(annotation.select());

        validate(query);

        return query;
    }

    public static QueryAST parse(Attr annotation) {
        SelectQueryAST query = new SelectQueryAST();

        query.setUnique(annotation.unique());
        query.setIndex(annotation.index());
        query.setFirst(annotation.first());
        query.setLast(annotation.last());
        query.setQueryString(annotation.select());

        validate(query);

        return query;
    }

    public static QueryAST parse(TagName annotation) {
        SelectQueryAST query = new SelectQueryAST();

        query.setUnique(annotation.unique());
        query.setIndex(annotation.index());
        query.setFirst(annotation.first());
        query.setLast(annotation.last());
        query.setQueryString(annotation.select());

        validate(query);

        return query;
    }

    public static void validate(SelectQueryAST ast) {
        if (ast.isFirst() && ast.isLast()) {
            throw new IllegalArgumentException("Select cant be true for first() and last() a the same time");
        }

    }

    private static String or(String value1, String value2) {
        return StringUtil.isBlank(value1) ? value2 : value1;
    }

}
