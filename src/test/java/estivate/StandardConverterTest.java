package estivate;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import estivate.annotations.Text;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StandardConverterTest extends EstivateTest {

    @Test
    public void convertorPrimitive1() throws IOException {

        InputStream document = read("/converter/u1.html");

        Result result = mapper.map(document, Result.class);

        Assertions.assertNotNull(result);

        Assertions.assertEquals(new Integer("3"), result.getInt1());
        Assertions.assertEquals(3, result.getInt2());

        Assertions.assertEquals(new Boolean("false"), result.getBool1());
        Assertions.assertEquals(false, result.isBool2());

        Assertions.assertEquals(new Character('C'), result.getChar1());
        Assertions.assertEquals('C', result.getChar2());

        Assertions.assertEquals(new Byte("11"), result.getByte1());
        Assertions.assertEquals(11, result.getByte2());

        Assertions.assertEquals(new Double("5.5"), result.getDouble1());
        Assertions.assertEquals(5.5d, result.getDouble2(), 0);

        Assertions.assertEquals(new Long("6"), result.getLong1());
        Assertions.assertEquals(6L, result.getLong2());

        Assertions.assertEquals(new Short("7"), result.getShort1());
        Assertions.assertEquals(7, result.getShort2());

        Assertions.assertEquals(new Float("10.2"), result.getFloat1());
        Assertions.assertEquals(10.2f, result.getFloat2(), 0);

        log.info(result.toString());
    }

    @Test
    public void convertorPrimitive2() throws IOException {

        InputStream document = read("/converter/u1_1.html");

        Result result = mapper.map(document, Result.class);

        Assertions.assertNotNull(result);

        Assertions.assertEquals(new Integer("3000"), result.getInt1());
        Assertions.assertEquals(3000, result.getInt2());

        Assertions.assertEquals(new Boolean("false"), result.getBool1());
        Assertions.assertEquals(false, result.isBool2());

        Assertions.assertEquals(new Character('C'), result.getChar1());
        Assertions.assertEquals('C', result.getChar2());

        Assertions.assertEquals(new Byte("11"), result.getByte1());
        Assertions.assertEquals(11, result.getByte2());

        Assertions.assertEquals(new Double("5000.5"), result.getDouble1());
        Assertions.assertEquals(5000.5d, result.getDouble2(), 0);

        Assertions.assertEquals(new Long("6000"), result.getLong1());
        Assertions.assertEquals(6000L, result.getLong2());

        Assertions.assertEquals(new Short("7000"), result.getShort1());
        Assertions.assertEquals(7000, result.getShort2());

        Assertions.assertEquals(new Float("1000.2"), result.getFloat1());
        Assertions.assertEquals(1000.2f, result.getFloat2(), 0);

        log.info(result.toString());
    }

    @Test
    public void convertorListPrimitive1() throws IOException {
        InputStream document = read("/converter/u4.html");

        ResultList1 result = mapper.map(document, ResultList1.class);

        Assertions.assertNotNull(result);

        List<Integer> integers = result.getIntegers1();
        for (Integer integer : integers) {
            log.debug(integer + "");
        }

        integers = result.getIntegers2();
        for (Integer integer : integers) {
            log.debug(integer + "");
        }

    }


    @Test
    public void convertorBigNumber1() throws IOException {
        InputStream document = read("/converter/u2.html");

        ResultBig result = mapper.map(document, ResultBig.class);

        Assertions.assertNotNull(result);

        Assertions.assertEquals(new BigInteger("1111111111111111111111111111111111111111111111111111111111"),
                result.getBigInt1());
        Assertions.assertEquals(new BigDecimal("2222222222222222222222222222222222222222222222222222222222"),
                result.getBigDec1());
    }

    @Data
    public static class ResultList1 {

        @Text(select = "div")
        public List<Integer> integers1;

        public List<Integer> integers2;

        @Text(select = "div")
        public void setIntegers2(List<Integer> integers) {
            integers2 = integers;
        }
    }

    @Data
    public static class ResultBig {

        @Text(select = "#bigIntId")
        public BigInteger bigInt1;

        @Text(select = "#bigDecId")
        public BigDecimal bigDec1;

    }

    @Data
    public static class Result {

        @Text(select = "#intId")
        public Integer int1;

        @Text(select = "#intId")
        public int int2;

        @Text(select = "#charId")
        public Character char1;

        @Text(select = "#charId")
        public char char2;

        @Text(select = "#byteId")
        public Byte byte1;

        @Text(select = "#byteId")
        public byte byte2;

        @Text(select = "#boolId")
        public Boolean bool1;

        @Text(select = "#boolId")
        public boolean bool2;

        @Text(select = "#floatId")
        public Float float1;

        @Text(select = "#floatId")
        public float float2;

        @Text(select = "#doubleId")
        public Double double1;

        @Text(select = "#doubleId")
        public double double2;

        @Text(select = "#longId")
        public Long long1;

        @Text(select = "#longId")
        public long long2;

        @Text(select = "#shortId")
        public Short short1;

        @Text(select = "#shortId")
        public short short2;

    }

}
