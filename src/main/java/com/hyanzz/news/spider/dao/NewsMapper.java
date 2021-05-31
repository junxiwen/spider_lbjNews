package com.hyanzz.news.spider.dao;

import com.hyanzz.news.spider.entity.News;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Description
 * @auther ywy
 * @create 2021-01-26 15:05
 */
@Mapper
public interface NewsMapper {

    @Select("select * from news")
    List<News> getNewList();


    void save(News news);

    @Delete("delete from news where date = #{date}")
    void deleteByDate(@Param("date") String date);
}
