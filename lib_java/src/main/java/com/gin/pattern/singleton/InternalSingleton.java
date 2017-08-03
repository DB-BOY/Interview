package com.gin.pattern.singleton;

/**
 * Created by wang.lichen on 2017/7/5.
 * 静态内部类 
 * 
 * 优点：加载时不会初始化静态变量INSTANCE，因为没有主动使用，达到Lazy loading
 */

public class InternalSingleton{
    private InternalSingleton(){}

    public static InternalSingleton getInstance(){
        return SingletonHolder.INSTANCE;
    }

    public void doSomething(){
        System.out.println("doSomething");
    }

    private static class SingletonHolder {
        private final static InternalSingleton INSTANCE = new InternalSingleton();
    }
}
