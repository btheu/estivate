package estivate.core.ast;

import java.lang.reflect.Type;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SimpleValueAST extends ValueAST {

	protected Type type;

	protected Class<?> rawClass;

	protected boolean isValueList = false;
	
	protected Object value;

}
