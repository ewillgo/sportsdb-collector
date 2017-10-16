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
import java.util.concurrent.TimeUnit;

public class Spider extends Thread {

    private final String name;
    private OkHttpClient httpClient;
    private volatile boolean stopped;
    private volatile boolean suspended;
    private final SpiderConfig config;
    private final DataQueue<UrlInfo> urlQueue;
    private final DataQueue<PageInfo> dataQueue;
    private static final Logger logger = LoggerFactory.getLogger(Spider.class);
    private static final SpiderConfig.Interval POLL_TIMEOUT = new SpiderConfig.Interval(5, TimeUnit.SECONDS);
    private static final String USER_AGENT = "User-Agent";
    private static final String USER_AGENT_AUTO = "auto";

    public Spider(String name, SpiderConfig config, DataQueue<UrlInfo> urlQueue, DataQueue<PageInfo> dataQueue) {
        super(name);
        this.name = name;
        this.config = config;
        this.urlQueue = urlQueue;
        this.dataQueue = dataQueue;
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
                while (!stopped && (urlInfo = urlQueue.poll(POLL_TIMEOUT.getInterval(), POLL_TIMEOUT.getTimeUnit())) != null) {

                    synchronized (this) {
                        while (suspended) {
                            wait();
                        }
                    }

                    Map<String, String> headerMap = null;
                    SpiderConfig.Header[] headers = config.getHttpClientConfig().getHeaders();
                    if (headers != null && headers.length > 0) {
                        headerMap = new LinkedHashMap<>();
                        for (SpiderConfig.Header header : headers) {
                            if (USER_AGENT.equalsIgnoreCase(header.getKey()) && USER_AGENT_AUTO.equalsIgnoreCase(header.getValue())) {
                                headerMap.put(header.getKey(), HttpUtil.randomUserAgent());
                            } else {
                                headerMap.put(header.getKey(), header.getValue());
                            }
                        }
                    }

                    try {
                        Response response = httpClient.newCall(HttpUtil.buildGetRequest(urlInfo.getUrl(), headerMap)).execute();
                        if (response.isSuccessful()) {
                            Document doc = Jsoup.parse(response.body().string());

                            PageInfo pageInfo = new PageInfo();
                            pageInfo.setUrl(urlInfo.getUrl());
                            pageInfo.setLevel(urlInfo.getLevel());
                            pageInfo.setTitle(doc.title());
                            pageInfo.setData(translateBlocks(doc));
                            dataQueue.put(pageInfo);

                            logger.info("page info: {}", pageInfo.toString());

                            if (urlInfo.getLevel() < config.getLevel() && config.getUrlPattern() != null) {
                                putMatchUrlToQueue(urlInfo.getLevel(), doc);
                            }
                        } else {
                            logger.error("Response fail, message: {}", response.toString());
                        }
                    } catch (IOException e) {
                        logger.error(e.getMessage(), e);
                    }

                    config.getInterval().getTimeUnit().sleep(config.getInterval().getInterval());
                }
            }
        } catch (InterruptedException e) {
            logger.info("Queue interrupted, queue size: {}", urlQueue.size());
        }
    }

    private Map<String, String> translateBlocks(Document document) {
        Map<String, String> data = new LinkedHashMap<>();
        SpiderConfig.Block[] blocks = config.getBlocks();

        if (blocks == null || blocks.length == 0) {
            return data;
        }

        for (SpiderConfig.Block block : blocks) {

            if (block.getColumns() == null || block.getColumns().length == 0) {
                continue;
            }

            Elements blockQuery = document.select(block.getQuery());

            if (blockQuery == null || blockQuery.isEmpty()) {
                continue;
            }

            Element b = blockQuery.get(0);
            for (SpiderConfig.Column column : block.getColumns()) {

                if (column.getName() == null || column.getQuery() == null) {
                    continue;
                }

                Elements el = b.select(column.getQuery());
                if (el == null || el.isEmpty()) {
                    continue;
                }

                data.put(column.getName(), el.get(0).text());
            }
        }

        return data;
    }

    private void putMatchUrlToQueue(int level, Document document) throws InterruptedException {
        Elements elements = document.select("a");
        if (elements != null && !elements.isEmpty()) {
            for (Element e : elements) {
                String url = e.absUrl("href");
                if (RegexUtil.isMatch(config.getUrlPattern(), url)) {
                    urlQueue.put(new UrlInfo(level + 1, url));
                }
            }
        }
    }

    private void init() throws InterruptedException {
        synchronized (this) {
            if (httpClient == null) {
                httpClient = SpiderUtil.buildHttpClient(config);
            }
        }
        synchronized (urlQueue) {
            if (urlQueue.isEmpty()) {
                urlQueue.put(new UrlInfo(1, config.getUrl()));
            }
        }
    }

    public void close() {
        stopped = true;
        spiderResume();
    }

    public synchronized void spiderSuspend() {
        suspended = true;
    }

    public synchronized void spiderResume() {
        suspended = false;
        notifyAll();
    }
}
