package com.owinfo.service.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Created by admin on 2017/10/20.
 */
public class DataValidate {
    private DataValidate(){}
    /**
     * 校验参数入库格式
     *
     */
    public static void validateCard(Map<String,Object> data){
        if(data.get("cardBalance")!=null && data.get("cardBalance")!=""){
            BigDecimal bigDecimal = new BigDecimal(String.valueOf(data.get("cardBalance")));
            data.put("cardBalance",bigDecimal);
        }
        if(data.get("cardAccountBalance")!=null && data.get("cardAccountBalance")!=""){
            BigDecimal bigDecimal = new BigDecimal(String.valueOf(data.get("cardAccountBalance")));
            data.put("cardAccountBalance",bigDecimal);
        }
        if(data.get("cardPrice")!=null && data.get("cardPrice")!=""){
            BigDecimal bigDecimal = new BigDecimal(String.valueOf(data.get("cardPrice")));
            data.put("cardPrice",bigDecimal);
        }
        if(data.get("liquidateNumber")!=null && data.get("liquidateNumber")!=""){
            BigDecimal bigDecimal = new BigDecimal(String.valueOf(data.get("liquidateNumber")));
            data.put("liquidateNumber",bigDecimal);
        }
        if(data.get("favorRate")!=null && data.get("favorRate")!=""){
            BigDecimal bigDecimal = new BigDecimal(String.valueOf(data.get("favorRate")));
            data.put("favorRate",bigDecimal);
        }
        data.put("transferMark","0");
    }

    public static String validateDate(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    public static String validateUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }


    public static boolean PublicAccount(Map<String, Object> data) {
        return true;
    }

}
