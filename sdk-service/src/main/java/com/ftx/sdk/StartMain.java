package com.ftx.sdk;

import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * 描述：主程序入口类
 *
 * @auth:xiaojun.yin
 * @createTime 2019-04-06 16:29
 */
public class StartMain {
    public static void main(String[] args) {
       try {
           FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext("classpath:server.xml");
           context.start();
       }catch (Exception e){
           e.printStackTrace();
           System.out.println("---------------------------------");
           System.out.println("service 启动失败!!");
           System.exit(-1);
           return;
       }
        System.out.println("---------------------------------");
        System.out.println("service 启动成功!!");
        synchronized (StartMain.class) {
            while (true) {
                try {
                    StartMain.class.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
