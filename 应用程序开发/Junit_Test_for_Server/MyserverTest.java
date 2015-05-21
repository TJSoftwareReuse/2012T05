package main.java;

import static org.junit.Assert.*;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

public class MyserverTest {

	@Test
	public void testRun() throws JMSException {
		
		String jmsProviderAddress = "tcp://localhost:61616";
		String destinationName = "RequestQueue";
		
		//构建模拟的客户端
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				jmsProviderAddress);

		Connection connection = connectionFactory.createConnection();
		Session session = connection.createSession(false,
				Session.AUTO_ACKNOWLEDGE);
		
		Destination PrivateClientDes = session.createQueue(destinationName+"C");
		Destination PrivateServertDes = session.createQueue(destinationName+"S");
		
		MessageProducer PrivateClientQueue=session.createProducer(PrivateClientDes);
		MessageConsumer PrivateServerQueue = session.createConsumer(PrivateServertDes);
		
		connection.start();
		
		//服务器端
		Myserver server = new Myserver(destinationName);
		
		//测试是否返回OK
		 Message message = session.createTextMessage("Q");
		 PrivateClientQueue.send(message);
		 server.run();
		 Message message3 = PrivateServerQueue.receive();
		 TextMessage textMessage = (TextMessage) message3;
		 String text = textMessage.getText();
		 assertEquals("failure - the string is not equal to OK ", "OK", text);
		 
		 //测试是否能够返回组员相应的组别
		 Message message2 = session.createTextMessage("周泽宏");
		 message = session.createTextMessage("Q");
		 PrivateClientQueue.send(message2);
		 PrivateClientQueue.send(message);
		 server.run();
		 message3 = PrivateServerQueue.receive();
		 Message message4 = PrivateServerQueue.receive();
		 textMessage = (TextMessage) message4;
		 text = textMessage.getText();
		 assertEquals("failure - the team is wrong ", "5", text);
		 
		 //测试不存在的组员
		 message2 = session.createTextMessage("张三");
		 message = session.createTextMessage("Q");
		 PrivateClientQueue.send(message2);
		 PrivateClientQueue.send(message);
		 server.run();
		 message3 = PrivateServerQueue.receive();
		 message4 = PrivateServerQueue.receive();
		 textMessage = (TextMessage) message4;
		 text = textMessage.getText();
		 assertEquals("failure - strings not same ", "404 not found", text);
		
		 //关闭客户端
		 PrivateClientQueue.close();
		 PrivateServerQueue.close();
		 session.close();
		 connection.close();
	}

}
