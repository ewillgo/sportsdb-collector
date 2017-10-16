package cc.sportsdb.collector.spider;

import cc.sportsdb.collector.spider.dataqueue.DefaultDataQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

public class SpiderBootstrap {

    private final Map<String, SpiderConfig> config;
    private final SpiderMonitor spiderMonitor;
    private final Map<String, Thread[]> threadMap = new LinkedHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(SpiderBootstrap.class);

    public SpiderBootstrap(SpiderConfigLoader loader, SpiderMonitor spiderMonitor) {
        this.config = loader.load();
        this.spiderMonitor = spiderMonitor;
        init();
        start();
    }

    public void start() {
        if (spiderMonitor == null) {
            return;
        }

        spiderMonitor.map().forEach((k, v) -> {
            v.urlMap().forEach((urlKey, urlValue) -> {
                urlValue.start();
            });
            v.dataMap().forEach((dataKey, dataValue) -> {
                dataValue.start();
            });
        });
    }

    private void init() {
        config.forEach((k, v) -> {
            DataQueue<UrlInfo> urlQueue = new DefaultDataQueue<>();
            DataQueue<PageInfo> dataQueue = new DefaultDataQueue<>();
            SpiderMonitor.SpiderInfo spiderInfo = new SpiderMonitor.SpiderInfo(v, urlQueue, dataQueue);

            for (int i = 0; i < v.getThread(); i++) {
                Spider spider = new Spider(v.getName(), v, urlQueue, dataQueue);
                spiderInfo.putUrlThread(spider.getName(), spider);
            }

            for (int j = 0; j < v.getDataHandler().getThread(); ++j) {
                Object dataHandler = null;

                try {
                    dataHandler = v.getDataHandler().getClazz().newInstance();
                    if (dataHandler instanceof QueueAware) {
                        ((QueueAware) dataHandler).setQueue(dataQueue);
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    break;
                }

                Thread tmpThread = (Thread) dataHandler;
                spiderInfo.putDataThread(tmpThread.getName(), tmpThread);
            }

            spiderMonitor.put(k, spiderInfo);
        });
    }

}
