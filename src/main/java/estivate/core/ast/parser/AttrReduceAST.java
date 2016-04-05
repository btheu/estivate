package estivate.core.ast.parser;

import estivate.core.ast.ReduceAST;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 
 * @author Benoit Theunissen
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AttrReduceAST extends ReduceAST {

	protected String attr;

}
