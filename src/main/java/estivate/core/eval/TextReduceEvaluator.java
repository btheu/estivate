package estivate.core.eval;

import org.jsoup.nodes.Element;

import estivate.core.ast.ReduceAST;
import estivate.core.ast.parser.TextReduceAST;
import estivate.core.eval.EstivateEvaluator.EvalContext;

public class TextReduceEvaluator implements ReduceASTEvaluator {

    public ReduceResult eval(EvalContext context, ReduceAST reduce) {
        
        TextReduceAST text = (TextReduceAST) reduce;
        
        String value;
        if(text.isOwn()){
            
            StringBuilder sb = new StringBuilder(50);
            for (Element element : context.getDom()) {
                if (sb.length() != 0)
                    sb.append(" ");
                sb.append(element.ownText());
            }

            value = sb.toString();
        }else{
            value = context.getDom().text();
        }
        
        return ReduceResult.builder().value(value).build();
    }

    public static estivate.core.eval.ReduceASTEvaluator.Factory factory = new Factory() {

        @Override
        public ReduceASTEvaluator expressionEvaluater(ReduceAST reduce) {
            if(reduce instanceof TextReduceAST){
                return new TextReduceEvaluator();
            }
            return super.expressionEvaluater(reduce);
        }
    };

}
