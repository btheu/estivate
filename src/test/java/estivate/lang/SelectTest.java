package estivate.lang;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import estivate.EstivateTest;
import estivate.annotations.Select;
import estivate.annotations.Text;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SelectTest extends EstivateTest {

    @Test
	public void selectIndex1() throws IOException {

		InputStream document = read("/select/u2.html");

		ResultIndex result = mapper.map(document, ResultIndex.class);

		Assertions.assertNotNull(result);
		assertContains("Name 1", result.getName1());
		assertContains("Name 2", result.getName2());
		assertContains("Name 3", result.getName3());

		assertNotContains("Name 2", result.getName1());
		assertNotContains("Name 3", result.getName1());

		assertNotContains("Name 1", result.getName2());
		assertNotContains("Name 3", result.getName2());

		assertNotContains("Name 1", result.getName3());
		assertNotContains("Name 2", result.getName3());
	}

	@Test
    public void selectString1() throws IOException {

        InputStream document = read("/select/u2.html");

        ResultString result = mapper.map(document, ResultString.class);

        Assertions.assertNotNull(result);
        assertNotBlank(result.getName1());
        assertNotBlank(result.getName2());
    }

	@Test
    public void select1() throws IOException {

        InputStream document = read("/select/u1.html");

        ResultElement result = mapper.map(document, ResultElement.class);

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getInput1());
        Assertions.assertNotNull(result.getInput2());

        Assertions.assertEquals("input", result.getInput1().tagName());
        Assertions.assertEquals("input", result.getInput2().tagName());

        log.info(result.toString());

    }

    @Test
    public void selectList1() throws IOException {

        InputStream document = read("/select/u2.html");

        Collection<ResultList> results = mapper.mapToList(document,
                ResultList.class);

        Assertions.assertNotNull(results);
        Assertions.assertEquals(3, results.size());

        for (ResultList result : results) {
            Assertions.assertNotNull(result);
            Assertions.assertNotNull(result.getName1());
            Assertions.assertNotNull(result.getName2());
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
    public static class ResultString {

        @Select(".name")
        public String name1;
        
		private String name2;

        @Select(".name")
        public void setName2(String name) {
			this.name2 = name;

        }
        
    }

	@Data
	public static class ResultIndex {

		@Select(value = ".name", index = 1)
		public String name1;

		@Select(value = ".name", index = 3)
		public String name3;

		private String name2;

		@Select(value = ".name", index = 2)
		public void setName2(String name) {
			this.name2 = name;

		}

	}

}
