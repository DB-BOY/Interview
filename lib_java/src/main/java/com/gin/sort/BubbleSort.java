package com.gin.sort;

public class BubbleSort {

	public static void main(String[] args) {

		int[] num = Constant.nums;

        Constant.print(num);
        System.out.println("\n---start: " + System.nanoTime());
        bubbleSort(num);
        System.out.println("---end  : " + System.nanoTime());

        System.out.println("\n");
        Constant.print(num);
    }

    public static void bubbleSort(int[] num) {
        int length = num.length;
		int tmp;
		for (int i = 0; i < length - 1; i++) {
			for (int j = 0; j < length - i - 1; j++) {
				if (num[j] < num[j + 1]) {
					tmp = num[j];
					num[j] = num[j + 1];
					num[j + 1] = tmp;
				}
			}
		}
	}

}
