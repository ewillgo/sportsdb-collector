package cc.sportsdb.collector.util;

import java.util.concurrent.TimeUnit;

public final class SpiderUtil {

    private SpiderUtil() {}

    public static TimeUnit toTimeUnit(String unit) {
        switch (unit.toLowerCase()) {
            case "m":
                return TimeUnit.MINUTES;
            case "h":
                return TimeUnit.HOURS;
            case "d":
                return TimeUnit.DAYS;
            default:
                return TimeUnit.SECONDS;
        }
    }
}
