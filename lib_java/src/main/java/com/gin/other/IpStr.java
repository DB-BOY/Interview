package com.gin.other;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wang.lichen on 2017/7/6.
 */

public class IpStr {


    public static void main(String[] args) {
        String pattern = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                +"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                +"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                +"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);

        String ip = "1.12.12.12";
        Matcher matcher = r.matcher(ip);
        if (matcher.find( )) {
            System.out.println("Found value: " + matcher.group(0) );
            System.out.println("Found value: " + matcher.group(1) );
            System.out.println("Found value: " + matcher.group(2) );
            System.out.println("Found value: " + matcher.group(3) );
        } else {
            System.out.println("NO MATCH");
        }


        ip = "127.0.0.1";
        boolean isMatch =Pattern.matches(pattern, ip);
        System.out.println(" ismatch : "+ isMatch);
    }
}
