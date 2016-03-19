package neomcfly.jsoupmapper;

import java.io.InputStream;

import org.junit.Test;

import junit.framework.TestCase;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JSoupMapperTest {

    JsoupMapper mapper = new JsoupMapper();

    @Test
    public void attr1() {

        InputStream document = read("/attr/u1.html");

        Result result = mapper.fromBody(document, Result.class);

        TestCase.assertNotNull(result);
        TestCase.assertNotNull(result.getName());

        log.info(result.toString());

    }

    @Test
    public void attr2() {

        InputStream document = read("/attr/u2.html");

        Result fromBody = mapper.fromBody(document, Result.class);

        TestCase.assertNotNull(fromBody);
        TestCase.assertNotNull(fromBody.getName());

        log.info(fromBody.toString());

    }

    @Data
    public static class Result {

        @JsoupSelect("#name")
        @JsoupText
        public String name;

    }

    private InputStream read(String string) {
        InputStream resourceAsStream = JsoupMapper.class
                .getResourceAsStream(string);
        TestCase.assertNotNull(resourceAsStream);
        return resourceAsStream;
    }
}
