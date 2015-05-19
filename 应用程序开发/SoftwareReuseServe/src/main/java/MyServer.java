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
        //����S->Cͨ��
        MyProducer mp = new MyProducer("tcp://localhost:61616", stcQueue);
        mp.connStart();
        //��ͻ��˷�����Ϣ����ʾ���Խ��ܿͻ��˵���������
        mp.sendMsg("OK");
        mp.connClose();
        //����C->ͨ��
        MyCustomer mc_name = new MyCustomer("tcp://localhost:61616",ctsQueue);
        mc_name.conStart();
        //���ܿͻ��˵�����
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
                mp.sendMsg("����������������ֽ");
                PM.sendPMMessage("Return Message",1);
            }
            mp.connClose();
        }
        catch (IOException e) {
            e.printStackTrace();
        }




    }



}
