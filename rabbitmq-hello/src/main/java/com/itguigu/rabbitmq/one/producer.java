package com.itguigu.rabbitmq.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @ ClassName : producer
 * @ Description : 生产者  发消息
 * @ Author : lsm
 * @Date: 2022-05-20 18:02
 */
public class producer {

    //队列名称
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) {
        //1.创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //2.工厂IP连接rabbitmq的队列
        factory.setHost("10.17.31.223");
        //用户名
        factory.setUsername("guest");
        //密码
        factory.setPassword("guest");
        try {
            //创建连接
            Connection connection = factory.newConnection();
            //获取信道
            Channel channel = connection.createChannel();
            /**
             * 生成一个队列
             * 1.队列名称
             * 2.队列里面的消息是否持久化（磁盘），默认情况下消息存储在内存中（非持久化）
             * 3.该队列是否只供一个消费者消费，是否进行消息共享，true可以多个消费者消费，false只能一个消费者消费
             * 4.是否自动删除，最后一个消费者断开连接后，该队列是否自动删除，true自动删除，false不自动删除
             * 5.其他参数：
             **/
            channel.queueDeclare(QUEUE_NAME, false, false , false, null);
            //发消息
            String message = "hello world"; //初次使用
            /***
             *发送一个消费
              *1.发送到那个交换机
             * 2.路由的key值是哪个，本次是队列名称
             * 3.其他参数信息
             * 4.发送消息的消息体
             **/
             channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("消息发送成功！");
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
