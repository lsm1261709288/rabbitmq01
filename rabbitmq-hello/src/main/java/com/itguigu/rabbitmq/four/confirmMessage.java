package com.itguigu.rabbitmq.four;

import com.itguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @ ClassName : confirmMessage
 * @ Description : 发布确认模式
 * 使用的时间 比较哪种确认方式是最好的
 *         //1.单个确认    发布1000个单独确认消息，耗时2036ms
 *         //2.批量确认    发布1000个批量确认消息，耗时304ms
 *         //异步批量确认  发布1000个批量异步确认消息，耗时235ms
 * @ Author : lsm
 * @Date: 2022-05-21 18:28
 */
public class confirmMessage {
    public static final int MessageCount = 1000;

    public static void main(String[] args) throws Exception {
        //1.单个确认
        //confirmMessage.publishMessageIndividually();
        //2.批量确认
//        confirmMessage.publishMessageBatch();
        //3.异步批量确认
        confirmMessage.publishMessageAsync();
    }
    //1.单个确认
    public static void publishMessageIndividually() throws Exception {
        //获取信道
        Channel channel = RabbitMqUtils.getChannel();
        //声明的队列，  随机生成一个队列
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,false,false,false,null);
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();
        for (int i = 0; i < MessageCount; i++) {
            String message = i + "";
            //批量发送消息
            channel.basicPublish("",queueName,null,message.getBytes());
            //确认消息，发布一条，确认一条
            boolean flag = channel.waitForConfirms();
            if(flag) {
                System.out.println("消息发送成功");
            }
        }
        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + MessageCount + "个单独确认消息，需要" + (end - begin) + "ms");
    }
    //2.批量确认
    public static void publishMessageBatch() throws Exception {
        //获取信道
        Channel channel = RabbitMqUtils.getChannel();
        //声明队列
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,false,false,false,null);
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();
        //批量确认消息的大小
        int batchSize = 100;
        for (int i = 0; i < MessageCount; i++) {
            String message = i + "";
            channel.basicPublish("",queueName,null,message.getBytes());
            //条件判断
            if (i % batchSize == 0) {
                //消息确认
                channel.waitForConfirms();
            }
        }
        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + MessageCount + "个批量确认消息，耗时" + (end - begin) + "ms");
    }
    //3.异步批量确认
    public static void publishMessageAsync() throws Exception {
        //获取信道
        Channel channel = RabbitMqUtils.getChannel();
        //声明队列
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,false,false,false,null);
        //开启确认发布
        channel.confirmSelect();

        /**线程安全有序的一个哈希表 适用于高并发的情况
         * 1.轻松的将序号与消息关联
         * 2.轻松批量删除条目 只要给到序号
         * 3.支持高并发 （多线程）
         */
        ConcurrentSkipListMap<Long, String> outStandingConfirms = new ConcurrentSkipListMap();

        //消息确认成功的回调函数
        ConfirmCallback ackCallback = (sequenceNumber, multiple) -> {
            if(multiple) {
                //2.删除已经确认的消息，剩下的就是未确认的消息
                ConcurrentNavigableMap<Long, String> confirmed = outStandingConfirms.headMap(sequenceNumber);
                confirmed.clear();
//                System.out.println("确认的批量消息" + sequenceNumber);
            } else {
                outStandingConfirms.remove(sequenceNumber);
//                System.out.println("确认的单个消息" + sequenceNumber);
            }
            System.out.println("确认的消息" + sequenceNumber);
        };
        //消息确认失败的回调函数
        ConfirmCallback nackCallback = (sequenceNumber, multiple) -> {
            System.out.println("未确认的消息" + sequenceNumber);
        };
        //添加一个异步确认的监听器，监听哪些消息成功了，哪些消息失败了
        /**
         *1.消息的标记 Tag  序列号
         * 2.是否为批量确认
         */

        channel.addConfirmListener(ackCallback, nackCallback); //异步通知

        //开始时间
        long begin = System.currentTimeMillis();
        for (int i = 0; i < MessageCount; i++) {
            String message = "消息" + i;
            channel.basicPublish("",queueName,null,message.getBytes());
            //1.此处记录下所有要发送的消息 消息的总和
            outStandingConfirms.put(channel.getNextPublishSeqNo(),message);
        }
        //批量发送异步确认消息
        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + MessageCount + "个批量异步确认消息，耗时" + (end - begin) + "ms");
    }
}
