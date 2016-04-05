package estivate.core.eval;

import estivate.core.ast.QueryAST;
import estivate.core.eval.EstivateEvaluater.EvalContext;

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
