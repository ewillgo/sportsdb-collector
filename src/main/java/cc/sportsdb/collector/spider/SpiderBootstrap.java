package cc.sportsdb.collector.spider;

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

    private void init() {
        config.forEach((k, v) -> {
            Thread[] threads = new Thread[v.getThread()];

        });
    }

}
