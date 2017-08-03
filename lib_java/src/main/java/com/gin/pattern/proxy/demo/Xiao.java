package com.gin.pattern.proxy.demo;

/**
 * Created by wang.lichen on 2017/8/3.
 */

public class Xiao implements ILawsuit {
    @Override
    public void submit() {
        System.out.println("拖欠工资，特此申请仲裁");
    }

    @Override
    public void burden() {

        System.out.println("劳动合同，银行流水");
    }

    @Override
    public void defend() {

        System.out.println("证据确凿，无话可说");
    }

    @Override
    public void finish() {

        System.out.println("诉讼成功，判决结算工资");
    }
}
