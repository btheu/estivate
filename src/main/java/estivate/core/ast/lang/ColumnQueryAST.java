package estivate.core.ast.lang;

import java.util.regex.Pattern;

import estivate.core.ast.QueryAST;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ColumnQueryAST extends QueryAST {

    protected String columnName;

    protected Pattern regex;

}
