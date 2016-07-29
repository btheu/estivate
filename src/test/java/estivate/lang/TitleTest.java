package estivate.lang;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

import estivate.EstivateTest;
import estivate.annotations.Select;
import estivate.annotations.Title;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TitleTest extends EstivateTest {

    @Test
    public void title1() throws IOException {

        InputStream document = read("/title/u1.html");

        Result result = mapper.map(document, Result.class);

        Assert.assertNotNull(result);

        Assert.assertEquals("Page Title", result.getTitle1());
        Assert.assertEquals("Page Title", result.getTitle2());

        log.info(result.toString());
    }

    @Test
    public void title2Rec() throws IOException {

        InputStream document = read("/title/u1.html");

        ResultPage result = mapper.map(document, ResultPage.class);

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getContent());

        Assert.assertEquals("Page Title", result.getContent().getTitle1());
        Assert.assertEquals("Page Title", result.getContent().getTitle2());

        log.info(result.toString());
    }

    @Data
    public static class Result {

        @Title
        public String title1;

        public String title2;

        @Title
        public void setValue2(String title2) {
            this.title2 = title2;
        }

    }

    @Data
    public static class ResultPage {

        @Select("#content")
        public ResultContent content;

    }

    @Data
    public static class ResultContent {

        @Title
        public String title1;

        public String title2;

        @Title
        public void setValue2(String title2) {
            this.title2 = title2;
        }

    }

}
