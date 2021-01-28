package estivate;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import estivate.annotations.Attr;
import estivate.annotations.Select;
import estivate.annotations.Text;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RecursiveTest extends EstivateTest {

    @Test
    public void selectRec3() throws IOException {

        InputStream document = read("/select/u2.html");

        Result3 result = mapper.map(document, Result3.class);

        Assertions.assertNotNull(result);

        Assertions.assertNotNull(result.getSubResults());
        Assertions.assertEquals(3, result.getSubResults().size());
        Assertions.assertEquals("role_1", result.getSubResults().get(0).getRole());
        Assertions.assertEquals("role_2", result.getSubResults().get(1).getRole());
        Assertions.assertEquals("role_3", result.getSubResults().get(2).getRole());

        log.info(result.toString());
    }

    @Test
    public void selectRec3Bis() throws IOException {

        InputStream document = read("/select/u2.html");

        Result3 result = mapper.map(document, Result3.class);

        Assertions.assertNotNull(result);

        Assertions.assertNotNull(result.getSubResults());
        Assertions.assertEquals(3, result.getSubResults().size());
        Assertions.assertEquals("Name 1", result.getSubResults().get(0).getName());
        Assertions.assertEquals("Name 2", result.getSubResults().get(1).getName());
        Assertions.assertEquals("Name 3", result.getSubResults().get(2).getName());

        log.info(result.toString());
    }

    @Data
    public static class Result3 {

        @Select(".name")
        public List<SubResult> subResults;

        @Data
        public static class SubResult {

            @Attr("role")
            String role;

            @Text
            String name;

        }

    }

    @Test
    public void selectRec1() throws IOException {

        InputStream document = read("/select/u2.html");

        ResultSingle result = mapper.map(document, ResultSingle.class);

        Assertions.assertNotNull(result);

        Assertions.assertNotNull(result.getSubResult1());
        Assertions.assertNotNull(result.getSubResult2());

        Assertions.assertEquals("Name 2", result.getSubResult1().getName());
        Assertions.assertEquals("Name 2", result.getSubResult2().getName());

        log.info(result.toString());
    }

    @Test
    public void selectRec2() throws IOException {

        InputStream document = read("/select/u2.html");

        ResultList result = mapper.map(document, ResultList.class);

        Assertions.assertNotNull(result);

        Assertions.assertNotNull(result.getSubResults1());
        Assertions.assertNotNull(result.getSubResults2());

        Assertions.assertEquals(3, result.getSubResults1().size());
        Assertions.assertEquals(3, result.getSubResults2().size());

        List<SubResult> subResults = result.getSubResults1();
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
        public SubResult subResult1;

        public SubResult subResult2;

        @Select("#div2")
        public void setSubResult2(SubResult subResult2) {
            this.subResult2 = subResult2;
        }

    }

    @Data
    public static class ResultList {

        @Select(".someClass")
        public List<SubResult> subResults1;

        public List<SubResult> subResults2;

        @Select(".someClass")
        public void setSubResult2(List<SubResult> subResults) {
            this.subResults2 = subResults;
        }

    }

    @Data
    public static class SubResult {

        @Text(select = ".name")
        public String name;

    }

}
