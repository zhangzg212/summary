package com.owinfo.object.dto;

import java.io.Serializable;

/**
 * Created by liyue on 2018/3/1.
 * <p>
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 */
public class CollectField implements Serializable {

    private static final long serialVersionUID = 1L;

    /**采集字段编号*/
    private String fieldNo;
    /**采集字段值*/
    private String fieIdValue;


    public String getFieldNo() {
        return fieldNo;
    }

    public void setFieldNo(String fieldNo) {
        this.fieldNo = fieldNo;
    }

    public String getFieIdValue() {
        return fieIdValue;
    }

    public void setFieIdValue(String fieIdValue) {
        this.fieIdValue = fieIdValue;
    }
}
