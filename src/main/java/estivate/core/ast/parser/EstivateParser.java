package estivate.core.ast.parser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import estivate.core.ClassUtils;
import estivate.core.MembersFinder;
import estivate.core.ast.EmptyQueryAST;
import estivate.core.ast.EmptyReduceAST;
import estivate.core.ast.EstivateAST;
import estivate.core.ast.ExpressionAST;
import estivate.core.ast.ExpressionsAST;
import estivate.core.ast.FieldExpressionAST;
import estivate.core.ast.MethodExpressionAST;
import estivate.core.ast.QueryAST;
import estivate.core.ast.ReduceAST;
import estivate.core.ast.ValueAST;
import estivate.core.impl.DefaultMembersFinder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EstivateParser {

    protected static MembersFinder membersFinder = new DefaultMembersFinder();

    protected static List<QueryParser.Factory> queryParserFactories = new ArrayList<QueryParser.Factory>();
    static {
        queryParserFactories.add(AttrParser.queryFactory);
        queryParserFactories.add(SelectParser.queryFactory);
        queryParserFactories.add(TagNameParser.queryFactory);
        queryParserFactories.add(TextParser.queryFactory);
        queryParserFactories.add(ValParser.queryFactory);
    }
    protected static List<ReduceParser.Factory> reduceParserFactories = new ArrayList<ReduceParser.Factory>();
    static {
        reduceParserFactories.add(AttrParser.reduceFactory);
        reduceParserFactories.add(TagNameParser.reduceFactory);
        reduceParserFactories.add(TextParser.reduceFactory);
        reduceParserFactories.add(TitleParser.reduceFactory);
        reduceParserFactories.add(ValParser.reduceFactory);
    }

    public static EstivateAST parse(Class<?> clazz) {
        EstivateAST ast = new EstivateAST();


        ast.setQuery(parseQuery(clazz.getAnnotations()));
        ast.setTargetType(clazz);
        ast.setTargetRawClass(ClassUtils.rawType(clazz));

        parseExpressions(ast, clazz);

        log.debug("AST of '{}' is {}",clazz.toString(), ast);

        return ast;
    }

    protected static void parseExpressions(ExpressionsAST ast, Class<?> clazz) {

        List<ExpressionAST> fieldExpressions = parseFields(clazz);
        List<ExpressionAST> methodExpressions = parseMethods(clazz);

        ast.getExpressions().addAll(fieldExpressions);
        ast.getExpressions().addAll(methodExpressions);

    }

    protected static List<ExpressionAST> parseFields(Class<?> clazz) {
        List<ExpressionAST> exps = new ArrayList<ExpressionAST>();
        List<Field> fields = membersFinder.listFields(clazz);
        for (Field field : fields) {
            FieldExpressionAST exp = parseField(field);
            if(!isEmptyExpression(exp)){
                exps.add(exp);
            }
        }
        return exps;
    }

    protected static List<ExpressionAST> parseMethods(Class<?> clazz) {
        List<ExpressionAST> exps = new ArrayList<ExpressionAST>();
        List<Method> methods = membersFinder.listMethods(clazz);
        for (Method method : methods) {
            MethodExpressionAST exp = parseMethod(method);
            if(!isEmptyExpression(exp)){
                exps.add(exp);
            }
        }
        return exps;
    }

    private static boolean isEmptyExpression(ExpressionAST exp) {
        return exp.getReduce() instanceof EmptyReduceAST && exp.getQuery() instanceof EmptyQueryAST;
    }

    private static FieldExpressionAST parseField(Field field) {
        FieldExpressionAST exp = new FieldExpressionAST();
        exp.setField(field);

        Type type = field.getGenericType();

        // common properties
        Annotation[] annotations = field.getAnnotations();

        exp.setQuery(parseQuery(annotations));
        exp.setReduce(parseReduce(annotations));

        // value
        ValueAST value = createValue(type);

        exp.setValue(value);

        return exp;
    }

    private static MethodExpressionAST parseMethod(Method method) {
        MethodExpressionAST exp = new MethodExpressionAST();
        exp.setMethod(method);

        // common properties
        Annotation[] annotations = method.getAnnotations();

        exp.setQuery(parseQuery(annotations));
        exp.setReduce(parseReduce(annotations));

        // values
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        for (Type type : genericParameterTypes) {

            ValueAST value = createValue(type);

            exp.getArguments().add(value);
        }

        return exp;
    }

    private static ValueAST createValue(Type type) {
        Class<?> rawType = ClassUtils.rawType(type);

        ValueAST value = new ValueAST();
        value.setType(type);
        value.setRawClass(rawType);
        value.setValueList(rawType.equals(List.class));

        return value;
    }

    @Deprecated
    public static interface QueryParser {

        QueryAST parseQuery(Annotation[] annotations);

        abstract class Factory {

            public QueryParser queryParser(Annotation[] annotations) {
                return null;
            }

        }

    }

    @Deprecated
    private static QueryAST parseQuery(Annotation[] annotations) {
        for (QueryParser.Factory factory : queryParserFactories) {
            QueryParser queryParser = factory.queryParser(annotations);
            if (queryParser != null) {
                return queryParser.parseQuery(annotations);
            }
        }
        return new EmptyQueryAST();
    }

    @Deprecated
    private static ReduceAST parseReduce(Annotation[] annotations) {
        for (ReduceParser.Factory factory : reduceParserFactories) {
            ReduceParser reduceParser = factory.reduceParser(annotations);
            if (reduceParser != null) {
                return reduceParser.parseReduce(annotations);
            }
        }
        return new EmptyReduceAST();
    }


    @Deprecated
    public static interface ReduceParser {

        ReduceAST parseReduce(Annotation[] annotations);

        abstract class Factory {

            public ReduceParser reduceParser(Annotation[] annotations) {
                return null;
            }

        }

    }

}
