package estivate;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import estivate.annotations.Select;
import estivate.annotations.Text;

@Slf4j
public class TextTest extends EstivateTest {

    @Test
    public void text2() throws IOException {

        InputStream document = this.read("/text/u2.html");

        Result2 result = this.mapper.map(document, Result2.class);

        Assert.assertNotNull(result);
        Assert.assertEquals("Text1 Text2 Text3 child of 3", result.getCas1());
        Assert.assertEquals("Text1 Text2 Text3", result.getCas2());

        Assert.assertEquals(3, result.getCas3().size());
        Assert.assertEquals("Text1", result.getCas3().get(0));
        Assert.assertEquals("Text2", result.getCas3().get(1));
        Assert.assertEquals("Text3 child of 3", result.getCas3().get(2));

        Assert.assertEquals(3, result.getCas4().size());
        Assert.assertEquals("Text1", result.getCas4().get(0));
        Assert.assertEquals("Text2", result.getCas4().get(1));
        Assert.assertEquals("Text3", result.getCas4().get(2));

        log.info(result.toString());
    }

    @Data
    public static class Result2 {

        @Text(select = "span")
        public List<String> cas3;

        @Text(select = "span")
        public String cas1;

        @Text(select = "span", own = true)
        public String cas2;

        @Text(select = "span", own = true)
        public List<String> cas4;

    }

    @Test
    public void text1() throws IOException {

        InputStream document = this.read("/text/u1.html");

        Result result = this.mapper.map(document, Result.class);

        Assert.assertNotNull(result);
        Assert.assertEquals("This is my name: John", result.getName1());
        Assert.assertEquals("This is my name: John", result.getName2());
        Assert.assertEquals("This is my name: John", result.getName3());

        log.info(result.toString());

    }

    @Test
    public void textOwn1() throws IOException {

        InputStream document = this.read("/text/u1.html");

        ResultOwn result = this.mapper.map(document, ResultOwn.class);

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getName1());
        Assert.assertNotNull(result.getName2());

        Assert.assertEquals("John", result.getName1());
        Assert.assertEquals("John", result.getName2());

        log.info(result.toString());

    }

    @Data
    public static class Result {

        @Text
        @Select(select = "#name")
        public String name3;

        @Text(select = "#name")
        public String name1;

        public String name2;

        @Text(select = "#name")
        public void setName2(String name2) {
            this.name2 = name2;
        }

    }

    @Data
    public static class ResultOwn {

        @Text(select = "#name", own = true)
        public String name1;

        public String name2;

        @Text(select = "#name", own = true)
        public void setName2(String name) {
            this.name2 = name;
        }

    }

}
