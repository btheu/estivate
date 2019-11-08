package estivate.core.ast.lang;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TableQueryAST extends SelectQueryAST {

    String rowSelector;

}
