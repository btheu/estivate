package estivate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

import org.jsoup.select.Elements;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import estivate.annotations.Convert;
import estivate.annotations.Text;
import estivate.converter.DateConvertor;
import estivate.converter.RegexExtractorConvertor;
import estivate.converter.UrlParameterExtractor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomConverterTest extends EstivateTest {

    @Test
    public void converterCustom1() throws IOException {

        InputStream document = read("/converter/u3.html");

        ResultDate result = mapper.map(document, ResultDate.class);

        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.SECOND, 59);
        instance.set(Calendar.MINUTE, 21);
        instance.set(Calendar.HOUR_OF_DAY, 23);

        instance.set(Calendar.DAY_OF_MONTH, 5);
        instance.set(Calendar.MONTH, 0);
        instance.set(Calendar.YEAR, 2016);

        Assertions.assertEquals(instance.getTime().toString(), result.getDate1().toString());

        Assertions.assertEquals(instance.getTime().toString(), result.getDate2().toString());

        Assertions.assertEquals(345, result.getInteger());

        assertEquals(new int[] { 12, 345, 6789 }, result.getIntegers());
    }

    private void assertEquals(int[] expected, int[] current) {
        Assertions.assertEquals(expected.length, current.length);

        for (int i = 0; i < current.length; i++) {
            Assertions.assertEquals(expected[i], current[i]);
        }

    }

    @Data
    public static class ResultDate {

        @Convert(value = DateConvertor.class, format = "yyyy-MM-dd HH:mm:ss")
        @Text(select = "#thatDate")
        public Date date1;

        public Date date2;

        @Convert(value = DateConvertor.class, format = "yyyy-MM-dd HH:mm:ss")
        @Text(select = "#thatDate")
        public void setDate2(Elements elements, Date date2) {
            this.date2 = date2;
        }

        @Convert(value = RegexExtractorConvertor.class, format = "\\d{2}(\\d{3})\\d*")
        @Text(select = "#thatNumber")
        public int integer;

        @Convert(value = RegexExtractorConvertor.class, format = "(\\d{2})(\\d{3})(\\d*)")
        @Text(select = "#thatNumber")
        public int[] integers;

        @Convert(value = UrlParameterExtractor.class, format = "dateId")
        @Text(select = "#thatUrl")
        public int dateInt;

    }

}
