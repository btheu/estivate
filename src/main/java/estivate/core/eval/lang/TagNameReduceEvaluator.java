package estivate.core.eval.lang;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import estivate.core.ast.ReduceAST;
import estivate.core.ast.lang.TagNameReduceAST;
import estivate.core.eval.EstivateEvaluator.EvalContext;
import estivate.core.eval.ReduceASTEvaluator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TagNameReduceEvaluator implements ReduceASTEvaluator {

	public ReduceResult eval(EvalContext context, ReduceAST reduce) {
		return eval(context, reduce, false);
	}

	public ReduceResult eval(EvalContext context, ReduceAST reduce, boolean isValueList) {
		
		Elements elements = context.getDom();

		Object value;

		if (isValueList) {
			List<String> list = new ArrayList<String>();
			log.debug("using list tagName()");
			for (Element element : elements) {
				list.add(element.tagName());
			}
			value = list;
		} else {
			if (elements.size() > 1) {
				log.warn(
						"'{}' tagName concats elements. Consider fixing the select expression to get only one element.",
						context.getMemberName());
			}
			log.debug("using simple tagName()");

			StringBuilder sb = new StringBuilder(50);
			for (Element element : elements) {
				if (sb.length() != 0)
					sb.append(" ");
				sb.append(element.tagName());
			}

			value = sb.toString();
		}

		return ReduceResult.builder().value(value).build();
	}

	public static estivate.core.eval.ReduceASTEvaluator.Factory factory = new Factory() {

		@Override
		public ReduceASTEvaluator expressionEvaluater(ReduceAST reduce) {
			if(reduce instanceof TagNameReduceAST){
				return new TagNameReduceEvaluator();
			}
			return super.expressionEvaluater(reduce);
		}
	};

}
