package com.owinfo.service.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.owinfo.object.dto.CollectField;

import java.util.*;
import java.util.stream.Collectors;


public class CollectFieldUtil {

    /**
     * 格式化采集字段
     * @param param
     * @return
     */
    public static String collectField(Map<String, Object> param) {
        List<HashMap<String, Object>> fields = (List<HashMap<String, Object>>) param.get("collectField");
        JSONArray array = new JSONArray();
        if(fields.size() > 0){
            for (HashMap<String, Object> hashMap : fields) {
                JSONObject field = new JSONObject();
                field.put("fieldNo", hashMap.get("fieldNo"));
                field.put("fieIdValue", hashMap.get("fieIdValue"));
                array.add(field);
            }
        }
        return array.toString();
    }

    /**
     * 反格式采集字段
     * @param collectField
     * @return
     */
    public static List<CollectField> collectField(String collectField) {
        JSONArray array = JSONArray.parseArray(collectField);
        if (array.size() > 0){
            List<CollectField> list = new ArrayList<>();
            for (Object field : array) {
                JSONObject var = (JSONObject) field;
                CollectField collect = new CollectField();
                collect.setFieldNo((String) var.get("fieldNo"));
                collect.setFieIdValue((String) var.get("fieIdValue"));
                list.add(collect);
            }
            return list;
        }
        return null;
    }
}
