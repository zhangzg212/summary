package com.sobey.util;
import com.alibaba.fastjson.JSONObject;
import com.sobey.entity.user.Constants;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by TS on 2017/7/10.
 */
public class HttpClientUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    /**
     * post请求（添加header）
     * @param body
     * @param url
     * @param map
     * @return
     */
    public static String postHeaderSpringTemplate(String body , String url , Map<String , String> map) throws Exception {
    	RestTemplate restTemplate = new RestTemplate();
    	HttpHeaders headers = new HttpHeaders();  		
    	MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
    	headers.setContentType(type);
    	headers.add("Accept", MediaType.APPLICATION_JSON.toString());
	      if(!ObjectUtils.isEmpty(map)){
		      for(String key : map.keySet()){
		          headers.add(key, map.get(key));
		      }
	      }
	      JSONObject jsonObj = JSONObject.parseObject(body);
	      org.springframework.http.HttpEntity<String> formEntity = new org.springframework.http.HttpEntity<String>(jsonObj.toString(), headers);
	      logger.info("formEntity"+formEntity);
	      logger.info("url"+url);
	      try {
	    	  String result = restTemplate.postForObject(url, formEntity, String.class);
	    	  logger.info("result"+result);
	    	  return result ==null?"":result;
		} catch (Exception e) {
			logger.info("result"+e);
			return "";
		}
    }
        /**
         * post请求（添加header）
         * @param body
         * @param url
         * @param map
         * @return
         */
        public static String postHeader(String body , String url , Map<String , String> map) throws Exception {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            HttpResponse result;
            if(!ObjectUtils.isEmpty(map)){
                for(String key : map.keySet()){
                    httpPost.setHeader(key, map.get(key));
                }
            }
            httpPost.setHeader("Content-type","application/json; charset=utf-8");
            httpPost.setHeader("Accept", "application/json");
            //StringEntity entity = new StringEntity(body, "utf-8");// 解决中文乱码问题
            StringEntity entity = new StringEntity(body,Charset.forName("UTF-8"));
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            result = httpClient.execute(httpPost);
            return EntityUtils.toString(result.getEntity(),"UTF-8");

        }
        public static String postHeaderTempt(String body , String url , Map<String , String> map) throws Exception {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            HttpResponse result;
            if(!ObjectUtils.isEmpty(map)){
                for(String key : map.keySet()){
                    httpPost.setHeader(key, map.get(key));
                }
            }
            httpPost.setHeader("Content-type","application/json;charset=UTF-8");
            httpPost.setHeader("Accept", "application/json, text/plain, */*");
            httpPost.setHeader("Accept-Encoding", "gzip, deflate");
            httpPost.setHeader("Accept-Language","zh;q=0.9");
            //StringEntity entity = new StringEntity(body, "utf-8");// 解决中文乱码问题
            StringEntity entity = new StringEntity(body,Charset.forName("UTF-8"));
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            result = httpClient.execute(httpPost);
            return EntityUtils.toString(result.getEntity());

        }
        /**
         * put请求（添加header）
         * @param body
         * @param url
         * @param map
         * @return
         */
        public static String putHeader(String body , String url , Map<String , String> map) throws Exception {

            DefaultHttpClient httpClient = new DefaultHttpClient();

            HttpPut httpPut = new HttpPut(url);
            HttpResponse result;
            if(!ObjectUtils.isEmpty(map)) {
                for (String key : map.keySet()) {
                    httpPut.setHeader(key, map.get(key));
                }
            }
            StringEntity entity = new StringEntity(body, "utf-8");// 解决中文乱码问题
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPut.setEntity(entity);
            result = httpClient.execute(httpPut);
            return EntityUtils.toString(result.getEntity());

        }

        /**
         * get请求(带header)
         * @param url
         * @param map
         * @return
         */
        public static String getHeader(String url , Map<String , String> map) throws Exception {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse;
            if(!ObjectUtils.isEmpty(map)){
                for(String key : map.keySet()){
                    httpGet.setHeader(key, map.get(key));
                }
            }
            httpResponse = httpClient.execute(httpGet);
            org.apache.http.HttpEntity entity = httpResponse.getEntity();
            return EntityUtils.toString(entity, "utf-8");
        }

    /**
     * 重写delete方法（含header）
     * @param body
     * @param url
     * @param map
     * @return
     */
    public static String deleteHeader(String body , String url , Map<String , String> map) throws Exception {
        HttpDelete httpDelete = new HttpDelete(url);
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse result;
        if(!ObjectUtils.isEmpty(map)){
            for(String key : map.keySet()){
                httpDelete.setHeader(key, map.get(key));
            }
        }
        StringEntity entity = new StringEntity(body, "utf-8");// 解决中文乱码问题
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpDelete.setEntity(entity);
        result = httpClient.execute(httpDelete);

        return EntityUtils.toString(result.getEntity());
    }

    //******************我是华丽的分隔线*********************

    /*private final static int timeout = DefaultProperties.getPropertyAsInt("http-timeout", 5000);*/

    /**
     * 配置设置
     * @param http
     */
    private static void setConfig(HttpRequestBase http, String token) {
        // 设置超时
        /*RequestConfig config = RequestConfig.custom().setSocketTimeout(timeout)
                .setConnectTimeout(timeout).build();*/
        RequestConfig config = RequestConfig.custom().setSocketTimeout(5000)
                .setConnectTimeout(5000).build();
        http.setConfig(config);

        // 設置httpGet的头部參數信息
        http.setHeader("sobeyhive-http-system", Constants.sobeyhive_http_system);
        http.setHeader("sobeyhive-http-site", Constants.sobeyhive_http_site);
        http.setHeader("Content-Type", Constants.Content_Type);
        http.setHeader("sobeyhive-http-tool", Constants.sobeyhive_http_tool);
        http.setHeader("Cache-Control", Constants.Cache_Control);

        if(StringUtils.isNotBlank(token))
            http.setHeader("sobeyhive-http-token", token);
    }

    /**
     *
     * @param url
     * @param params
     * @return String
     */
    public static String get(String url, List<BasicNameValuePair> params) {
        return get(url, params, null);
    }

    /**
     * 带用户token请求
     * @param url
     * @param params
     */
    public static String get(String url, List<BasicNameValuePair> params,String token) {
        long startTime=new Date().getTime();
        String responseStr = "";
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            if (params == null)
                params = new ArrayList<BasicNameValuePair>();
            // 对参数编码
            String param = URLEncodedUtils.format(params, "UTF-8");
            // 将URL与参数拼接
            HttpGet httpget = new HttpGet(url + "?" + param);
            // 设置超时
            setConfig(httpget,token);
            logger.info("executing request <get> " + httpget.getURI());
            // 执行get请求.
            CloseableHttpResponse response = httpclient.execute(httpget);

            // 获取响应实体
            HttpEntity entity = response.getEntity();
            // 打印响应状态
            logger.info(response.getStatusLine().toString());
            if (entity != null) {
                // 打印响应内容长度
                logger.info("Response content length: "
                        + entity.getContentLength());
                // 打印响应内容
                responseStr = EntityUtils.toString(entity);
                logger.info("Response content: " + responseStr);

            }

        } catch (ClientProtocolException e) {
            logger.error("", e);
        } catch (ParseException e) {
            logger.error("", e);
        } catch (IOException e) {
            logger.error("", e);
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                logger.error("", e);
            }
        }
        long endTime=new Date().getTime();
        logger.info("executing request time:" + (endTime-startTime)+"ms");
        return responseStr;
    }

    /**
     *
     * @param url
     * @param params
     * @return
     */
    public static String post(String url, List<BasicNameValuePair> params) {
        return post(url, params, null,null);

    }
    /**
     * 带用户token请求
     * @param url
     * @param params
     */
    public static String post(String url, List<BasicNameValuePair> params,String token,String body) {
        long startTime=new Date().getTime();
        String responseStr = "";
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost
        HttpPost httppost = new HttpPost(url);
        setConfig(httppost,token);
        UrlEncodedFormEntity uefEntity;
        StringEntity bodyEntity;
        try {
            if (params == null)
                params = new ArrayList<BasicNameValuePair>();
            uefEntity = new UrlEncodedFormEntity(params, "UTF-8");
            bodyEntity = new StringEntity(body, "UTF-8");
            httppost.setEntity(uefEntity);
            httppost.setEntity(bodyEntity);
            logger.info("executing request <post> " + httppost.getURI());
            CloseableHttpResponse response = httpclient.execute(httppost);
            // 打印响应状态
            logger.info(response.getStatusLine().toString());
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseStr = EntityUtils.toString(entity, "UTF-8");
                logger.debug("Response content: " + responseStr);

            }

        } catch (ClientProtocolException e) {
            logger.error("", e);
        } catch (UnsupportedEncodingException e1) {
            logger.error("", e1);
        } catch (IOException e) {
            logger.error("", e);
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        long endTime=new Date().getTime();
        logger.info("executing request time:" + (endTime-startTime)+"ms");
        return responseStr;
    }

    /**
     * 带用户token请求
     * @param url
     * @param params
     */
    public static String put(String url, List<BasicNameValuePair> params,String token,String body) {
        long startTime=new Date().getTime();
        String responseStr = "";
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost
        HttpPut httpput = new HttpPut(url);
        setConfig(httpput,token);
        UrlEncodedFormEntity uefEntity;
        StringEntity bodyEntity;
        try {
            if (params == null)
                params = new ArrayList<BasicNameValuePair>();
            uefEntity = new UrlEncodedFormEntity(params, "UTF-8");
            bodyEntity = new StringEntity(body, "UTF-8");
            httpput.setEntity(uefEntity);
            httpput.setEntity(bodyEntity);
            System.out.println("executing request <put> " + httpput.getURI());
            CloseableHttpResponse response = httpclient.execute(httpput);
            // 打印响应状态
            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseStr = EntityUtils.toString(entity, "UTF-8");
                logger.debug("Response content: " + responseStr);

            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        long endTime=new Date().getTime();
        System.out.println("executing request time:" + (endTime-startTime)+"ms");
        return responseStr;
    }
    public static String PostRequest(String URL,String obj) {
        String jsonString="";
        try {
            //创建连接
            URL url = new URL(URL);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST"); //设置请求方法
            connection.setRequestProperty("Charsert", "UTF-8"); //设置请求编码
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type",
                    "application/json");
            connection.connect();
            //POST请求
            DataOutputStream out = new DataOutputStream(
                    connection.getOutputStream()); //关键的一步
            out.writeBytes(obj);
            out.flush();
            out.close();
            // 读取响应
            if (connection.getResponseCode()==200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
                String lines;
                StringBuffer sb = new StringBuffer("");
                while ((lines = reader.readLine()) != null) {
                    lines = new String(lines.getBytes());
                    sb.append(lines);
                }
                jsonString=sb.toString();
                reader.close();
            }//返回值为200输出正确的响应信息

            if (connection.getResponseCode()==400) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream(),Charset.forName("UTF-8")));
                String lines;
                StringBuffer sb = new StringBuffer("");
                while ((lines = reader.readLine()) != null) {
                    lines = new String(lines.getBytes());
                    sb.append(lines);
                }
                jsonString=sb.toString();
                reader.close();
            }//返回值错误，输出错误的返回信息
            // 断开连接
            connection.disconnect();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        logger.info("jsonString:" + jsonString);
        return jsonString;
    }

    public static String applePost(String url, String paramName, String paramValue) throws IOException {
        logger.info("调用链接苹果服务器接口请求 url :" + url);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        List <NameValuePair> nvps = new ArrayList <NameValuePair>();
        nvps.add(new BasicNameValuePair(paramName, paramValue));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
        httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
        httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        CloseableHttpResponse response2 = httpclient.execute(httpPost);
        int statusCode = response2.getStatusLine().getStatusCode();
        logger.info("调用链接苹果服务器接口请求成功,响应码:"+ statusCode +"--->" + url);
        HttpEntity entity2 = response2.getEntity();
        String str = EntityUtils.toString(entity2, "utf-8");
        response2.close();
        return str;
    }
}
