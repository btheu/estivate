package estivate.hotfix;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import estivate.EstivateTest;
import estivate.annotations.Select;
import estivate.annotations.Text;

@Slf4j
public class LBC1 extends EstivateTest {

    private List<Article> articles;

    /**
     * 
     * @throws IOException
     * @throws SecurityException
     * @throws NoSuchFieldException
     */
    @Test
    @SuppressWarnings("unchecked")
    public void hotfix1() throws IOException, NoSuchFieldException, SecurityException {

        InputStream document = this.read("/hotfixes/lbc1.html");

        // sinumlate a use case from other api
        Type type = LBC1.class.getDeclaredField("articles").getGenericType();

        List<Article> articles = (List<Article>) this.mapper.map(document, type);

        Assert.assertNotNull(articles);
        Assert.assertEquals(35, articles.size());

        log.info(articles.toString());

        for (Article article : articles) {
            assertNotBlank(article.getTitre());
        }
        
    }

    @Data
    @Select(select = ".tabsContent li")
    public static class Article {

        @Text(select = ".item_title")
        public String titre;

    }

}
