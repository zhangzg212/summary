package com.sobey.soap;

import java.util.List;

public class TetsSoap {
    public static void main(String[] args) {
        MobileCodeWS ws = new MobileCodeWS();
        MobileCodeWSSoap mobileCodeWSSoap = ws.getMobileCodeWSSoap();
        String result = mobileCodeWSSoap.getMobileCodeInfo("15756876542", "");
        System.out.println(result);
        //全部数据
        ArrayOfString databaseInfo = mobileCodeWSSoap.getDatabaseInfo();
        List<String> results = databaseInfo.getString();
        for(String info : results){
            System.out.println(info);
        }
    }
}
