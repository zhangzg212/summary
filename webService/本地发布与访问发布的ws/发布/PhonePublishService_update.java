package com.sobey.soap;


import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;

@WebService(serviceName = "phoneService",targetNamespace = "com.sobey.phone")
//声明该业务类对外提供webservice服务,默认只对public修饰的方法发布  指定发布服务名 指定命名空间
public class PhonePublishService {
    /**
     *  wsimport发布webservice服务
     * */
    @WebMethod(operationName = "getPhoneInfo") //指定发布的方法名 返回类型名 入参名
    public @WebResult(name = "phone") Phone getPhoneService(@WebParam(name = "osName") String name){
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

    public void sayHello(String name){
        System.out.println("你好："+name);
    }

    void sayBye(String name){
        System.out.println("再见："+name);
    }

    @WebMethod(exclude = true) //不发布该方法
    public void sayLuck(String name){
        System.out.println("好运："+name);
    }

    public static void main(String[] args) {
        String address1 = "http://127.0.0.1:8888/ws/phoneService";
        String address2 = "http://127.0.0.1:8888/ws/phoneManage";
        Endpoint.publish(address1,new PhonePublishService());
        Endpoint.publish(address2,new PhonePublishService());
        System.out.println("发布wsdl地址："+address1+"?WSDL");
    }
}
