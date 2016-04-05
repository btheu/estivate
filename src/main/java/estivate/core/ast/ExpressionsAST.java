package estivate.core.ast;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ExpressionsAST {

	protected QueryAST query;
	
	protected List<ExpressionAST> expressions = new ArrayList<ExpressionAST>();

}
