package estivate.core.eval;

import estivate.EstivateEvaluater.EvalContext;
import estivate.annotations.ast.ReduceAST;
import estivate.annotations.ast.parser.AttrReduceAST;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AttrReduceEvaluater implements ReduceASTEvaluater {

	public void eval(EvalContext context, ReduceAST reduce) {

		AttrReduceAST attr = (AttrReduceAST) reduce;
		
		log.debug("Attr found '{}'", context.getDom().attr(attr.getAttr()));
		
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
