package estivate.core.ast.parser;

import java.lang.annotation.Annotation;

import estivate.annotations.Val;
import estivate.core.ast.EstivateAST;
import estivate.core.ast.ExpressionAST;
import estivate.core.ast.lang.ValReduceAST;
import estivate.core.ast.parser.EstivateParser.AnnotationParser;
import estivate.utils.AnnotationsUtils;

/**
 * Parse {@link Val} annotation
 * 
 * @author Benoit Theunissen
 *
 */
public class ValParser implements AnnotationParser {

	public static final ValParser INSTANCE = new ValParser();
	
	public static final Class<? extends Annotation> TYPE = Val.class;

    public void parseAnnotation(EstivateAST ast, Annotation[] annotations) {}

    public void parseAnnotation(ExpressionAST ast, Annotation[] annotations) {
        
    	Val annotation = (Val) AnnotationsUtils.find(annotations, TYPE);
        if(annotation != null){
        	ast.setQuery(SelectParser.parse(annotation));
        	
        	ValReduceAST reduce = new ValReduceAST();
    		
    		ast.setReduce(reduce);
        }
    }

}
