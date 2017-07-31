package com.gin.other;

/**
 * Created by wang.lichen on 2017/7/28.
 */

public class Inter728 {


    public static void main(String[] args) {
        String str = "asdfasqwersdfaasdfadfasdfasdfasdfasdfasdfasfasdf";
        char[] chars = new char[250];
        char[] ch = str.toCharArray();
        int index;

        for (int i = 0, length = ch.length; i < length; i++) {
            index = ch[i];
            if (chars[index] == 0) {
                chars[index] = 1;
            }
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0, length = chars.length; i < length; i++) {
            if (chars[i] == 1) {
                sb.append((char) i);
            }
        }
        System.out.println(sb.toString());

    }
}
