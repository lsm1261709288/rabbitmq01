package com.itguigu.rabbitmq.senven;

import com.itguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @ ClassName : ReceiveLogsTopic02
 * @ Description : 消费者接受消息
 * @ Author : lsm
 * @Date: 2022-05-24 14:08
 */
public class ReceiveLogsTopic02 {
    //交换机名称
    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {
        //获取信道
        Channel channel = RabbitMqUtils.getChannel();
        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        /**
         * 声明一个队列
         * 1.队列名称
         * 2.队列里面的消息是否持久化（磁盘），默认情况下消息存储在内存中（非持久化）
         * 3.该队列是否只供一个消费者消费，是否进行消息共享，true可以多个消费者消费，false只能一个消费者消费
         * 4.是否自动删除，最后一个消费者断开连接后，该队列是否自动删除，true自动删除，false不自动删除
         * 5.其他参数：
         **/
        String queueName = "Q2";
        channel.queueDeclare(queueName,false,false,false,null);
        //绑定交换机
        channel.queueBind(queueName,EXCHANGE_NAME,"*.*.rabbit");
        channel.queueBind(queueName,EXCHANGE_NAME,"lazy.#");

        //消息的接受
        System.out.println("ReceiveLogsTopic02：等待接受消息......");
        DeliverCallback deliverCallback = (consumeTag, message) ->{
            System.out.println("ReceiveLogsTopic01接受到消息：" + " 绑定键："+message.getEnvelope().getRoutingKey()+",消息：" + message.getEnvelope().getExchange());
        };
        channel.basicConsume(queueName,true,deliverCallback,consumeTag -> {});
    }
}
