package estivate.core.eval;

import estivate.core.ast.QueryAST;
import estivate.core.ast.parser.SelectQueryAST;
import estivate.core.eval.EstivateEvaluator.EvalContext;

public class SelectQueryEvaluator implements QueryASTEvaluator {

	public EvalContext eval(EvalContext context, QueryAST query) {
		SelectQueryAST select = (SelectQueryAST) query;
		
		EvalContext result = context.toBuilder().build();
		
		result.setDom(result.getDom().select(select.getQueryString()));
		
		return result;
	}

	public static QueryASTEvaluator.Factory factory = new Factory() {
		@Override
		public QueryASTEvaluator expressionEvaluater(QueryAST query) {
			if(query instanceof SelectQueryAST){
				return new SelectQueryEvaluator();
			}
			return super.expressionEvaluater(query);
		}
	};
	
}
