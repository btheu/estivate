package estivate.hotfix;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import estivate.EstivateTest;
import estivate.annotations.Text;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Sof36333383 extends EstivateTest {

	@Test
	public void sof_36333383() throws IOException {
		// http://stackoverflow.com/questions/36333383/data-scraping-for-an-android-app-from-a-local-html-page

		InputStream document = read("/sof/u36333383.html");

		Document doc = Jsoup.parse(document, "UTF-8", "/");

		Result_36333383 result = mapper.map(doc, Result_36333383.class);

		Assertions.assertNotNull(result);

		log.info(result.toString());
	}

	@Data
	public static class Result_36333383 {

		@Text(select = "font:matchesOwn(.*:.*)")
		public List<String> times;

		@Text(select = "font:not(:matchesOwn(.*:.*))")
		public List<String> others;

	}

}
