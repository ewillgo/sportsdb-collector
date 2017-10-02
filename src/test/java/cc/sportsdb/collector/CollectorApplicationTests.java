package cc.sportsdb.collector;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CollectorApplicationTests {

    private static final Logger logger = LoggerFactory.getLogger(CollectorApplicationTests.class);

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void contextLoads() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
        String html = restTemplate.exchange("http://ip.cn", HttpMethod.GET, new HttpEntity<>(headers), String.class).getBody();
//        restTemplate.getForEntity("https://www.baidu.com", String.class, headers);
//        String html = restTemplate.getForObject("https://www.baidu.com", String.class);
        logger.info(html);
    }

}
