package cc.sportsdb.collector.service;

import cc.sportsdb.collector.spider.SpiderBootstrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpiderService {

    @Autowired
    private SpiderBootstrap spiderBootstrap;



}
