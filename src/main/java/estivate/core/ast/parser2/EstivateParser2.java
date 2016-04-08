package estivate.core.ast.parser2;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import estivate.core.ClassUtils;
import estivate.core.MembersFinder;
import estivate.core.ast.EmptyQueryAST;
import estivate.core.ast.EmptyReduceAST;
import estivate.core.ast.EstivateAST;
import estivate.core.ast.ExpressionAST;
import estivate.core.ast.FieldExpressionAST;
import estivate.core.ast.MethodExpressionAST;
import estivate.core.ast.lang.ListValueAST;
import estivate.core.ast.lang.SimpleValueAST;
import estivate.core.ast.parser.AttrParser;
import estivate.core.ast.parser.SelectParser;
import estivate.core.ast.parser.TextParser;
import estivate.core.impl.DefaultMembersFinder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EstivateParser2 {

    protected static List<ClassParser> classParsers = new ArrayList<ClassParser>();
    protected static List<MemberParser> memberParsers = new ArrayList<MemberParser>();
    protected static List<AnnotationParser> annotationParsers = new ArrayList<AnnotationParser>();

    protected static MembersFinder membersFinder = new DefaultMembersFinder();

    public static EstivateAST parse(Class<?> clazz) {
        EstivateAST ast = new EstivateAST();

        for (ClassParser parser : classParsers) {
            parser.parseClass(ast, clazz);
        }

        log.debug("AST of '{}' is {}",clazz.toString(), ast);

        return ast;
    }

    public static ClassParser cParser = new ClassParser() {

        public void parseClass(EstivateAST ast, Class<?> clazz) {

            ast.setTargetType(clazz);
            ast.setTargetRawClass(ClassUtils.rawType(clazz));

            for (AnnotationParser parser : annotationParsers) {
                parser.parseAnnotation(ast, clazz.getAnnotations());
            }

            List<AccessibleObject> list = membersFinder.list(clazz);
            for (AccessibleObject member : list) {
                for (MemberParser parser : memberParsers) {
                    parser.parseMember(ast, member);
                }
            }


        }
    };

    public static SimpleValueAST parseType2(Type type) {
        SimpleValueAST value = new SimpleValueAST();

        Class<?> rawType = ClassUtils.rawType(type);

        boolean isValueList = rawType.equals(List.class);

        value.setType(type);
        value.setRawClass(rawType);
        value.setValueList(isValueList);

        if(isValueList){
            Class<?>[] typeArguments = ClassUtils.typeArguments(value.getType());
            if (typeArguments.length != 1) {
                throw new IllegalArgumentException("Cant handle such generic type: " + value.getType().toString());
            }
            
            value.setAst(EstivateParser2.parse(ClassUtils.rawType(typeArguments[0])));

        }else{
            value.setAst(EstivateParser2.parse(value.getRawClass()));
        }


        return value;
    }

    public static MemberParser fieldParser = new MemberParser() {

        public void parseMember(EstivateAST ast, AccessibleObject member) {
            if(member instanceof Field){
                Field field = (Field) member;

                FieldExpressionAST fieldAST = new FieldExpressionAST();
                fieldAST.setField(field);

                for (AnnotationParser parser : annotationParsers) {
                    parser.parseAnnotation(fieldAST, field.getAnnotations());
                }

                if(!isEmptyExpression(fieldAST)){
                    parseType(fieldAST, field.getGenericType());

                    ast.getExpressions().add(fieldAST);
                }
            }
        }

        public void parseType(FieldExpressionAST ast, Type type) {

            SimpleValueAST value = parseType2(type);

            ast.setValue(value);

        }


    };

    public static MemberParser methodParser = new MemberParser() {

        public void parseMember(EstivateAST ast, AccessibleObject member) {
            if(member instanceof Method){
                Method method = (Method) member;

                MethodExpressionAST methodAST = new MethodExpressionAST();
                methodAST.setMethod(method);

                for (AnnotationParser parser : annotationParsers) {
                    parser.parseAnnotation(methodAST, method.getAnnotations());
                }

                if(!isEmptyExpression(methodAST)){
                    parseType(methodAST, method.getGenericParameterTypes());

                    ast.getExpressions().add(methodAST);
                }
            }
        }

        public void parseType(MethodExpressionAST ast, Type... types) {

            ListValueAST list = new ListValueAST();

            for (Type type : types) {

                SimpleValueAST value = parseType2(type);

                list.getValues().add(value);

            }

            ast.setValues(list);

        }

    };

    private static boolean isEmptyExpression(ExpressionAST exp) {
        return exp.getReduce() instanceof EmptyReduceAST && exp.getQuery() instanceof EmptyQueryAST;
    }
    
    static{
        classParsers.add(cParser);
        memberParsers.add(fieldParser);
        memberParsers.add(methodParser);

        annotationParsers.add(AttrParser.INSTANCE);
        annotationParsers.add(SelectParser.INSTANCE);
        annotationParsers.add(TextParser.INSTANCE);
    }

    public interface ClassParser {
        public void parseClass(EstivateAST ast, Class<?> clazz);
    }

    public interface MemberParser {
        public void parseMember(EstivateAST ast, AccessibleObject member);
    }

    public interface AnnotationParser {
        public void parseAnnotation(EstivateAST ast, Annotation[] annotations);

        public void parseAnnotation(ExpressionAST ast, Annotation[] annotations);
    }

}
