package estivate;

import java.io.InputStream;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import estivate.annotations.Val;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValTest {

    EstivateMapper mapper = new EstivateMapper();

    @Test
    public void val2() {

        InputStream document = read("/val/u2.html");

        Result2 result = mapper.map(document, Result2.class);

        Assert.assertNotNull(result);

        Assert.assertEquals("My Value 1 My Value 2 My Value 3",
                result.getValue());

        Assert.assertEquals(3, result.getValues().size());
        Assert.assertEquals("My Value 1", result.getValues().get(0));
        Assert.assertEquals("My Value 2", result.getValues().get(1));
        Assert.assertEquals("My Value 3", result.getValues().get(2));

        log.info(result.toString());
    }

    @Data
    public static class Result2 {

        @Val(select = "input")
        public String value;

        @Val(select = "input")
        public List<String> values;
    }

    @Test
    public void val1() {

        InputStream document = read("/val/u1.html");

        Result result = mapper.map(document, Result.class);

        Assert.assertNotNull(result);

        Assert.assertEquals("My Value", result.getValue1());
        Assert.assertEquals("My Value", result.getValue2());

        log.info(result.toString());
    }

    @Data
    public static class Result {

        @Val(select = "#name")
        public String value1;

        public String value2;

        @Val(select = "#name")
        public void setValue2(String value2) {
            this.value2 = value2;
        }

    }

    private InputStream read(String string) {
        InputStream resourceAsStream = EstivateMapper.class
                .getResourceAsStream(string);
        Assert.assertNotNull(resourceAsStream);
        return resourceAsStream;
    }
}
