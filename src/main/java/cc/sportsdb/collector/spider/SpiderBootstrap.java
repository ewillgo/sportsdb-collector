package cc.sportsdb.collector.spider;

import cc.sportsdb.collector.spider.dataqueue.DefaultDataQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

public class SpiderBootstrap {

    private final Map<String, SpiderConfig> config;
    private final Map<String, Thread[]> threadMap = new LinkedHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(SpiderBootstrap.class);

    public SpiderBootstrap(SpiderConfigLoader loader) {
        this.config = loader.load();
        init();
    }

    public void start() {

    }

    private void init() {
        config.forEach((k, v) -> {
            DataQueue<UrlInfo> urlQueue = new DefaultDataQueue<>();
            DataQueue<PageInfo> dataQueue = new DefaultDataQueue<>();
            Thread[] urlThreads = new Thread[v.getThread()];
            for (int i = 0; i < urlThreads.length; i++) {
                urlThreads[i] = new Spider(v.getName(), v, urlQueue, dataQueue);
            }

            Thread[] handlerThreads = new Thread[v.getDataHandler().getThread()];
            for (int j = 0; j < handlerThreads.length; ++j) {
                Object dataHandler = null;

                try {
                    dataHandler = v.getDataHandler().getClazz().newInstance();
                    if (dataHandler instanceof QueueAware) {
                        ((QueueAware) dataHandler).setQueue(dataQueue);
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    continue;
                }

                handlerThreads[j] = (Thread) dataHandler;
            }
        });
    }

}
