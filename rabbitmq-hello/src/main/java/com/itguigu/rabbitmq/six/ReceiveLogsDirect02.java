package com.itguigu.rabbitmq.six;

import com.itguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @ ClassName : ReceiveLogsDirect01
 * @ Description : 消费者接受消息
 * @ Author : lsm
 * @Date: 2022-05-24 10:49
 */
public class ReceiveLogsDirect02 {
    //交换机名称
    public static final String EXCHANGE_NAME = "direct_logs";

    //接受消息
    public static void main(String[] args) throws Exception {
        //获取信道
        Channel channel = RabbitMqUtils.getChannel();
        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //声明一个队列
        String queueName = "disk";
        channel.queueDeclare(queueName,false,false,false,null);
        //交换机绑定
        channel.queueBind(queueName,EXCHANGE_NAME,"error");

        //消息的接受
        System.out.println("ReceiveLogsDirect02：等待接收消息,把接收到的消息打印在屏幕........... ");
        //消息的接受
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLogs02接受到的消息：" + new String(message.getBody(),"UTF-8"));
        };
        channel.basicConsume(queueName,true,deliverCallback,consumerTag ->{});

    }
}
