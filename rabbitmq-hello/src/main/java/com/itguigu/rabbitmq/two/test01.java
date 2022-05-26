package com.itguigu.rabbitmq.two;

import com.itguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * @ ClassName : test01
 * @ Description : 生产者发送大量的消息
 * @ Author : lsm
 * @Date: 2022-05-21 14:11
 */
public class test01 {
    //队列名称
    public static final String QUEUE_NAME = "hello";
    //发送大量消息
    public static void main(String[] args) throws Exception {
        //调用工具类中的静态方法
        Channel channel = RabbitMqUtils.getChannel();

        /**
         * 生成一个队列
         * 1.队列名称
         * 2.队列里面的消息是否持久化（磁盘），默认情况下消息存储在内存中（非持久化）
         * 3.该队列是否只供一个消费者消费，是否进行消息共享，true可以多个消费者消费，false只能一个消费者消费
         * 4.是否自动删除，最后一个消费者断开连接后，该队列是否自动删除，true自动删除，false不自动删除
         * 5.其他参数：
         **/
          channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        //从控制台输入消息发送
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            /***
             *发送一个消费
             *1.发送到那个交换机
             * 2.路由的key值是哪个，本次是队列名称
             * 3.其他参数信息
             * 4.发送消息的消息体
             **/
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("消息发送完成  " + message);
        }

    }
}
