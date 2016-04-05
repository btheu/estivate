package estivate.core.eval;

import estivate.annotations.ast.ReduceAST;
import estivate.core.eval.EstivateEvaluater.EvalContext;
import lombok.Builder;
import lombok.Data;

public interface ReduceASTEvaluater {

    public ReduceResult eval(EvalContext context, ReduceAST reduce);

	public abstract class Factory {

		public ReduceASTEvaluater expressionEvaluater(ReduceAST reduce) {
			return null;
		}
	}

	@Data
	@Builder
	public static class ReduceResult {
	    protected Object value;
	}
	
}
