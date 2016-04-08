package estivate.core.ast;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public abstract class ExpressionAST extends NodeAST {

    protected Boolean optional;
    
	protected ReduceAST reduce;

	protected ConverterAST converter;

}
