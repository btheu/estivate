package estivate.core.eval;

import org.jsoup.nodes.Element;

import estivate.core.ast.ReduceAST;
import estivate.core.ast.parser.TextReduceAST;
import estivate.core.eval.EstivateEvaluater.EvalContext;

public class TextReduceEvaluater implements ReduceASTEvaluater {

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

    public static estivate.core.eval.ReduceASTEvaluater.Factory factory = new Factory() {

        @Override
        public ReduceASTEvaluater expressionEvaluater(ReduceAST reduce) {
            if(reduce instanceof TextReduceAST){
                return new TextReduceEvaluater();
            }
            return super.expressionEvaluater(reduce);
        }
    };

}
