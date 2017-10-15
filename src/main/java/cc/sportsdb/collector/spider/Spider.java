package cc.sportsdb.collector.spider;

import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Spider extends Thread {

    private final String name;
    private OkHttpClient httpClient;
    private volatile boolean stopped;
    private final SpiderConfig config;
    private final BlockingQueue<UrlInfo> queue;
    private static final Logger logger = LoggerFactory.getLogger(Spider.class);

    private static final int POLL_TIMEOUT_SECONDS = 5;

    public Spider(String name, SpiderConfig config, BlockingQueue<UrlInfo> queue) {
        super(name);
        this.name = name;
        this.queue = queue;
        this.config = config;
    }

    public String getSpiderName() {
        return name;
    }

    @Override
    public void run() {
        stopped = false;
        try {
            buildHttpClient();
            while (!stopped) {
                UrlInfo urlInfo = queue.poll(POLL_TIMEOUT_SECONDS, TimeUnit.SECONDS);

            }
        } catch (InterruptedException e) {
            logger.info("{}, Queue interrupted, queue size: {}", Thread.currentThread().getName(), queue.size());
        }
    }

    private void buildHttpClient() {
        httpClient = null;
    }

    public void close() {
        stopped = true;
    }
}
