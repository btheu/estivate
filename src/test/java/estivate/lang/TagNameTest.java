package estivate.lang;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import estivate.EstivateTest;
import estivate.annotations.Select;
import estivate.annotations.TagName;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TagNameTest extends EstivateTest {

    @Test
    public void tagName2() throws IOException {

        InputStream document = this.read("/tagName/u2.html");

        Result2 result = this.mapper.map(document, Result2.class);

        Assertions.assertNotNull(result);

        Assertions.assertEquals("input div span", result.getTagName1());
        Assertions.assertEquals("input div span", result.getTagName2());

        Assertions.assertEquals(3, result.getTagNames().size());
        Assertions.assertEquals("input", result.getTagNames().get(0));
        Assertions.assertEquals("div", result.getTagNames().get(1));
        Assertions.assertEquals("span", result.getTagNames().get(2));

        log.info(result.toString());
    }

    @Data
    public static class Result2 {

        @TagName(select = ".tag")
        public String tagName1;

        @TagName
        @Select(".tag")
        public String tagName2;

        @TagName(select = ".tag")
        public List<String> tagNames;

    }

    @Test
    public void tagName1() throws IOException {

        InputStream document = this.read("/tagName/u1.html");

        Result result = this.mapper.map(document, Result.class);

        Assertions.assertNotNull(result);

        Assertions.assertEquals("input", result.getTagName1());
        Assertions.assertEquals("input", result.getTagName2());
        Assertions.assertEquals("input", result.getTagName3());

        log.info(result.toString());
    }

    @Data
    public static class Result {

        @TagName(select = "#name")
        public String tagName1;

        @TagName
        @Select("#name")
        public String tagName2;

        public String tagName3;

        @TagName(select = "#name")
        public void setTagName3(String tagName3) {
            this.tagName3 = tagName3;
        }

    }

}
