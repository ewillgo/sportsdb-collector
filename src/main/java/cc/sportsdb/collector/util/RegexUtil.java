package cc.sportsdb.collector.util;

import java.util.regex.Pattern;

public final class RegexUtil {

    private RegexUtil() {
    }

    public static final Pattern URL = Pattern.compile("[a-zA-z]+://[^\\s]*");
    public static final Pattern NUMBER = Pattern.compile("^[0-9]+$");

    public static boolean isMatch(Pattern pattern, String value) {
        return pattern.matcher(value).matches();
    }
}
