package estivate.annotations.ast;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FieldListExpressionAST extends FieldExpressionAST {

	protected ExpressionsAST expressions;

}
