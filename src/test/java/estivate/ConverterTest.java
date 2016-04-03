package estivate;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Assert;
import org.junit.Test;

import estivate.EstivateMapper;
import estivate.annotations.Text;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConverterTest {

	EstivateMapper mapper = new EstivateMapper();

	@Test
	public void convertorPrimitive1() {

		InputStream document = read("/converter/u1.html");

		Result result = mapper.map(document, Result.class);

		Assert.assertNotNull(result);

		Assert.assertEquals(new Integer("3"), result.getInt1());
		Assert.assertEquals(3, result.getInt2());

		Assert.assertEquals(new Boolean("false"), result.getBool1());
		Assert.assertEquals(false, result.isBool2());

		Assert.assertEquals(new Character('C'), result.getChar1());
		Assert.assertEquals('C', result.getChar2());

		Assert.assertEquals(new Byte("11"), result.getByte1());
		Assert.assertEquals(11, result.getByte2());

		
        Assert.assertEquals(new Double("5.5"), result.getDouble1());
        Assert.assertEquals(5.5d, result.getDouble2(),0);

        Assert.assertEquals(new Long("6"), result.getLong1());
        Assert.assertEquals(6L, result.getLong2());

        Assert.assertEquals(new Short("7"), result.getShort1());
        Assert.assertEquals(7, result.getShort2());

        Assert.assertEquals(new Float("10.2"), result.getFloat1());
        Assert.assertEquals(10.2f, result.getFloat2(),0);
		 
		log.info(result.toString());
	}

	@Test
	public void convertorBigNumber1() {
		InputStream document = read("/converter/u2.html");

		ResultBig result = mapper.map(document, ResultBig.class);

		Assert.assertNotNull(result);

		Assert.assertEquals(new BigInteger("1111111111111111111111111111111111111111111111111111111111"), result.getInt1());
		Assert.assertEquals(new BigDecimal("2222222222222222222222222222222222222222222222222222222222"), result.getDec1());
	}
	
	@Data
	public static class ResultBig {
		
		@Text(select="#bigIntId")
		public BigInteger int1;
		
		@Text(select="#bigDecId")
		public BigDecimal dec1;
		
	}
	
	@Data
	public static class Result {

		@Text(select="#intId")
		public Integer int1;

		@Text(select="#intId")
		public int int2;

		@Text(select="#charId")
		public Character char1;

		@Text(select="#charId")
		public char char2;


		@Text(select="#byteId")
		public Byte byte1;

		@Text(select="#byteId")
		public byte byte2;

		@Text(select="#boolId")
		public Boolean bool1;

		@Text(select="#boolId")
		public boolean bool2;


		@Text(select="#floatId")
		public Float float1;

		@Text(select="#floatId")
		public float float2;

		@Text(select="#doubleId")
		public Double double1;

		@Text(select="#doubleId")
		public double double2;


		@Text(select="#longId")
		public Long long1;

		@Text(select="#longId")
		public long long2;


		@Text(select="#shortId")
		public Short short1;

		@Text(select="#shortId")
		public short short2;

	}

	private InputStream read(String string) {
		InputStream resourceAsStream = EstivateMapper.class
				.getResourceAsStream(string);
		Assert.assertNotNull(resourceAsStream);
		return resourceAsStream;
	}
}
