package neomcfly.jsoupmapper;

import java.io.InputStream;
import java.util.List;

import org.junit.Test;

import junit.framework.TestCase;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JSoupMapperRecursiveTest {

    JSoupMapper mapper = new JSoupMapper();

    @Test
    public void selectRec1() {

        InputStream document = read("/select/u2.html");

        ResultSingle result = mapper.map(document, ResultSingle.class);

        TestCase.assertNotNull(result);
        
        TestCase.assertNotNull(result.getSubResult());
        TestCase.assertNotNull(result.getSubResult2());
        
        TestCase.assertEquals("Name 2",result.getSubResult().getName());
        TestCase.assertEquals("Name 2",result.getSubResult2().getName());

        log.info(result.toString());
    }

    @Test
    public void selectRec2() {

        InputStream document = read("/select/u2.html");

        ResultList result = mapper.map(document, ResultList.class);

        TestCase.assertNotNull(result);
        
        TestCase.assertNotNull(result.getSubResults());
        TestCase.assertNotNull(result.getSubResults2());
        
        TestCase.assertEquals(3, result.getSubResults().size());
        TestCase.assertEquals(3, result.getSubResults2().size());

        log.info(result.toString());

    }

    @Data
    public static class ResultSingle {

        @JSoupSelect("#div2")
        public SubResult subResult;
        
		public SubResult subResult2;
        
        @JSoupSelect("#div2")
        public void setSubResult2(SubResult subResult2){
			this.subResult2 = subResult2;
        }

    }

    @Data
    public static class ResultList {

        @JSoupSelect(".someClass")
        public List<SubResult> subResults;

		public List<SubResult> subResults2;
        
		@JSoupSelect(".someClass")
        public void setSubResult2(List<SubResult> subResults){
			subResults2 = subResults;
        }
        
    }

    @Data
    public static class SubResult {

        @JSoupSelect(".name")
        @JSoupText
        public String name;

    }

    private InputStream read(String string) {
        InputStream resourceAsStream = JSoupMapper.class
                .getResourceAsStream(string);
        TestCase.assertNotNull(resourceAsStream);
        return resourceAsStream;
    }
}
