package estivate.core.eval.lang;

import estivate.core.ast.ReduceAST;
import estivate.core.ast.lang.SimpleValueAST;
import estivate.core.ast.lang.TitleReduceAST;
import estivate.core.eval.EstivateEvaluator2.EvalContext;
import estivate.core.eval.EstivateEvaluator2.ReduceEvaluator;

public class TitleReduceEvaluator implements ReduceEvaluator {

	public static final TitleReduceEvaluator INSTANCE = new TitleReduceEvaluator();

	public void evalReduce(EvalContext context, ReduceAST reduce,
			SimpleValueAST valueAST) {
		
		if(reduce instanceof TitleReduceAST){
			context.getValue().put(valueAST, context.getDocument().title());
		}
	}

}
