package estivate.core.eval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import estivate.core.ClassUtils;
import estivate.core.Converter;
import estivate.core.ast.ConverterAST;
import estivate.core.ast.EstivateAST;
import estivate.core.ast.ExpressionAST;
import estivate.core.ast.ExpressionsAST;
import estivate.core.ast.FieldExpressionAST;
import estivate.core.ast.MethodExpressionAST;
import estivate.core.ast.QueryAST;
import estivate.core.ast.ReduceAST;
import estivate.core.ast.ValueAST;
import estivate.core.ast.lang.CustomConverterAST;
import estivate.core.ast.lang.ListValueAST;
import estivate.core.ast.lang.SimpleValueAST;
import estivate.core.eval.lang.AttrReduceEvaluator;
import estivate.core.eval.lang.SelectQueryEvaluator;
import estivate.core.eval.lang.TagNameReduceEvaluator;
import estivate.core.eval.lang.TextReduceEvaluator;
import estivate.core.eval.lang.TitleReduceEvaluator;
import estivate.core.eval.lang.ValReduceEvaluator;
import estivate.core.impl.PrimitiveConverter;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EstivateEvaluator2 {

	protected static PrimitiveConverter primitiveConverter = new PrimitiveConverter();

	public static Object eval(Document document, EstivateAST ast) {
		return eval(document, new Elements(document), ast);
	}

	private static Object eval(Document document, Elements queryResult, EstivateAST ast) {

		Object target = ClassUtils.newInstance(ast.getTargetRawClass());

		EvalContext context = new EvalContext.EvalContextBuilder()
				.target(target)
				.document(document)
				.queryResult(queryResult)
				.optional(ast.isOptional())
				.value(new HashMap<ValueAST, Object>())
				.build();

		evalExpressions(context.toBuilder().build(), ast);

		return target;
	}

	public static List<?> evalToList(Document document, EstivateAST ast) {
		return evalToList(document, new Elements(document), ast);
	}

	private static List<?> evalToList(Document document, Elements queryResult, EstivateAST ast) {

		// eval class query 
		EvalContext context = evalQuery(document, queryResult, ast);

		List<Object> results = new ArrayList<Object>();

		for (Element element : context.getQueryResult()) {

			Object target = ClassUtils.newInstance(ast.getTargetRawClass());

			// copy of the context with new queryResult
			evalExpressions(context.toBuilder()
					.target(target)
					.queryResult(new Elements(element))
					.build(), ast);

			results.add(target);
		}

		return results;
	}

	private static EvalContext evalQuery(Document document,
			Elements queryResult, EstivateAST ast) {
		EvalContext context = new EvalContext.EvalContextBuilder()
				.document(document)
				.queryResult(queryResult)
				.optional(ast.isOptional())
				.value(new HashMap<ValueAST, Object>())
				.build();

		evalQuery(context, ast.getQuery());
		return context;
	}

	public static void evalExpressions(EvalContext context, ExpressionsAST ast) {

		// Eval Query
		evalQuery(context,ast.getQuery());

		List<ExpressionAST> expressions = ast.getExpressions();
		for (ExpressionAST expression : expressions) {
			// copy of the context
			evalExpression(context.toBuilder().build(), expression);
		}
	}

	protected static void evalExpression(EvalContext context, ExpressionAST expression) {
		for (ExpressionEvaluator eval : EXPRESSION_EVALUATORS) {
			eval.evalExpression(context, expression);
		}
	}

	private static void evalConvert(EvalContext context, ConverterAST converter, SimpleValueAST value) {

		Object currentValue = context.getValue().get(value);

		Class<?> targetType = value.getRawClass();

		// Standard assignment
		if(targetType.equals(Document.class)){
			context.getValue().put(value, context.getDocument());
			return;
		}
		if(targetType.equals(Elements.class)){
			context.getValue().put(value, context.getQueryResult());
			return;
		}
		if(targetType.equals(Element.class)){
			Elements dom = context.getQueryResult();
			if (dom.size() == 1) {
				context.getValue().put(value,dom.first());
			} else {
				throw new IllegalArgumentException("Cant eval single Element value. Size of the selected DOM was '" + dom.size() + "'");
			}
			return;
		}

		// Custom Convert

		if(converter instanceof CustomConverterAST){
			CustomConverterAST customConverter = (CustomConverterAST) converter;

			Converter newInstance = ClassUtils.newInstance(customConverter.getConverterClass());

			Object convertedValue = newInstance.convert(currentValue, targetType);

			context.getValue().put(value, convertedValue);

			return;
		}

		// Primitive Convert

		if(primitiveConverter.canConvert(currentValue, targetType)){

			Object convertedValue = primitiveConverter.convert(currentValue, targetType);

			context.getValue().put(value, convertedValue);

			return;
		}

		// HTML to String
		if(currentValue.getClass().equals(Elements.class) && targetType.equals(String.class)){
			log.debug("> String convert");
			context.getValue().put(value, currentValue.toString());
			log.debug("< String convert");
			return;
		}
		
		// Recursive assignment
		if(currentValue.getClass().equals(Elements.class)){
			log.debug("> recursive convert");
			if(value.isValueList()){
				currentValue =  evalToList(context.document,context.getQueryResult(),value.getAst());
			}else{
				currentValue = eval(context.document,context.getQueryResult(),value.getAst());
			}
			context.getValue().put(value, currentValue);
			log.debug("< recursive convert");
			return;
		}

		// Direct assignment
		if(ClassUtils.isAssignableValue(targetType, currentValue)){
			context.getValue().put(value, currentValue);
			return;
		}



	}

	private static void evalConvert(EvalContext context, ConverterAST converter, ListValueAST values) {
		List<SimpleValueAST> values2 = values.getValues();
		for (SimpleValueAST simpleValueAST : values2) {
			evalConvert(context.toBuilder().build(), converter, simpleValueAST);
		}
	}

	private static void evalReduce(EvalContext context, ReduceAST reduce, ValueAST value) {
		if(value instanceof SimpleValueAST){
			SimpleValueAST simpleValueAST = (SimpleValueAST) value;

			evalReduceSimpleValue( context,  reduce,  simpleValueAST);
		}
		if(value instanceof ListValueAST){
			ListValueAST listValueAST = (ListValueAST) value;
			for (SimpleValueAST simpleValueAST : listValueAST.getValues()) {

				evalReduceSimpleValue( context,  reduce,  simpleValueAST);
			}
		}
	}

	private static void evalReduceSimpleValue(EvalContext context, ReduceAST reduce, SimpleValueAST simpleValueAST) {
		log.debug("{} > eval reduce : {}",context.getMemberName(),context.getQueryResult());
		for (ReduceEvaluator eval : REDUCE_EVALUATORS) {
			eval.evalReduce(context, reduce, simpleValueAST);
		}
		log.debug("{} < eval reduce : {}",context.getMemberName(),context.getQueryResult());
	}

	private static void evalQuery(EvalContext context, QueryAST query) {
		log.debug("{} > eval query : {}",context.getMemberName(),context.getQueryResult());
		for (QueryEvaluator eval : QUERY_EVALUATORS) {
			eval.evalQuery(context, query);
		}
		log.debug("{} < eval query : {}",context.getMemberName(),context.getQueryResult());
	}


	public static final ExpressionEvaluator fieldEvaluator = new ExpressionEvaluator() {

		public void evalExpression(EvalContext context, ExpressionAST expression) {
			if(expression instanceof FieldExpressionAST){

				FieldExpressionAST fieldExpression = (FieldExpressionAST) expression;

				context.setMemberName(fieldExpression.getField().getName());

				// Query
				evalQuery(context, fieldExpression.getQuery());

				// Reduce
				evalReduce(context, fieldExpression.getReduce(), fieldExpression.getValue());

				// Convert
				evalConvert(context, fieldExpression.getConverter(), fieldExpression.getValue());

				// Value
				evalValue(context, fieldExpression);
			}
		}

		private void evalValue(EvalContext context, FieldExpressionAST expression) {
			ClassUtils.setValue(expression.getField(), context.getTarget(), context.getValue().get(expression.getValue()));
		}

	};
	public static final ExpressionEvaluator methodEvaluator = new ExpressionEvaluator() {

		public void evalExpression(EvalContext context, ExpressionAST expression) {
			if(expression instanceof MethodExpressionAST){

				MethodExpressionAST methodExpression = (MethodExpressionAST) expression;

				context.setMemberName(methodExpression.getMethod().getName());

				// Query
				evalQuery(context, methodExpression.getQuery());

				// Reduce
				evalReduce(context, methodExpression.getReduce(), methodExpression.getValues());

				// Convert
				evalConvert(context, methodExpression.getConverter(), methodExpression.getValues());

				// Value
				evalValue(context, methodExpression);
			}
		}



		private void evalValue(EvalContext context, MethodExpressionAST expression) {

			List<SimpleValueAST> values = expression.getValues().getValues();

			Object[] arguments = new Object[values.size()];

			for (int i = 0; i < values.size(); i++) {
				arguments[i] =  context.getValue().get(values.get(i));
			}

			ClassUtils.setValue(expression.getMethod(), context.getTarget(), arguments);

		}

	};


	public static final List<ExpressionEvaluator> EXPRESSION_EVALUATORS = new ArrayList<ExpressionEvaluator>();
	public static final List<QueryEvaluator> QUERY_EVALUATORS = new ArrayList<QueryEvaluator>();
	public static final List<ReduceEvaluator> REDUCE_EVALUATORS = new ArrayList<ReduceEvaluator>();
	static {

		EXPRESSION_EVALUATORS.add(fieldEvaluator);
		EXPRESSION_EVALUATORS.add(methodEvaluator);

		QUERY_EVALUATORS.add(SelectQueryEvaluator.INSTANCE);

		REDUCE_EVALUATORS.add(EmptyReduceEvaluator.INSTANCE);
		REDUCE_EVALUATORS.add(AttrReduceEvaluator.INSTANCE);
		REDUCE_EVALUATORS.add(TextReduceEvaluator.INSTANCE);
		REDUCE_EVALUATORS.add(ValReduceEvaluator.INSTANCE);
		REDUCE_EVALUATORS.add(TagNameReduceEvaluator.INSTANCE);
		REDUCE_EVALUATORS.add(TitleReduceEvaluator.INSTANCE);
	}

	public interface QueryEvaluator {

		public void evalQuery(EvalContext context, QueryAST query);

	}
	public interface ReduceEvaluator {

		public void evalReduce(EvalContext context, ReduceAST reduce,  SimpleValueAST valueAST);

	}

	public interface ExpressionEvaluator {

		public void evalExpression(EvalContext context, ExpressionAST expression);

	}

	@Data
	@Builder(toBuilder=true)
	public static class EvalContext {
		protected Object target;

		protected String memberName;

		protected boolean optional = false;

		protected Document document;

		protected Elements queryResult;

		protected Map<ValueAST,Object> value;

		@Deprecated
		protected Object reduceResult;

		// alias final value
		@Deprecated
		protected Object convertResult;

	}



}