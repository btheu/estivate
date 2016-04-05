package estivate.core.eval;

import estivate.EstivateEvaluater.EvalContext;
import estivate.annotations.ast.QueryAST;
import estivate.annotations.ast.parser.SelectQueryAST;

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
