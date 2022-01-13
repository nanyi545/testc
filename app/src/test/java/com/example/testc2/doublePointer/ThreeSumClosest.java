package com.example.testc2.doublePointer;

import org.junit.Test;

import java.util.Arrays;


/**
 * Given an integer array nums of length n and an integer target, find three integers in nums such that the sum is closest to target.
 *
 * Return the sum of the three integers.
 *
 https://leetcode-cn.com/problems/3sum-closest


 */
public class ThreeSumClosest {


    @Test
    public void test1(){
        int[] arr1 = {-1,2,1,-4};
        System.out.println(threeSumClosest(arr1,1));
    }


    public int threeSumClosest(int[] nums, int target) {
        Arrays.sort(nums);
        int size=nums.length;
        int diff = Integer.MAX_VALUE;
        int v = 0;
        for (int i=0;i<size-2;i++){
            int start = i+1;
            int end = size-1;
            while(start<end){
                int sum = nums[i]+nums[start]+nums[end];
                int d = Math.abs(sum - target);
                if(d<diff){
                    diff = d;
                    v = sum;
                }
                if(sum>target){
                    end = end - 1;
                } else {
                    start = start+1;
                }
            }
        }
        return v;
    }

}
