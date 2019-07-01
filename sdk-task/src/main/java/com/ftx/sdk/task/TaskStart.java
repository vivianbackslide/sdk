package com.ftx.sdk.task;

import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * 描述：
 *
 * @auth:xiaojun.yin
 * @createTime 2019-05-06 09:10
 */
public class TaskStart {
    public static void main(String[] args) {
        try {
            FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext("classpath:application.xml");
            context.start();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("---------------------------------");
            System.out.println("task 启动失败!!");
            System.exit(-1);
            return;
        }
        System.out.println("---------------------------------");
        System.out.println("task 启动成功!!");
        synchronized (TaskStart.class) {
            while (true) {
                try {
                    TaskStart.class.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
