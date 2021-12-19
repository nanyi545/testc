package com.example.testc2;

import org.junit.Test;

import java.util.Arrays;

public class TestSort {

    @Test
    /**
     *  test sort time ...
     */
    public void test1(){
        int size = 1000000;
        int[] arr = new int[size];
        for (int i=0;i<size;i++){
            arr[i] = (int) (Math.random()*size);
        }
        long t1 = System.currentTimeMillis();
        Arrays.sort(arr);
        long t2 = System.currentTimeMillis();
        System.out.println("sort time:"+(t2-t1));

        long total= 0;
        for (int i=0;i<size;i++){
            total+= arr[i];
        }
        t1 = System.currentTimeMillis();
        System.out.println("loop time:"+(t1-t2));


        int psize= 1000;
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<psize;i++){
            sb.append(arr[i]);
            sb.append("-");
        }
        System.out.println("arr:"+(sb.toString()));
    }

    //  https://leetcode-cn.com/problems/partition-equal-subset-sum/solution/fen-ge-deng-he-zi-ji-by-leetcode-solution/  ???

    @Test
    public void test2(){

        int size = 1000000;
        int[] arr = new int[size];
        for (int i=0;i<size;i++){
            arr[i] = (int) (Math.random()*size*100);
        }

        long t1 = System.currentTimeMillis();
        long t2;
        Arrays.sort(arr);
        t2 = System.currentTimeMillis();
        System.out.println("point1:"+(t2-t1));

        int m = 5000;
        double target_average = 10000*100;
        int target_sum = (int) (m * target_average);

        int targetIndex = getIndex(arr,target_average);
        int i1 = arr[targetIndex];
        System.out.println("targetIndex:"+targetIndex+"  v:"+i1);
        int range[] = {targetIndex,targetIndex};

        double diff = i1 - target_average;
        System.out.println("range:["+range[0]+","+range[1]+"]   diff:"+diff/target_sum);

        t2 = System.currentTimeMillis();
        System.out.println("point2:"+(t2-t1));

        for (int i=1;i<m;i++){
            boolean b1 = (range[0]<=0);
            if(diff<0 || b1){
                range[1]++;
                diff+=( arr[range[1]] - target_average );
            } else {
                range[0]--;
                diff+=( arr[range[0]] - target_average );
            }
            System.out.println("range:["+range[0]+","+range[1]+"]   diff:"+diff/target_sum);
        }

        System.out.println("range:["+range[0]+","+range[1]+"]   diff:"+ (diff/target_sum) );

        t2 = System.currentTimeMillis();
        System.out.println("p3:"+(t2-t1));
    }

    public int getIndex(int[] arr, double target ){
        for (int i=1;i<arr.length;i++){
            if(target > arr[i-1] && target<=arr[i]  ){
                return i;
            }
        }
        return -1;
    }



}
