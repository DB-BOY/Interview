package com.gin.pattern.singleton;

/**
 * Created by wang.lichen on 2017/7/5.
 * 枚举，《Effective Java》作者推荐使用的方法，
 * 优点：不仅能避免多线程同步问题，而且还能防止反序列化重新创建新的对象
 */

public enum EnumSingleton{
    INSTANCE;
    public void doSomeThing(){

        System.out.println("doSomething()");
    }
}
