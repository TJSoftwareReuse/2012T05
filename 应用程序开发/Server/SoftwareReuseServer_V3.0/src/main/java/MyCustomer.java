import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by mark on 5/18/15.
 */
public class MyCustomer {


    //params of activemq
    //the activemq server address
    private String jmsProviderAddress;
    //the activemq Queue name
    private String destinationName;

    //param need to close
    private Connection conn;
    private Session session;
    private MessageConsumer consumer;

    public MyCustomer(String _jmsAddress, String _destinationName){
        jmsProviderAddress = _jmsAddress;
        destinationName = _destinationName;
    }

    public void conStart() {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(jmsProviderAddress);
        try {
            conn = connectionFactory.createConnection();
            session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination dest = session.createQueue(destinationName);
            consumer = session.createConsumer(dest);
            conn.start();
            //log4j output: customer start
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public String getMsg(){
        String strMsg = null;
        try {
            Message msg = consumer.receive();
            TextMessage textMessage = (TextMessage) msg;
            strMsg = textMessage.getText();
            //log4j output Received message from ActiveMQ + destinationName: + Text
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return strMsg;
    }

    public void connClose(){
        try {
            consumer.close();
            session.close();
            conn.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
        //log4j output: customer close
    }


    public static void main(String[] args) throws JMSException {

        MyCustomer customer = new MyCustomer("tcp://localhost:61616", "testQueue");
        customer.conStart();
        String msg = customer.getMsg();
        customer.connClose();

        System.out.println(msg);
    }
}
