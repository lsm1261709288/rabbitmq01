package com.itguigu.rabbitmq.six;

import com.itguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @ ClassName : EmitLogDirect
 * @ Description : 生产者 发送消息
 * @ Author : lsm
 * @Date: 2022-05-24 11:01
 */
public class EmitLogDirect {
    //交换机名称
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        //获取信道
        Channel channel = RabbitMqUtils.getChannel();

        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //创建多个bindingKey
//        Map<String, String> bindingKeyMap = new HashMap<>();
//        bindingKeyMap.put("info","普通info信息");
//        bindingKeyMap.put("warning","警告warning信息");
//        bindingKeyMap.put("error","错误error信息");
//        bindingKeyMap.put("debug","调试 debug 信息");
//        for (Map.Entry<String, String> bindingKeyEntry : bindingKeyMap.entrySet()){
//            String bindingKey = bindingKeyEntry.getKey();
//            String message = bindingKeyEntry.getValue();


        //发送消息
        System.out.println("请输入要发送的消息：");
        //手动录入方式
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            /***
             *发送一个消费
             *1.发送到那个交换机
             * 2.路由的key值是哪个，本次是队列名称
             * 3.其他参数信息
             * 4.发送消息的消息体
             **/
            channel.basicPublish(EXCHANGE_NAME, "error", null, message.getBytes("UTF-8"));
            System.out.println("生产者发出消息:" + message);
//        }
        }
    }
}
