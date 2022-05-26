package com.itguigu.rabbitmq.five;

import com.itguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @ ClassName : ReceiveLogs01
 * @ Description : 接受消息
 * @ Author : lsm
 * @Date: 2022-05-23 17:33
 */
public class ReceiveLogs01 {

    //交换机名称
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        //获取信道
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        /**
        * 声明一个临时队列  队列的名称是随机的
         * 当消费者断开与该队列的连接时，队列自动删除
         */
        String queueName = channel.queueDeclare().getQueue();
        //将声明的临时队列绑定到交换机 其中 routingkey(也称之为 binding key)为空字符串
        channel.queueBind(queueName,EXCHANGE_NAME,"");
        System.out.println("ReceiveLogs01等待接收消息,把接收到的消息打印在屏幕........... ");
        //消息的接受
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLogs01接受到的消息：" + new String(message.getBody(),"UTF-8"));
        };
        channel.basicConsume(queueName,true,deliverCallback,consumerTag ->{});
    }
}
