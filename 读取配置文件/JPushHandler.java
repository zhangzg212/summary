package com.sobey.jpush;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import com.sobey.utils.Config;
import com.sobey.utils.HttpClientUtil;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.PushPayload;

final class JPushHandler {
	private static final Logger logger = Logger.getLogger(JPushHandler.class);
	private JPushClient jPushClient = null;
	private String app_key;
	private String app_secret;

	private JPushHandler() {
	}

	private static class InstanceHolder {
		private static JPushHandler instance = new JPushHandler();
	}

	private JPushClient getJPushClient() {
		if (null == jPushClient) {
			synchronized (this) {
				if (null == jPushClient) {
					Config config = Config.getInstance();
					String secret = config.getString("jpush.secret");
					String appkey = config.getString("jpush.appkey");
					logger.info("appkey:" + appkey + "secret:" + secret);
					app_key = appkey;
					app_secret = secret;
					jPushClient = new JPushClient(secret, appkey);
				}
			}
		}
		return jPushClient;
	}

	public static final JPushHandler getInstance() {
		return InstanceHolder.instance;
	}

	/**
	 * POST请求发送消息
	 * 
	 * @param url 请求地址
	 * @param pushPayload
	 */
	public void sendPush(String url, PushPayload pushPayload) {
		getJPushClient();
		String str = app_key + ":" + app_secret;
		String auth = Base64.encodeBase64String(str.getBytes());
		String content = pushPayload.toString();
		logger.info("pushPayload转换为json数据:" + content);
		String back = HttpClientUtil.sendPost(url, content, auth);
		logger.info("发送消息的结果为:" + back);
	}
	
	public void sendPush(String url, String json) {
		getJPushClient();
		String str = app_key + ":" + app_secret;
		String auth = Base64.encodeBase64String(str.getBytes());
		String back = HttpClientUtil.sendPost(url, json, auth);
		logger.info("发送消息的结果为:" + back);
	}

	/**
	 * 发送push消息体
	 *
	 * @param pushPayload
	 */
	public void sendPush(PushPayload pushPayload) {
		try {
			PushResult result = getJPushClient().sendPush(pushPayload);
			logger.info("发送jpush消息返回信息:" + result);
		} catch (APIConnectionException e) {
			logger.error(e.getMessage(), e);
		} catch (APIRequestException e) {
			logger.error(e.getMessage(), e);
		}
	}
}
