package com.gin.sort;

public class Constant {
	
	public static int[] nums={
			10,11,25,1,4,7,8,55,47,21247,44510,85,62,18,314,862,145,645,
			151,121,5,21,531,451,251,52,451,635,21,154,21,51,251,2,124,2,124,2,15,
			51,1021,21,51,2,410,35,165,123,140,51,583,456,856,2143,1,8,1,0,
			89,84,874,3,32,415,8121,471,255,74,851,52,6,126,66
	};

    public static void print(int[] num) {
        for (int i = 0, length = num.length; i < length; i++) {
            System.out.print(num[i] + " ");
            if (i % 10 == 9)
                System.out.println();
        }
    }

}
