package btheu.jsoupmapper;

import java.io.InputStream;

import org.junit.Test;

import btheu.jsoupmapper.JSoupMapper;
import btheu.jsoupmapper.JSoupSelect;
import btheu.jsoupmapper.JSoupTagName;
import junit.framework.TestCase;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JSoupMapperTagNameTest {

    JSoupMapper mapper = new JSoupMapper();

    @Test
    public void tagName() {

        InputStream document = read("/tagName/u1.html");

        Result result = mapper.map(document, Result.class);

        TestCase.assertNotNull(result);
        
        TestCase.assertEquals("input", result.getTagName1());
        TestCase.assertEquals("input", result.getTagName2());
        
        log.info(result.toString());
    }

    @Data
    public static class Result {

        @JSoupSelect("#name")
        @JSoupTagName
        public String tagName1;

        public String tagName2;

        @JSoupSelect("#name")
        @JSoupTagName
        public void setTagName2(String tagName2) {
            this.tagName2 = tagName2;
        }

    }

    private InputStream read(String string) {
        InputStream resourceAsStream = JSoupMapper.class
                .getResourceAsStream(string);
        TestCase.assertNotNull(resourceAsStream);
        return resourceAsStream;
    }
}
