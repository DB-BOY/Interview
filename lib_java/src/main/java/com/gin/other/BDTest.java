package com.gin.other;

/**
 * Created by wang.lichen on 2017/7/28.
 */

public class BDTest {


    public static void main(String[] args) {
        BDTest bdTest = new BDTest();
        Person p1 = new Person("bob", 1);
        System.out.println(p1.toString());
        bdTest.test1(p1);
        System.out.println("result: " + p1);

        Person p2 = new Person("bob2", 22);
        bdTest.test2(p2);
        System.out.println("result2: " + p2);

        String str = new String("3333");

        bdTest.testStr(str);
        System.out.println(str);
    }

    private void testStr(String str) {
        str = "1111111111111";
    }

    private void test1(Person person) {
        person.name = "tom";
        person.age = 2;
    }

    private void test2(Person person) {
        person = new Person("bb", 33);
    }


}

class Person {
    public String name;
    public int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "name: " + name + " age: " + age;
    }
}
