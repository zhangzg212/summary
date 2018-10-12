package com.sobey.service.impl;

import com.alibaba.fastjson.JSON;
import com.sobey.base.entity.BlackAwake;
import com.sobey.base.entity.UsercountPojo;
import com.sobey.dao.UsercountDao;
import com.sobey.service.IBlackAwakeService;
import com.sobey.util.ReturnInfo;
import javapns.devices.Device;
import javapns.devices.implementations.basic.BasicDevice;
import javapns.notification.AppleNotificationServerBasicImpl;
import javapns.notification.PushNotificationManager;
import javapns.notification.PushNotificationPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;


@Service
public class BlackAwakeServiceImpl implements IBlackAwakeService {

	@Autowired
	private UsercountDao usercountDao;
	private static Logger LOGGER = LoggerFactory.getLogger(BlackAwakeServiceImpl.class);
	@Override
	public Object blackAwake(BlackAwake blackAwake) throws Exception {
		{
			//根据userCode查出deviceToken
			UsercountPojo usercountPojo = usercountDao.findTokenByCode(blackAwake.getToUsercode());
			if(usercountPojo == null){
				LOGGER.info("呼叫用户未登录");
				return 	ReturnInfo.error("呼叫用户未登录");
			}
			String deviceToken = usercountPojo.getDeviceToken();
			String json = JSON.toJSONString(blackAwake);
			File path = new File(ResourceUtils.getURL("classpath:").getPath());
			String certificatePath  = path.toString() + File.separator + "cert_voip.p12";
			String certificatePassword = "123";//此处注意导出的证书密码不能为空因为空密码会报错
			PushNotificationManager pushManager = new PushNotificationManager();
			try
			{
				PushNotificationPayload payLoad = new PushNotificationPayload();
				payLoad.addAlert(json.toString()); // 消息内容
				if(blackAwake.getOperType() == 2){
					payLoad.addAlert("cancel");
				}
				payLoad.addBadge(1); // iphone应用图标上小红圈上的数值
				payLoad.addSound("default");//铃音
				LOGGER.info("向IOS推送参数 deviceToken = "+deviceToken+" , alert = " +json+" , sound ="+"default");
				//true：表示的是产品发布推送服务 false：表示的是产品测试推送服务
				pushManager.initializeConnection(new AppleNotificationServerBasicImpl(certificatePath, certificatePassword, false));
				// 发送push消息
				Device device = new BasicDevice();
				device.setToken(deviceToken);
				pushManager.sendNotification(device, payLoad, true);
				LOGGER.info("推送完成");
				pushManager.stopConnection();
				LOGGER.info("关闭连接");
			}
			catch (Exception e)
			{
				e.printStackTrace();
				LOGGER.info("推送出错 error : "+e.toString());
				pushManager.stopConnection();
				LOGGER.info("关闭连接");
				return ReturnInfo.error("推送出错");
			}
		}
		return ReturnInfo.success();
	}
}
