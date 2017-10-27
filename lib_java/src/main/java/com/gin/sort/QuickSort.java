package com.gin.sort;

/**
 * Created by wang.lichen on 2017/8/8.
 */

public class QuickSort {

    public static void main(String[] args) {


        int[] num = Constant.nums;

        Constant.print(num);
        System.out.println();
        System.out.println();
        quickSort(num);
        Constant.print(num);
    }

    public static void quickSort(int[] num) {
        if (num != null) {
            quickSort(num, 0, num.length - 1);
        }
    }

    private static void quickSort(int[] num, int start, int end) {
        if (start >= end || num == null) {
            return;
        }
        int i = start, j = end;
        int tmp = num[start];
        if (i < j) {
            while (i < j) {
                while (i < j && num[i] > tmp)
                    i++;
                if (i < j)
                    num[j--] = num[i];
                while (i < j && num[j] < tmp)
                    j--;
                if (i < j)
                    num[i++] = num[j];
            }
            num[i] = tmp;
            quickSort(num, start, i - 1);
            quickSort(num, i + 1, end);
        }


    }

    private static int partition(int[] num, int start, int end) {
        int last = num[end];
        int i = start - 1;
        for (int j = start; j <= end - 1; j++) {
            if (num[j] <= last) {
                i++;
                if (i != j) {
                    num[i] = num[i] ^ num[j];
                    num[j] = num[i] ^ num[j];
                    num[i] = num[i] ^ num[j];
                }
            }
        }
        if ((i + 1) != end) {
            num[i + 1] = num[i + 1] ^ num[end];
            num[end] = num[i + 1] ^ num[end];
            num[i + 1] = num[i + 1] ^ num[end];
        }
        return i + 1;
    }
}
