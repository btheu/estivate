package estivate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;

import estivate.annotations.Select;
import estivate.annotations.Text;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SelectTest extends EstivateTest {

    @Test(expected=IllegalArgumentException.class)
    public void selectFail1() throws IOException {

        InputStream document = read("/select/u2.html");

        mapper.map(document, ResultFail1.class);

    }
    
    @Test(expected=IllegalArgumentException.class)
    public void selectFail2() throws IOException {
        
        InputStream document = read("/select/u2.html");
        
        mapper.map(document, ResultFail2.class);
        
    }

    @Test
    public void select1() throws IOException {

        InputStream document = read("/select/u1.html");

        ResultElement result = mapper.map(document, ResultElement.class);

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getInput1());
        Assert.assertNotNull(result.getInput2());

        Assert.assertEquals("input", result.getInput1().tagName());
        Assert.assertEquals("input", result.getInput2().tagName());

        log.info(result.toString());

    }

    @Test
    public void selectList1() throws IOException {

        InputStream document = read("/select/u2.html");

        Collection<ResultList> results = mapper.mapToList(document,
                ResultList.class);

        Assert.assertNotNull(results);
        Assert.assertEquals(3, results.size());

        for (ResultList result : results) {
            Assert.assertNotNull(result);
            Assert.assertNotNull(result.getName1());
            Assert.assertNotNull(result.getName2());
        }

        log.info(results.toString());

    }

    @Data
    public static class ResultElement {

        @Select(select="#id1 input")
        public Element input1;

        public Element input2;

        @Select("#id1 input")
        public void setName2(Element input) {
            this.input2 = input;
        }

    }

    @Data
    @Select(".someClass")
    public static class ResultList {

        @Text(select=".name")
        public String name1;

        public String name2;

        @Text(select=".name")
        public void setName2(String name) {
            this.name2 = name;
        }

    }

    @Data
    public static class ResultFail1 {

        @Select(".name")
        public String name1;

    }

    @Data
    public static class ResultFail2 {

        @Select(".name")
        public void setName2(String name) {

        }

    }

}
