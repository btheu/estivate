package estivate;

import java.io.IOException;
import java.io.InputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;

public abstract class EstivateTest {

	protected EstivateMapper mapper = new EstivateMapper();

	protected void assertNotBlank(String object) {
		Assertions.assertNotNull("Should not be blank", object);
		Assertions.assertNotEquals("Should not be blank", "", object.trim());
	}

	protected void assertContains(String contains, String word) {
		Assertions.assertTrue(word.contains(contains));
	}

	protected void assertNotContains(String contains, String word) {
		Assertions.assertFalse(word.contains(contains));
	}

	protected InputStream read(String string) {
		InputStream resourceAsStream = EstivateMapper.class.getResourceAsStream(string);
		Assertions.assertNotNull(resourceAsStream);
		return resourceAsStream;
	}

	protected Document readDocument(String string) {
		try {
			return Jsoup.parse(read(string), "UTF-8", "/");
		} catch (IOException e) {
			throw new RuntimeException("Cant parse document.", e);
		}
	}

}
