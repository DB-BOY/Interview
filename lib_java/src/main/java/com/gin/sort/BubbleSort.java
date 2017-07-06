package com.gin.sort;

public class BubbleSort {

	public static void main(String[] args) {

		int[] num = Constant.nums;
		int length = num.length;
		for (int i = 0; i < length ; i++) {
			System.out.print(num[i]+" ");
			if(i%10==9)
				System.out.println();
		}
		int tmp;

		System.out.println("\n---start: "+System.nanoTime());
		for (int i = 0; i < length - 1; i++) {
			for (int j = 0; j < length - i - 1; j++) {
				if (num[j] < num[j + 1]) {
					tmp = num[j];
					num[j] = num[j + 1];
					num[j + 1] = tmp;
				}
			}
		}
		System.out.println("---end  : "+System.nanoTime());

		System.out.println("\n");
		for (int i = 0; i < length ; i++) {
			System.out.print(num[i]+" ");
			if(i%10==9)
				System.out.println();
		}
	}

}
