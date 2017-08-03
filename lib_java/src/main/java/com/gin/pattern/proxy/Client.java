package com.gin.pattern.proxy;

/**
 * Created by wang.lichen on 2017/8/3.
 */

public class Client {

    public static void main(String[] args) {
        RealSubject real = new RealSubject();

        ProxySubject proxy = new ProxySubject(real);
        proxy.visit();
    }
}
