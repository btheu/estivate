package estivate.core.eval;

import estivate.EstivateEvaluater.EvalContext;
import estivate.annotations.ast.ExpressionAST;

public interface ExpressionASTEvaluater {

	void eval(EvalContext context, ExpressionAST expression);
	
	public abstract class Factory {

		public ExpressionASTEvaluater expressionEvaluater(ExpressionAST expression){
			return null;
		}
	}


}
