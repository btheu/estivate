package estivate.converter;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import estivate.core.Converter;

public class RegexExtractorConvertor implements Converter {

    public static final Map<String, Pattern> PATTERNS = new HashMap<String, Pattern>();

    public boolean canConvert(Object value, Class<?> targetType) {
        return targetType.isAssignableFrom(int.class) || targetType.isAssignableFrom(int[].class)
                || targetType.isAssignableFrom(String.class) || targetType.isAssignableFrom(String[].class);
    }

    public Object convert(Object value, Class<?> targetType, String format) {

        Pattern compile = getPattern(format);
        Matcher matcher = compile.matcher(value.toString());
        if (!matcher.find()) {
            throw new RuntimeException("Expression '" + format + "' cant match '" + value.toString() + "'");
        }

        if (targetType.isAssignableFrom(int.class)) {
            return Integer.parseInt(matcher.group(1));
        }
        if (targetType.isAssignableFrom(int[].class)) {

            int[] result = new int[matcher.groupCount()];

            for (int i = 0; i < matcher.groupCount(); i++) {
                result[i] = Integer.parseInt(matcher.group(i + 1));
            }

            return result;
        }
        if (targetType.isAssignableFrom(String[].class)) {

            String[] result = new String[matcher.groupCount()];

            for (int i = 0; i < matcher.groupCount(); i++) {
                result[i] = matcher.group(i + 1);
            }

            return result;
        }

        return matcher.group(1);
    }

    private Pattern getPattern(String format) {
        Pattern pattern = PATTERNS.get(format);
        if (pattern == null) {
            pattern = Pattern.compile(format);
            PATTERNS.put(format, pattern);
        }
        return pattern;
    }

}
