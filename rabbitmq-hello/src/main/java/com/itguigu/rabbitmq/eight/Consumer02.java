package com.itguigu.rabbitmq.eight;

import com.itguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * @ ClassName : Consumer02
 * @ Description : 死信队列 消费者02
 * @ Author : lsm
 * @Date: 2022-05-24 16:32
 */
public class Consumer02 {

    //死信队列名称
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {
        //获取信道
        Channel channel = RabbitMqUtils.getChannel();

        //接受消息
        System.out.println("Consumer02等待接收消息 ......");
        DeliverCallback deliverCallback = (consumeTag, message) ->{
            System.out.println("Consumer02接受到消息：" + new String(message.getBody(),"UTF-8"));
        };
        channel.basicConsume(DEAD_QUEUE,true,deliverCallback,consumeTag ->{});
    }
}
