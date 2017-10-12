package cc.sportsdb.collector.spider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Spider implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpiderConfigLoader loader = new SpiderConfigLoader(applicationContext);
        loader.load();
    }

    private static class SpiderConfigLoader {
        private ApplicationContext applicationContext;
        private static final Logger logger = LoggerFactory.getLogger(SpiderConfigLoader.class);
        private static final String CHARSET = "UTF-8";

        public SpiderConfigLoader(ApplicationContext applicationContext) {
            this.applicationContext = applicationContext;
        }

        public void load() {
            Resource resource = applicationContext.getResource("classpath:spider-config.xml");
            try {
                Document doc = Jsoup.parse(resource.getFile(), CHARSET);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
