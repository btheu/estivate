package estivate.core.eval.lang;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import estivate.core.ast.ReduceAST;
import estivate.core.ast.lang.SimpleValueAST;
import estivate.core.ast.lang.TextReduceAST;
import estivate.core.eval.EstivateEvaluator2.ReduceEvaluator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TextReduceEvaluator implements ReduceEvaluator {

    public static final TextReduceEvaluator INSTANCE = new TextReduceEvaluator();

    public void evalReduce(
            estivate.core.eval.EstivateEvaluator2.EvalContext context,
            ReduceAST reduce, SimpleValueAST valueAST) {

        if(reduce instanceof TextReduceAST){

            TextReduceAST text = (TextReduceAST) reduce;
            
            Elements elements = context.getQueryResult();

            Object value;

            if (valueAST.isValueList()) {
                List<String> list = new ArrayList<String>();
                if (text.isOwn()) {
                    log.debug("using list owntext()");
                    for (Element element : elements) {
                        list.add(element.ownText());
                    }
                } else {
                    log.debug("using list text()");
                    for (Element element : elements) {
                        list.add(element.text());
                    }
                }
                value = list;
            } else {
                if (elements.size() > 1) {
                    log.warn(
                            "'{}' text using first element. Consider fixing the select expression to get only one element.",
                            context.getMemberName());
                }
                if (text.isOwn()) {
                    log.debug("using simple owntext()");

                    StringBuilder sb = new StringBuilder(50);
                    for (Element element : elements) {
                        if (sb.length() != 0)
                            sb.append(" ");
                        sb.append(element.ownText());
                    }

                    value = sb.toString();
                } else {
                    log.debug("using simple text()");
                    value = elements.text();
                }
            }
            
            context.getValue().put(valueAST, value);
        }

    }

}
