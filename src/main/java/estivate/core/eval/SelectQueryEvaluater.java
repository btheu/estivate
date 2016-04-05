package estivate.core.eval;

import estivate.core.ast.QueryAST;
import estivate.core.ast.parser.SelectQueryAST;
import estivate.core.eval.EstivateEvaluater.EvalContext;

public class SelectQueryEvaluater implements QueryASTEvaluater {

	public EvalContext eval(EvalContext context, QueryAST query) {
		SelectQueryAST select = (SelectQueryAST) query;
		
		EvalContext result = context.toBuilder().build();
		
		result.setDom(result.getDom().select(select.getQueryString()));
		
		return result;
	}

	public static QueryASTEvaluater.Factory factory = new Factory() {
		@Override
		public QueryASTEvaluater expressionEvaluater(QueryAST query) {
			if(query instanceof SelectQueryAST){
				return new SelectQueryEvaluater();
			}
			return super.expressionEvaluater(query);
		}
	};
	
}
