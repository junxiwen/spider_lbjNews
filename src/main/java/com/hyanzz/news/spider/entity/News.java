package com.hyanzz.news.spider.entity;

import lombok.Data;

/**
 * @Description
 * @auther ywy
 * @create 2021-01-26 14:21
 */
@Data
public class News {
    private Integer id;
    private String type;
    private String indextitle;
    private String filename;
    private String title;
    private String shortTitle;
    private String lable;
    private String createtime;
    private String thumbnail;
    private String url;
    private String date;
}
