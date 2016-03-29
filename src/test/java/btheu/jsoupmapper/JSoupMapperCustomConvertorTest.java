package btheu.jsoupmapper;

import java.io.InputStream;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import btheu.jsoupmapper.core.TypeConvertor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JSoupMapperCustomConvertorTest {

	JSoupMapper mapper = new JSoupMapper();

	@Test
	public void convertorCustom1() {
		
		InputStream document = read("/convertor/u3.html");

		ResultDate result = mapper.map(document, ResultDate.class);
		
		Assert.assertNotNull(result.getDate1());
		
	}

	
	@Data
	public static class ResultDate {
		
		@Text(select="#thatDate", converter=DateConvertor.class)
		public Date date1;
		
	}

	private InputStream read(String string) {
		InputStream resourceAsStream = JSoupMapper.class
				.getResourceAsStream(string);
		Assert.assertNotNull(resourceAsStream);
		return resourceAsStream;
	}
	
	public static class DateConvertor implements TypeConvertor{
		@Override
		public boolean canConvert(Class<?> targetType, Object value) {
			return targetType.isAssignableFrom(Date.class) && value.getClass().equals(String.class);
		}
		@Override
		public Object convert(Class<?> targetType, Object value) {
			log.info("converting Date with custom converter");
			return new Date();
		}
	}
	
}
