package com.itguigu.rabbitmq.senven;

import com.itguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @ ClassName : ReceiveLogsTopic01
 * @ Description : 消费者接受消息
 * @ Author : lsm
 * @Date: 2022-05-24 13:52
 */
public class ReceiveLogsTopic01 {
    //交换机名称
    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {
        //获取信道
        Channel channel = RabbitMqUtils.getChannel();

        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        //声明一个队列
        String queueName = "Q1";
        channel.queueDeclare(queueName,false,false,false,null);
        //绑定交换机
        channel.queueBind(queueName,EXCHANGE_NAME,"*.orange.*");

        //消息的接受
        System.out.println("ReceiveLogsTopic01：等待接受消息......");
        DeliverCallback deliverCallback = (consumeTag, message) ->{
//            System.out.println("ReceiveLogsTopic01接受到消息：" + new String(message.getBody(),"UTF-8"));
            System.out.println("ReceiveLogsTopic01接受到消息：" + " 绑定键："+message.getEnvelope().getRoutingKey()+",消息：" + message.getBody());
        };
        channel.basicConsume(queueName,true,deliverCallback,consumeTag -> {});
    }
}
