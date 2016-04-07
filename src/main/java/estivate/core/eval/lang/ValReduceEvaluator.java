package estivate.core.eval.lang;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import estivate.core.ast.ReduceAST;
import estivate.core.ast.lang.ValReduceAST;
import estivate.core.eval.EstivateEvaluator.EvalContext;
import estivate.core.eval.ReduceASTEvaluator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValReduceEvaluator implements ReduceASTEvaluator {

	public ReduceResult eval(EvalContext context, ReduceAST reduce) {
		return eval(context, reduce, false);
	}

	public ReduceResult eval(EvalContext context, ReduceAST reduce, boolean isValueList) {
		Elements elements = context.getDom();

		Object value;

		if (isValueList) {
			List<String> list = new ArrayList<String>();
			log.debug("using list val()");
			for (Element element : elements) {
				list.add(element.val());
			}
			value = list;
		} else {
			if (elements.size() > 1) {
				log.warn(
						"'{}' val concats elements. Consider fixing the select expression to get only one element.",
						context.getMemberName());
			}
			log.debug("using simple val()");

			StringBuilder sb = new StringBuilder(50);
			for (Element element : elements) {
				if (sb.length() != 0)
					sb.append(" ");
				sb.append(element.val());
			}

			value = sb.toString();
		}

		return ReduceResult.builder().value(value).build();
	}

	public static estivate.core.eval.ReduceASTEvaluator.Factory factory = new Factory() {

		@Override
		public ReduceASTEvaluator expressionEvaluater(ReduceAST reduce) {
			if(reduce instanceof ValReduceAST){
				return new ValReduceEvaluator();
			}
			return super.expressionEvaluater(reduce);
		}
	};

}
