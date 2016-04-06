package estivate.core.eval;

import estivate.core.ast.EmptyReduceAST;
import estivate.core.ast.ReduceAST;
import estivate.core.eval.EstivateEvaluator.EvalContext;
import lombok.extern.slf4j.Slf4j;

/**
 * Return the current DOM as result.
 * 
 * @author Benoit Theunissen
 *
 */
@Slf4j
public class EmptyReduceEvaluator implements ReduceASTEvaluator {

	public ReduceResult eval(EvalContext context, ReduceAST reduce) {
		return ReduceResult.builder().value(context.getDom()).build();
	}
	
	public static estivate.core.eval.ReduceASTEvaluator.Factory factory = new Factory() {
		
		@Override
		public ReduceASTEvaluator expressionEvaluater(ReduceAST reduce) {
			if(reduce instanceof EmptyReduceAST){
				return new EmptyReduceEvaluator();
			}
			return super.expressionEvaluater(reduce);
		}
	};


}
