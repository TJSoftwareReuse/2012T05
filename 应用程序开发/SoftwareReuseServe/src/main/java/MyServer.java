import cm.PropertiesUtil;
import com.team8.PerformanceManagement.PM;

import javax.jms.JMSException;
import javax.servlet.jsp.tagext.TryCatchFinally;
import java.io.File;
import java.io.IOException;

/**
 * Created by John on 2015/5/19.
 */
class Myserver implements Runnable {

    private String stcQueue;
    private String ctsQueue;


    public Myserver(String QueueName,String FmPath) {
        stcQueue = QueueName + "S";
        ctsQueue = QueueName + "c";
    }

    public void run() {
        //建立S->C通道
        MyProducer mp = new MyProducer("tcp://localhost:61616", stcQueue);
        mp.connStart();
        //向客户端发送信息，表示可以接受客户端的名字请求
        mp.sendMsg("OK");
        mp.connClose();
        //建立C->通道
        MyCustomer mc_name = new MyCustomer("tcp://localhost:61616",ctsQueue);
        mc_name.conStart();
        //接受客户端的名字
        String stu_name = mc_name.getMsg();
        PM.sendPMMessage(stu_name,1);
        try {
            PropertiesUtil propertiesUtil = new PropertiesUtil("/Users/mark/Desktop/1.properties");
            String name_group_path = propertiesUtil.readValue("name_group");
            PropertiesUtil util = new PropertiesUtil(name_group_path);
            String groupId = propertiesUtil.readValue(stu_name);
            mp = new MyProducer("tcp://localhost:61616",stcQueue);
            mp.connStart();
            if(groupId!=null) {
                mp.sendMsg(groupId);
                PM.sendPMMessage("Return Message",1);
            } else {
                mp.sendMsg("此人已死，有事烧纸");
                PM.sendPMMessage("Return Message",1);
            }
            mp.connClose();
        }
        catch (IOException e) {
            e.printStackTrace();
        }




    }



}
