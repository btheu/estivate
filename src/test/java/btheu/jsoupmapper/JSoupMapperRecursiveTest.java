package btheu.jsoupmapper;

import java.io.InputStream;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JSoupMapperRecursiveTest {

    JSoupMapper mapper = new JSoupMapper();

    @Test
    public void selectRec1() {

        InputStream document = read("/select/u2.html");

        ResultSingle result = mapper.map(document, ResultSingle.class);

        Assert.assertNotNull(result);
        
        Assert.assertNotNull(result.getSubResult());
        Assert.assertNotNull(result.getSubResult2());
        
        Assert.assertEquals("Name 2",result.getSubResult().getName());
        Assert.assertEquals("Name 2",result.getSubResult2().getName());

        log.info(result.toString());
    }

    @Test
    public void selectRec2() {

        InputStream document = read("/select/u2.html");

        ResultList result = mapper.map(document, ResultList.class);

        Assert.assertNotNull(result);
        
        Assert.assertNotNull(result.getSubResults());
        Assert.assertNotNull(result.getSubResults2());
        
        Assert.assertEquals(3, result.getSubResults().size());
        Assert.assertEquals(3, result.getSubResults2().size());

        List<SubResult> subResults = result.getSubResults();
        for (SubResult subResult : subResults) {
			subResult.getName();
		}
        
        List<SubResult> subResults2 = result.getSubResults2();
        for (SubResult subResult : subResults2) {
        	subResult.getName();
        }
        
        log.info(result.toString());

    }

    @Data
    public static class ResultSingle {

        @Select("#div2")
        public SubResult subResult;
        
		public SubResult subResult2;
        
        @Select("#div2")
        public void setSubResult2(SubResult subResult2){
			this.subResult2 = subResult2;
        }

    }

    @Data
    public static class ResultList {

        @Select(".someClass")
        public List<SubResult> subResults;

		public List<SubResult> subResults2;
        
		@Select(".someClass")
        public void setSubResult2(List<SubResult> subResults){
			subResults2 = subResults;
        }
        
    }

    @Data
    public static class SubResult {

    	@Text(select=".name")
        public String name;

    }

    private InputStream read(String string) {
        InputStream resourceAsStream = JSoupMapper.class
                .getResourceAsStream(string);
        Assert.assertNotNull(resourceAsStream);
        return resourceAsStream;
    }
}
