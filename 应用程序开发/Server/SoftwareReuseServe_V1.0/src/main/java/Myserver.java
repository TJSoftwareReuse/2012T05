import cm.PropertiesUtil;
import com.team8.PerformanceManagement.PM;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

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

        while(true){
            String stu_name = mc_name.getMsg();
            if (stu_name.equals("Q")){
                System.out.println("Thread finish //////////");
                break;
            }
            System.out.println(stu_name + "////////////////");
            PM.sendPMMessage(stu_name, 1);
            try {
                PropertiesUtil propertiesUtil = new PropertiesUtil("src/file/project.properties");
                String name_group_path = propertiesUtil.readValue("DataSource");
                System.out.println(name_group_path + "//////////");

                //New hashmap
                HashMap<String,String> hashMap = new HashMap<String,String>();
                File file = new File(name_group_path);
                BufferedReader reader = null;
                try {
                    System.out.println("read file begin");
                    reader = new BufferedReader(new FileReader(file));
                    String tempString = null;
                    while ((tempString = reader.readLine())!=null) {
                        String[] a = tempString.split("=");
                        hashMap.put(a[0],a[1]);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String groupId = hashMap.get(stu_name);
                System.out.println(groupId + "////////////");
                if(groupId!=null) {
                    mp.sendMsg(groupId);
                    PM.sendPMMessage("Return Message", 1);
                    System.out.println("Return Message Success////////////");
                } else {
                    mp.sendMsg("404 not found");
                    PM.sendPMMessage("Return Message", 1);
                    System.out.println("Return Message Failed////////////");
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        mp.connClose();
        mc_name.connClose();

    }
}
