package estivate;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import estivate.annotations.Attr;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AttrTest extends EstivateTest { 

    @Test
    public void attr2() throws IOException {

        InputStream document = read("/attr/u2.html");

        Result2 result = mapper.map(document, Result2.class);

        Assert.assertNotNull(result);
        Assert.assertEquals("role_1 role_2 role_3", result.getRole());

        Assert.assertEquals(3, result.getRoles().size());
        Assert.assertEquals("role_1", result.getRoles().get(0));
        Assert.assertEquals("role_2", result.getRoles().get(1));
        Assert.assertEquals("role_3", result.getRoles().get(2));

        log.info(result.toString());

    }

    @Data
    public static class Result2 {

        @Attr(select = "div", value = "role")
        public String role;

        @Attr(select = "div", value = "role")
        public List<String> roles;

    }

    @Test
    public void attr1() throws IOException {

        InputStream document = read("/attr/u1.html");

        Result result = mapper.map(document, Result.class);

        Assert.assertNotNull(result);
        Assert.assertEquals("role_of_name", result.getName1());
        Assert.assertEquals("role_of_name", result.getName2());

        log.info(result.toString());

    }

    @Data
    public static class Result {

        @Attr(select = "#name", value = "role")
        public String name1;

        public String name2;

        @Attr(select = "#name", value = "role")
        public void setName2(String name2) {
            this.name2 = name2;
        }

    }

}
