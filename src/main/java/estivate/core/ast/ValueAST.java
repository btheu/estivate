package estivate.core.ast;

import java.lang.reflect.Type;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ValueAST {

	protected Type type;

	protected Class<?> rawClass;

	protected Object value;

}
