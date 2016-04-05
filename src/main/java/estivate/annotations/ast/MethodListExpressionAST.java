package estivate.annotations.ast;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MethodListExpressionAST extends MethodExpressionAST {

	protected ExpressionsAST expressions;

}
