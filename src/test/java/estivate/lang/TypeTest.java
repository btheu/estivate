package estivate.lang;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

import org.jsoup.nodes.Element;
import org.junit.Test;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import estivate.EstivateTest;
import estivate.annotations.Select;
import estivate.annotations.Text;

/**
 * Test the argument Type case
 * 
 * @author btheu
 *
 */
@Slf4j
public class TypeTest extends EstivateTest {

    protected List<ResultList> resultListField;

    protected ResultElement resultListElement;

    @Test
    @SuppressWarnings("unchecked")
    public void typeList() throws IOException, NoSuchFieldException, SecurityException {

        InputStream document = this.read("/select/u2.html");

        Type type = TypeTest.class.getDeclaredField("resultListField").getGenericType();

        List<ResultList> result = (List<ResultList>) this.mapper.map(document, type);

        log.info(result.toString());
    }

    @Test
    public void typeElement() throws IOException, NoSuchFieldException, SecurityException {

        InputStream document = this.read("/select/u1.html");

        Type type = TypeTest.class.getDeclaredField("resultListElement").getGenericType();

        ResultElement result = (ResultElement) this.mapper.map(document, type);

        log.info(result.toString());

    }

    @Data
    public static class ResultElement {

        @Select(select = "#id1 input")
        public Element input1;

        public Element input2;

        @Select("#id1 input")
        public void setName2(Element input) {
            this.input2 = input;
        }

    }

    @Data
    @Select(".someClass")
    public static class ResultList {

        @Text(select = ".name")
        public String name1;

        public String name2;

        @Text(select = ".name")
        public void setName2(String name) {
            this.name2 = name;
        }

    }

}
