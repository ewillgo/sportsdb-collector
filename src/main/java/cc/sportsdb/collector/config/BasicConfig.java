package cc.sportsdb.collector.config;

import cc.sportsdb.collector.spider.SpiderBootstrap;
import cc.sportsdb.collector.spider.SpiderXmlConfigLoader;
import cc.sportsdb.collector.util.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class BasicConfig implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        return new MappingJackson2HttpMessageConverter(JsonUtil.OBJECT_MAPPER);
    }

    @Bean
    public SpiderBootstrap spiderBootstrap() {
        return new SpiderBootstrap(new SpiderXmlConfigLoader(applicationContext));
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return JsonUtil.OBJECT_MAPPER;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
