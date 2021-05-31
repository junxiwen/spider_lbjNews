package com.hyanzz.news.spider.entity;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Description
 * @auther ywy
 * @create 2021-01-26 14:20
 */
@Data
@ToString
public class PData {
    private String date;
    private String date_str;
    private List<News> video_arr;
    private List<News> web_arr;
}
