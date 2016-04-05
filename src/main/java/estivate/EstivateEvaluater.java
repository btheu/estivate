package estivate;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import estivate.annotations.ast.EstivateAST;
import estivate.annotations.ast.ExpressionAST;
import estivate.annotations.ast.ExpressionsAST;
import estivate.annotations.ast.FieldExpressionAST;
import estivate.annotations.ast.MethodExpressionAST;
import estivate.annotations.ast.QueryAST;
import estivate.annotations.ast.ReduceAST;
import estivate.core.eval.AttrReduceEvaluater;
import estivate.core.eval.EmptyQueryEvaluater;
import estivate.core.eval.ExpressionASTEvaluater;
import estivate.core.eval.ExpressionASTEvaluater.Factory;
import estivate.core.eval.QueryASTEvaluater;
import estivate.core.eval.ReduceASTEvaluater;
import estivate.core.eval.SelectQueryEvaluater;
import estivate.core.eval.TextReduceEvaluater;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EstivateEvaluater {

	public static final List<QueryASTEvaluater.Factory> queryEvalFacts = new ArrayList<QueryASTEvaluater.Factory>();
	static {
		queryEvalFacts.add(EmptyQueryEvaluater.factory);
		queryEvalFacts.add(SelectQueryEvaluater.factory);
	}
	public static final List<ReduceASTEvaluater.Factory> reduceEvalFacts = new ArrayList<ReduceASTEvaluater.Factory>();
	static {
		reduceEvalFacts.add(TextReduceEvaluater.factory);
		reduceEvalFacts.add(AttrReduceEvaluater.factory);
	}
	public static final List<Factory> expressionEvalFacts = new ArrayList<ExpressionASTEvaluater.Factory>();
	static {
		expressionEvalFacts.add(FieldExpEvaluater.factory);
		expressionEvalFacts.add(MethodExpEvaluater.factory);
	}


	public static Object eval(Document document, EstivateAST ast) {

		EvalContext context = new EvalContext.EvalContextBuilder()
				.document(document)
				.dom(new Elements(document))
				.optional(ast.isOptional()).build();

		evalExpressions(context, ast);

		return null;
	}

	public static void evalExpressions(EvalContext context, ExpressionsAST ast) {
		List<ExpressionAST> expressions = ast.getExpressions();
		for (ExpressionAST expression : expressions) {
			// copy of the context
			evalExpression(context.toBuilder().build(), expression);
		}
	}

	private static void evalExpression(EvalContext context, ExpressionAST expression) {
		boolean noFound = true;
		for (Factory evalFact : expressionEvalFacts) {
			ExpressionASTEvaluater eval = evalFact.expressionEvaluater(expression);
			if(eval != null){
				eval.eval(context.toBuilder().build(), expression);
				noFound = false;
			}
		}
		if(noFound){
			log.error("No Expression Evaluater found for '{}'",expression.getClass().getSimpleName());
		}
	}

	private static EvalContext evalQuery(EvalContext build, QueryAST query) {
		for (QueryASTEvaluater.Factory factory : queryEvalFacts) {
			QueryASTEvaluater eval = factory.expressionEvaluater(query);
			if(eval != null){
				return eval.eval(build, query);
			}
		}
		log.error("No Query Evaluater found for '{}'",query.getClass().getSimpleName());
		return null;
	}

	private static void evalReduce(EvalContext context, ReduceAST reduce) {
		boolean noFound = true;
		for (ReduceASTEvaluater.Factory factory : reduceEvalFacts) {
			ReduceASTEvaluater eval = factory.expressionEvaluater(reduce);
			if(eval != null){
				eval.eval(context, reduce);
				noFound = false;
			}
		}
		if(noFound){
			log.error("No Reduce Evaluater found for '{}'",reduce.getClass().getSimpleName());
		}
	}


	public static class FieldExpEvaluater implements ExpressionASTEvaluater{

		public void eval(EvalContext context, ExpressionAST expression) {
			FieldExpressionAST exp = (FieldExpressionAST) expression;
			log.trace("> eval field '{}'",exp.getField().getName());

			EvalContext contextSelect = evalQuery(context.toBuilder().build(),exp.getQuery());

			evalReduce(contextSelect.toBuilder().build(),exp.getReduce());

			log.trace("< eval field");
		}

		public static Factory factory = new ExpressionASTEvaluater.Factory() {

			@Override
			public ExpressionASTEvaluater expressionEvaluater(ExpressionAST expression) {
				if(expression instanceof FieldExpressionAST){
					return new FieldExpEvaluater();
				}
				return super.expressionEvaluater(expression);
			}
		};

	}



	public static class MethodExpEvaluater implements ExpressionASTEvaluater{

		public void eval(EvalContext context, ExpressionAST expression) {
			MethodExpressionAST exp = (MethodExpressionAST) expression;
			log.trace("> eval method '{}()'",exp.getMethod().getName());

			EvalContext contextSelect = evalQuery(context.toBuilder().build(),exp.getQuery());

			evalReduce(contextSelect.toBuilder().build(),exp.getReduce());

			log.trace("< eval method");
		}

		public static Factory factory = new ExpressionASTEvaluater.Factory() {

			@Override
			public ExpressionASTEvaluater expressionEvaluater(ExpressionAST expression) {
				if(expression instanceof MethodExpressionAST){
					return new MethodExpEvaluater();
				}
				return super.expressionEvaluater(expression);
			}
		};
	}

	@Data
	@Builder(toBuilder=true)
	public static class EvalContext {

		protected boolean optional = false;

		protected Document document;

		protected Elements dom;

	}

}
