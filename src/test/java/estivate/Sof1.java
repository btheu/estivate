package estivate;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

import estivate.annotations.Attr;
import estivate.annotations.Select;
import estivate.annotations.Text;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Sof1 extends EstivateTest {

	@Test
	public void sof_36385967() throws IOException {
		// http://stackoverflow.com/questions/36385967/parsing-problems-while-data-scraping

		InputStream document = read("/sof/u36385967.html");

		Document doc = Jsoup.parse(document, "UTF-8", "/");

		Result_36385967 result = mapper.map(doc, Result_36385967.class);

		Assert.assertNotNull(result);

		log.info(result.toString());
	}

	@Data
	public static class Result_36385967 {

		@Text(select = "table[border=1] font:matchesOwn(.*:.*)")
		public List<String> times;

		@Text(select = "table[border=1] td[bgcolor!=#C0C0C0] font")
		public List<String> others;

		private List<Day> days;

		@Select(select = "table[border=1] > tbody > tr")
		public void setDays(List<Day> days) {
			this.days = days;
		}

		@Data
		public static class Day {

			@Text(select = "td", first = true)
			public String date;

			@Select(select = "td")
			public List<Program> programs;
		}

		@Data
		public static class Program {

			@Attr(select = "td[colspan!='']", value = "colspan", first = true)
			public int colspan;

			@Text(select = "td table")
			public String detail;

			public List<String> times;
		}
	}

	@Test
	public void sof_36333383() throws IOException {
		// http://stackoverflow.com/questions/36333383/data-scraping-for-an-android-app-from-a-local-html-page

		InputStream document = read("/sof/u36333383.html");

		Document doc = Jsoup.parse(document, "UTF-8", "/");

		Result_36333383 result = mapper.map(doc, Result_36333383.class);

		Assert.assertNotNull(result);

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
