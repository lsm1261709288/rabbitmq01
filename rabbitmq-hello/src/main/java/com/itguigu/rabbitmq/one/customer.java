package com.itguigu.rabbitmq.one;

import com.rabbitmq.client.*;

/**
 * @ ClassName : customer
 * @ Description :  消费者  接受消息
 * @ Author : lsm
 * @Date: 2022-05-20 19:57
 */
public class customer {
    //队列名称
    public static final String QUEUE_NAME = "hello";
    //接受消息
    public static void main(String[] args) throws Exception{
        //1.创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //2.工厂IP连接rabbitmq的队列
        factory.setHost("10.17.31.223");
        //用户名
        factory.setUsername("guest");
        //密码
        factory.setPassword("guest");
        //创建rabbitmq的连接
        Connection connection = factory.newConnection();
        //获取信道
        Channel channel = connection.createChannel();

        //声明 接受消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(new String(message.getBody()));
        };
        //取消消息时的回调
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("消息消费被中断！");
        };
        /**
         * 消费者消费消息
         * 1.消费哪个队列
         * 2.消费成功之后是否自动应答，true 代表自动应答，false代表手动应答
         * 3.消费者未成功消费消息的回调
         * 4.消费者取消消费的回调
         **/
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }

}
