package estivate.core.ast;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ListValuesAST extends ValueAST {

	protected List<SimpleValueAST> values = new ArrayList<SimpleValueAST>();

}
