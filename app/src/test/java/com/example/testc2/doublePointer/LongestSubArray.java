package com.example.testc2.doublePointer;

import org.junit.Test;


/**
 * find the longest sub array with target sum ...
 *
 */
public class LongestSubArray {

    @Test
    public void test1(){
        int a[] = {1,1,4,2,3};
        System.out.println(longest(a,5));
    }


    @Test
    public void test2(){
        int a[] = {1,1,2,3,4,1,1,1,2,3,4};
        System.out.println(longest(a,11));
    }


    @Test
    public void test3(){
        int a[] = {1,1,20,3,4};
        System.out.println(longest(a,20));
    }


    public int longest(int[] nums, int x) {
        int l = 0;
        int r = -1;
        int max = -1;
        int t = 0;
        while(true){
            while(t<x){
                r++;
                t+=nums[r];
                if(r==nums.length-1){
                    break;
                }
            }
            if(t==x){
                int max_temp = r-l+1;
                if(max_temp>max){
                    max=max_temp;
                }
            }
            while (t>=x){

                /**
                 *         int a[] = {1,1,20,3,4};
                 *         System.out.println(longest(a,20));
                 */
                if(t==x){
                    int max_temp = r-l+1;
                    if(max_temp>max){
                        max=max_temp;
                    }
                }
                t-=nums[l];
                l++;
            }

            if(r==nums.length-1){
                break;
            }
        }
        return max;
    }


}
