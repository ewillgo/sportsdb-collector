package cc.sportsdb.collector.spider;

public interface DataHandler<T> {
    boolean handle(T data);
}
