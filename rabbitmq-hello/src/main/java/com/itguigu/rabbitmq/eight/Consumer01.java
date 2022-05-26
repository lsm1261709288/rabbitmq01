package com.itguigu.rabbitmq.eight;

import com.itguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * @ ClassName : Consumer01
 * @ Description : 死信队列 消费者01
 * @ Author : lsm
 * @Date: 2022-05-24 15:46
 */
public class Consumer01 {
    //死信交换机和普通交换机
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    public static final String DEAD_EXCHANGE = "dead_exchange";
    //死信队列和普通队列
    public static final String NORMAL_QUEUE = "normal_queue";
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {
        //获取信道
        Channel channel = RabbitMqUtils.getChannel();
        //声明一个死信交换机和普通交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
        //声明一个普通队列
        Map<String,Object> arguments = new HashMap<>();
        //正常队列设置死信交换机
        arguments.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        //设置死信routerKey
        arguments.put("x-dead-letter-routing-key","lisi");
        //设置过期时间
//        arguments.put("x-message-ttl",10000);
        //设置正常队列长度的限制
//        arguments.put("x-max-length",6);

        channel.queueDeclare(NORMAL_QUEUE,false,false,false,arguments);

        //声明一个死信队列
        channel.queueDeclare(DEAD_QUEUE,false,false,false,null);

        //绑定普通队列和普通交换机
        channel.queueBind(NORMAL_QUEUE,NORMAL_EXCHANGE,"zhangsan");
        //绑定死信队列和死信交换机
        channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE,"lisi");

        //接受消息
        System.out.println("Consumer01等待接收消息 ......");
        DeliverCallback deliverCallback = (consumeTag, message) ->{
            String msg = new String(message.getBody(),"UTF-8");
            if (msg.equals("info5")) {
                System.out.println("Consumer01接收到的消息是：" + msg + "此消息是被C1拒绝的");
                //basicReject(long deliveryTag, boolean requeue)
                //消息接受的标记， 是否重新放回到队列中
                channel.basicReject(message.getEnvelope().getDeliveryTag(),false);
            } else {
                System.out.println("Consumer01接收到的消息是：" + msg);
                channel.basicAck(message.getEnvelope().getDeliveryTag(),false);

            }
//            System.out.println("Consumer01接受到消息：" + new String(message.getBody(),"UTF-8"));
        };
        //开启手动应答
        channel.basicConsume(NORMAL_QUEUE,false,deliverCallback,consumeTag ->{});
    }
}
