package com.liyue.ws;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * Created by liyue on 2017/11/16.
 *
 *
 *      读取配置文件属性
 *
 */
public class PropertiesUtil {

    public static String wsdlLocation;

    public static Map<String, Object> param;

    static {
        Properties properties = new Properties();
        InputStream inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("Config.properties");
        try {
            properties.load(inputStream);
            param = new HashMap<>();
            Iterator<Object> iterator = properties.keySet().iterator();
            while (iterator.hasNext()){
                String key = (String) iterator.next();
                param.put(key, properties.getProperty(key));
            }
            inputStream.close();
            wsdlLocation = getPropertiesValue("TASK_URL");
        } catch (IOException e) {
        }
    }

    public  static String getPropertiesValue(String key) {
        return (String) param.get(key);
    }
}
