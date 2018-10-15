/*
 * 文件名：OwinfoBeanUtils.java
 * 版权：
 * 描述：〈描述〉对Bean进行操作的类
 * 修改时间：2017年9月5日
 * 修改内容：〈修改内容〉
 */
package com.owinfo.web.util;

import java.io.File;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 〈一句话功能简述〉 对Bean进行操作的类〈功能详细描述〉
 *
 * @author gooqhy
 * @version [版本号, 2017年9月10日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@SuppressWarnings("rawtypes")
public class OwinfoBeanUtils {

    // private static PropertyUtilsBean propertyUtilsBean;

    /**
     * 〈一句话功能简述〉 复制 source 的值给 target 对象，如果 target 对象里面有对应的值的话 〈功能详细描述〉
     *
     * @param source  存放着数据的源对象
     * @param target  需要获取数据的目标对象
     * @param filters 不需要目标对象获取的字段名称
     * @throws Exception Java运行时异常
     * @see [类、类#方法、类#成员]
     */
    public static void copyBeanProperties(Object source, Object target,
                                          String... filters)
            throws Exception {

        List<Field> sourceList =
                getDeclaredFieldAll(source.getClass(), filters);
        List<Field> targetList =
                getDeclaredFieldAll(target.getClass(), filters);

        for (Field targetField : targetList) {
            // 如果具有Final属性则直接跳过
            if (Modifier.isFinal(targetField.getModifiers())) {
                continue;
            }

            Field sourceField = searchFields(sourceList, targetField.getName());

            if (sourceField == null) {
                continue;
            }

            if (!sourceField.isAccessible()) {
                sourceField.setAccessible(true);
            }

            if (!targetField.isAccessible()) {
                targetField.setAccessible(true);
            }

            Object obj = sourceField.get(source);

            if (obj == null) {
                continue;
            }

            try {
                targetField.set(target, obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 〈一句话功能简述〉将对象转为Map
     * 〈功能详细描述〉
     *
     * @param obj 实体
     * @return 对象Map
     * @throws Exception 类型转换异常
     * @see [类、类#方法、类#成员]
     */
    public static Map<String, Object> describeMap(Object obj)
            throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != obj) {
            Class clazz = obj.getClass();
            List<Field> temp = getDeclaredFieldAll(clazz);
            Field[] fields = temp.toArray(new Field[temp.size()]);
            if (null != fields && fields.length > 0) {
                AccessibleObject.setAccessible(fields, true);
                for (Field field : fields) {
                    map.put(field.getName(), field.get(obj));
                }
            }
        }
        return map;
    }

    /**
     * 〈一句话功能简述〉判断对象中的属性是否为空
     * 〈功能详细描述〉
     *
     * @param obj     实体
     * @param filters 需要过滤的属性
     * @return true/false：空/不为空
     * @throws Exception 系统异常
     * @see [类、类#方法、类#成员]
     */
    public static boolean isEmptyObj(Object obj, String... filters)
            throws Exception {
        boolean isEmptyObj = false;
        if (null != obj) {
            Class clazz = obj.getClass();
            List<Field> targetList = getDeclaredFieldAll(clazz, filters);
            Field[] fields = targetList.toArray(new Field[targetList.size()]);
            int length = fields.length;
            int cnt = 0;
            if (null != fields && length > 0) {
                AccessibleObject.setAccessible(fields, true);
                for (Field field : fields) {
                    Object type = field.getType().getName();
                    Object value = field.get(obj);
                    if (type instanceof String) {
                        if (null == value || "".equals(value)) {
                            cnt++;
                        }
                    } else {
                        if (null == value) {
                            cnt++;
                        }
                    }

                }
            }
            if (length == cnt) {
                isEmptyObj = true;
            }
        } else {
            isEmptyObj = true;
        }

        return isEmptyObj;
    }

    /**
     * 〈一句话功能简述〉根据class类，获取类中所有字段属性 〈功能详细描述〉
     *
     * @param clazz   class类
     * @param filters 不需要再clazz中获取的字段
     * @return 类中所有字段属性但不包括需过滤的字段
     * @see [类、类#方法、类#成员]
     */
    public static List<Field> getDeclaredFieldAll(Class clazz,
                                                  String... filters) {

        Class superClass = Object.class;

        List<Field> fieldList = new ArrayList<Field>();

        while (superClass != clazz) {
            fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }

        if (filters != null) {
            int len = filters.length;

            for (int i = 0; i < len; i++) {
                String filter = filters[i];

                int size = fieldList.size();

                for (; size > 0; size--) {
                    Field field = fieldList.get(size - 1);

                    if (field.getName().equals(filter)) {
                        fieldList.remove(field);
                    }
                }
            }

        }

        return fieldList;
    }

    /**
     * 〈一句话功能简述〉根据字段名称获取字段对象 〈功能详细描述〉
     *
     * @param fields 字段对象集合
     * @param name   需要查找的字段对象的名称
     * @return 查询的结果，如果没找到，则返回null,找到了则返回查询到的字段对象
     * @see [类、类#方法、类#成员]
     */
    public static Field searchFields(List<Field> fields, String name) {

        Field field = null;

        int len = fields.size();

        for (int i = 0; i < len; i++) {

            Field tempField = fields.get(i);

            if (tempField.getName().equals(name))
            // if (tempField.getName() == name)
            {
                field = tempField;
            }
        }

        return field;
    }

    public static String formatDateStr(String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(dateStr);
        return sdf.format(date);
    }

    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + File.separator + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + File.separator + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    //删除文件夹
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
