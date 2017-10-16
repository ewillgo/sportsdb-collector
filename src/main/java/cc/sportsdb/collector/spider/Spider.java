package cc.sportsdb.collector.spider;

import cc.sportsdb.collector.util.HttpUtil;
import cc.sportsdb.collector.util.RegexUtil;
import cc.sportsdb.collector.util.SpiderUtil;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Spider extends Thread {

    private final String name;
    private OkHttpClient httpClient;
    private volatile boolean stopped;
    private final SpiderConfig config;
    private final BlockingQueue<UrlInfo> queue;
    private final DataHandler<PageInfo> dataHandler;
    private static final Logger logger = LoggerFactory.getLogger(Spider.class);
    private static final SpiderConfig.Interval POLL_TIMEOUT = new SpiderConfig.Interval(5, TimeUnit.SECONDS);

    public Spider(String name, SpiderConfig config, BlockingQueue<UrlInfo> queue, DataHandler<PageInfo> dataHandler) {
        super(name);
        this.name = name;
        this.queue = queue;
        this.config = config;
        this.dataHandler = dataHandler;
    }

    public String getSpiderName() {
        return name;
    }

    @Override
    public void run() {
        stopped = false;
        try {
            while (!stopped) {
                init();
                UrlInfo urlInfo = null;
                while (!stopped && (urlInfo = queue.poll(POLL_TIMEOUT.getInterval(), POLL_TIMEOUT.getTimeUnit())) != null) {

                    Map<String, String> headerMap = null;
                    SpiderConfig.Header[] headers = config.getHttpClientConfig().getHeaders();
                    if (headers != null && headers.length > 0) {
                        headerMap = new LinkedHashMap<>();
                        for (SpiderConfig.Header header : headers) {
                            headerMap.put(header.getKey(), header.getValue());
                        }
                    }

                    try {
                        Response response = httpClient.newCall(HttpUtil.buildGetRequest(urlInfo.getUrl(), headerMap)).execute();
                        if (response.isSuccessful()) {
                            Document doc = Jsoup.parse(response.body().string());


                            PageInfo pageInfo = new PageInfo();
                            dataHandler.handle(pageInfo);

                            if (urlInfo.getLevel() < config.getLevel() && config.getUrlPattern() != null) {
                                putMatchUrlToQueue(urlInfo.getLevel(), doc);
                            }
                        } else {
                            logger.error("Response fail, message: {}", response.toString());
                        }
                    } catch (IOException e) {
                        logger.error(e.getMessage(), e);
                    }

                }

                config.getInterval().getTimeUnit().sleep(config.getInterval().getInterval());
            }
        } catch (InterruptedException e) {
            logger.info("Queue interrupted, queue size: {}", queue.size());
        }
    }

    private void putMatchUrlToQueue(int level, Document document) throws InterruptedException {
        Elements elements = document.select("a");
        if (elements != null && !elements.isEmpty()) {
            for (Element e : elements) {
                String url = e.absUrl("href");
                if (RegexUtil.isMatch(config.getUrlPattern(), url)) {
                    queue.put(new UrlInfo(level + 1, url));
                }
            }
        }
    }

    private void init() throws InterruptedException {
        httpClient = SpiderUtil.buildHttpClient(config);
        synchronized (queue) {
            if (queue.isEmpty()) {
                queue.put(new UrlInfo(1, config.getUrl()));
            }
        }
    }

    public void close() {
        stopped = true;
    }
}
