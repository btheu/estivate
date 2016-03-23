package neomcfly.jsoupmapper;

import java.io.InputStream;

import org.junit.Test;

import junit.framework.TestCase;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JSoupMapperConvertorTest {

    JSoupMapper mapper = new JSoupMapper();

    @Test
    public void convertorPrimitive1() {

        InputStream document = read("/convertor/u1.html");

        Result result = mapper.map(document, Result.class);

        TestCase.assertNotNull(result);
        
        TestCase.assertEquals(new Integer("3"), result.getInt1());
        TestCase.assertEquals(3, result.getInt2());
        
        TestCase.assertEquals(new Boolean("false"), result.getBool1());
        TestCase.assertEquals(false, result.getBool2());
        
        TestCase.assertEquals(new Double("5.5"), result.getDouble1());
        TestCase.assertEquals(5.5d, result.getDouble2());
        
        TestCase.assertEquals(new Long("6"), result.getLong1());
        TestCase.assertEquals(6L, result.getLong2());
        
        TestCase.assertEquals(new Short("7"), result.getShort1());
        TestCase.assertEquals(7, result.getShort2());
        
        TestCase.assertEquals(new Float("10.2"), result.getFloat1());
        TestCase.assertEquals(10.2f, result.getFloat2());
        
        TestCase.assertEquals(new Character('C'), result.getChar1());
        TestCase.assertEquals('C', result.getChar2());
        
        TestCase.assertEquals(new Byte("11"), result.getByte1());
        TestCase.assertEquals(0x11, result.getByte2());
        
        log.info(result.toString());
    }

    @Data
    public static class Result {

        @JSoupSelect("#intId")
        @JSoupText
        public Integer int1;
        
        @JSoupSelect("#intId")
        @JSoupText
        public int int2;
        
        
        @JSoupSelect("#floatId")
        @JSoupText
        public Float float1;
        
        @JSoupSelect("#floatId")
        @JSoupText
        public float float2;
        
        
        @JSoupSelect("#boolId")
        @JSoupText
        public Boolean bool1;
        
        @JSoupSelect("#boolId")
        @JSoupText
        public float bool2;

        
        @JSoupSelect("#doubleId")
        @JSoupText
        public Double double1;
        
        @JSoupSelect("#doubleId")
        @JSoupText
        public double double2;
        
        
        @JSoupSelect("#longId")
        @JSoupText
        public Long long1;
        
        @JSoupSelect("#longId")
        @JSoupText
        public long long2;
        
        
        @JSoupSelect("#shortId")
        @JSoupText
        public Short short1;
        
        @JSoupSelect("#shortId")
        @JSoupText
        public short short2;
        
        
        @JSoupSelect("charId")
        @JSoupText
        public Character char1;
        
        @JSoupSelect("#charId")
        @JSoupText
        public char char2;
        
        
        @JSoupSelect("byteId")
        @JSoupText
        public Byte byte1;
        
        @JSoupSelect("#byteId")
        @JSoupText
        public byte byte2;

        
        
    }

    private InputStream read(String string) {
        InputStream resourceAsStream = JSoupMapper.class
                .getResourceAsStream(string);
        TestCase.assertNotNull(resourceAsStream);
        return resourceAsStream;
    }
}
