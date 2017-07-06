package com.gin.design.singleton;

/**
 * Created by wang.lichen on 2017/7/5.
 *五、 双重校验锁，在当前的内存模型中无效
 */
public class  LockSingleton{
        private volatile static LockSingleton singleton;
        private LockSingleton(){}

        //详见：http://www.ibm.com/developerworks/cn/java/j-dcl.html
        public static LockSingleton getInstance(){
            if(singleton==null){
                synchronized(LockSingleton.class){
                    if(singleton==null){
                        singleton=new LockSingleton();
                    }
                }
            }
            return singleton;
        }
    }
