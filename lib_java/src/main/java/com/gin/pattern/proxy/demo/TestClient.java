package com.gin.pattern.proxy.demo;

/**
 * Created by wang.lichen on 2017/8/3.
 */

public class TestClient {


    public static void main(String[] args) {
        ILawsuit xiao = new Xiao();
        ILawsuit lawyer = new Lawyer(xiao);

        lawyer.submit();
        lawyer.burden();
        lawyer.defend();
        lawyer.finish();
    }
}
