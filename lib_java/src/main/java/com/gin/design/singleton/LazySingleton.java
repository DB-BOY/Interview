package com.gin.design.singleton;

/**
 * Created by wang.lichen on 2017/7/5.
 *
 * 懒汉，常用的写法
 */

public class LazySingleton{
    private static LazySingleton singleton;
    private LazySingleton(){
    }
    public static LazySingleton getInstance(){
        if(singleton==null){
            singleton=new LazySingleton();
        }
        return singleton;
    }
}
