package com.owinfo.service.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
* @description: 将一个 Map 对象转化为一个 JavaBean
* @author hekunlin on 2017/10/18 17:20
*/
public class BeanToMapUtil {
	/**
	 * @param map
	 *            包含属性值的 map
	 * @param obj
	 *            bean对象
	 * @return 转化出来的 JavaBean 对象
	 * @throws IntrospectionException
	 *             如果分析类属性失败
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 *             如果实例化 JavaBean 失败
	 * @throws InstantiationException
	 *             如果实例化 JavaBean 失败
	 * @throws InvocationTargetException
	 *             如果调用属性的 setter 方法失败
	 */
	public static void convertMap(Map<String, Object> map, Object obj) throws IntrospectionException,IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor property : propertyDescriptors) {
			String key = property.getName();
			if (map.containsKey(key)) {
				Object value = map.get(key);
				// 得到property对应的setter方法
				Method setter = property.getWriteMethod();
				// 得到property对应的getter方法
				Method getter = property.getReadMethod();
				final String objects = getter.getGenericReturnType().toString();
				if (objects.contains("Integer")) {
					Integer v = Integer.parseInt(String.valueOf(value));
					setter.invoke(obj, v);
				} else if (objects.contains("String")) {
					String v = String.valueOf(value);
					setter.invoke(obj, v);
				} else if (objects.contains("Double")) {
					double v = Double.parseDouble(String.valueOf(value));
					setter.invoke(obj, v);
				} else if (objects.contains("Float")) {
					float v = Float.parseFloat((String) value);
					setter.invoke(obj, v);
				} else if (objects.contains("Long")) {
					long v = Long.parseLong(String.valueOf(value));
					setter.invoke(obj, v);
				} else if (objects.contains("Boolean")) {
					boolean v = Boolean.parseBoolean(String.valueOf(value));
					setter.invoke(obj, v);
				} else if (objects.contains("Date")) {
					Date v = parseDate(String.valueOf(value));
					setter.invoke(obj, v);
				}
			}
		}
	}

	/**
	 * 将一个 JavaBean 对象转化为一个 Map
	 * 
	 * @param bean
	 *            要转化的JavaBean 对象
	 * @return 转化出来的 Map 对象
	 * @throws IntrospectionException
	 *             如果分析类属性失败
	 * @throws IllegalAccessException
	 *             如果实例化 JavaBean 失败
	 * @throws InvocationTargetException
	 *             如果调用属性的 setter 方法失败
	 */
	public static Map convertBean(Object bean) throws IntrospectionException, IllegalAccessException,
			InvocationTargetException {
		Class type = bean.getClass();
		Map<String, Object> returnMap = new HashMap<String, Object>(200);
		BeanInfo beanInfo = Introspector.getBeanInfo(type);

		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();
			if (!"class".equals(propertyName)) {
				Method readMethod = descriptor.getReadMethod();
				Object result = readMethod.invoke(bean, new Object[0]);
				if (result != null) {
					if (result instanceof Date) {
						returnMap.put(propertyName, String.valueOf(((Date) result).getTime()));
					} else {
						returnMap.put(propertyName, String.valueOf(result));
					}
				} else {
					returnMap.put(propertyName, "");
				}
			}
		}
		return returnMap;
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
}
