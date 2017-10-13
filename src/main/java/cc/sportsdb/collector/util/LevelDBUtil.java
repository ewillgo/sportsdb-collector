package cc.sportsdb.collector.util;

import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBComparator;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.WriteBatch;
import org.iq80.leveldb.impl.Iq80DBFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public final class LevelDBUtil {

    private LevelDBUtil() {
    }

    public static DB open(String name) throws IOException {
        return openDB(name, null);
    }

    public static DB openDB(String name, DBComparator comparator) throws IOException {
        Options options = new Options();
        options.createIfMissing(true);
        if (comparator != null) {
            options.comparator(comparator);
        }
        return Iq80DBFactory.factory.open(new File(name), options);
    }

    public static void put(DB db, String key, Object value) {
        db.put(key.getBytes(), JsonUtil.toJsonString(value).getBytes());
    }

    public static <T> T get(DB db, String key, Class<T> clazz) {
        return JsonUtil.parse(new String(db.get(key.getBytes())), clazz);
    }

    public static void delete(DB db, String key) {
        db.delete(key.getBytes());
    }

    public static void batch(DB db, WriteBatch writeBatch) {
        db.write(writeBatch);
    }

    public static void deleteDB(String path) throws IOException {
        Iq80DBFactory.factory.destroy(new File(path), new Options());
    }

    public static void closeDB(DB db) throws IOException {
        db.close();
    }

}
