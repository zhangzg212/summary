package com.sobey.soap;


import javax.jws.WebService;
import javax.xml.ws.Endpoint;

@WebService   //声明该业务类对外提供webservice服务
public class PhonePublishService {
    /**
     *  wsimport发布webservice服务
     * */
    public Phone getPhoneService(String name){
        Phone phone = new Phone();
        phone.setName(name);
        if(name.equals("android")){
            phone.setCompany("google");
            phone.setTatal(85);
        }else if(name.equals("ios")){
            phone.setCompany("apple");
            phone.setTatal(10);
        }else {
            phone.setCompany("other");
            phone.setTatal(5);
        }
        return phone;
    }

    public static void main(String[] args) {
        String address1 = "http://127.0.0.1:8888/ws/phoneService";
        String address2 = "http://127.0.0.1:8888/ws/phoneManage";
        Endpoint.publish(address1,new PhonePublishService());
        Endpoint.publish(address2,new PhonePublishService());
        System.out.println("发布wsdl地址："+address1+"?WSDL");
    }
}
