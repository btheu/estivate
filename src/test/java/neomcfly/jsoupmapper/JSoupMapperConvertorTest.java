package neomcfly.jsoupmapper;

import java.io.InputStream;

import org.junit.Test;

import junit.framework.TestCase;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JSoupMapperConvertorTest {

    JSoupMapper mapper = new JSoupMapper();

    @Test
    public void convertorInt1() {

        InputStream document = read("/convertor/u1.html");

        Result result = mapper.map(document, Result.class);

        TestCase.assertNotNull(result);
        
        TestCase.assertEquals(new Integer(3), result.getValue1());
        TestCase.assertEquals(3, result.getValue2());
        
        log.info(result.toString());
    }

    @Data
    public static class Result {

        @JSoupSelect("#intId")
        @JSoupText
        public Integer value1;
        
        @JSoupSelect("#intId")
        @JSoupText
        public int value2;

    }

    private InputStream read(String string) {
        InputStream resourceAsStream = JSoupMapper.class
                .getResourceAsStream(string);
        TestCase.assertNotNull(resourceAsStream);
        return resourceAsStream;
    }
}
