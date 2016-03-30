package estivate;

import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

import estivate.EstivateMapper;
import estivate.annotations.Attr;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AttrTest {

    EstivateMapper mapper = new EstivateMapper();

    @Test
    public void attr1() {

        InputStream document = read("/attr/u1.html");

        Result result = mapper.map(document, Result.class);

        Assert.assertNotNull(result);
        Assert.assertEquals("role_of_name",result.getName1());
        Assert.assertEquals("role_of_name",result.getName2());

        log.info(result.toString());

    }

    @Data
    public static class Result {

    	@Attr(select="#name", value="role")
        public String name1;

        public String name2;

        @Attr(select="#name", value="role")
        public void setName2(String name2) {
            this.name2 = name2;
        }

    }

    private InputStream read(String string) {
        InputStream resourceAsStream = EstivateMapper.class
                .getResourceAsStream(string);
        Assert.assertNotNull(resourceAsStream);
        return resourceAsStream;
    }
}
