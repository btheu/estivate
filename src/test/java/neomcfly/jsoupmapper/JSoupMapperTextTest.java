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

        Result result = mapper.fromBody(document, Result.class);

        TestCase.assertNotNull(result);
        TestCase.assertNotNull(result.getName1());
        TestCase.assertNotNull(result.getName2());

        log.info(result.toString());

    }

    @Test
    public void text2() {

        InputStream document = read("/text/u2.html");

        Result fromBody = mapper.fromBody(document, Result.class);

        TestCase.assertNotNull(fromBody);
        TestCase.assertNotNull(fromBody.getName1());
        TestCase.assertNotNull(fromBody.getName2());

        log.info(fromBody.toString());

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

    private InputStream read(String string) {
        InputStream resourceAsStream = JSoupMapper.class
                .getResourceAsStream(string);
        TestCase.assertNotNull(resourceAsStream);
        return resourceAsStream;
    }
}
