package estivate.annotations.ast;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ExpressionsAST {

	protected List<ExpressionAST> expressions = new ArrayList<ExpressionAST>();

}
