package estivate.annotations.ast;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MethodListExpressionAST extends MethodExpressionAST {

	protected ExpressionsAST expressions;

}
