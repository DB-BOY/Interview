package com.gin.pattern.proxy.demo;

/**
 * Created by wang.lichen on 2017/8/3.
 * 代理律师
 */

public class Lawyer implements ILawsuit {
    private ILawsuit iLawsuit;

    public Lawyer(ILawsuit iLawsuit) {
        this.iLawsuit = iLawsuit;
    }

    @Override
    public void submit() {
        iLawsuit.submit();
    }

    @Override
    public void burden() {
        iLawsuit.burden();
    }

    @Override
    public void defend() {
        iLawsuit.defend();
    }

    @Override
    public void finish() {
        iLawsuit.finish();
    }
}
