package estivate.core.eval.lang;

import org.jsoup.helper.StringUtil;
import org.jsoup.select.Elements;

import estivate.core.ast.QueryAST;
import estivate.core.ast.lang.SelectQueryAST;
import estivate.core.eval.EstivateEvaluator.EvalContext;
import estivate.core.eval.QueryASTEvaluator;

public class SelectQueryEvaluator implements QueryASTEvaluator {

	public EvalContext eval(EvalContext context, QueryAST query) {
		SelectQueryAST ast = (SelectQueryAST) query;
		
		EvalContext result = context.toBuilder().build();
		
		String queryString = ast.getQueryString();
		if(StringUtil.isBlank(queryString)){
			result.setDom(result.getDom());
		}else{
			Elements select2 = result.getDom().select(queryString);
			
			if(ast.isFirst()){
				result.setDom(new Elements(select2.first()));
			} else if(ast.isLast()){
				result.setDom(new Elements(select2.last()));
			} else if(ast.isUnique() && select2.size() != 1){
				throw new RuntimeException("No unique element after query");
			}else{
				result.setDom(select2);
			}
			
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
