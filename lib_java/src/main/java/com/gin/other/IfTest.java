package com.gin.other;

public class IfTest {

    public static void main(String[] args) {
        
        if (new Object(){
            {
                System.out.print("hello ");
            }
        }==null){
            System.out.print("hello ");
        }else{
            System.out.print(" world");
        }
    }
}
