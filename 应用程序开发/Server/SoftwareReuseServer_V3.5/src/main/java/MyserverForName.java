import cm.PropertiesUtil;
import com.team8.PerformanceManagement.PM;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by John on 2015/5/19.
 */
public class MyserverForName implements Runnable {

    private String stcQueue;
    private String ctsQueue;
    private String activemqServerURL = "tcp://localhost:61616";
    private String CMPath = "src/file/project.properties";
    private PropertiesUtil propertiesUtil;

    public MyserverForName(String QueueName) throws IOException {
        stcQueue = QueueName + "S";
        ctsQueue = QueueName + "C";
        propertiesUtil = new PropertiesUtil(CMPath);
    }

    public void run() {
        MyProducer mp = new MyProducer(activemqServerURL, stcQueue);
        mp.connStart();
        mp.sendMsg("OK");

        MyCustomer mc_name = new MyCustomer(activemqServerURL,ctsQueue);
        mc_name.conStart();

        while(true){
            //Msg get from the client
            String stu_name = mc_name.getMsg();
            if (stu_name.equals("Q")){
                System.out.println("Thread finish //////////");
                break;
            }
            System.out.println(stu_name + "////////////////");
            try {
                PM.setPathName(propertiesUtil.readValue("PM"));
                PM.sendPMMessage(stu_name, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String outPutMsg = "";

            try {
                PropertiesUtil propertiesUtil = new PropertiesUtil("src/file/project.properties");
                String name_group_path = propertiesUtil.readValue("DataSource");
                System.out.println(name_group_path + "//////////");

                //New hashmap
                File file = new File(name_group_path);
                BufferedReader reader = null;
                try {
                    System.out.println("read file begin");
                    reader = new BufferedReader(new FileReader(file));
                    String tempString = null;
                    while ((tempString = reader.readLine())!=null) {
                        String[] a = tempString.split("=");
                        if(a[1].equals(stu_name)){
                            outPutMsg = outPutMsg +a[0] + "\n";
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println(outPutMsg + "////////////");
                if(outPutMsg!= "") {
                    mp.sendMsg(outPutMsg);
                    PM.setPathName(propertiesUtil.readValue("PM"));
                    PM.sendPMMessage("Return Message", 1);
                    System.out.println("Return Message Success////////////");
                } else {
                    mp.sendMsg("404 not found");
                    PM.setPathName(propertiesUtil.readValue("PM"));
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
