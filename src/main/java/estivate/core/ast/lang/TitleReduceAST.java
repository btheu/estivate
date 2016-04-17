package estivate.core.ast.lang;

import estivate.core.ast.ReduceAST;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TitleReduceAST extends ReduceAST {

	protected boolean optional;
	
}
