package com.gin.pattern.proxy;

/**
 * Created by wang.lichen on 2017/8/3.
 * <p>
 * 真实实现类
 */

public class RealSubject extends Subject {
    @Override
    public void visit() {
        System.out.println("real subject");
    }
}
