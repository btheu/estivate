package estivate.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import estivate.core.Converter;
import lombok.extern.slf4j.Slf4j;

/**
 * Format sample: "picto-(\\w)?\\.png"
 * 
 * @author Benoit Theunissen
 *
 */
@Slf4j
public class RegexExtractorConvertor implements Converter {

    public static final Map<String, Pattern> PATTERNS = new HashMap<String, Pattern>();

    public boolean canConvert(Object value, Class<?> targetType) {
        return targetType.isAssignableFrom(int.class) || targetType.isAssignableFrom(int[].class)
                || targetType.isAssignableFrom(short.class) || targetType.isAssignableFrom(short[].class)
                || targetType.isAssignableFrom(String.class) || targetType.isAssignableFrom(String[].class);
    }

    public Object convert(Object value, Class<?> targetType, String format) {

        Pattern compile = getPattern(format);
        Matcher matcher = compile.matcher(value.toString());

        List<String> matches = new ArrayList<String>();

        while (matcher.find()) {
            int groupCount = matcher.groupCount();
            for (int i = 0; i < groupCount; i++) {
                String current = matcher.group(i + 1);
                log.debug("matching: {}", current);
                matches.add(current);
            }
        }
        if (matches.isEmpty()) {
            throw new RuntimeException("Expression '" + format + "' cant match '" + value.toString() + "'");
        }
        if (targetType.isAssignableFrom(double.class)) {
            return Double.parseDouble(matches.get(0));
        }
        if (targetType.isAssignableFrom(long.class)) {
            return Long.parseLong(matches.get(0));
        }
        if (targetType.isAssignableFrom(int.class)) {
            return Integer.parseInt(matches.get(0));
        }
        if (targetType.isAssignableFrom(short.class)) {
            return Short.parseShort(matches.get(0));
        }
        if (targetType.isAssignableFrom(double[].class)) {

            double[] result = new double[matches.size()];
            int idx = 0;
            for (String match : matches) {
                result[idx++] = Double.parseDouble(match);
            }

            return result;
        }
        if (targetType.isAssignableFrom(long[].class)) {

            long[] result = new long[matches.size()];
            int idx = 0;
            for (String match : matches) {
                result[idx++] = Long.parseLong(match);
            }

            return result;
        }
        if (targetType.isAssignableFrom(int[].class)) {

            int[] result = new int[matches.size()];
            int idx = 0;
            for (String match : matches) {
                result[idx++] = Integer.parseInt(match);
            }

            return result;
        }
        if (targetType.isAssignableFrom(short[].class)) {

            short[] result = new short[matches.size()];
            int idx = 0;
            for (String match : matches) {
                result[idx++] = Short.parseShort(match);
            }

            return result;
        }
        if (targetType.isAssignableFrom(String[].class)) {
            return matches.toArray(new String[] {});
        }

        return matches.get(0);
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
