package estivate.core.ast;

import java.lang.reflect.Type;

import lombok.Data;

@Data
public class ValueAST {

	protected Type type;

	protected Class<?> rawClass;

	protected boolean isValueList = false;
	
	protected Object value;

}
