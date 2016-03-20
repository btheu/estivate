package neomcfly.jsoupmapper;

import java.io.InputStream;
import java.util.List;

import org.junit.Test;

import junit.framework.TestCase;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JSoupMapperRecursiveTest {

    JSoupMapper mapper = new JSoupMapper();

    @Test
    public void selectRec1() {

        InputStream document = read("/select/u2.html");

        ResultList result = mapper.map(document, ResultList.class);

        TestCase.assertNotNull(result);
        TestCase.assertNotNull(result.getSubResults());

        TestCase.assertEquals(result.getSubResults().size(), 3);

        log.info(result.toString());

    }

    @Test
    public void selectRec2() {

        InputStream document = read("/select/u2.html");

        ResultSingle result = mapper.map(document, ResultSingle.class);

        TestCase.assertNotNull(result);
        TestCase.assertNotNull(result.getSubResult());
        TestCase.assertNotNull(result.getSubResult().getName());

        log.info(result.toString());

    }

    @Data
    public static class ResultSingle {

        @JSoupSelect("#div2")
        public SubResult subResult;

    }

    @Data
    public static class ResultList {

        @JSoupSelect(".someClass")
        public List<SubResult> subResults;

    }

    @Data
    public static class SubResult {

        @JSoupSelect(".name")
        @JSoupText
        public String name;

    }

    private InputStream read(String string) {
        InputStream resourceAsStream = JSoupMapper.class
                .getResourceAsStream(string);
        TestCase.assertNotNull(resourceAsStream);
        return resourceAsStream;
    }
}
