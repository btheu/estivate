package estivate.core.eval;

import estivate.EstivateEvaluater.EvalContext;
import estivate.annotations.ast.ReduceAST;

public interface ReduceASTEvaluater {

	public void eval(EvalContext context, ReduceAST reduce);

	public abstract class Factory {

		public ReduceASTEvaluater expressionEvaluater(ReduceAST reduce) {
			return null;
		}
	}

}
