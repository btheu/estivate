package btheu.jsoupmapper;

import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JSoupMapperValTest {

    JSoupMapper mapper = new JSoupMapper();

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

    	@Val(select="#name")
        public String value1;

        public String value2;

        @Val(select="#name")
        public void setValue2(String value2) {
            this.value2 = value2;
        }

    }

    private InputStream read(String string) {
        InputStream resourceAsStream = JSoupMapper.class
                .getResourceAsStream(string);
        Assert.assertNotNull(resourceAsStream);
        return resourceAsStream;
    }
}
