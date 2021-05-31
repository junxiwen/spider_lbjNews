package com.hyanzz.news.spider.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description
 * @auther ywy
 * @create 2020-06-17 15:37
 */
public class HttpUtil {

    private static CloseableHttpClient client = HttpClients.createDefault();


    public static String httpGet(String url) {
        //返回数据
        String responseString = null;
        // 获取http客户端
        //CloseableHttpClient client = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(3000).setConnectionRequestTimeout(1000)
                .setSocketTimeout(3000).build();
        // 通过httpget方式来实现我们的get请求
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);
        // 通过client调用execute方法，得到我们的执行结果就是一个response，所有的数据都封装在response里面了
        CloseableHttpResponse Response = null;

        try {
            Response = client.execute(httpGet);
            // HttpEntity是一个中间的桥梁，在httpClient里面，是连接我们的请求与响应的一个中间桥梁，所有的请求参数都是通过HttpEntity携带过去的
            // 所有的响应的数据，也全部都是封装在HttpEntity里面
            HttpEntity entity = Response.getEntity();
            // 通过EntityUtils 来将我们的数据转换成字符串
            responseString = EntityUtils.toString(entity, "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (null != Response) {
                    Response.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return responseString;
    }
}
