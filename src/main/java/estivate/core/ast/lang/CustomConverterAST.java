package estivate.core.ast.lang;

import estivate.core.Converter;
import estivate.core.ast.ConverterAST;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CustomConverterAST extends ConverterAST {

    protected Class<? extends Converter> converterClass;

    protected String format;

}
