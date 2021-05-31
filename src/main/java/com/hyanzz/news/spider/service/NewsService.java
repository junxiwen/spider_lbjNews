package com.hyanzz.news.spider.service;

import com.hyanzz.news.spider.entity.News;

import java.util.List;

/**
 * @Description
 * @auther ywy
 * @create 2021-01-26 15:06
 */
public interface NewsService {
    List<News> getList();

    Integer saveNews(String date);
}
