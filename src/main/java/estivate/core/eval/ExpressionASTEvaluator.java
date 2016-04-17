package estivate.core.eval;

import estivate.core.ast.ExpressionAST;
import estivate.core.eval.EstivateEvaluator.EvalContext;

public interface ExpressionASTEvaluator {

	public void eval(EvalContext context, ExpressionAST expression);
	
	public abstract class Factory {

		public ExpressionASTEvaluator expressionEvaluater(ExpressionAST expression){
			return null;
		}
	}


}
