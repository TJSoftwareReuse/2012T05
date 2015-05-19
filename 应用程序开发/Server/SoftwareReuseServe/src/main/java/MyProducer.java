import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by mark on 5/18/15.
 */
public class MyProducer {

    //the activemq provider address
    private String jmsProviderAddress;
    //the activemq queue name
    private String destinationName;


    //the params need to close
    private Connection conn;
    private Session session;
    private MessageProducer producer;

    public MyProducer(String _jmsProviderAddress, String _destinationName){
        jmsProviderAddress = _jmsProviderAddress;
        destinationName = _destinationName;
    }

    public void connStart(){
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(jmsProviderAddress);
            conn = connectionFactory.createConnection();
            session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination dest = session.createQueue(destinationName);
            producer = session.createProducer(dest);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String _msg) {
        Message message = null;
        try {
            message = session.createTextMessage(_msg);
            producer.send(message);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void connClose(){

        try {
            producer.close();
            session.close();
            conn.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws JMSException {

        MyProducer producer = new MyProducer("tcp://localhost:61616", "testQueue");
        producer.connStart();
        producer.sendMsg("MarKdeYANG, testSuccess");
        producer.connClose();

        System.out.println("Message sent.");
    }
}
