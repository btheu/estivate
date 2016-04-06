package estivate.core.eval;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import estivate.core.ast.ReduceAST;
import estivate.core.ast.parser.AttrReduceAST;
import estivate.core.eval.EstivateEvaluator.EvalContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AttrReduceEvaluator implements ReduceASTEvaluator {

	public ReduceResult eval(EvalContext context, ReduceAST reduce) {

		AttrReduceAST attr = (AttrReduceAST) reduce;
		
		return ReduceResult.builder().value(context.getDom().attr(attr.getAttr())).build();
	}
	
	public ReduceResult eval(EvalContext context, ReduceAST reduce, boolean isValueList) {
		AttrReduceAST attr = (AttrReduceAST) reduce;
		
		Object value;
		
		Elements elements = context.getDom();
		if (isValueList) {
			List<String> list = new ArrayList<String>();

			for (Element element : elements) {
				list.add(element.attr(attr.getAttr()));
			}

			value = list;
		} else {
			if (elements.size() > 1) {
				log.warn(
						"'{}' attr concats elements. Consider fixing the Query expression to get only one element.",
						attr.getAttr());
			}

			StringBuilder sb = new StringBuilder(50);
			for (Element element : elements) {
				if (sb.length() != 0)
					sb.append(" ");
				sb.append(element.attr(attr.getAttr()));
			}

			value = sb.toString();
		}
		
		return ReduceResult.builder().value(value).build();
	}
	
	public static estivate.core.eval.ReduceASTEvaluator.Factory factory = new Factory() {
		
		@Override
		public ReduceASTEvaluator expressionEvaluater(ReduceAST reduce) {
			if(reduce instanceof AttrReduceAST){
				return new AttrReduceEvaluator();
			}
			return super.expressionEvaluater(reduce);
		}
	};



}
