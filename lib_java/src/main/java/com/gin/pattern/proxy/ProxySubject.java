package com.gin.pattern.proxy;

/**
 * Created by wang.lichen on 2017/8/3.
 * <p>
 * 代理类
 */

public class ProxySubject extends Subject {
    private RealSubject realSubject;

    public ProxySubject(RealSubject realSubject) {
        this.realSubject = realSubject;
    }

    @Override
    public void visit() {
        System.out.println(" proxy ");
        realSubject.visit();
    }
}
