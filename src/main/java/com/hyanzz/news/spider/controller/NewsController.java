package com.hyanzz.news.spider.controller;

import com.hyanzz.news.spider.entity.News;
import com.hyanzz.news.spider.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Description
 * @auther ywy
 * @create 2021-01-26 15:08
 */
@RestController
@RequestMapping("/news")
public class NewsController {


    @Autowired
    private NewsService newsService;

    @GetMapping("/getList")
    public List<News> getList(){
        return newsService.getList();
    }


    @GetMapping("/saveNews")
    public String saveNews(String date){
        Integer count = newsService.saveNews(date);
        return "ok:"+count;
    }


    @GetMapping("/statistics/{startDate}/{endDate}")
    public String statisticsNews(@PathVariable("startDate") String startDate,@PathVariable("endDate") String endDate) throws ParseException {
        List<String> daysBetweenTwoDays = getDaysBetweenTwoDays(startDate, endDate);
        Integer sum = 0;
        for (String date : daysBetweenTwoDays) {
            sum +=newsService.saveNews(date);
        }
        return "ok:"+sum;
    }


    public static void main(String[] args) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//注意月份是MM
        Date start = simpleDateFormat.parse("2021-01-02");
        Date end = simpleDateFormat.parse("2021-02-01");
        List<String> betweenDates = getDaysBetweenTwoDays("2021-01-02","2021-02-02");
        System.out.println(betweenDates);
    }

    public static List<String> getDaysBetweenTwoDays(String startDay,String endDay) throws ParseException {
        List<String> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//格式化为年月
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        min.setTime(sdf.parse(startDay));
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), min.get(Calendar.DATE),0,0,0);
        max.setTime(sdf.parse(endDay));
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), max.get(Calendar.DATE),23,59,59);
        Calendar curr = min;
        while (curr.before(max)) {
            list.add(sdf.format(curr.getTime()));
            curr.add(Calendar.DATE, 1);
        }
        return list;
    }
}
