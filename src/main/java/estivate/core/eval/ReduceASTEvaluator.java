package estivate.core.eval;

import estivate.core.ast.ReduceAST;
import estivate.core.eval.EstivateEvaluator.EvalContext;
import lombok.Builder;
import lombok.Data;

@Deprecated
public interface ReduceASTEvaluator {

	@Deprecated
    public ReduceResult eval(EvalContext context, ReduceAST reduce);
    
    public ReduceResult eval(EvalContext context, ReduceAST reduce, boolean isValueList);

	public abstract class Factory {

		public ReduceASTEvaluator expressionEvaluater(ReduceAST reduce) {
			return null;
		}
	}

	@Data
	@Builder
	public static class ReduceResult {
	    protected Object value;
	}
	
}
