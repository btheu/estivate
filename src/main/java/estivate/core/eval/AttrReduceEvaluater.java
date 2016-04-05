package estivate.core.eval;

import estivate.annotations.ast.ReduceAST;
import estivate.annotations.ast.parser.AttrReduceAST;
import estivate.core.eval.EstivateEvaluater.EvalContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AttrReduceEvaluater implements ReduceASTEvaluater {

	public ReduceResult eval(EvalContext context, ReduceAST reduce) {

		AttrReduceAST attr = (AttrReduceAST) reduce;
		
		return ReduceResult.builder().value(context.getDom().attr(attr.getAttr())).build();
	}
	
	public static estivate.core.eval.ReduceASTEvaluater.Factory factory = new Factory() {
		
		@Override
		public ReduceASTEvaluater expressionEvaluater(ReduceAST reduce) {
			if(reduce instanceof AttrReduceAST){
				return new AttrReduceEvaluater();
			}
			return super.expressionEvaluater(reduce);
		}
	};


}
