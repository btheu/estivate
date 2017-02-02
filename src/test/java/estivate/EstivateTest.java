package estivate;

import java.io.IOException;
import java.io.InputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;

public abstract class EstivateTest {

	protected EstivateMapper mapper = new EstivateMapper();

	protected void assertNotBlank(String object) {
		Assert.assertNotNull("Should not be blank", object);
		Assert.assertNotEquals("Should not be blank", "", object.trim());
	}

	protected void assertContains(String contains, String word) {
		Assert.assertTrue(word.contains(contains));
	}

	protected void assertNotContains(String contains, String word) {
		Assert.assertFalse(word.contains(contains));
	}

	protected InputStream read(String string) {
		InputStream resourceAsStream = EstivateMapper.class.getResourceAsStream(string);
		Assert.assertNotNull(resourceAsStream);
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
