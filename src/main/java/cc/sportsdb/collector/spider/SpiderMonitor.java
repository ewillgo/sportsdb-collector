package cc.sportsdb.collector.spider;

import java.util.HashMap;
import java.util.Map;

public class SpiderMonitor {

    private Map<String, SpiderInfo> dataMap = new HashMap<>();

    public Map<String, SpiderInfo> map() {
        return dataMap;
    }

    public void put(String key, SpiderInfo spiderInfo) {
        dataMap.put(key, spiderInfo);
    }

    public SpiderInfo get(String key) {
        return dataMap.get(key);
    }

    public static class SpiderInfo {
        private final SpiderConfig config;
        private final DataQueue<UrlInfo> urlQueue;
        private final DataQueue<PageInfo> dataQueue;
        private final Map<String, Thread> urlThreadMap = new HashMap<>();
        private final Map<String, Thread> dataThreadMap = new HashMap<>();

        public SpiderInfo(SpiderConfig config, DataQueue<UrlInfo> urlQueue, DataQueue<PageInfo> dataQueue) {
            this.config = config;
            this.urlQueue = urlQueue;
            this.dataQueue = dataQueue;
        }

        public SpiderConfig getConfig() {
            return config;
        }

        public Map<String, Thread> urlMap() {
            return urlThreadMap;
        }

        public Map<String, Thread> dataMap() {
            return dataThreadMap;
        }

        public Thread getDataThread(String key) {
            return dataThreadMap.get(key);
        }

        public void putDataThread(String key, Thread thread) {
            dataThreadMap.put(key, thread);
        }

        public Thread getUrlThread(String key) {
            return urlThreadMap.get(key);
        }

        public void putUrlThread(String key, Thread thread) {
            urlThreadMap.put(key, thread);
        }
    }
}
