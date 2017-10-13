package cc.sportsdb.collector.spider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Map;

public class SpiderXmlConfigLoader implements SpiderConfigLoader {

    private ApplicationContext applicationContext;
    private static final String CHARSET = "UTF-8";
    private static final Logger logger = LoggerFactory.getLogger(SpiderXmlConfigLoader.class);

    public SpiderXmlConfigLoader(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Map<String, SpiderConfig> load() {
        Resource resource = applicationContext.getResource("classpath:spider-config.xml");
        try {
            Document doc = Jsoup.parse(resource.getFile(), CHARSET);
            Elements elements = doc.select("spider");
            logger.info("{}", doc.body());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }
}
