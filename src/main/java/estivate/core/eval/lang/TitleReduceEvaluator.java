package estivate.core.eval.lang;

import estivate.core.ast.ReduceAST;
import estivate.core.ast.lang.TitleReduceAST;
import estivate.core.eval.EstivateEvaluator.EvalContext;
import estivate.core.eval.ReduceASTEvaluator;

public class TitleReduceEvaluator implements ReduceASTEvaluator {

	public ReduceResult eval(EvalContext context, ReduceAST reduce) {
		return eval(context, reduce, false);
	}

	public ReduceResult eval(EvalContext context, ReduceAST reduce, boolean isValueList) {
		return ReduceResult.builder().value(context.getDocument().title()).build();
	}

	public static estivate.core.eval.ReduceASTEvaluator.Factory factory = new Factory() {

		@Override
		public ReduceASTEvaluator expressionEvaluater(ReduceAST reduce) {
			if(reduce instanceof TitleReduceAST){
				return new TitleReduceEvaluator();
			}
			return super.expressionEvaluater(reduce);
		}
	};

}
