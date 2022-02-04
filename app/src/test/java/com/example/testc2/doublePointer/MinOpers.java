package com.example.testc2.doublePointer;


import org.junit.Test;

/**
 * You are given an integer array nums and an integer x. In one operation, you can either remove the leftmost or the rightmost element from the array nums and subtract its value from x. Note that this modifies the array for future operations.
 *
 * Return the minimum number of operations to reduce x to exactly 0 if it is possible, otherwise, return -1.
 *
 * -----------------------> check {@link LongestSubArray}
 */
public class MinOpers {

    @Test
    public void test1(){
        int a[] = {1,1,4,2,3};
        System.out.println(minOperations(a,5));
    }


    public int minOperations(int[] nums, int x) {
        int sum = 0;
        for(int i = 0;i<nums.length;i++){
            sum+=nums[i];
        }
        if(sum<x){
            return -1;
        }
        if(sum==x){
            return nums.length;
        }
        int i1 = longest(nums, sum - x);
        if(i1>0){
            return nums.length-i1;
        }
        return -1;
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
