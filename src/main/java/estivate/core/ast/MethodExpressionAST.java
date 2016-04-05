package estivate.core.ast;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MethodExpressionAST extends ExpressionAST {

	protected Method method;

	protected Type[] targetType;

	protected Class<?>[] targetRawClass;

	protected Object[] value;

}
