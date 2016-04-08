package estivate.core.ast;

import java.lang.reflect.Method;
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

	@Deprecated
	protected List<ValueAST> arguments = new ArrayList<ValueAST>();
	
}
