package com.owinfo.service.util;

import com.owinfo.object.entity.DisputeAll;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 文件名：.java
 * 版权： 北京联众信安成都分公司
 * 描述： 测试list排序
 * 创建时间：2018年01月03日
 * <p>
 * 〈一句话功能简述〉测试list排序〈功能详细描述〉
 *
 * @author sun
 * @version [版本号, 2018年01月03日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
    public class MySortList<E> {

    /**
     * 对列表中的数据按指定字段进行排序。要求类必须有相关的方法返回字符串、整型、日期等值以进行比较。
     *
     * @param list
     * @param method
     * @param reverseFlag
     */
    public void sortByMethod(List<E> list, final String method,
                             final boolean reverseFlag) {
        Collections.sort(list, new Comparator<Object>() {
            @SuppressWarnings("unchecked")
            public int compare(Object arg1, Object arg2) {
                int result = 0;
                try {
                    Method m1 = ((E) arg1).getClass().getMethod(method, null);
                    Method m2 = ((E) arg2).getClass().getMethod(method, null);
                    Object obj1 = m1.invoke(((E) arg1), null);
                    Object obj2 = m2.invoke(((E) arg2), null);
                    if (obj1 instanceof String) {
                        // 字符串
                        result = obj1.toString().compareTo(obj2.toString());
                    } else if (obj1 instanceof Date) {
                        // 日期
                        long l = ((Date) obj1).getTime() - ((Date) obj2).getTime();
                        if (l > 0) {
                            result = 1;
                        } else if (l < 0) {
                            result = -1;
                        } else {
                            result = 0;
                        }
                    } else if (obj1 instanceof Integer) {
                        // 整型（Method的返回参数可以是int的，因为JDK1.5之后，Integer与int可以自动转换了）
                        result = (Integer) obj1 - (Integer) obj2;
                    } else {
                        // 目前尚不支持的对象，直接转换为String，然后比较，后果未知
                        result = obj1.toString().compareTo(obj2.toString());

                        System.err.println("MySortList.sortByMethod方法接受到不可识别的对象类型，转换为字符串后比较返回...");
                    }

                    if (reverseFlag) {
                        // 倒序
                        result = -result;
                    }
                } catch (NoSuchMethodException nsme) {
                    nsme.printStackTrace();
                } catch (IllegalAccessException iae) {
                    iae.printStackTrace();
                } catch (InvocationTargetException ite) {
                    ite.printStackTrace();
                }

                return result;
            }
        });
    }
    // 测试函数
    /*public static void main(String[] args) throws Exception {
        // 生成自定义对象，然后对它按照指定字段排序
        List<test> listMember = new ArrayList<test>();
        listMember.add(new test("4101", "川A66666", "1992-12-01"));
        listMember.add(new test("4102", "川A55555", "2017-11-01"));
        listMember.add(new test("4103", "川A44444", "2015-12-01"));
        System.out.println("Member当前顺序...");
        System.out.println(listMember);

        // 方式一排序输出
        System.out.println("Member默认排序（用自带的compareTo方法）后...");
        Collections.sort(listMember);
        System.out.println(listMember);
        System.out.println("Member倒序（用自带的compareTo方法）后...");
        Collections.sort(listMember, Collections.reverseOrder());
        System.out.println(listMember);

        // 方式二排序输出
        MySortList<test> msList = new MySortList<test>();
        msList.sortByMethod(listMember, "etcCardId", false);
        System.out.println("Member按字段etcCardId排序后...");
        System.out.println(listMember);

        msList.sortByMethod(listMember, "vehicleLicense", false);
        System.out.println("Member按字段vehicleLicense排序后...");
        System.out.println(listMember);

        msList.sortByMethod(listMember, "tradeDate", true);
        System.out.println("Member按字段tradeDate倒序后...");
        System.out.println(listMember);
    }*/
}