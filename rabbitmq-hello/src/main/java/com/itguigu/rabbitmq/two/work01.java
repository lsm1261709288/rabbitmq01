package com.itguigu.rabbitmq.two;

import com.itguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.*;

/**
 * @ ClassName : work01 工作队列（任务队列）
 * @ Description : 工作线程 消费者接受消息
 * @ Author : lsm
 * @Date: 2022-05-21 11:58
 */
public class work01 {
    //队列名称
    public static final String QUEUE_NAME = "hello";
    //接受消息
    public static void main(String[] args) throws Exception {
        //调用工具类中的静态方法：类名.方法名
        Channel channel = RabbitMqUtils.getChannel();

        //消息的接受
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("接受到的消息" + new String(message.getBody()));
        };

        //消息接受被取消时，执行下面的内容
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println(consumerTag + "消息者取消消费接口回调的逻辑");
        };
        System.out.println("C2等待消息接受......");
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
