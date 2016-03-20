package neomcfly.jsoupmapper;

import java.io.InputStream;

import org.junit.Test;

import junit.framework.TestCase;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JSoupMapperTextTest {

    JSoupMapper mapper = new JSoupMapper();

    @Test
    public void text1() {

        InputStream document = read("/text/u1.html");

        Result result = mapper.map(document, Result.class);

        TestCase.assertNotNull(result);
        TestCase.assertNotNull(result.getName1());
        TestCase.assertNotNull(result.getName2());

        log.info(result.toString());

    }

    @Test
    public void textOwn1() {

        InputStream document = read("/text/u1.html");

        ResultOwn result = mapper.map(document, ResultOwn.class);

        TestCase.assertNotNull(result);
        TestCase.assertNotNull(result.getName1());
        TestCase.assertNotNull(result.getName2());

        TestCase.assertEquals("John", result.getName1());
        TestCase.assertEquals("John", result.getName2());
        TestCase.assertEquals("John", result.getName3());
        TestCase.assertEquals("John", result.getName4());

        log.info(result.toString());

    }

    @Data
    public static class Result {

        @JSoupSelect("#name")
        @JSoupText
        public String name1;

        public String name2;

        @JSoupSelect("#name")
        @JSoupText
        public void setName2(String name2) {
            this.name2 = name2;
        }

    }

    @Data
    public static class ResultOwn {

        @JSoupSelect("#name")
        @JSoupText(own = true)
        public String name1;

        @JSoupSelect("#name")
        @JSoupText(true)
        public String name2;

        public String name3;

        public String name4;

        @JSoupSelect("#name")
        @JSoupText(own = true)
        public void setName3(String name) {
            this.name3 = name;
        }

        @JSoupSelect("#name")
        @JSoupText(true)
        public void setName4(String name) {
            this.name4 = name;
        }

    }

    private InputStream read(String string) {
        InputStream resourceAsStream = JSoupMapper.class
                .getResourceAsStream(string);
        TestCase.assertNotNull(resourceAsStream);
        return resourceAsStream;
    }
}
