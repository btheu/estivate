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
import estivate.core.eval.ExpressionASTEvaluater;
import estivate.core.eval.ExpressionASTEvaluater.Factory;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EstivateEvaluater {

	
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
		for (Factory evalFact : expressionEvalFacts) {
			ExpressionASTEvaluater eval = evalFact.expressionEvaluater(expression);
			if(eval != null){
				eval.eval(context.toBuilder().build(), expression);
			}
		}
	}

	public static class FieldExpEvaluater implements ExpressionASTEvaluater{

		public void eval(EvalContext context, ExpressionAST expression) {
			FieldExpressionAST exp = (FieldExpressionAST) expression;
			log.trace("> eval field '{}'",exp.getField().getName());
			
			
			
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
