package com.owinfo.service.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: st
 * Date: 2017-10-16
 * Time: 15:09
 */
public class ClearingUtil {

    /**
     * 初始化HttpClient
     */
    private static CloseableHttpClient httpClient = HttpClients.createDefault();

    private final static String CLEARING_UTIL_YEAR = "YEAR";
    private final static String CLEARING_UTIL_MONTH = "MONTH";
    private final static String CLEARING_UTIL_DAY = "DAY";
    private final static String CLEARING_UTIL_HOUR = "HOUR";
    private final static String CLEARING_UTIL_MINUTE = "MINUTE";
    private final static String CLEARING_UTIL_SECOND = "SECOND";

    /**
     * 计算两个日期间隔的年 or 月 or 日
     * params String date String otherDate
     * 参数格式 yyyy-mm-dd或者yyyy-mm-dd
     */
    public static int intervalToDate(String date, String otherDate, String unit){
        Period period = Period.between(formatParamDate(date), formatParamDate(otherDate));
        switch (unit) {
            case CLEARING_UTIL_YEAR:
                return period.getYears();
            case CLEARING_UTIL_MONTH:
                return period.getMonths();
            case CLEARING_UTIL_DAY:
                return period.getDays();
            default:
                return period.getDays();
        }
    }
    /**
     * 计算两个日期间隔的年、月、日
     * params String date String otherDate
     * 参数格式 yyyy-mm-dd或者yyyy-mm-dd
     * 返回参数y-m-d 对应为年-月-日
     */
    public static String intervalYMDToDate(String date, String otherDate){
        return intervalToDate(date, otherDate, CLEARING_UTIL_YEAR) + "-"
                + intervalToDate(date, otherDate, CLEARING_UTIL_MONTH) + "-"
                + intervalToDate(date, otherDate, CLEARING_UTIL_DAY);
    }

    /**
     * 计算两个日期间隔的时 or 分 or 秒
     * params String time String otherTime
     * 参数格式hh:mm:ss 或者 hhmmss
     */
    public static long intervalToTime(String date, String otherDate, String unit){
        switch (unit) {
            case CLEARING_UTIL_HOUR:
                return ChronoUnit.HOURS.between(formatParamTime(date),formatParamTime(otherDate));
            case CLEARING_UTIL_MINUTE:
                return ChronoUnit.MINUTES.between(formatParamTime(date),formatParamTime(otherDate));
            case CLEARING_UTIL_SECOND:
                return ChronoUnit.SECONDS.between(formatParamTime(date),formatParamTime(otherDate));
            default:
                return ChronoUnit.SECONDS.between(formatParamTime(date),formatParamTime(otherDate));
        }
    }

    /**
     * 计算两个日期间隔的时、分、秒
     * params String time String otherTime
     * 参数格式hh:mm:ss 或者 hhmmss
     * 返回参数h:m:s 对应为年-月-日
     */
    public static String intervalYMDToTime(String date, String otherDate){
        return intervalToTime(date, otherDate, CLEARING_UTIL_HOUR) + ":"
                + intervalToTime(date, otherDate, CLEARING_UTIL_MINUTE) + ":"
                + intervalToTime(date, otherDate, CLEARING_UTIL_SECOND);
    }


    private static LocalDate formatParamDate(String args) {
        if(args.length()==10 && args.contains("-")) {
            return LocalDate.of(Integer.valueOf(args.split("-")[0]),
                    Integer.valueOf(args.split("-")[1]),
                    Integer.valueOf(args.split("-")[2]));
        }else {
            return LocalDate.of(Integer.valueOf(args.substring(0,4)),
                    Integer.valueOf(args.substring(5,7)),
                    Integer.valueOf(args.substring(8,args.length())));
        }

    }

    private static LocalTime formatParamTime(String args) {
        if (args.length() == 8 && args.contains(":")) {
            return LocalTime.of(Integer.valueOf(args.split(":")[0]),
                    Integer.valueOf(args.split(":")[1]),
                    Integer.valueOf(args.split(":")[2]));
        } else {
            return LocalTime.of(Integer.valueOf(args.substring(0,2)),
                    Integer.valueOf(args.substring(3,5)),
                    Integer.valueOf(args.substring(6,args.length())));
        }
    }

    //当天时间字符串
    public static String getNowDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowDateString = formatter.format(date);
        return nowDateString;
    }

    //当天时间字符串
    public static String getNowDayT(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String nowDateString = formatter.format(date);
        return nowDateString;
    }

    //当天时间字符串
    public static String getNowDayF(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String nowDateString = formatter.format(date);
        return nowDateString;
    }

    //当天时间字符串
    public static String getNowDay(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String nowDateString = formatter.format(date);
        return nowDateString;
    }

    //当前时间的Date
    public static Date getNowDateTwo(Date date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(sdf.format(new Date()));
    }

    //格式化时间格式
    public static String formatDateStr(String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(dateStr);
        return sdf.format(date);
    }

    //当天时间字符串
    public static String getAfterDayTwo(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String nowDateString = formatter.format(date);
        return nowDateString;
    }

    //当前时间的前一天字符串
    public static String getNextDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String nowDateString = formatter.format(date);
        return nowDateString;
    }
    //当前时间的后一天字符串
    public static String getAfterDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, +1);
        date = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String nowDateString = formatter.format(date);
        return nowDateString;
    }
    //当前时间的前一天Date
    public static Date getNextDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date = calendar.getTime();
        return date;
    }

    //当前时间的前30天Date
    public static Date before30Date(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -30);
        date = calendar.getTime();
        return date;
    }

    //当前时间的前30天字符串
    public static String before30DateStr(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -30);
        date = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String nowDateString = formatter.format(date);
        return nowDateString;
    }

    //当前时间后一天Date
    public static Date getAfterDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, +1);
        date = calendar.getTime();
        return date;
    }
    //生成32位UUID
    public static String getUUID(){
        String uid = UUID.randomUUID().toString();
        return uid.replaceAll("-", "");
    }
    //天数加一
    public static Date addDate(Date date){
        Format f = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, 1);// 今天+1天
        Date tomorrow = c.getTime();
        return tomorrow;
    }
    //月份加一
    public static Date addOneMoth(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        //c.add(calendar.WEEK_OF_MONTH, 1);
        c.add(c.MONTH, 1);// 当前月+1
        Date tomorrow = c.getTime();
        return tomorrow;
    }
    //月份加一字符串
    public static String addOneMothStr(Date date){
        Format f = new SimpleDateFormat("yyyy-MM");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(c.MONTH, 1);// 当前月+1
        Date tomorrow = c.getTime();
        String nowDateString = f.format(tomorrow);
        return nowDateString;
    }
    //月份加一字符串
    public static String addOneMothStrB(Date date){
        Format f = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(c.MONTH, 1);// 当前月+1
        Date tomorrow = c.getTime();
        String nowDateString = f.format(tomorrow);
        return nowDateString;
    }
    //当前月字符串
    public static String getNowMonthStr(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
        String nowDateString = formatter.format(date);
        return nowDateString;
    }
    //当天时间字符串
    public static String getNowMonthStrB(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String nowDateString = formatter.format(date);
        return nowDateString;
    }

    //推送数据POST
    public static String postData(String url) throws Exception {
        URL restURL = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) restURL.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setAllowUserInteraction(false);
        PrintStream ps = new PrintStream(conn.getOutputStream());
        ps.close();
        BufferedReader bReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line, resultStr = "";
        while (null != (line = bReader.readLine())) {
            resultStr += line;
        }
        bReader.close();
        return resultStr;
    }
    //推送数据GET
    public static void getData(String url) throws Exception {
//        HttpGet get = new HttpGet(url);
//        ResponseHandler<String> responseHandler = new BasicResponseHandler();
//        String responseStr = null;
//        try {
//            responseStr = httpClient.execute(get, responseHandler);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return responseStr;
        //httpClient
        HttpClient httpClient = new DefaultHttpClient();
        // get method
        HttpGet httpGet = new HttpGet(url);
        //response
        try{
            httpClient.execute(httpGet);
        }catch (Exception e) {
            e.printStackTrace();
        }

//        //get response into String
//        String temp="";
//        try{
//            HttpEntity entity = response.getEntity();
//            temp=EntityUtils.toString(entity,"UTF-8");
//        }catch (Exception e) {}
//
//        return temp;
    }

    //推送数据GET
    public static void PostData(String url) throws Exception {

    }

    //两个时间相差的天数
    public static int daysOfTwo(Date startDate, Date endDate) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(endDate);
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);

        return Integer.parseInt(String.valueOf(between_days));

    }
	
	//指定时间字符串加指定分钟数后的时间字符串
    public static String addMinute(String startTime, int minute)throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(startTime));//+3小时
        cal.add(Calendar.MINUTE, minute);
        System.out.println(sdf.format(cal.getTime()));
		return sdf.format(cal.getTime());
    }
}
