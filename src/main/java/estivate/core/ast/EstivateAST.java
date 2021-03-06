package estivate.core.ast;

import java.lang.reflect.Type;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EstivateAST extends ExpressionsAST {

    protected Type targetType;

    protected Class<?> targetRawClass;

}
