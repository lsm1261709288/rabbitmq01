package com.itguigu.rabbitmq.three;

import com.itguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.util.Scanner;

/**
 * @ ClassName : task02
 * @ Description : 生产者  消息在手动应答时是不消失，重新放回消息队列中
 * @ Author : lsm
 * @Date: 2022-05-21 16:06
 */
public class task02 {
    //队列名称
    public static final String TASK_QUEUE_NAME = "ack_queue";
    //发送消息
    public static void main(String[] args) throws Exception {
        //调用utils工具类中的静态方法，获取到信道
        Channel channel = RabbitMqUtils.getChannel();
        //开启发布确认的方法
        channel.confirmSelect();
        //声明队列
        boolean durable = true;//需要让QUEUE队列持久化
        channel.queueDeclare(TASK_QUEUE_NAME, durable, false, false, null);
        //从控制台手动录入消息发送
        System.out.println("请输入您要发送的消息：");
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
            //发送消息
            //设置生产者发送消息持久化（磁盘） MessageProperties.PERSISTENT_TEXT_PLAIN，非持久化：内存
            channel.basicPublish("",TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
            System.out.println("消息发送完成  " + message);
        }


    }

}
