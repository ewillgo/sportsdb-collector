package cc.sportsdb.collector.spider.dataqueue;

import cc.sportsdb.collector.spider.DataQueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class DefaultDataQueue<T> implements DataQueue<T> {

    private final BlockingQueue<T> queue;

    public DefaultDataQueue() {
        this.queue = new LinkedBlockingQueue<>();
    }

    @Override
    public T poll(long timeout, TimeUnit timeUnit) throws InterruptedException {
        return queue.poll(timeout, timeUnit);
    }

    @Override
    public boolean put(T data) throws InterruptedException {
        queue.put(data);
        return true;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public int size() {
        return queue.size();
    }
}
