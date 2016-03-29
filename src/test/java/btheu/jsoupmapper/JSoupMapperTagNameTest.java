package btheu.jsoupmapper;

import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JSoupMapperTagNameTest {

    JSoupMapper mapper = new JSoupMapper();

    @Test
    public void tagName() {

        InputStream document = read("/tagName/u1.html");

        Result result = mapper.map(document, Result.class);

        Assert.assertNotNull(result);
        
        Assert.assertEquals("input", result.getTagName1());
        Assert.assertEquals("input", result.getTagName2());
        
        log.info(result.toString());
    }

    @Data
    public static class Result {

    	@TagName(select="#name")
        public String tagName1;

        public String tagName2;

        @TagName(select="#name")
        public void setTagName2(String tagName2) {
            this.tagName2 = tagName2;
        }

    }

    private InputStream read(String string) {
        InputStream resourceAsStream = JSoupMapper.class
                .getResourceAsStream(string);
        Assert.assertNotNull(resourceAsStream);
        return resourceAsStream;
    }
}
