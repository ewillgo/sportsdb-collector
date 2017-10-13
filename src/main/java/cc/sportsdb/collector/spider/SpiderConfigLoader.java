package cc.sportsdb.collector.spider;

import java.util.Map;

public interface SpiderConfigLoader {
    Map<String, SpiderConfig> load();
}
