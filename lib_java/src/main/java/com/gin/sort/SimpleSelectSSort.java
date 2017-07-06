package com.gin.sort;

public class SimpleSelectSSort {

	public static void main(String[] args) {
			int[] num  = Constant.nums;
			int length = num.length;
			int tmp ;
			
			for(int i = 0;i<length -1 ;i++){
				int min = i;
				for(int j = i+1;j<length;j++){
					if(num[j]>num[min])
						min = j;
				}
				if(min !=i){
					tmp = num[min];
					num[min]= num[i];
					num[i]=tmp;
				}
			}
			
			
			for (int i = 0; i < length ; i++) {
				System.out.print(num[i]+" ");
				if(i%10==9)
					System.out.println();
			}
	}

}
