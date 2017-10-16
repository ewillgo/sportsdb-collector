package cc.sportsdb.collector.spider;

public class UrlInfo {
    private int level;
    private String url;

    public UrlInfo(int level, String url) {
        this.level = level;
        this.url = url;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "UrlInfo{" +
                "level=" + level +
                ", url='" + url + '\'' +
                '}';
    }
}
