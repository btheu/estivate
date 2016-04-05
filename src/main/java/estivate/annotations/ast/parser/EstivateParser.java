package estivate.annotations.ast.parser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import estivate.annotations.ast.EmptyQueryAST;
import estivate.annotations.ast.EmptyReduceAST;
import estivate.annotations.ast.EstivateAST;
import estivate.annotations.ast.ExpressionAST;
import estivate.annotations.ast.ExpressionsAST;
import estivate.annotations.ast.FieldExpressionAST;
import estivate.annotations.ast.MethodExpressionAST;
import estivate.annotations.ast.QueryAST;
import estivate.annotations.ast.ReduceAST;
import estivate.core.MembersFinder;
import estivate.core.impl.DefaultMembersFinder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EstivateParser {

	protected static MembersFinder membersFinder = new DefaultMembersFinder();

	protected static List<QueryParser.Factory> queryParserFactories = new ArrayList<QueryParser.Factory>();
	static {
		queryParserFactories.add(SelectParser.queryFactory);
		queryParserFactories.add(AttrParser.queryFactory);
		queryParserFactories.add(TextParser.queryFactory);
	}
	protected static List<ReduceParser.Factory> reduceParserFactories = new ArrayList<ReduceParser.Factory>();
	static {
		reduceParserFactories.add(AttrParser.reduceFactory);
		reduceParserFactories.add(TextParser.reduceFactory);
	}

	public static EstivateAST parse(Class<?> clazz) {
		EstivateAST ast = new EstivateAST();

		ast.setTargetType(clazz);

		parseExpressions(ast, clazz);

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
		List<Field> list = membersFinder.listFields(clazz);
		for (Field field : list) {
			exps.add(parseField(field));
		}
		return exps;
	}

	private static FieldExpressionAST parseField(Field field) {
		FieldExpressionAST exp = new FieldExpressionAST();
		exp.setField(field);

		// common properties
		parseQueryAndReduce(exp, field.getAnnotations());

		// is not list

		// is list

		return exp;
	}

	private static void parseQueryAndReduce(ExpressionAST exp, Annotation[] annotations) {
		exp.setQuery(parseQuery(annotations));
		exp.setReduce(parseReduce(annotations));
	}

	private static QueryAST parseQuery(Annotation[] annotations) {
		for (QueryParser.Factory factory : queryParserFactories) {
			QueryParser queryParser = factory.queryParser(annotations);
			if (queryParser != null) {
				return queryParser.parseQuery(annotations);
			}
		}
		return new EmptyQueryAST();
	}

	private static ReduceAST parseReduce(Annotation[] annotations) {
		for (ReduceParser.Factory factory : reduceParserFactories) {
			ReduceParser reduceParser = factory.reduceParser(annotations);
			if (reduceParser != null) {
				return reduceParser.parseReduce(annotations);
			}
		}
		return new EmptyReduceAST();
	}

	protected static List<ExpressionAST> parseMethods(Class<?> clazz) {

		List<ExpressionAST> exps = new ArrayList<ExpressionAST>();
		List<Method> list = membersFinder.listMethods(clazz);
		for (Method method : list) {
			exps.add(parseMethod(method));
		}
		return exps;
	}

	private static MethodExpressionAST parseMethod(Method method) {
		MethodExpressionAST exp = new MethodExpressionAST();
		exp.setMethod(method);

		parseQueryAndReduce(exp, method.getAnnotations());

		return exp;
	}

	public static interface QueryParser {

		QueryAST parseQuery(Annotation[] annotations);

		abstract class Factory {

			public QueryParser queryParser(Annotation[] annotations) {
				return null;
			}

		}

	}

	public static interface ReduceParser {

		ReduceAST parseReduce(Annotation[] annotations);

		abstract class Factory {

			public ReduceParser reduceParser(Annotation[] annotations) {
				return null;
			}

		}

	}

}
