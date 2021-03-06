package estivate.core.ast;

import java.lang.reflect.Field;

import estivate.core.ast.lang.SimpleValueAST;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FieldExpressionAST extends ExpressionAST {

	protected Field field;

	protected SimpleValueAST value;
	
}
