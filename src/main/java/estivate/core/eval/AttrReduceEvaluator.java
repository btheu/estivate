package estivate.core.eval;

import estivate.core.ast.ReduceAST;
import estivate.core.ast.parser.AttrReduceAST;
import estivate.core.eval.EstivateEvaluator.EvalContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AttrReduceEvaluator implements ReduceASTEvaluator {

	public ReduceResult eval(EvalContext context, ReduceAST reduce) {

		AttrReduceAST attr = (AttrReduceAST) reduce;
		
		return ReduceResult.builder().value(context.getDom().attr(attr.getAttr())).build();
	}
	
	public static estivate.core.eval.ReduceASTEvaluator.Factory factory = new Factory() {
		
		@Override
		public ReduceASTEvaluator expressionEvaluater(ReduceAST reduce) {
			if(reduce instanceof AttrReduceAST){
				return new AttrReduceEvaluator();
			}
			return super.expressionEvaluater(reduce);
		}
	};


}
