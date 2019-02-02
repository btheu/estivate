package estivate.lang;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

import estivate.EstivateTest;
import estivate.annotations.Is;
import estivate.annotations.Select;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IsTest extends EstivateTest {

    @Test
    public void is2() throws IOException {

        InputStream document = this.read("/is/u1.html");

        Result2 result = this.mapper.map(document, Result2.class);

        Assert.assertNotNull(result);

        Assert.assertEquals(false, result.isValue1());
        Assert.assertEquals(false, result.isValue2());
        Assert.assertEquals(true, result.isValue3());

        log.info(result.toString());
    }

    @Data
    public static class Result2 {

        @Is(select = "#id1", value = ".someClass2")
        public boolean value1;

        @Is(select = "#id1", value = ".someClass3")
        public boolean value2;

        @Is(select = "#id3", value = ".someClass1")
        public boolean value3;

    }

    @Test
    public void is1() throws IOException {

        InputStream document = this.read("/is/u1.html");

        Result result = this.mapper.map(document, Result.class);

        Assert.assertNotNull(result);

        Assert.assertEquals(true, result.getValue1());
        Assert.assertEquals(true, result.isValue2());
        Assert.assertEquals(true, result.isValue3());

        log.info(result.toString());
    }

    @Data
    public static class Result {

        @Is(select = "#id1", value = ".someClass1")
        public Boolean value1;

        @Is(".someClass1")
        @Select("#id1")
        public boolean value2;

        public boolean value3;

        @Is(select = "#id1", value = ".someClass1")
        public void setValue3(boolean value3) {
            this.value3 = value3;
        }

    }

}
