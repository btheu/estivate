package estivate.core.eval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import estivate.core.ClassUtils;
import estivate.core.ast.ConverterAST;
import estivate.core.ast.EstivateAST;
import estivate.core.ast.ExpressionAST;
import estivate.core.ast.ExpressionsAST;
import estivate.core.ast.FieldExpressionAST;
import estivate.core.ast.MethodExpressionAST;
import estivate.core.ast.QueryAST;
import estivate.core.ast.ReduceAST;
import estivate.core.ast.ValueAST;
import estivate.core.ast.lang.ListValueAST;
import estivate.core.ast.lang.SimpleValueAST;
import estivate.core.eval.lang.AttrReduceEvaluator;
import estivate.core.eval.lang.SelectQueryEvaluator;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EstivateEvaluator2 {


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
		List<Object> results = new ArrayList<Object>();

		for (Element element : queryResult) {

			Object target = ClassUtils.newInstance(ast.getTargetRawClass());

			EvalContext context = new EvalContext.EvalContextBuilder()
					.target(target)
					.document(document)
					.queryResult(new Elements(element))
					.optional(ast.isOptional())
					.value(new HashMap<ValueAST, Object>())
					.build();

			// copy of the context
			evalExpressions(context.toBuilder().build(), ast);

			results.add(target);
		}

		return results;
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

	private static void evalConvert(EvalContext context, ConverterAST converter, ValueAST value) {
		// TODO Auto-generated method stub

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
		for (ReduceEvaluator eval : REDUCE_EVALUATORS) {
			eval.evalReduce(context, reduce, simpleValueAST);
		}
	}

	private static void evalQuery(EvalContext context, QueryAST query) {
		for (QueryEvaluator eval : QUERY_EVALUATORS) {
			eval.evalQuery(context, query);
		}
	}


	public static final ExpressionEvaluator fieldEvaluator = new ExpressionEvaluator() {
		
		public void evalExpression(EvalContext context, ExpressionAST expression) {
			if(expression instanceof FieldExpressionAST){
				
				FieldExpressionAST fieldExpression = (FieldExpressionAST) expression;
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
			ClassUtils.setValue(expression.getField(), context.getTarget(), expression.getValue().getValue());
		}
		
	};
	public static final ExpressionEvaluator methodEvaluator = new ExpressionEvaluator() {

		public void evalExpression(EvalContext context, ExpressionAST expression) {
			if(expression instanceof MethodExpressionAST){

				MethodExpressionAST methodExpression = (MethodExpressionAST) expression;
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

		REDUCE_EVALUATORS.add(AttrReduceEvaluator.INSTANCE);
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
