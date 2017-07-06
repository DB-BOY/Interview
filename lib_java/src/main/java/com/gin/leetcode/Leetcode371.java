package com.gin.leetcode;

public class Leetcode371 {

	public static void main(String[] args) {
		int a=-1,b=1;
		
//		 while(b != 0){  
//	            int c =(a & b)<< 1;  
//	            a = a ^ b;  
//	            b = c ;  
//	        }
//		sum(a,b);
			System.out.println(System.currentTimeMillis());
			System.out.println(sum(a,b));
			System.out.println(System.currentTimeMillis());
	}
	static int sum(int a , int b){
	    if(b==0) return 0;
	    return sum(a^b, (a&b)<<1);
	}
}
