package estivate.core.eval;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import estivate.core.ClassUtils;
import estivate.core.ast.EstivateAST;
import estivate.core.ast.ExpressionAST;
import estivate.core.ast.ExpressionsAST;
import estivate.core.ast.QueryAST;
import estivate.core.ast.ReduceAST;
import estivate.core.eval.lang.AttrReduceEvaluator;
import estivate.core.eval.lang.SelectQueryEvaluator;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EstivateEvaluator2 {


    public static Object eval(Document document, EstivateAST ast) {
        return eval(document, new Elements(document), ast);
    }

	private static Object eval(Document document, Elements queryResult, EstivateAST ast) {
		
    	Object target = ClassUtils.newInstance(ast.getTargetRawClass());
        
        EvalContext context = new EvalContext.EvalContextBuilder()
        		.target(target)
                .document(document)
                .queryResult(queryResult)
                .optional(ast.isOptional())
                .build();

        evalExpressions(context.toBuilder().build(), ast);

        return target;
	}
    
    public static List<?> evalToList(Document document, EstivateAST ast) {
    	return evalToList(document, new Elements(document), ast);
    }
    
	private static List<?> evalToList(Document document, Elements queryResult, EstivateAST ast) {
		List<Object> results = new ArrayList<Object>();
		
		for (Element element : queryResult) {

    		Object target = ClassUtils.newInstance(ast.getTargetRawClass());

    		EvalContext context = new EvalContext.EvalContextBuilder()
    				.document(document)
    				.queryResult(new Elements(element))
    				.target(target)
    				.optional(ast.isOptional())
    				.build();

    		evalExpressions(context.toBuilder().build(), ast);

    		results.add(target);
    	}

    	return results;
	}
    
    public static void evalExpressions(EvalContext context, ExpressionsAST ast) {
    	
    	// TODO Eval Query
    	
        List<ExpressionAST> expressions = ast.getExpressions();
        for (ExpressionAST expression : expressions) {
            // copy of the context
            evalExpression(context.toBuilder().build(), expression);
        }
    }

    protected static void evalExpression(EvalContext context, ExpressionAST expression) {}


    public static final List<QueryEvaluator> queryEvals = new ArrayList<QueryEvaluator>();
    public static final List<ReduceEvaluator> reduceEvals = new ArrayList<ReduceEvaluator>();
    static {
        queryEvals.add(SelectQueryEvaluator.INSTANCE);
        
        reduceEvals.add(AttrReduceEvaluator.INSTANCE);
    }
    
    public interface QueryEvaluator {
    	
    	public void evalQuery(EvalContext context, QueryAST query);
    	
    }
    public interface ReduceEvaluator {

    	public void evalReduce(EvalContext context, ReduceAST reduce, boolean isValueList);

    }
    
    @Data
    @Builder(toBuilder=true)
    public static class EvalContext {
    	protected Object target;

    	protected String memberName;
    	
        protected boolean optional = false;

        protected Document document;

        protected Elements queryResult;
        
        protected Object reduceResult;

        // alias final value
        protected Object convertResult;
        
    }



}
