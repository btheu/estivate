package estivate.lang;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import estivate.EstivateTest;
import estivate.annotations.Select;
import estivate.annotations.Val;

@Slf4j
public class ValTest extends EstivateTest {

    @Test
    public void val2() throws IOException {

        InputStream document = this.read("/val/u2.html");

        Result2 result = this.mapper.map(document, Result2.class);

        Assert.assertNotNull(result);

        Assert.assertEquals("My Value 1 My Value 2 My Value 3", result.getValue1());
        Assert.assertEquals("My Value 1 My Value 2 My Value 3", result.getValue2());

        Assert.assertEquals(3, result.getValues().size());
        Assert.assertEquals("My Value 1", result.getValues().get(0));
        Assert.assertEquals("My Value 2", result.getValues().get(1));
        Assert.assertEquals("My Value 3", result.getValues().get(2));

        log.info(result.toString());
    }

    @Data
    public static class Result2 {

        @Val(select = "input")
        public String value1;

        @Val
        @Select("input")
        public String value2;

        @Val(select = "input")
        public List<String> values;
    }

    @Test
    public void val1() throws IOException {

        InputStream document = this.read("/val/u1.html");

        Result result = this.mapper.map(document, Result.class);

        Assert.assertNotNull(result);

        Assert.assertEquals("My Value", result.getValue1());
        Assert.assertEquals("My Value", result.getValue2());
        Assert.assertEquals("My Value", result.getValue3());

        log.info(result.toString());
    }

    @Data
    public static class Result {

        @Val(select = "#name")
        public String value1;

        @Val
        @Select("#name")
        public String value2;

        public String value3;

        @Val(select = "#name")
        public void setValue3(String value3) {
            this.value3 = value3;
        }

    }

}
