package com.hyanzz.news.spider.job;

import com.hyanzz.news.spider.service.NewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description
 * @auther ywy
 * @create 2021-01-26 16:00
 */
@Slf4j
@Component
public class JobTask {

    @Autowired
    private NewsService newsService;


    @Scheduled(cron = "0 0/30 * * * ?")
    public void minuteExport(){
        Integer count = newsService.saveNews(getDate());
        log.info("定时任务保存新闻,执行时间:{},日期:{},数据量:{}" ,new Date(),getDate(),count);
    }

//    @Scheduled(fixedRate = 1000 * 10)
//    public void fiveSecondExport(){
//        log.info("每10s执行一次：{}" , getDate());
//    }

    private String getDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }
}
