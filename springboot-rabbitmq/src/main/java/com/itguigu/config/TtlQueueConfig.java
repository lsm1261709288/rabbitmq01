package com.itguigu.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @ ClassName : TtlQueueConfig
 * @ Description : TTL死信队列配置类
 * @ Author : lsm
 * @Date: 2022-05-25 10:16
 */
@Configuration
public class TtlQueueConfig {
    //普通交换机名称
    public static final String X_EXCHANGE = "X";
    //死信交换机名称
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";
    //普通队列名称
    public static final String QUEUE_A = "QA";
    public static final String QUEUE_B = "QB";
    //死信队列名称
    public static final String DEAD_LETTER_QUEUE = "QD";
    //普通队列名称
    public static final String QUEUE_C = "QC";


    //声明普通交换机  X_EXCHANGE
    @Bean("xExchange")
    public DirectExchange xExchange() {
        return new DirectExchange(X_EXCHANGE);
    }
    //声明死信交换机
    @Bean("yExchange")
    public DirectExchange yExchange() {
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }
    //声明普通队列
    @Bean("queueA")
    public Queue queueA() {
        Map<String,Object> args = new HashMap<>(3);
        //设置当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
        //设置当前队列死信RoutingKey
        args.put("x-dead-letter-routing-key","YD");
        //设置过期时间 TTL 单位ms
        args.put("x-message-ttl",10000);
        return QueueBuilder.durable(QUEUE_A).withArguments(args).build();
    }
    @Bean("queueB")
    public Queue queueB() {
        Map<String,Object> args = new HashMap<>(3);
        //设置当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
        //设置当前队列死信RoutingKey
        args.put("x-dead-letter-routing-key","YD");
        //设置过期时间 TTL 单位ms
        args.put("x-message-ttl",40000);
        return QueueBuilder.durable(QUEUE_B).withArguments(args).build();
    }
    @Bean("queueC")
    public Queue queueC() {
        Map<String,Object> args = new HashMap<>();
        //设置当前队列绑定死信交换机
        args.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
        //设置当前队列的routingKey
        args.put("x-dead-letter-routing-key","YD");

        return QueueBuilder.durable(QUEUE_C).withArguments(args).build();
    }

    //声明死信队列
    @Bean("queueD")
    public Queue queueD() {
        return new Queue(DEAD_LETTER_QUEUE);
    }

    //声明队列 A 绑定 X 交换机
    @Bean
    public Binding queueABindingX(@Qualifier("queueA") Queue queueA, @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueA).to(xExchange).with("XA");
    }
    //声明队列 B 绑定 X 交换机
    @Bean
    public Binding queueBBindingX(@Qualifier("queueB") Queue queueB, @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueB).to(xExchange).with("XB");
    }
    //声明队列 C 绑定 X 交换机
    @Bean
    public Binding queueCBindingX(@Qualifier("queueC") Queue queueC, @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueC).to(xExchange).with("XC");
    }
    //声明队列 D 绑定 Y 交换机
    @Bean
    public Binding queueDBindingY(@Qualifier("queueD") Queue queueD, @Qualifier("yExchange") DirectExchange yExchange) {
        return BindingBuilder.bind(queueD).to(yExchange).with("YD");
    }




}
