package TestQueue;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class MyConsumer {
	public static void main(String[] args) throws JMSException {
		String jmsProviderAddress = "tcp://172.22.118.3:61616";
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				jmsProviderAddress);

		Connection connection = connectionFactory.createConnection();
		Session session = connection.createSession(false,
				Session.AUTO_ACKNOWLEDGE);
		
		long date = new Date().getTime();
		String id  = Long.toString(date);
		String destinationName = "RequestQueue";
		
		Destination publicDes = session.createQueue(destinationName);
		Destination PrivateClientDes = session.createQueue(id+"C");
		Destination PrivateServertDes = session.createQueue(id+"S");
		
		MessageProducer PublicQueue=session.createProducer(publicDes);		
		MessageProducer PrivateClientQueue=session.createProducer(PrivateClientDes);
		MessageConsumer PrivateServerQueue = session.createConsumer(PrivateServertDes);

		connection.start();
		System.out.println("Connection Started...");
		System.out.println("Preparing for answering your questions...");

			
		Message message = session.createTextMessage(id);
		PublicQueue.send(message);
		
		
		message = PrivateServerQueue.receive();//can add parameter 1000ms
		TextMessage textMessage = (TextMessage) message;
		String text = textMessage.getText();
		if(text.equals("OK"))
		{
			System.out.println("I'm ready...");
			System.out.println("Input student name to get infomation.(Input Q to exit.)");
		}
		else if(text.equals("Don't have license, cannot offer service anymore!!!!!"))
		{
			System.out.println(text);
			PublicQueue.close();
			PrivateClientQueue.close();
			PrivateServerQueue.close();
			session.close();
			connection.close();
			return;
		}
		else {
			System.out.println("It's hard to tell you that an error happened...GoodBye~");
			PublicQueue.close();
			PrivateClientQueue.close();
			PrivateServerQueue.close();
			session.close();
			connection.close();
			return;
		}

		
		BufferedReader strin=new BufferedReader(new InputStreamReader(System.in)); 
		try {
			text=strin.readLine();
			while(!text.equals("Q"))
			{
				message = session.createTextMessage(text);
				PrivateClientQueue.send(message);
				message = PrivateServerQueue.receive();
				textMessage = (TextMessage) message;
				System.out.print("Name:"+text+" "+textMessage.getText());
				
				text=strin.readLine();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		message = session.createTextMessage("Q");
		PrivateClientQueue.send(message);		
		PublicQueue.close();
		PrivateClientQueue.close();
		PrivateServerQueue.close();
		session.close();
		connection.close();
		System.out.println("I'm tring to quit :) Have a good day.");
	}
}
