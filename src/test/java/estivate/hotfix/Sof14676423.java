package estivate.hotfix;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

import estivate.EstivateTest;
import estivate.annotations.Select;
import estivate.annotations.Text;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Sof14676423 extends EstivateTest {

	@Test
	public void sof_14676423() throws IOException {
		// http://stackoverflow.com/questions/14676423/parsing-xml-into-hashmap-using-dom

		InputStream document = read("/sof/u14676423.xml");

		Document doc = Jsoup.parse(document, "UTF-8", "/");

		Result_14676423 result = mapper.map(doc, Result_14676423.class);

		Assert.assertNotNull(result);

		log.info(result.toString());
	}

	@Data
	public static class Result_14676423 {

		@Select("user")
		List<User> users;
	
		@Data
		public static class User {

			@Text(select="login")
			String name;
			
			@Text(select="password")
			String password;
			
			@Text(select="enabled")
			boolean enabled;
			
		}

	}

	
	
	
}
