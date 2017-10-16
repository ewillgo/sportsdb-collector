package cc.sportsdb.collector.spider.datahandler;

import cc.sportsdb.collector.spider.DataQueue;
import cc.sportsdb.collector.spider.PageInfo;
import cc.sportsdb.collector.spider.QueueAware;
import cc.sportsdb.collector.spider.SpiderConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class IpProxyHandler extends Thread implements QueueAware {

    private volatile boolean stopped;
    private DataQueue<PageInfo> dataQueue;
    private static final Logger logger = LoggerFactory.getLogger(IpProxyHandler.class);
    private static final SpiderConfig.Interval POLL_TIMEOUT = new SpiderConfig.Interval(5, TimeUnit.SECONDS);

    @Override
    public void setQueue(DataQueue<PageInfo> queue) {
        this.dataQueue = queue;
    }

    @Override
    public void run() {
        stopped = false;
        PageInfo pageInfo = null;
        try {
            while (!stopped && (pageInfo = dataQueue.poll(POLL_TIMEOUT.getInterval(), POLL_TIMEOUT.getTimeUnit())) != null) {
                Map<String, String> data = pageInfo.getData();
                logger.info("data: {}", data.toString());
            }
        } catch (InterruptedException e) {
            logger.info("Queue interrupted, queue size: {}", dataQueue.size());
        }
    }

    public void close() {
        stopped = true;
    }
}
