package estivate.core.ast.lang;

import java.util.ArrayList;
import java.util.List;

import estivate.core.ast.ValueAST;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ListValueAST extends ValueAST {

	protected List<SimpleValueAST> values = new ArrayList<SimpleValueAST>();

}
