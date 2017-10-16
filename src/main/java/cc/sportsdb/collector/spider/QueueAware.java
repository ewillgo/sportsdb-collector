package cc.sportsdb.collector.spider;

public interface QueueAware {
    void setQueue(DataQueue<PageInfo> queue);
}
