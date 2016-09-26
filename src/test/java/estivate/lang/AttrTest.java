package estivate.lang;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import estivate.EstivateTest;
import estivate.annotations.Attr;
import estivate.annotations.Select;

@Slf4j
public class AttrTest extends EstivateTest {

    @Test
    public void attr2() throws IOException {

        InputStream document = this.read("/attr/u2.html");

        Result2 result = this.mapper.map(document, Result2.class);

        Assert.assertNotNull(result);
        Assert.assertEquals("role_1 role_2 role_3", result.getRole1());
        Assert.assertEquals("role_1 role_2 role_3", result.getRole2());

        Assert.assertEquals(3, result.getRoles().size());
        Assert.assertEquals("role_1", result.getRoles().get(0));
        Assert.assertEquals("role_2", result.getRoles().get(1));
        Assert.assertEquals("role_3", result.getRoles().get(2));

        log.info(result.toString());

    }

    @Data
    public static class Result2 {

        @Attr(select = "div", value = "role")
        public String role1;

        @Attr("role")
        @Select(select = "div")
        public String role2;

        @Attr(select = "div", value = "role")
        public List<String> roles;

    }

    @Test
    public void attr1() throws IOException {

        InputStream document = this.read("/attr/u1.html");

        Result result = this.mapper.map(document, Result.class);

        Assert.assertNotNull(result);
        Assert.assertEquals("role_of_name", result.getName1());
        Assert.assertEquals("role_of_name", result.getName2());
        Assert.assertEquals("role_of_name", result.getName3());

        log.info(result.toString());

    }

    @Data
    public static class Result {

        @Attr(select = "#name", value = "role")
        public String name1;

        @Attr("role")
        @Select(select = "#name")
        public String name2;

        public String name3;

        @Attr(select = "#name", value = "role")
        public void setName3(String name3) {
            this.name3 = name3;
        }

    }

}
