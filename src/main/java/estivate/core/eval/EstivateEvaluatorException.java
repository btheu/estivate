package estivate.core.eval;

import estivate.core.eval.EstivateEvaluator.EvalContext;
import lombok.Getter;

@SuppressWarnings("serial")
public class EstivateEvaluatorException extends RuntimeException {

    @Getter
    private EvalContext context;

    public EstivateEvaluatorException(EvalContext context, String message) {
        super(message);
        this.context = context.toBuilder().build();
    }

}
