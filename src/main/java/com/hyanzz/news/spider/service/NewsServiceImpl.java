package com.hyanzz.news.spider.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.hyanzz.news.spider.dao.NewsMapper;
import com.hyanzz.news.spider.entity.News;
import com.hyanzz.news.spider.entity.PData;
import com.hyanzz.news.spider.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @auther ywy
 * @create 2021-01-26 15:06
 */
@Slf4j
@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsMapper newsMapper;

    @Override
    public List<News> getList() {
        return newsMapper.getNewList();
    }

    @Override
    @Transactional
    public Integer saveNews(String date) {
        newsMapper.deleteByDate(date);
        List<News> list = spiderNewsList(date);
        if(CollectionUtil.isNotEmpty(list)){
            list.stream().forEach(news -> {
                try {
                    newsMapper.save(news);
                }catch (Exception e){
                    log.error("异常:{}",e);
                }
            });
            return list.size();
        }
        return 0;
    }

    public static String filterEmoji(String source) {

        if(null == source){
            return source;
        }

        source = source.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "");
        source = source.replaceAll("[\\u263a]", "");
        source = source.replaceAll("[\\u200D]", "");
        source = source.replaceAll("️", "");

        if (!containsEmoji(source)) {
            return source;//如果不包含，直接返回
        }
        //到这里铁定包含
        StringBuilder buf = null;

        int len = source.length();

        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);

            if (isEmojiCharacter(codePoint)) {
                if (buf == null) {
                    buf = new StringBuilder(source.length());
                }

                buf.append(codePoint);
            } else {
                buf.append("*");
            }
        }

        if (buf == null) {
            return source;//如果没有找到 emoji表情，则返回源字符串
        } else {
            if (buf.length() == len) {//这里的意义在于尽可能少的toString，因为会重新生成字符串
                buf = null;
                return source;
            } else {
                return buf.toString();
            }
        }
    }

    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    public static boolean containsEmoji(String source) {
        if (null == source) {
            return false;
        }
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (isEmojiCharacter(codePoint)) {
                //do nothing，判断到了这里表明，确认有表情字符
                return true;
            }
        }
        return false;
    }


    public List<News> spiderNewsList(String dateStr) {
        List<News> newsList = new ArrayList<>();
        String s = HttpUtil.httpGet("https://news.zhibo8.cc/nba/json/"+dateStr+".htm");
        String res = decodeUnicode(s);
        res = filterEmoji(res);
        try {
            PData pData = JSON.parseObject(res, PData.class);
            List<News> videoDataList = pData.getVideo_arr();
            if (CollectionUtil.isNotEmpty(videoDataList)) {
                videoDataList.stream().forEach(videoData -> {
                    String indextitle = videoData.getIndextitle();
                    String title = videoData.getTitle();
                    String shortTitle = videoData.getShortTitle();
                    String url = videoData.getUrl();
                    if (checkExists(indextitle) ||checkExists(title) ||checkExists(shortTitle)) {
                        log.info("indextitle:{},title:{},shortTitle:{},url:{}",
                                indextitle, title, shortTitle, url);
                        videoData.setDate(dateStr);
                        videoData.setUrl("https://news.zhibo8.cc/"+url);
                        newsList.add(videoData);
                    }
                });
            }
        } catch (Exception e) {
            log.error("转换异常:{}", e);
        }

        return newsList;
    }

    private static boolean checkExists(String s) {
        if (StrUtil.isBlank(s)) {
            return false;
        }
        return s.contains("詹姆斯") ||
                s.contains("勒布朗")||
                s.contains("老詹")||
                s.contains("科比")||
                s.contains("KB")||
                s.contains("LBJ");
    }

    public static String decodeUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len; ) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }

                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }
}
