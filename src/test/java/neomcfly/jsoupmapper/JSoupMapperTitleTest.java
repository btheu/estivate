package neomcfly.jsoupmapper;

import java.io.InputStream;

import org.junit.Test;

import junit.framework.TestCase;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JSoupMapperTitleTest {

    JSoupMapper mapper = new JSoupMapper();

    @Test
    public void title1() {

        InputStream document = read("/title/u1.html");

        Result result = mapper.map(document, Result.class);

        TestCase.assertNotNull(result);

        TestCase.assertEquals("Page Title", result.getTitle1());
        TestCase.assertEquals("Page Title", result.getTitle2());

        log.info(result.toString());
    }

    @Test
    public void title2() {

        InputStream document = read("/title/u1.html");

        ResultPage result = mapper.map(document, ResultPage.class);

        TestCase.assertNotNull(result);
        TestCase.assertNotNull(result.getContent());

        TestCase.assertEquals("Page Title", result.getContent().getTitle1());
        TestCase.assertEquals("Page Title", result.getContent().getTitle2());

        log.info(result.toString());
    }

    @Data
    public static class Result {

        @JSoupSelect("#content")
        @JSoupTitle
        public String title1;

        public String title2;

        @JSoupSelect("#content")
        @JSoupTitle
        public void setValue2(String title2) {
            this.title2 = title2;
        }

    }

    @Data
    public static class ResultPage {

        @JSoupSelect("#content")
        public ResultContent content;

    }

    @Data
    public static class ResultContent {

        @JSoupTitle
        public String title1;

        public String title2;

        @JSoupTitle
        public void setValue2(String title2) {
            this.title2 = title2;
        }

    }

    private InputStream read(String string) {
        InputStream resourceAsStream = JSoupMapper.class
                .getResourceAsStream(string);
        TestCase.assertNotNull(resourceAsStream);
        return resourceAsStream;
    }
}
