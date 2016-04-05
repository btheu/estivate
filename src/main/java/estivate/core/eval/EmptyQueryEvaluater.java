package estivate.core.eval;

import estivate.core.ast.EmptyQueryAST;
import estivate.core.ast.QueryAST;
import estivate.core.eval.EstivateEvaluater.EvalContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmptyQueryEvaluater implements QueryASTEvaluater {

	public EvalContext eval(EvalContext context, QueryAST query) {

		log.debug("Empty select. Using root element.");
		
		return context.toBuilder().build();
	}

	public static QueryASTEvaluater.Factory factory = new Factory() {
		@Override
		public QueryASTEvaluater expressionEvaluater(QueryAST query) {
			if(query instanceof EmptyQueryAST){
				return new EmptyQueryEvaluater();
			}
			return super.expressionEvaluater(query);
		}
	};
	
}
