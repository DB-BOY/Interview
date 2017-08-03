package com.gin.pattern.proxy.dynamic;

import com.gin.pattern.proxy.demo.ILawsuit;
import com.gin.pattern.proxy.demo.Xiao;

import java.lang.reflect.Proxy;

/**
 * Created by wang.lichen on 2017/8/3.
 */

public class TestClient {


    public static void main(String[] args) {

        ILawsuit subject = new Xiao();//构造被代理对象

        DynamicProxy proxy = new DynamicProxy(subject); //动态代理

        ClassLoader loader = subject.getClass().getClassLoader(); //被代理对象的classloader

        //动态构造代理者
        ILawsuit lawyer = (ILawsuit) Proxy.newProxyInstance(loader, new Class[]{ILawsuit.class}, proxy);

        lawyer.submit();
        lawyer.burden();
        lawyer.submit();
        lawyer.finish();

    }
}
