package com.gin.hash;

/**
 * Created by wang.lichen on 2017/7/11.
 */

public class HashTest {
    int hashcode = 999; 

    public static void main(String[] args) {
        
    }

    @Override
    public int hashCode() {
        return hashcode;
    }

    @Override
    public boolean equals(Object o) {
        
        return super.equals(o);
    }
}
