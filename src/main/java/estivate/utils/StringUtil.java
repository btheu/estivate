package estivate.utils;

public class StringUtil {

    public static boolean isBlank(String word) {
        return word == null || word.trim().length() == 0;
    }

}
