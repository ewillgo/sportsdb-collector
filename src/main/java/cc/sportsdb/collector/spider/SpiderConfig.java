package cc.sportsdb.collector.spider;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class SpiderConfig {

    private Boolean enable;
    private Boolean showLog;
    private String id;
    private String name;
    private String description;
    private String url;
    private Pattern urlPattern;
    private Integer thread;
    private Interval interval;
    private Integer level;
    private DataHandler dataHandler;
    private Proxy proxy;
    private Block[] blocks;
    private HttpClientConfig httpClientConfig;

    public HttpClientConfig getHttpClientConfig() {
        return httpClientConfig;
    }

    public void setHttpClientConfig(HttpClientConfig httpClientConfig) {
        this.httpClientConfig = httpClientConfig;
    }

    public Boolean getShowLog() {
        return showLog;
    }

    public void setShowLog(Boolean showLog) {
        this.showLog = showLog;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Interval getInterval() {
        return interval;
    }

    public void setInterval(Interval interval) {
        this.interval = interval;
    }

    public DataHandler getDataHandler() {
        return dataHandler;
    }

    public void setDataHandler(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Pattern getUrlPattern() {
        return urlPattern;
    }

    public void setUrlPattern(Pattern urlPattern) {
        this.urlPattern = urlPattern;
    }

    public Integer getThread() {
        return thread;
    }

    public void setThread(Integer thread) {
        this.thread = thread;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    public Block[] getBlocks() {
        return blocks;
    }

    public void setBlocks(Block[] blocks) {
        this.blocks = blocks;
    }

    @Override
    public String toString() {
        return "SpiderConfig{" +
                "enable=" + enable +
                ", showLog=" + showLog +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", urlPattern=" + urlPattern +
                ", thread=" + thread +
                ", interval=" + interval +
                ", level=" + level +
                ", dataHandler=" + dataHandler +
                ", proxy=" + proxy +
                ", blocks=" + Arrays.toString(blocks) +
                ", httpClientConfig=" + httpClientConfig +
                '}';
    }

    public static class HttpClientConfig {
        private Interval readTimeout;
        private Interval connectionTimeout;
        private Boolean followRedirects;
        private Boolean followSslRedirects;
        private Header[] headers;

        public Header[] getHeaders() {
            return headers;
        }

        public void setHeaders(Header[] headers) {
            this.headers = headers;
        }

        public Interval getReadTimeout() {
            return readTimeout;
        }

        public void setReadTimeout(Interval readTimeout) {
            this.readTimeout = readTimeout;
        }

        public Interval getConnectionTimeout() {
            return connectionTimeout;
        }

        public void setConnectionTimeout(Interval connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
        }

        public Boolean getFollowRedirects() {
            return followRedirects;
        }

        public void setFollowRedirects(Boolean followRedirects) {
            this.followRedirects = followRedirects;
        }

        public Boolean getFollowSslRedirects() {
            return followSslRedirects;
        }

        public void setFollowSslRedirects(Boolean followSslRedirects) {
            this.followSslRedirects = followSslRedirects;
        }

        @Override
        public String toString() {
            return "HttpClientConfig{" +
                    "readTimeout=" + readTimeout +
                    ", connectionTimeout=" + connectionTimeout +
                    ", followRedirects=" + followRedirects +
                    ", followSslRedirects=" + followSslRedirects +
                    ", headers=" + Arrays.toString(headers) +
                    '}';
        }
    }

    public static class Header {
        private String key;
        private String value;

        public Header(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "Header{" +
                    "key='" + key + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }
    }

    public static class Interval {
        private Integer interval;
        private TimeUnit timeUnit;

        public Interval(Integer interval, TimeUnit timeUnit) {
            this.interval = interval;
            this.timeUnit = timeUnit;
        }

        public Integer getInterval() {
            return interval;
        }

        public void setInterval(Integer interval) {
            this.interval = interval;
        }

        public TimeUnit getTimeUnit() {
            return timeUnit;
        }

        public void setTimeUnit(TimeUnit timeUnit) {
            this.timeUnit = timeUnit;
        }

        @Override
        public String toString() {
            return "Interval{" +
                    "interval=" + interval +
                    ", timeUnit=" + timeUnit +
                    '}';
        }
    }

    public static class DataHandler {
        private Class<?> clazz;
        private Integer thread;

        public DataHandler(Integer thread, String className) throws ClassNotFoundException {
            this.thread = thread;
            this.clazz = loadClass(className);
        }

        private static Class<?> loadClass(String className) throws ClassNotFoundException {

            if (className == null || "".equals(className.trim())) {
                throw new IllegalArgumentException("Class name could not be empty");
            }

            return DataHandler.class.getClassLoader().loadClass(className);
        }

        @Override
        public String toString() {
            return "DataHandler{" +
                    "clazz=" + clazz +
                    ", thread=" + thread +
                    '}';
        }
    }

    public static class Proxy {
        private Boolean enable;
        private String hosts;

        public Proxy(Boolean enable, String hosts) {
            this.enable = enable;
            this.hosts = hosts;
        }

        public Boolean getEnable() {
            return enable;
        }

        public String getHosts() {
            return hosts;
        }

        @Override
        public String toString() {
            return "Proxy{" +
                    "enable=" + enable +
                    ", hosts='" + hosts + '\'' +
                    '}';
        }
    }

    public static class Block {
        private String query;
        private Column[] columns;

        public Block(String query, Column[] columns) {
            this.query = query;
            this.columns = columns;
        }

        public String getQuery() {
            return query;
        }

        public Column[] getColumns() {
            return columns;
        }

        @Override
        public String toString() {
            return "Block{" +
                    "query='" + query + '\'' +
                    ", columns=" + Arrays.toString(columns) +
                    '}';
        }
    }

    public static class Column {
        private String name;
        private String query;

        public Column(String name, String query) {
            this.name = name;
            this.query = query;
        }

        public String getName() {
            return name;
        }

        public String getQuery() {
            return query;
        }

        @Override
        public String toString() {
            return "Column{" +
                    "name='" + name + '\'' +
                    ", query='" + query + '\'' +
                    '}';
        }
    }

}
