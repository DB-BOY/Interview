package com.gin.thread;

/**
 * Created by wang.lichen on 2017/7/11.
 * 死锁
 * 多个线程抢夺一个资源
 * 
 * 1. 互斥
 * 2. 不可抢占资源
 * 3. 占有且申请条件
 * 4. 循环等待条件
 * 解决： 加锁顺序，死锁检索
 */

public class DeadLockTest {
    
    static class MyTask implements Runnable{
        Object object1 = "obj1";
        Object object2 = "obj2";
        int flag ;
        
        @Override 
        public void run() {
            if(flag ==1){
                synchronized (object1){
                    System.out.println("locking "+ object1);
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    synchronized (object2){
                        System.out.println("obj1 -->  obj2 ");

                    }
                }
            }
            else if (flag ==2){
                synchronized (object2){
                    System.out.println("locking "+ object2);
                    synchronized (object1){
                        System.out.println("obj2  -->  obj1 ");

                    }
                }
            }
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }
    }
    
    public static void main(String[] args) {
        MyTask myTask = new MyTask();
        myTask.setFlag(1);
        Thread t1 = new Thread(myTask);
        t1.start();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        MyTask myTask1 = new MyTask();
        myTask1.setFlag(2);
        Thread t2 = new Thread(myTask1);
        t2.start();
    }
}
