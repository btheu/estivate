package estivate.converter;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import estivate.core.Converter;

/**
 * Format: URI parameter name
 * 
 * @author Benoit Theunissen
 *
 */
public class UrlParameterExtractor implements Converter {

    public boolean canConvert(Object value, Class<?> targetType) {
        return targetType.isAssignableFrom(int.class) || targetType.isAssignableFrom(String.class)
                || targetType.isAssignableFrom(Map.class);
    }

    public Object convert(Object value, Class<?> targetType, String format) {

        List<String> formats = Arrays.asList(format.toLowerCase().split("-"));

        Map<String, String> parameters = new HashMap<String, String>();

        String query = URI.create(value.toString()).getQuery();

        String[] keyValues = query.split("&");
        for (String keyValue : keyValues) {
            String[] split = keyValue.split("=");
            if (formats.isEmpty() || formats.contains(split[0].toLowerCase())) {
                parameters.put(split[0].toLowerCase(), split[1]);
            }
        }

        if (targetType.isAssignableFrom(Map.class)) {
            return parameters;
        }

        if (targetType.isAssignableFrom(int.class)) {
            return Integer.parseInt(parameters.get(format.toLowerCase()));
        }

        return parameters.get(format.toLowerCase());
    }

}
