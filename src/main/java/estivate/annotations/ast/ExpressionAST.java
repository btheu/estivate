package estivate.annotations.ast;

import lombok.Data;

@Data
public abstract class ExpressionAST {

	protected SelectAST select;

	protected ReduceAST reduce;

	protected Boolean optional;

	protected Boolean isTargetList = false;

}
