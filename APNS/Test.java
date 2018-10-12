/**
 * Created by Administrator on 2018/7/22.
 */
import com.notnoop.apns.*;
import java.io.FileNotFoundException;

public class Test {

    /** * get push service
     * @return
     * @throws FileNotFoundException
     */

    public ApnsService  getPushService() throws FileNotFoundException {
        String certPath = "D://applecert.p12";
        ApnsService service =
                APNS.newService()
                        .withCert(certPath, "123456")
                        .withProductionDestination()
                        .build();
        return service;
    }

    public void pushVOIP() throws Exception{

        String deviceToken = "5e11eefc6f8b427b6646bcaf5320a5e714b121859f21b0ee78d88d3f411ac69b6";
        ApnsService  service = getPushService();
        try{
            String call = APNS.newPayload().customField("cmd","call").build();
            //String cancel = APNS.newPayload().customField("cmd","cancel").build();
            service.push(deviceToken, call);
            //  service.push(deviceToken, cancel);
        }catch (Exception e){
            throw new Exception(e);
        }finally {
            if (service != null){
                service.stop();
            }
        }
    }


}
