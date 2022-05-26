package com.itguigu.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @ ClassName : RabbitMqUtils
 * @ Description : 创建工厂，连接工厂的工具类
 * @ Author : lsm
 * @Date: 2022-05-21 13:54
 */
public class RabbitMqUtils {
    //得到一个连接的 channel
    public static Channel getChannel() throws Exception{
        //1创建立连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //工厂IP连接rabbitmq队列
        factory.setHost("10.17.31.223");
        //用户名
        factory.setUsername("guest");
        //密码
        factory.setPassword("guest");
        //2.创建连接
        Connection connection = factory.newConnection();
        //3.获取信道
        Channel channel = connection.createChannel();
        return channel;
    }
}
