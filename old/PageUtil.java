/**
 * 文件名：.java
 * 版权： 北京联众信安成都分公司
 * 描述： 分页工具类
 * 创建时间：2017年09月22日
 */
package com.owinfo.service.util;

import com.owinfo.core.entity.AllDetailDto;
import com.owinfo.core.entity.TransDetailAll;
import com.owinfo.object.entity.DisputeAll;
import com.owinfo.object.entity.MonthBill;
import com.owinfo.object.entity.SingleCardClear;
import com.owinfo.object.entity.HighWayCost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉分页〈功能详细描述〉
 *
 * @author liuxingchi
 * @version [版本号, 2017年09月22日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class PageUtil {
    /**
     * 〈一句话功能简述〉工作流接口数据分页〈功能详细描述〉
     *
     * @param page
     * @param pageSize
     * @param list1
     * @return
     * @throws Exception 异常
     * @see [类、类#方法、类#成员]
     */
    public static Map<String, Object> MyPage(int page, int pageSize, List<AllDetailDto> list1, long total) {
        Map<String, Object> tempMap = new HashMap<>();
        tempMap.put("page", page);
        tempMap.put("pageSize", pageSize);
        tempMap.put("total", total);
        tempMap.put("list", list1);
        return tempMap;
    }

    public static Map<String, Object> MyPageThree(int page, int pageSize, List<HighWayCost> list1, long total, String allTotal) {
        List<HighWayCost> list2 = new ArrayList<>();
        int seeStart = (page - 1) * pageSize;
        int seeLength = page * pageSize;
        if ((list1.size() - seeStart) < pageSize) {
            seeLength = list1.size();
        }
        //分页返回
        for (int i = seeStart; i < seeLength; i++) {
            list2.add(list1.get(i));
        }
        Map<String, Object> tempMap = new HashMap<>();
        tempMap.put("page", page);
        tempMap.put("pageSize", pageSize);
        tempMap.put("total", total);
        tempMap.put("list", list2);
        tempMap.put("allTotal", allTotal);
        return tempMap;
    }

    public static Map<String, Object> MyPageTwo(int page, int pageSize, List<SingleCardClear> list1, long total) {
        List<SingleCardClear> list2 = new ArrayList<>();
        int seeStart = (page - 1) * pageSize;
        int seeLength = page * pageSize;
        if ((list1.size() - seeStart) < pageSize) {
            seeLength = list1.size();
        }
        //分页返回
        for (int i = seeStart; i < seeLength; i++) {
            list2.add(list1.get(i));
        }
        Map<String, Object> tempMap = new HashMap<>();
        tempMap.put("page", page);
        tempMap.put("pageSize", pageSize);
        tempMap.put("total", total);
        tempMap.put("list", list2);
        return tempMap;
    }

    public static Map<String, Object> MyPageFour(int page, int pageSize, List<DisputeAll> list1, long total) {
        List<DisputeAll> list2 = new ArrayList<>();
        int seeStart = (page - 1) * pageSize;
        int seeLength = page * pageSize;
        if ((list1.size() - seeStart) < pageSize) {
            seeLength = list1.size();
        }
        //分页返回
        for (int i = seeStart; i < seeLength; i++) {
            list2.add(list1.get(i));
        }
        Map<String, Object> tempMap = new HashMap<>();
        tempMap.put("page", page);
        tempMap.put("pageSize", pageSize);
        tempMap.put("total", total);
        tempMap.put("list", list2);
        return tempMap;
    }

    public static Map<String, Object> MyPageFive(int page, int pageSize, List<MonthBill> list1, long total, Map<String, Object> clientInfo) {
        List<MonthBill> list2 = new ArrayList<>();
        int seeStart = (page - 1) * pageSize;
        int seeLength = page * pageSize;
        if ((list1.size() - seeStart) < pageSize) {
            seeLength = list1.size();
        }
        //分页返回
        for (int i = seeStart; i < seeLength; i++) {
            list2.add(list1.get(i));
        }
        Map<String, Object> tempMap = new HashMap<>();
        tempMap.put("page", page);
        tempMap.put("pageSize", pageSize);
        tempMap.put("total", total);
        tempMap.put("list", list2);
        tempMap.put("clientInfo", clientInfo);
        return tempMap;
    }
}
