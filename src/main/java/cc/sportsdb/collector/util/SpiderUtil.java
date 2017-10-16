package cc.sportsdb.collector.util;

import cc.sportsdb.collector.spider.SpiderConfig;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

public final class SpiderUtil {

    private SpiderUtil() {
    }

    public static OkHttpClient buildHttpClient(SpiderConfig config) {
        return new OkHttpClient.Builder()
                .followRedirects(true)
                .followSslRedirects(true)
                .readTimeout(config.getHttpClientConfig().getReadTimeout().getInterval(), config.getHttpClientConfig().getReadTimeout().getTimeUnit())
                .connectTimeout(config.getHttpClientConfig().getConnectionTimeout().getInterval(), config.getHttpClientConfig().getConnectionTimeout().getTimeUnit())
                .build();
    }

    public static TimeUnit toTimeUnit(String unit) {
        switch (unit.toLowerCase()) {
            case "s":
                return TimeUnit.SECONDS;
            case "m":
                return TimeUnit.MINUTES;
            case "h":
                return TimeUnit.HOURS;
            case "d":
                return TimeUnit.DAYS;
            default:
                throw new IllegalArgumentException("Illegal time unit");
        }
    }
}
