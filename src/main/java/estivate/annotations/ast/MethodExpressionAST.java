package estivate.annotations.ast;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MethodExpressionAST extends ExpressionAST {

	protected Method method;

	protected Type[] targetType;

	protected Class<?>[] targetRawClass;

	protected Object[] value;

}
