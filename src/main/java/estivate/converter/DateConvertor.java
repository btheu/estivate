package estivate.converter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import estivate.core.Converter;
import estivate.utils.StringUtil;
import lombok.SneakyThrows;

/**
 * <p>
 * Format option shoud be compliant with {@link SimpleDateFormat}
 * 
 * @author Benoit Theunissen
 *@see SimpleDateFormat
 */
public class DateConvertor implements Converter {

    public static final Map<String, SimpleDateFormat> FORMATTERS = new HashMap<String, SimpleDateFormat>();

    public boolean canConvert(Object value, Class<?> targetType) {
        return targetType.isAssignableFrom(Date.class);
    }

    @SneakyThrows
    public Object convert(Object value, Class<?> targetType, String format) {
        SimpleDateFormat sdf = getPattern(format);

        Date parse = sdf.parse(value.toString());

        return parse;
    }

    private SimpleDateFormat getPattern(String format) {
        assert !StringUtil.isBlank(format);

        SimpleDateFormat pattern = FORMATTERS.get(format);
        if (pattern == null) {
            pattern = new SimpleDateFormat(format);
            FORMATTERS.put(format, pattern);
        }
        return pattern;
    }

}
