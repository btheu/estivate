package estivate;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;

import estivate.annotations.Convert;
import estivate.annotations.Text;
import estivate.core.Converter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomConverterTest {

    EstivateMapper mapper = new EstivateMapper();

    @Test
    public void converterCustom1() {

        InputStream document = read("/converter/u3.html");

        ResultDate result = mapper.map(document, ResultDate.class);

        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.SECOND, 59);
        instance.set(Calendar.MINUTE, 21);
        instance.set(Calendar.HOUR_OF_DAY, 23);

        instance.set(Calendar.DAY_OF_MONTH, 5);
        instance.set(Calendar.MONTH, 0);
        instance.set(Calendar.YEAR, 2016);

        Assert.assertEquals(instance.getTime().toString(),
                result.getDate1().toString());

        Assert.assertEquals(instance.getTime().toString(),
                result.getDate2().toString());

    }

    @Data
    public static class ResultDate {

        @Convert(DateConvertor.class)
        @Text(select = "#thatDate")
        public Date date1;

        public Date date2;

        @Convert(DateConvertor.class)
        @Text(select = "#thatDate")
        public void setDate2(Elements elements, Date date2) {
            this.date2 = date2;
        }

    }

    private InputStream read(String string) {
        InputStream resourceAsStream = CustomConverterTest.class
                .getResourceAsStream(string);
        Assert.assertNotNull(resourceAsStream);
        return resourceAsStream;
    }

    public static class DateConvertor implements Converter {

        SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        @Override
        public boolean canConvert(Object value, Class<?> targetType) {
            return targetType.isAssignableFrom(Date.class)
                    && value.getClass().equals(String.class);
        }

        @Override
        public Object convert(Object value, Class<?> targetType) {
            try {
                log.info("converting Date with custom converter");
                Date parse = SDF.parse((String) value);
                log.info("custom converter {}", parse);
                return parse;
            } catch (ParseException e) {
                log.error(e.getMessage(), e);
                return null;
            }
        }
    }

}
