package estivate.core.ast;

import lombok.Data;

@Data
public abstract class ExpressionAST extends NodeAST {

    protected Boolean optional;
    
	protected ReduceAST reduce;

	protected ConverterAST converter;

	// Simple Value or List Value
	protected ValueAST value;
}
