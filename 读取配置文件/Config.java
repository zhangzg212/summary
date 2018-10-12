package com.sobey.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Config {
	private final static Logger logger = Logger.getLogger(Config.class);
	private static final String configFileName = "config.properties";
	private Properties props;

	private static class InstanceHolder {
		private static Config instance = new Config();
	}

	private Config() {
		init();
	}

	private void init() {
		props = new Properties();
		InputStream in = null;
		try {
			in = this.getClass().getClassLoader().getResourceAsStream(configFileName);
			props.load(in);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public static final Config getInstance() {
		return InstanceHolder.instance;
	}

	/**
	 * 获取字符串
	 *
	 * @param key
	 * @return
	 */
	public String getString(String key) {
		String value = props.getProperty(key);
		if (StringUtils.isEmpty(value))
			logger.error("get property [" + key + "] is emply");
		return value;
	}

	/**
	 * 获取整形
	 *
	 * @param key
	 * @return
	 */
	public int getInt(String key) {
		Object objVal = props.get(key);
		if (null != objVal) {
			return Integer.parseInt(objVal.toString());
		}
		logger.error("get property [" + key + "] is emply");
		return 0;
	}

	/**
	 * 获取Boolean配置
	 *
	 * @param key
	 * @return
	 */
	public boolean getBoolean(String key) {
		Object objVal = props.get(key);
		if (null != objVal) {
			return Boolean.valueOf(objVal.toString());
		}
		logger.error("get property [" + key + "] is emply");
		return Boolean.FALSE;
	}

}
