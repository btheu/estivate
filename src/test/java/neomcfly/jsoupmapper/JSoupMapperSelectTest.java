package neomcfly.jsoupmapper;

import java.io.InputStream;
import java.util.Collection;

import org.jsoup.nodes.Element;
import org.junit.Test;

import junit.framework.TestCase;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JSoupMapperSelectTest {

    JSoupMapper mapper = new JSoupMapper();

    @Test
    public void select1() {

        InputStream document = read("/select/u1.html");

        ResultElement result = mapper.map(document, ResultElement.class);

        TestCase.assertNotNull(result);
        TestCase.assertNotNull(result.getInput1());
        TestCase.assertNotNull(result.getInput2());

        TestCase.assertEquals("input", result.getInput1().tagName());
        TestCase.assertEquals("input", result.getInput2().tagName());

        log.info(result.toString());

    }

    @Test
    public void selectList1() {

        InputStream document = read("/select/u2.html");

        Collection<ResultList> results = mapper.mapToList(document,
                ResultList.class);

        TestCase.assertNotNull(results);
        TestCase.assertEquals(results.size(), 3);

        for (ResultList result : results) {
            TestCase.assertNotNull(result);
            TestCase.assertNotNull(result.getName1());
            TestCase.assertNotNull(result.getName2());
        }

        log.info(results.toString());

    }

    @Data
    public static class ResultElement {

        @JSoupSelect("#id1 input")
        public Element input1;

        public Element input2;

        @JSoupSelect("#id1 input")
        public void setName2(Element input) {
            this.input2 = input;
        }

    }

    @Data
    @JSoupListSelect(".someClass")
    public static class ResultList {

        @JSoupSelect(".name")
        @JSoupText
        public String name1;

        public String name2;

        @JSoupSelect(".name")
        @JSoupText
        public void setName2(String name) {
            this.name2 = name;
        }

    }

    private InputStream read(String string) {
        InputStream resourceAsStream = JSoupMapper.class
                .getResourceAsStream(string);
        TestCase.assertNotNull(resourceAsStream);
        return resourceAsStream;
    }
}
