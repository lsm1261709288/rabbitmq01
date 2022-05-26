package com.itguigu.rabbitmq.five;

import com.itguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * @ ClassName : EmitLog
 * @ Description : 生产者  发送消息
 * @ Author : Lsm
 * @Date: 2022-05-23 17:55
 */
public class EmitLog {

    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        /***
         *声明一个exchange
         * 1.交换机名称
         * 2.交换机类型
         **/
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入要发送的消息");
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME,"",null,message.getBytes("UTF-8"));
            System.out.println("生产者发送消息：" + message);
        }

    }
}
