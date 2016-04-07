package estivate.core.ast;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ExpressionsAST extends NodeAST {

	protected List<ExpressionAST> expressions = new ArrayList<ExpressionAST>();

}
