package com.gin.leetcode;

import java.util.ArrayList;
import java.util.List;

public class FizzBuzz412 {
//	Write a program that outputs the string representation of numbers from 1 to n.

//	But for multiples of three it should output “Fizz” instead of the number
//	and for the multiples of five output “Buzz”. 
//	For numbers which are multiples of both three and five output “FizzBuzz”. 
	
	
	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
		System.out.println(fizzBuzz(15).toString());
		System.out.println(System.currentTimeMillis());
		System.out.println(fizzBuzz2(15).toString());
		System.out.println(System.currentTimeMillis());
	}
	public static List<String> fizzBuzz2(int n) {
		List<String> result = new ArrayList<String>();
		for(int i = 1;i<=n;i++){
			String out = "";
			 if(i%3==0){
				 out+="Fizz";
			}
			 if(i%5==0){
				 out +="Buzz";
			}
			 if(out.length() ==0){
				 out =""+i;
			}
			 result.add(out);
		}
		return result;
    }
	public static List<String> fizzBuzz(int n) {
		List<String> result = new ArrayList<String>();
		for(int i = 1;i<=n;i++){
			if(i%15==0){
				result.add("FizzBuzz");
			}else if(i%3==0){
				result.add("Fizz");
			}else if(i%5==0){
				result.add("Buzz");
			}else{
				result.add(""+i);
			}
		}
		return result;
    }
}
