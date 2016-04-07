package estivate.core.ast;

import lombok.Data;

@Data
public abstract class ExpressionAST {

    protected Boolean optional;
    
	protected QueryAST query;

	protected ReduceAST reduce;

	protected ConverterAST converter;

}
