package estivate.core.ast.parser2;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import estivate.core.ClassUtils;
import estivate.core.MembersFinder;
import estivate.core.ast.EstivateAST;
import estivate.core.ast.ExpressionAST;
import estivate.core.ast.FieldExpressionAST;
import estivate.core.ast.ListValuesAST;
import estivate.core.ast.SimpleValueAST;
import estivate.core.impl.DefaultMembersFinder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EstivateParser2 {

    protected static List<ClassParser> classParsers = new ArrayList<ClassParser>();
    protected static List<MemberParser> memberParsers = new ArrayList<MemberParser>();
    protected static List<AnnotationParser> annotationParsers = new ArrayList<AnnotationParser>();
    protected static List<TypeParser> typeParsers = new ArrayList<TypeParser>();

    protected static MembersFinder membersFinder = new DefaultMembersFinder();

    public static EstivateAST parse(Class<?> clazz) {
        EstivateAST ast = new EstivateAST();

        for (ClassParser parser : classParsers) {

            parser.parseClass(ast, clazz);

        }


        //
        //		parseExpressions(ast, clazz);

        log.debug("AST of '{}' is {}",clazz.toString(), ast);

        return ast;
    }

    public static ClassParser cParser = new ClassParser() {

        public void parseClass(EstivateAST ast, Class<?> clazz) {

            ast.setTargetType(clazz);
            ast.setTargetRawClass(ClassUtils.rawType(clazz));

            //ast.setQuery(parseQuery(clazz.getAnnotations()));
            
            List<AccessibleObject> list = membersFinder.list(clazz);
            for (AccessibleObject member : list) {
                for (MemberParser parser : memberParsers) {
                    parser.parseMember(ast, member);
                }
            }


        }
    };

    public static MemberParser fieldParser = new MemberParser() {

        public void parseMember(EstivateAST ast, AccessibleObject member) {
            if(member instanceof Field){
                Field field = (Field) member;
                
                FieldExpressionAST fieldAST = new FieldExpressionAST();
                
                for (AnnotationParser parser : annotationParsers) {
                    parser.parseMember(fieldAST, field.getAnnotations());
                }
                for (TypeParser parser : typeParsers) {
                    parser.parseType(fieldAST, field.getType());
                }
                
                ast.getExpressions().add(fieldAST);
            }
        }
        
    };
    
    public static TypeParser typeParser = new TypeParser() {

        public void parseType(ExpressionAST ast, Type... types) {
            
            ListValuesAST list = new ListValuesAST();
            
            for (Type type : types) {
                
                SimpleValueAST value = new SimpleValueAST();
                
                value.setType(type);
                value.setRawClass(ClassUtils.rawType(type));
                value.setValueList(value.getRawClass().equals(List.class));
                
                list.getValues().add(value);
                
            }
            
            ast.setValue(list);
            
        }
        
    };
    
    public interface ClassParser {
        public void parseClass(EstivateAST ast, Class<?> clazz);
    }

    public interface MemberParser {
        public void parseMember(EstivateAST ast, AccessibleObject member);
    }

    public interface AnnotationParser {
        public void parseMember(ExpressionAST ast, Annotation[] annotations);
    }
    
    public interface TypeParser {
        public void parseType(ExpressionAST ast, Type...types);
    }

}
