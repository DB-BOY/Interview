package com.gin.search;

/**
 * Created by wang.lichen on 2017/8/6.
 */

public class BinarySearch {


    public static void main(String[] args) {
        int[] ar = {1, 3, 4, 5, 6, 7, 8, 9, 11};
        System.out.println(binarySearch(ar, 9));
    }

    public static int binarySearch(int[] arrays, int key) {
        int low = 0;
        int high = arrays.length - 1;
        int mid;

        while (low <= high) {
            mid = (low + high) / 2;
            if (key == arrays[mid]) {
                return mid;
            } else if (key < arrays[mid]) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        return -1;
    }

    /**
     * 递归实现
     *
     * @param arrays
     * @param key
     * @param begin
     * @param end
     *
     * @return
     */
    public static int binarySearch(int[] arrays, int key, int begin, int end) {

        int mid = (begin + end) / 2;
        if (key < arrays[begin] || key > arrays[end] || begin > end) {
            return -1;
        }


        if (key < arrays[mid]) {
            return binarySearch(arrays, key, begin, mid - 1);
        } else if (key > arrays[mid]) {
            return binarySearch(arrays, key, mid + 1, end);
        } else {
            return mid;
        }

    }

}
