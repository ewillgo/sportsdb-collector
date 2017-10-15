package cc.sportsdb.collector.spider;

import cc.sportsdb.collector.util.SpiderUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class SpiderXmlConfigLoader implements SpiderConfigLoader {

    private ApplicationContext applicationContext;
    private static final String CHARSET = "UTF-8";
    private static final Logger logger = LoggerFactory.getLogger(SpiderXmlConfigLoader.class);

    public SpiderXmlConfigLoader(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Map<String, SpiderConfig> load() {
        Document doc = null;
        Map<String, SpiderConfig> configMap = new LinkedHashMap<>();
        Resource resource = applicationContext.getResource("classpath:spider-config.xml");

        try {
            doc = Jsoup.parse(resource.getFile(), CHARSET);
        } catch (Exception e) {
            logger.info(e.getMessage(), e);
            return configMap;
        }

        Elements elements = doc.select("spider");

        elements.forEach((element) -> {
            SpiderConfig config = new SpiderConfig();

            try {
                Elements id = element.getElementsByTag("id");
                if (id != null && !id.isEmpty()) {
                    config.setId(id.get(0).text());
                }

                Elements name = element.getElementsByTag("name");
                if (name != null && !name.isEmpty()) {
                    config.setName(name.get(0).text());
                }

                Elements description = element.getElementsByTag("description");
                if (description != null && !description.isEmpty()) {
                    config.setDescription(description.get(0).text());
                }

                Elements url = element.getElementsByTag("url");
                if (url != null && !url.isEmpty()) {
                    config.setUrl(url.get(0).text());
                }

                Elements urlPattern = element.getElementsByTag("url-pattern");
                if (urlPattern != null && !urlPattern.isEmpty()) {
                    config.setUrlPattern(Pattern.compile(urlPattern.get(0).text()));
                }

                Elements thread = element.getElementsByTag("thread");
                if (thread != null && !thread.isEmpty()) {
                    config.setThread(Integer.parseInt(thread.get(0).text()));
                }

                Elements interval = element.getElementsByTag("interval");
                if (interval != null && !interval.isEmpty()) {
                    String tmp = interval.get(0).text();
                    config.setInterval(new SpiderConfig.Interval(
                            Integer.parseInt(tmp.substring(0, tmp.length() - 1)),
                            SpiderUtil.toTimeUnit(tmp.substring(tmp.length() - 1, tmp.length()))
                    ));
                }

                Elements level = element.getElementsByTag("level");
                if (level != null && !level.isEmpty()) {
                    config.setLevel(Integer.parseInt(level.get(0).text()));
                }

                Elements dataHandler = element.getElementsByTag("data-handler");
                if (dataHandler != null && !dataHandler.isEmpty()) {
                    Integer threadValue = null;
                    String classNameValue = null;

                    Elements className = dataHandler.get(0).getElementsByTag("class-name");
                    if (className != null && !className.isEmpty()) {
                        classNameValue = className.get(0).text();
                    }

                    Elements dataThread = dataHandler.get(0).getElementsByTag("thread");
                    if (dataThread != null && !dataThread.isEmpty()) {
                        threadValue = Integer.parseInt(dataThread.get(0).text());
                    }

                    if ((classNameValue != null && !classNameValue.isEmpty()) && (threadValue != null && threadValue != 0)) {
                        config.setDataHandler(new SpiderConfig.DataHandler(threadValue, classNameValue));
                    }
                }

                Elements proxy = element.getElementsByTag("proxy");
                if (proxy != null && !proxy.isEmpty()) {
                    String hostsValue = null;
                    Boolean enableValue = false;

                    Elements enable = proxy.get(0).getElementsByTag("enable");
                    if (enable != null && !enable.isEmpty()) {
                        enableValue = Boolean.parseBoolean(enable.get(0).text());
                    }

                    Elements hosts = proxy.get(0).getElementsByTag("hosts");
                    if (hosts != null && !hosts.isEmpty()) {
                        hostsValue = hosts.get(0).text();
                    }

                    config.setProxy(new SpiderConfig.Proxy(enableValue, hostsValue));
                }

                Elements blocks = element.getElementsByTag("blocks");
                if (blocks != null && !blocks.isEmpty()) {
                    List<SpiderConfig.Block> blockList = new ArrayList<>();
                    Elements block = blocks.get(0).getElementsByTag("block");
                    if (block != null && !block.isEmpty()) {
                        block.forEach((b) -> {
                            List<SpiderConfig.Column> columnList = new ArrayList<>();
                            String tableQuery = b.attr("query");
                            Elements columns = b.getElementsByTag("column");
                            if (columns != null && !columns.isEmpty()) {
                                columns.forEach((c) -> {
                                    columnList.add(new SpiderConfig.Column(c.attr("name"), c.attr("query")));
                                });
                            }
                            blockList.add(new SpiderConfig.Block(tableQuery, columnList.toArray(new SpiderConfig.Column[0])));
                        });
                    }
                    config.setBlocks(blockList.toArray(new SpiderConfig.Block[0]));
                }

                configMap.put(config.getId(), config);

            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        });

        return configMap;
    }
}
