import cm.PropertiesUtil;
import com.team8.PerformanceManagement.PM;

import java.io.IOException;

/**
 * Created by John on 2015/5/19.
 */
public class Myserver implements Runnable {

    private String stcQueue;
    private String ctsQueue;
    private String activemqServerURL = "tcp://localhost:61616";

    public Myserver(String QueueName) {
        stcQueue = QueueName + "S";
        ctsQueue = QueueName + "C";
    }

    public void run() {
        MyProducer mp = new MyProducer(activemqServerURL, stcQueue);
        mp.connStart();
        mp.sendMsg("OK");

        MyCustomer mc_name = new MyCustomer(activemqServerURL,ctsQueue);
        mc_name.conStart();
        String stu_name = mc_name.getMsg();
        System.out.println(stu_name + "////////////////");
        PM.sendPMMessage(stu_name,1);
        try {
            PropertiesUtil propertiesUtil = new PropertiesUtil("src/file/project.properties");
            String name_group_path = propertiesUtil.readValue("DataSource");
            System.out.println(name_group_path + "//////////");
            PropertiesUtil util = new PropertiesUtil(name_group_path);
            String groupId = util.readValue(stu_name);
            System.out.println(groupId + "////////////");
            if(groupId!=null) {
                mp.sendMsg(groupId);
                PM.sendPMMessage("Return Message", 1);
                System.out.println("Return Message Success////////////");
            } else {
                mp.sendMsg("本人已死，有事烧纸");
                PM.sendPMMessage("Return Message", 1);
                System.out.println("Return Message Failed////////////");
            }
            mp.connClose();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
