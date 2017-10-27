package com.gin.sort;

public class SimpleSelectSSort {

    public static void main(String[] args) {
        int[] num = Constant.nums;

        Constant.print(num);
        System.out.println();
        System.out.println();

        simpleSelectSort(num);

        Constant.print(num);
    }

    public static void simpleSelectSort(int[] num) {
        int length = num.length;
        int tmp;
        for (int i = 0; i < length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < length; j++) {
                if (num[j] > num[min])
                    min = j;
            }
            if (min != i) {
                tmp = num[min];
                num[min] = num[i];
                num[i] = tmp;
            }
        }


    }
}
