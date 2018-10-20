import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.Connection;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

import javax.jms.*;
import java.io.IOException;
// 一对一
public class testActiveMq {
    @Test
    public void testQueueProducer() throws JMSException {
        //1.创建一个连接工厂的对象
        ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://192.168.153.133:61616");
        //2.创建Connection
        javax.jms.Connection connection = connectionFactory.createConnection();
        //3.开启连接
        connection.start();
        //4.创建一个session对象
            //第一个参数：是否开启事务 一般不开启，如果开启，第二参数无意义
            //第二个参数：应答模式。自动应答或者手动应答。一般是自动应答
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5.创建一个Destination对象，queue、topic两种形式
        Queue queue = session.createQueue("test-queue");
        MessageProducer producer = session.createProducer(queue);
      //  ActiveMQTextMessage textMessage = new ActiveMQTextMessage();
        //textMessage.setText("hello ActiveMq");
        TextMessage textMessage = session.createTextMessage("hello activeMq");
        //6.发送消息
        producer.send(textMessage);
        producer.close();
        session.close();
        connection.close();
    }

    @Test
    public void testQueueComsumer() throws JMSException, IOException {
        //1.创建一个连接工厂的对象
        ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://192.168.153.133:61616");
        //2.创建Connection
        javax.jms.Connection connection = connectionFactory.createConnection();
        //3.开启连接
        connection.start();
        //4.创建一个session对象
        //第一个参数：是否开启事务 一般不开启，如果开启，第二参数无意义
        //第二个参数：应答模式。自动应答或者手动应答。一般是自动应答
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5.从此队列中获取消息
        Queue queue = session.createQueue("spring-queue");
        MessageConsumer consumer = session.createConsumer(queue);
        //6.监听发送消息的接口
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                TextMessage message1 = (TextMessage) message;
                String text;
                try {
                    //获取监听到的消息
                    text = message1.getText();
                    System.out.println(text);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        //等待接收消息
        System.in.read();

        consumer.close();
        session.close();
        connection.close();

    }

    @Test
    public void testTopicProducer() throws JMSException {
        //1.创建一个连接工厂的对象
        ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://192.168.153.133:61616");
        //2.创建Connection
        javax.jms.Connection connection = connectionFactory.createConnection();
        //3.开启连接
        connection.start();
        //4.创建一个session对象
        //第一个参数：是否开启事务 一般不开启，如果开启，第二参数无意义
        //第二个参数：应答模式。自动应答或者手动应答。一般是自动应答
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5.创建一个Destination对象，queue、topic两种形式
        Topic topic = session.createTopic("test-topic");
        MessageProducer producer = session.createProducer(topic);
        TextMessage test_topic = session.createTextMessage("test topic");
        producer.send(test_topic);
        producer.close();
        session.close();
        connection.close();

    }
    @Test
    public void testTopicConsumer() throws JMSException, IOException {
        //1.创建一个连接工厂的对象
        ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://192.168.153.133:61616");
        //2.创建Connection
        javax.jms.Connection connection = connectionFactory.createConnection();
        //3.开启连接
        connection.start();
        //4.创建一个session对象
        //第一个参数：是否开启事务 一般不开启，如果开启，第二参数无意义
        //第二个参数：应答模式。自动应答或者手动应答。一般是自动应答
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5.创建一个Destination对象，queue、topic两种形式
        Topic topic = session.createTopic("test-topic");
        MessageConsumer consumer = session.createConsumer(topic);
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                TextMessage message1 = (TextMessage) message;
                String text;
                try {
                    //获取监听到的消息
                    text = message1.getText();
                    System.out.println(text);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        System.out.println("topic消费者1启动");
        //等待接收消息
        System.in.read();

        consumer.close();
        session.close();
        connection.close();
    }
}
