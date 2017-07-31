package com.gin.other;

/**
 * Created by wang.lichen on 2017/7/28.
 */

public class QcgTest {

    static Toy st1 = new Toy(111);
    static Toy st2 = new Toy(222);
    static Toy st3 = new Toy(333);
    Toy t1 = new Toy(1);
    Toy t2 = new Toy(2);
    Toy t3 = new Toy(3);

    QcgTest() {
        System.out.println("Boy()");
        t3 = new Toy(33);
        st3 = new Toy(3333);
    }

    public static void main(String[] args) {
        QcgTest b = new QcgTest();
        b.f();
    }

    void f() {
        System.out.println("f()");
    }


}

class Toy {
    public Toy(int code) {
        System.out.println("toy: " + code);
    }
}
