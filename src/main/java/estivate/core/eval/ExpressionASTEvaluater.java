package estivate.core.eval;

import estivate.annotations.ast.ExpressionAST;
import estivate.core.eval.EstivateEvaluater.EvalContext;

public interface ExpressionASTEvaluater {

	public void eval(EvalContext context, ExpressionAST expression);
	
	public abstract class Factory {

		public ExpressionASTEvaluater expressionEvaluater(ExpressionAST expression){
			return null;
		}
	}


}
