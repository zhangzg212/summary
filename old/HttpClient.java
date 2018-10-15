package com.owinfo.service.util;

import com.alibaba.fastjson.JSONObject;
import com.netflix.client.http.HttpResponse;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyue on 2017/12/4.
 */
public class HttpClient {

    private static Logger logger = LoggerFactory.getLogger(HttpClient.class);

    public static void send(String url, String paramName, String paramValue) {
        try{
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair(paramName, paramValue));
            //设置参数到请求对象中
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
            httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            int statusCode = client.execute(httpPost).getStatusLine().getStatusCode();
            client.close();
            logger.info("调用SOCKET服务接口成功,响应码:"+ statusCode +"--->" + url);
        } catch (Exception e){
            logger.error("调用SOCKET服务接口失败", e.toString());
        }
    }

    public static String paPost(String url, String paramName, String paramValue) throws IOException {
        logger.info("调用获取平安充值数据服务接口请求 url :" + url);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        List <NameValuePair> nvps = new ArrayList <NameValuePair>();
        nvps.add(new BasicNameValuePair(paramName, paramValue));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
        httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
        httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        CloseableHttpResponse response2 = httpclient.execute(httpPost);
        int statusCode = response2.getStatusLine().getStatusCode();
        logger.info("调用获取平安充值数据服务接口成功,响应码:"+ statusCode +"--->" + url);
        HttpEntity entity2 = response2.getEntity();
        String str = EntityUtils.toString(entity2, "utf-8");
        response2.close();
        return str;
    }
}
