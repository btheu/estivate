package estivate.core.eval;

import estivate.EstivateEvaluater.EvalContext;
import estivate.annotations.ast.ReduceAST;
import estivate.annotations.ast.parser.TextReduceAST;

public class TextReduceEvaluater implements ReduceASTEvaluater {

	public void eval(EvalContext context, ReduceAST reduce) {
		
	}
	
	public static estivate.core.eval.ReduceASTEvaluater.Factory factory = new Factory() {
		
		@Override
		public ReduceASTEvaluater expressionEvaluater(ReduceAST reduce) {
			if(reduce instanceof TextReduceAST){
				return new TextReduceEvaluater();
			}
			return super.expressionEvaluater(reduce);
		}
	};


}
