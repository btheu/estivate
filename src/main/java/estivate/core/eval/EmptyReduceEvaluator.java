package estivate.core.eval;

import estivate.core.ast.ReduceAST;
import estivate.core.ast.lang.SimpleValueAST;
import estivate.core.eval.EstivateEvaluator2.EvalContext;
import estivate.core.eval.EstivateEvaluator2.ReduceEvaluator;

public class EmptyReduceEvaluator implements ReduceEvaluator{

    public static final EmptyReduceEvaluator INSTANCE = new EmptyReduceEvaluator();
    
    public void evalReduce(EvalContext context, ReduceAST reduce, SimpleValueAST valueAST) {
        context.getValue().put(valueAST, context.getQueryResult());
    }

}
