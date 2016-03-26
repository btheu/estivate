package btheu.jsoupmapper;

import java.io.InputStream;

import org.junit.Test;

import btheu.jsoupmapper.JSoupAttr;
import btheu.jsoupmapper.JSoupMapper;
import btheu.jsoupmapper.JSoupSelect;
import junit.framework.TestCase;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JSoupMapperAttrTest {

    JSoupMapper mapper = new JSoupMapper();

    @Test
    public void attr1() {

        InputStream document = read("/attr/u1.html");

        Result result = mapper.map(document, Result.class);

        TestCase.assertNotNull(result);
        TestCase.assertEquals("role_of_name",result.getName1());
        TestCase.assertEquals("role_of_name",result.getName2());

        log.info(result.toString());

    }

    @Data
    public static class Result {

        @JSoupSelect("#name")
        @JSoupAttr("role")
        public String name1;

        public String name2;

        @JSoupSelect("#name")
        @JSoupAttr("role")
        public void setName2(String name2) {
            this.name2 = name2;
        }

    }

    private InputStream read(String string) {
        InputStream resourceAsStream = JSoupMapper.class
                .getResourceAsStream(string);
        TestCase.assertNotNull(resourceAsStream);
        return resourceAsStream;
    }
}
