package estivate.core.eval.lang;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import estivate.core.ast.ReduceAST;
import estivate.core.ast.lang.SimpleValueAST;
import estivate.core.ast.lang.ValReduceAST;
import estivate.core.eval.EstivateEvaluator2.EvalContext;
import estivate.core.eval.EstivateEvaluator2.ReduceEvaluator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValReduceEvaluator implements ReduceEvaluator {

	public static final ValReduceEvaluator INSTANCE = new ValReduceEvaluator();
	
	public void evalReduce(EvalContext context, ReduceAST reduce,
			SimpleValueAST simpleValueAST) {

		if(reduce instanceof ValReduceAST){

			Elements elements = context.getQueryResult();

			Object reduceValue;

			if (simpleValueAST.isValueList()) {
				List<String> list = new ArrayList<String>();

				for (Element element : elements) {
					list.add(element.val());
				}

				reduceValue = list;
			} else {
				if (elements.size() > 1) {
					log.warn(
							"'{}' Val concats elements. Consider fixing the Query expression to get only one element.",
							context.getMemberName());
				}

				StringBuilder sb = new StringBuilder(50);
				for (Element element : elements) {
					if (sb.length() != 0)
						sb.append(" ");
					sb.append(element.val());
				}

				reduceValue = sb.toString();
			}
			
			
			context.getValue().put(simpleValueAST, reduceValue);
		}
	}


}
