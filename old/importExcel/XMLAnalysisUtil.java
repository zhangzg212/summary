package com.owinfo.service.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class XMLAnalysisUtil {
	
	private XMLAnalysisUtil(){}
	
	/**
	 * 获取XML文档的Document对象，解析失败返回NULL
	 * @param xmlPath
	 *    XML文件路径
	 * @return
	 *    Document对象
	 */
	public static Document getDocumentObject(String xmlPath){
		try {
			return new SAXReader().read(new File(xmlPath));
		} catch (DocumentException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 *  获取对象属性与值之间对应关系的Map集合
	 * @param bean
	 *    对象
	 * @return
	 *    解析之后的Map集合
	 */
    public static Map<String, String> getFieldValueMap(Object bean) {  
        Class<?> cls = bean.getClass();  
        Map<String, String> valueMap = new HashMap<String, String>();  
        Method[] methods = cls.getDeclaredMethods();  
        Field[] fields = cls.getDeclaredFields();  
        for (Field field : fields) {  
            try {  
                String fieldType = field.getType().getSimpleName();  
                String fieldGetName = parGetName(field.getName());  
                if (!checkGetMet(methods, fieldGetName)) {  
                    continue;  
                }  
                Method fieldGetMet = cls.getMethod(fieldGetName, new Class[] {});  
                Object fieldVal = fieldGetMet.invoke(bean, new Object[] {});  
                String result = null;  
                if ("Date".equals(fieldType)) {  
                    result = fmtDate((Date) fieldVal);  
                } else {  
                    if (null != fieldVal) {  
                        result = String.valueOf(fieldVal);  
                    }  
                }  
                valueMap.put(field.getName(), result);  
            } catch (Exception e) {
                continue;
            }  
        }  
        return valueMap;  
    }
    
    
    /**
     *  设置值到对象的属性中
     * @param bean
     *    操作的对象
     * @param valMap
     *    属性值的map
     */
    public static void setFieldValue(Object bean, Map<String, String> valMap) {  
        Class<?> cls = bean.getClass();  
        Method[] methods = cls.getDeclaredMethods();  
        Field[] fields = cls.getDeclaredFields();  
        for (Field field : fields) {  
            try {  
                String fieldSetName = parSetName(field.getName());  
                if (!checkSetMet(methods, fieldSetName)) {  
                    continue;  
                }  
                Method fieldSetMet = cls.getMethod(fieldSetName,  field.getType());  
                String  fieldKeyName = field.getName();  
                String value = valMap.get(fieldKeyName);  
                if (null != value && !"".equals(value)) {  
                    String fieldType = field.getType().getSimpleName();  
                    if ("String".equals(fieldType)) {  
                        fieldSetMet.invoke(bean, value);  
                    } else if ("Date".equals(fieldType)) {  
                        Date temp = parseDate(value);  
                        fieldSetMet.invoke(bean, temp);  
                    } else if ("Integer".equals(fieldType)  
                            || "int".equals(fieldType)) {  
                        Integer intval = Integer.parseInt(value);  
                        fieldSetMet.invoke(bean, intval);  
                    } else if ("Long".equalsIgnoreCase(fieldType)) {  
                        Long temp = Long.parseLong(value);  
                        fieldSetMet.invoke(bean, temp);  
                    } else if ("Double".equalsIgnoreCase(fieldType)) {  
                        Double temp = Double.parseDouble(value);  
                        fieldSetMet.invoke(bean, temp);  
                    } else if ("Boolean".equalsIgnoreCase(fieldType)) {  
                        Boolean temp = Boolean.parseBoolean(value);  
                        fieldSetMet.invoke(bean, temp);  
                    } else {  
                        System.out.println("not supper type" + fieldType);  
                    }  
                }  
            } catch (Exception e) {  
                continue;  
            }  
        }  
    }
    
    /** 
     * 判断该属性是否存在set方法
     */  
    private static boolean checkSetMet(Method[] methods, String fieldSetMet) {  
        for (Method met : methods) {  
            if (fieldSetMet.equals(met.getName())) {  
                return true;  
            }  
        }  
        return false;  
    }
    
    /** 
     * 格式化String为Date 
     */  
    private static Date parseDate(String datestr) {  
        if (null == datestr || "".equals(datestr)) {  
            return null;  
        }  
        try {  
            String fmtstr = null;  
            if (datestr.indexOf(':') > 0) {  
                fmtstr = "yyyy-MM-dd HH:mm:ss";  
            } else {  
                fmtstr = "yyyy-MM-dd";  
            }  
            SimpleDateFormat sdf = new SimpleDateFormat(fmtstr, Locale.UK);  
            return sdf.parse(datestr);  
        } catch (Exception e) {  
            return null;  
        }  
    }
    
    /** 
     * Date转化为String 
     */  
    private static String fmtDate(Date date) {  
        if (date == null) {  
            return null;  
        }  
        try {  
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",  Locale.US);  
            return sdf.format(date);  
        } catch (Exception e) {  
            return null;  
        }  
    } 
    
    /** 
     * 判断该属性是否存在get方法
     */  
    private static boolean checkGetMet(Method[] methods, String fieldGetMet) {  
        for (Method met : methods) {  
            if (fieldGetMet.equals(met.getName())) {  
                return true;  
            }  
        }  
        return false;  
    }
    
    /** 
     * 拼接属性的 get方法 
     */  
    private static String parGetName(String fieldName) {  
        if (null == fieldName || "".equals(fieldName)) {  
            return null;  
        }  
        int startIndex = 0;  
        if (fieldName.charAt(0) == '_')  
            startIndex = 1;  
        return "get"  
                + fieldName.substring(startIndex, startIndex + 1).toUpperCase()  
                + fieldName.substring(startIndex + 1);  
    }
    
    /** 
     * 拼接属性的 set方法 
     */  
    private static String parSetName(String fieldName) {  
        if (null == fieldName || "".equals(fieldName)) {  
            return null;  
        }  
        int startIndex = 0;  
        if (fieldName.charAt(0) == '_')  
            startIndex = 1;  
        return "set"  
                + fieldName.substring(startIndex, startIndex + 1).toUpperCase()  
                + fieldName.substring(startIndex + 1);  
    }
    
    /** 
     * 获取属性名（调用parGetName） 
     */  
/*    private static String parKeyName(String fieldName) {  
        String fieldGetName = parGetName(fieldName);  
        if (fieldGetName != null && fieldGetName.trim() != ""  
                && fieldGetName.length() > 3) {  
            return fieldGetName.substring(3);  
        }  
        return fieldGetName;  
    } */
}