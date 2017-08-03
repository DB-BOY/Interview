package com.gin.thread;

import com.gin.pattern.singleton.InternalSingleton;

/**
 * Created by wang.lichen on 2017/7/5.
 * 多线程调用单例类
 */

public class Threads2Singleton {

    public static void main(String[] args) {
        Threads2Singleton singleton = new  Threads2Singleton();
        singleton.test();
    }
    public void test(){
        MyThread m1 = new MyThread();
        MyThread m2 = new MyThread();
        MyThread m3 = new MyThread();
        
        Thread t1 = new Thread(m1);
        Thread t2 = new Thread(m2);
        Thread t3 = new Thread(m3);
        t1.start();
        t2.start();
        t3.start();
        
    }
    
    class MyThread implements Runnable{

        @Override
        public void run() {
            InternalSingleton singleton = InternalSingleton.getInstance();
            System.out.println(singleton);
            singleton.doSomething();
            System.out.println(Thread.currentThread().getName());
        }
    }
}
