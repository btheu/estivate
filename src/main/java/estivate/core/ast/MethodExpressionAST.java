package estivate.core.ast;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MethodExpressionAST extends ExpressionAST {

	protected Method method;

	protected List<ValueAST> arguments = new ArrayList<ValueAST>();
	
}
