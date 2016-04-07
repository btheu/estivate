package estivate.core.ast.lang;

import estivate.core.ast.QueryAST;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SelectQueryAST extends QueryAST {

	protected String queryString;

	protected int index = -1;

	protected boolean unique = false;

	protected boolean first = false;

	protected boolean last = false;

}
