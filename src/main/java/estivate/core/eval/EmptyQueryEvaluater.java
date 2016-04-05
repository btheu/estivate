package estivate.core.eval;

import estivate.EstivateEvaluater.EvalContext;
import estivate.annotations.ast.EmptyQueryAST;
import estivate.annotations.ast.QueryAST;
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
