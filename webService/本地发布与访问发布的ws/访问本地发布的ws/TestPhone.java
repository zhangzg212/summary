package com.sobey.phonepublish;
/**
 * 测试自己发布的webservice
 */
public class TestPhone {

    public static void main(String[] args) {
        PhonePublishServiceService ws = new PhonePublishServiceService();
        PhonePublishService phonePublishServicePort = ws.getPhonePublishServicePort();
        Phone android = phonePublishServicePort.getPhoneService("android");
        System.out.println(android.getName());
        System.out.println(android.getCompany());
        System.out.println(android.getTatal());
    }

}
