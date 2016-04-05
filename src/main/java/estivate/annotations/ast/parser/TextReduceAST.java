package estivate.annotations.ast.parser;

import estivate.annotations.ast.ReduceAST;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TextReduceAST extends ReduceAST {

    protected boolean own = false;
    
}
