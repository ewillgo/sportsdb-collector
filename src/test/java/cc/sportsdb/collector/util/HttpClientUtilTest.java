package cc.sportsdb.collector.util;

import cc.sportsdb.collector.config.Okhttp3Properties;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HttpClientUtilTest {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(HttpClientUtilTest.class);

    @Test
    public void buildTest() throws IOException {
        Okhttp3Properties config = new Okhttp3Properties();
        config.setConnectTimeout(20);
        config.setReadTimeout(20);

//        OkHttpClient client = HttpUtil.build(config, new Proxy(Proxy.Type.HTTP, new InetSocketAddress("27.40.156.202", 808)));
        OkHttpClient client = HttpUtil.build(config);

        Request request = new Request.Builder()
                .addHeader("User-Agent", HttpUtil.randomUserAgent())
                .url("https://www.baidu.com")
                .get()
                .build();

        Response response = client.newCall(request).execute();

        Document doc = Jsoup.parse(response.body().string());
        Elements elements = doc.select("#u1");
        Elements nav = elements.select("[name=tj_trnews]");
        logger.info("{}", nav.text());


//        logger.info("{}", response.body().string());
    }
}
