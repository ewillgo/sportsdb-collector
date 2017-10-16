package cc.sportsdb.collector.spider;

import java.util.concurrent.TimeUnit;

public interface DataQueue<T> {
    T poll(long timeout, TimeUnit timeUnit) throws InterruptedException;

    boolean put(T data) throws InterruptedException;

    boolean isEmpty();

    int size();
}
