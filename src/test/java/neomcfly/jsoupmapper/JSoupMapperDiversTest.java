package neomcfly.jsoupmapper;

import java.io.InputStream;

import org.junit.Test;

import junit.framework.TestCase;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JSoupMapperDiversTest {

    JSoupMapper mapper = new JSoupMapper();

    @Test
    public void first() {

        InputStream document = read("/divers/u2.html");

        ResultFirst result = mapper.map(document, ResultFirst.class);

        TestCase.assertNotNull(result);
        
        TestCase.assertEquals("Name 1", result.getValue1());
        TestCase.assertEquals("Name 1", result.getValue2());
        
        log.info(result.toString());
    }
    
    @Test
    public void last() {
    	
    	InputStream document = read("/divers/u2.html");
    	
    	ResultLast result = mapper.map(document, ResultLast.class);
    	
    	TestCase.assertNotNull(result);
    	
    	TestCase.assertEquals("Name 3", result.getValue1());
    	TestCase.assertEquals("Name 3", result.getValue2());
    	
    	log.info(result.toString());
    }

    @Data
    public static class ResultFirst {

        @JSoupSelect(".someClass")
        @JSoupFirst
        @JSoupText
        public String value1;

        public String value2;

        @JSoupSelect(".someClass")
        @JSoupFirst
        @JSoupText
        public void setValue2(String value2) {
            this.value2 = value2;
        }

    }
    
    @Data
    public static class ResultLast {
    	
    	@JSoupSelect(".someClass")
    	@JSoupLast
    	@JSoupText
    	public String value1;
    	
    	public String value2;
    	
    	@JSoupSelect(".someClass")
    	@JSoupLast
    	@JSoupText
    	public void setValue2(String value2) {
    		this.value2 = value2;
    	}
    	
    }

    private InputStream read(String string) {
        InputStream resourceAsStream = JSoupMapper.class
                .getResourceAsStream(string);
        TestCase.assertNotNull(resourceAsStream);
        return resourceAsStream;
    }
}
