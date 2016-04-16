package estivate;

import java.io.IOException;
import java.io.InputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;

public abstract class EstivateTest {

	protected EstivateMapper2 mapper = new EstivateMapper2();
	
	protected void assertNotBlank(String object) {
		Assert.assertNotNull(object);
		Assert.assertNotEquals("", object.trim());
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
