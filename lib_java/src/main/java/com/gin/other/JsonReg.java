package com.gin.other;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wang.lichen on 2017/7/11.
 * 正则解析json
 */

public class JsonReg {

    public static void main(String[] args) {
        String json = "{name:\"laowang\",age: 18}";
        
        Pattern p  = Pattern.compile("\\w+:(\"\\w+\"|\\d*)");
        Matcher m = p.matcher(json);
        
        while (m.find()){
            String text = m.group();
            int dotPos = text.indexOf(":");
            String key = text.substring(0,dotPos);
            String value = text.substring(dotPos,text.length());
            System.out.println(key );
            System.out.println(value);
        }
    }
}
