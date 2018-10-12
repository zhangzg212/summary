package com.sobey.util;


import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebServiceMobileService {

    public void getMobile(String mobileNo,String userId) throws IOException {
        URL url = new URL("http://ws.webxml.com.cn/WebServices/MobileCodeWS.asmx/getMobileCodeInfo?mobileCode="+mobileNo+"&userID="+userId);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
            InputStream is = conn.getInputStream();
            //内存流
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = is.read(buffer))!=-1){
                baos.write(buffer,0,len);
            }
            System.out.println("GET请求返回数据： "+baos.toString());
            baos.close();
            is.close();
        }
    }

    public void postMobile(String mobileNo,String userId) throws IOException {
        HttpClient client = new HttpClient();
        PostMethod postMetheod = new PostMethod("http://ws.webxml.com.cn/WebServices/MobileCodeWS.asmx/getMobileCodeInfo");
        postMetheod.setParameter("mobileCode",mobileNo);
        postMetheod.setParameter("userID",userId);
        int code = client.executeMethod(postMetheod);
        String result = postMetheod.getResponseBodyAsString();
        System.out.println("POST请求返回数据: "+result);
    }

    public void soapMobile() throws IOException {
        HttpClient client = new HttpClient();
        PostMethod postMetheod = new PostMethod("http://ws.webxml.com.cn/WebServices/MobileCodeWS.asmx");
        postMetheod.setRequestBody("D:/soap.xml");
        postMetheod.setRequestHeader("Content-Type","text/xml; charset=utf-8");
        int code = client.executeMethod(postMetheod);
        String result = postMetheod.getResponseBodyAsString();
        System.out.println("soap请求返回数据: "+result);
    }

    public static void main(String[] args) throws IOException {
        WebServiceMobileService ws = new WebServiceMobileService();
        //ws.postMobile("15756876542","");
        ws.soapMobile();
    }
}
