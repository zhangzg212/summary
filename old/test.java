package com.owinfo.service.util;

/**
 * 文件名：.java
 * 版权： 北京联众信安成都分公司
 * 描述：
 * 创建时间：2018年01月04日
 * <p>
 * 〈一句话功能简述〉〈功能详细描述〉
 *
 * @author sun
 * @version [版本号, 2018年01月04日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class test implements Comparable<test>{
    private String etcCardId;
    private String vehicleLicense;
    private String tradeDate;

    public String getEtcCardId() {
        return etcCardId;
    }

    public void setEtcCardId(String etcCardId) {
        this.etcCardId = etcCardId;
    }

    public String getVehicleLicense() {
        return vehicleLicense;
    }

    public void setVehicleLicense(String vehicleLicense) {
        this.vehicleLicense = vehicleLicense;
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public test(String etcCardId, String vehicleLicense, String tradeDate) {
        this.etcCardId = etcCardId;
        this.vehicleLicense = vehicleLicense;
        this.tradeDate = tradeDate;
    }

    public test(){

    }

    @Override
    public int compareTo(test o) {
        return this.getTradeDate().compareTo(o.getTradeDate());
    }

    // 返回打印用
    @Override
    public String toString() {
        return etcCardId + "|" + vehicleLicense + "|" + tradeDate ;
    }

}
