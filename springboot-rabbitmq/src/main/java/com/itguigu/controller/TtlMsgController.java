package com.itguigu.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.Date;

/**
 * @ ClassName : TtlMsgController
 * @ Description : 发起一个请求 http://localhost:8080/ttl/sendMsg/嘻嘻嘻
 * @ Author : lsm
 * @Date: 2022-05-25 11:32
 */
@RestController
@Slf4j
@RequestMapping("/ttl")
public class TtlMsgController {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    //开始发消息
    @RequestMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable String message) {
        log.info("当前时间{}，发送一条信息给两个ttl队列{}", new Date(), message);
        rabbitTemplate.convertAndSend("X","XA","消息来自ttl为10s的队列"+message);
        rabbitTemplate.convertAndSend("X","XB","消息来自ttl为40s的队列"+message);
    }
    //开始发消息
    @GetMapping("/sendExpirationMsg/{message}/{ttlTime}")
    public void sendMsg(@PathVariable String message, @PathVariable String ttlTime) {
        log.info("当前时间：{},发送一条时长{}毫秒 TTL 信息给队列 C:{}",new Date(),ttlTime,message);
        rabbitTemplate.convertAndSend("X","XC",message,correlationData -> {
            correlationData.getMessageProperties().setExpiration(ttlTime);
            return correlationData;
        });

    }
}
