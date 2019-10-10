package estivate.core.ast;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public abstract class ExpressionAST extends NodeAST {

    protected Boolean optional = false;

    protected ReduceAST reduce = new EmptyReduceAST();

    protected ConverterAST converter;

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
