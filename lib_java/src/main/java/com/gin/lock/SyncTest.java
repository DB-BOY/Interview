package com.gin.lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wang.lichen on 2017/7/6.
 * 
 * synchronized锁 this 和  类名的区别
 * 
 * synchronized(class)
 * synchronized(this)
 * －＞线程各自获取monitor，不会有等待．
 * 
 * synchronized(this)
 * synchronized(this)
 * －＞如果不同线程监视同一个实例对象，就会等待，如果不同的实例，不会等待．
 * 
 * synchronized(class)
 * synchronized(class)
 * －＞如果不同线程监视同一个实例或者不同的实例对象，都会等待．
 * 
 * 
 * 
 * 
 */

public class SyncTest implements Runnable {

    private static boolean flag = true;

    //    private static synchronized void testSyncMethod() { // 注意static修改的同步方法，监视器=class
    //        for (int i = 0; i < 100; i++) {
    //            System.out.println("testSyncMethod:" + i);
    //        }
    //    }

    private String name ;

    public SyncTest(String name) {
        this.name = name;
    }

    private void testSyncMethod() {
        synchronized (this) {
            for (int i = 0; i < 100; i++) {
                System.out.println("--testSyncMethod:" + i+"   ==name： "+  name);
            }
        }
    }

    private void testSyncBlock() {

        synchronized (this) { // 注意this做为监视器．它与class分别是二个不同监视器．不会存在class被获取，this就要等的现象．这也是我以前关于监视器的一个误区．
            for (int i = 0; i < 100; i++) {
                System.out.println("testSyncBlock:" + i+"   ==name： " + name);
            }
        }

//         synchronized (SyncTest.class) { // 显示使用获取class做为监视器．它与static synchronized method隐式获取class监视器一样．
//         for (int i = 0; i < 100; i++) {
//         System.out.println("testSyncBlock:" + i);
//         }
//         }
    }

    @Override
    public void run() {

        // flag是static的变量．所以，不同的线程会执行不同的方法,只有这样才能看到不同的锁定效果．
        if (flag) {
            flag = false;
            testSyncMethod();
        } else {
            flag = true;
            testSyncBlock();
        }
    }

    public static void main(String[] args) {
        ExecutorService exec = Executors.newFixedThreadPool(2);
        SyncTest rt = new SyncTest("rt1");
        SyncTest rt1 = new SyncTest("rt2");
        exec.execute(rt);
        exec.execute(rt1);
        exec.shutdown();
    }
}
