package estivate;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import estivate.annotations.TagName;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TagNameTest extends EstivateTest {

    @Test
    public void tagName2() throws IOException {

        InputStream document = read("/tagName/u2.html");

        Result2 result = mapper.map(document, Result2.class);

        Assert.assertNotNull(result);

        Assert.assertEquals("input div span", result.getTagName());

        Assert.assertEquals(3, result.getTagNames().size());
        Assert.assertEquals("input", result.getTagNames().get(0));
        Assert.assertEquals("div", result.getTagNames().get(1));
        Assert.assertEquals("span", result.getTagNames().get(2));

        log.info(result.toString());
    }

    @Data
    public static class Result2 {

        @TagName(select = ".tag")
        public String tagName;

        @TagName(select = ".tag")
        public List<String> tagNames;

    }

    @Test
    public void tagName1() throws IOException {

        InputStream document = read("/tagName/u1.html");

        Result result = mapper.map(document, Result.class);

        Assert.assertNotNull(result);

        Assert.assertEquals("input", result.getTagName1());
        Assert.assertEquals("input", result.getTagName2());

        log.info(result.toString());
    }

    @Data
    public static class Result {

        @TagName(select = "#name")
        public String tagName1;

        public String tagName2;

        @TagName(select = "#name")
        public void setTagName2(String tagName2) {
            this.tagName2 = tagName2;
        }

    }

}
