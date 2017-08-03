package com.gin.other;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang.lichen on 2017/8/2.
 */

public class GenericTest {


    public static void main(String[] args) {


    }

    private void test() {

        Eatable eatable = new Eatable();
        Fruit fruit = new Fruit();
        Apple apple = new Apple();
        RedApple redApple = new RedApple();
        SmallRedApple smallRedApple = new SmallRedApple();


        List<? extends Apple> exList;

        //     exList= new ArrayList<Fruit>();//
        exList = new ArrayList<RedApple>();
        exList = new ArrayList<Apple>();

        //    exList.add(fruit);
        //    exList.add(apple);
        //    exList.add(smallRedApple);


        List<? super Apple> suList;
        suList = new ArrayList<Fruit>();
        suList = new ArrayList<Apple>();
        //    suList= new ArrayList<RedApple>();//

        suList.add(apple);
        suList.add(redApple);

    }

    class Eatable {}

    class Fruit extends Eatable {}

    class Apple extends Fruit {}

    class RedApple extends Apple {}

    class SmallRedApple extends RedApple {}


    class MyEatable<T extends Fruit> {


    }
}






