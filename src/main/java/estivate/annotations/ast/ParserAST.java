package estivate.annotations.ast;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import estivate.core.MembersFinder;
import estivate.core.impl.DefaultMembersFinder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ParserAST {

	protected static MembersFinder membersFinder = new DefaultMembersFinder();

	protected static List<SelectPaser.Factory> selectParserFactories = new ArrayList<ParserAST.SelectPaser.Factory>();
	static {

	}
	protected static List<ReducePaser.Factory> reduceParserFactories = new ArrayList<ParserAST.ReducePaser.Factory>();
	static {

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
		parseSelectAndReduce(exp, field.getAnnotations());

		// is not list
		exp.setIsTargetList(false);

		// is list

		return exp;
	}

	private static void parseSelectAndReduce(ExpressionAST exp, Annotation[] annotations) {
		SelectAST selectAST = new SelectAST();
		parseSelect(selectAST, annotations);

		ReduceAST reduceAST = new ReduceAST();
		parseReduce(reduceAST, annotations);

		exp.setSelect(selectAST);
		exp.setReduce(reduceAST);

	}

	private static void parseSelect(SelectAST selectAST, Annotation[] annotations) {
		for (SelectPaser.Factory factory : selectParserFactories) {
			SelectPaser selectParser = factory.selectParser(annotations);
			if (selectParser != null) {
				selectParser.parse(selectAST, annotations);
			}
		}
	}

	private static void parseReduce(ReduceAST reduceAST, Annotation[] annotations) {
		for (ReducePaser.Factory factory : reduceParserFactories) {
			ReducePaser reduceParser = factory.reduceParser(annotations);
			if (reduceParser != null) {
				reduceParser.parse(reduceAST, annotations);
			}
		}
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

		parseSelectAndReduce(exp, method.getAnnotations());

		return exp;
	}

	public static interface SelectPaser {

		void parse(SelectAST selectAst, Annotation[] annotations);

		abstract class Factory {

			SelectPaser selectParser(Annotation[] annotations) {
				return null;
			}

		}

	}

	public static interface ReducePaser {

		void parse(ReduceAST reduceAst, Annotation[] annotations);

		abstract class Factory {

			ReducePaser reduceParser(Annotation[] annotations) {
				return null;
			}

		}

	}

}
