package estivate.core.eval;

import estivate.EstivateEvaluater.EvalContext;
import estivate.annotations.ast.QueryAST;

public interface QueryASTEvaluater {

	/**
	 * Give a new context after eval of the query
	 * 
	 * @param context
	 * @param expression
	 * @return a new context after eval of the query
	 */
	public EvalContext eval(EvalContext context, QueryAST query);

	public abstract class Factory {

		public QueryASTEvaluater expressionEvaluater(QueryAST query) {
			return null;
		}
	}

}
