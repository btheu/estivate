package estivate.hotfix;

import java.io.IOException;
import java.io.InputStream;

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
public class Hotfix1 extends EstivateTest {

	/**
	 * 
	 * @throws IOException
	 */
	@Test
	public void hotfix1() throws IOException {

		InputStream document = read("/hotfixes/u1.html");

		Document doc = Jsoup.parse(document, "UTF-8", "/");

		Page result = mapper.map(doc, Page.class);

		Assert.assertNotNull(result);
		Assert.assertEquals("About 25,100,000 results", result.getResultStatistics());

		log.info(result.toString());
		
	}

    @Data
    public static class Page {

        // get the div holding statistics
    	@Text
    	@Select(select= "#resultStats")
    	//@Text(select = "#resultStats")
        public String resultStatistics;

    }

}
