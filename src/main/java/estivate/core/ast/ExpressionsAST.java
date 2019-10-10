package estivate.core.ast;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ExpressionsAST extends NodeAST {

    protected boolean optional = false;

    protected List<ExpressionAST> expressions = new ArrayList<ExpressionAST>();

    /**
     * Once optional was set as true for this expressions, then other parse pass
     * cant change it back to false.
     * 
     * @param value
     *            the new value
     */
    public void setOptional(boolean value) {
        this.optional = optional || value;
    }

}
