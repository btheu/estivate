package estivate.core.eval.lang;

import org.jsoup.helper.StringUtil;

import estivate.core.ast.QueryAST;
import estivate.core.ast.lang.SelectQueryAST;
import estivate.core.eval.EstivateEvaluator;
import estivate.core.eval.QueryASTEvaluator;
import estivate.core.eval.EstivateEvaluator.EvalContext;
import estivate.core.eval.QueryASTEvaluator.Factory;

public class SelectQueryEvaluator implements QueryASTEvaluator {

	public EvalContext eval(EvalContext context, QueryAST query) {
		SelectQueryAST select = (SelectQueryAST) query;
		
		EvalContext result = context.toBuilder().build();
		
		String queryString = select.getQueryString();
		if(StringUtil.isBlank(queryString)){
			result.setDom(result.getDom());
		}else{
			result.setDom(result.getDom().select(queryString));
		}
		
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
