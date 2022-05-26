package com.itguigu.rabbitmq.eight;

import com.itguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

/**
 * @ ClassName : Producer
 * @ Description : 死信队列生产者 发送消息
 * @ Author : lsm
 * @Date: 2022-05-24 16:41
 */
public class Producer {
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);

        //设置TTL过期时间 单位是ms
//        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration("10000").build();

        for(int i = 0; i < 10; i++) {
            String message = "info" + i;
//            channel.basicPublish(NORMAL_EXCHANGE,"zhangsan",properties,message.getBytes());
              channel.basicPublish(NORMAL_EXCHANGE,"zhangsan",null,message.getBytes());

            System.out.println("生产者发送消息：" + message);
        }



    }
}
