package estivate.core.ast;

import lombok.Data;

@Data
public abstract class ExpressionAST {

	protected QueryAST query;

	protected ReduceAST reduce;

	protected Boolean optional;

}
