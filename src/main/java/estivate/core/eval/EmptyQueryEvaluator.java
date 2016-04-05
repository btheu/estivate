package estivate.core.eval;

import estivate.core.ast.EmptyQueryAST;
import estivate.core.ast.QueryAST;
import estivate.core.eval.EstivateEvaluator.EvalContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmptyQueryEvaluator implements QueryASTEvaluator {

	public EvalContext eval(EvalContext context, QueryAST query) {

		log.debug("Empty select. Using root element.");
		
		return context.toBuilder().build();
	}

	public static QueryASTEvaluator.Factory factory = new Factory() {
		@Override
		public QueryASTEvaluator expressionEvaluater(QueryAST query) {
			if(query instanceof EmptyQueryAST){
				return new EmptyQueryEvaluator();
			}
			return super.expressionEvaluater(query);
		}
	};
	
}
