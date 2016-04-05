package estivate.annotations.ast;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FieldExpressionAST extends ExpressionAST {

	protected Field field;

	protected Type targetType;

	protected Class<?> targetRawClass;

	protected Object value;
}
