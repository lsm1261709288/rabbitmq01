package com.itguigu.rabbitmq.three;

import com.itguigu.rabbitmq.utils.RabbitMqUtils;
import com.itguigu.rabbitmq.utils.SleepUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @ ClassName : work03
 * @ Description : 消费者  消息在手动应答时是不消失，重新放回消息队列中
 * @ Author : lsm
 * @Date: 2022-05-21 16:20
 */
public class work03 {
    //队列名称
    public static final String TASK_QUEUE_NAME = "ack_queue";
    //接受消息
    public static void main(String[] args) throws Exception {
        //调用工具类，获取信道
        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("C1 等待接收消息处理时间较短");

        //消息的接受
        DeliverCallback deliverCallback = (consumeTag, message) -> {
            //沉睡1s
            SleepUtils.sleep(1);
            System.out.println("接受到的消息  " + new String(message.getBody(),"UTF-8"));

            /**
             * 1.消息标记 Tag
             * 2.是否批量应答，true：批量应答 false：未批量应答，只处理当前消息
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };

        //设置不公平分发 默认0为轮训分发，1为不公平分发
//        int prefetchCount = 1;
        //预期值
        int prefetchCount = 2;
        channel.basicQos(prefetchCount);
        //采用手动应答的方式，当消息处理完成之后，消费者向队列发送应答消息
        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, deliverCallback, consumeTag ->{
            System.out.println("消息者取消消费接口回调的逻辑" + consumeTag);
        });

    }
}
