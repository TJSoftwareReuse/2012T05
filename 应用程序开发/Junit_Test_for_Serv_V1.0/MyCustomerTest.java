package main.java;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

public class MyCustomerTest {
	
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
		
		MessageProducer PublicQueue=session.createProducer(publicDes);		
		
		connection.start();
		
		//����ģ��ķ�������
		 MyCustomer serverGetRequestCustomer = new MyCustomer(jmsProviderAddress, destinationName);
		 serverGetRequestCustomer.conStart();
		 
		 //�ͻ��˷���message
		 Message message = session.createTextMessage("test");
		 PublicQueue.send(message);
		 
		 //�������˽���message
		 String msg = serverGetRequestCustomer.getMsg();
		 
		 //�رտͻ���
		 PublicQueue.close();
		 session.close();
		 connection.close();
		 
		 //�رշ�������
		 serverGetRequestCustomer.connClose();
		 
		 assertEquals("failure - strings not same", "test", msg);
	}

}
