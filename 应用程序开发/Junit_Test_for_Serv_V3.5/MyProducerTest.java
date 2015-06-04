import static org.junit.Assert.*;

import org.junit.Test;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class MyProducerTest {
	
	@Test
	public void testGetMsg() throws JMSException {
		
		String jmsProviderAddress = "tcp://localhost:61616";
		String destinationName = "RequestQueue";
		
		//����ģ��Ŀͻ���
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				jmsProviderAddress);

		Connection connection = connectionFactory.createConnection();
		Session session = connection.createSession(false,
				Session.AUTO_ACKNOWLEDGE);
		
		Destination publicDes = session.createQueue(destinationName);
		
		MessageConsumer PublicQueue=session.createConsumer(publicDes);		
		
		connection.start();
		
		//����ģ��ķ�������
		MyProducer mp = new MyProducer(jmsProviderAddress, destinationName);
		mp.connStart();
		 
		//�������˷���message
		mp.sendMsg("test");
		 
		//�ͻ��˽���message
		Message message = PublicQueue.receive();
		TextMessage textMessage = (TextMessage) message;
		String text = textMessage.getText();
		 
		//�رտͻ���
		PublicQueue.close();
		session.close();
		connection.close();
		 
		//�رշ�������
		mp.connClose();
		 
		assertEquals("failure - strings not same", "test", text);
	}

}
