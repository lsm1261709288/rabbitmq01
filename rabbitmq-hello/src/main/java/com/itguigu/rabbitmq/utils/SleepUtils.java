package com.itguigu.rabbitmq.utils;

/**
 * @ ClassName : SleepUtils
 * @ Description : 睡眠工具类
 * @ Author : lsm
 * @Date: 2022-05-21 16:43
 */
public class SleepUtils {
    public static void sleep(int second){
        try {
            Thread.sleep(1000*second);
        } catch (InterruptedException _ignored)
        {Thread.currentThread().interrupt();
        }
    }
}
