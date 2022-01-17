package com.example.testc2.math;


import org.junit.Test;

/**
 * A peak element is an element that is strictly greater than its neighbors.
 *
 * Given an integer array nums, find a peak element, and return its index. If the array contains multiple peaks, return the index to any of the peaks.
 * You may imagine that nums[-1] = nums[n] = -∞.
 *
 * You must write an algorithm that runs in O(log n) time.
 *
 * 链接：https://leetcode-cn.com/problems/find-peak-element
 *
 */
public class FindPeakElement {

    /**
     * Example 1:
     * Input: nums = [1,2,3,1]
     * Output: 2
     * Explanation: 3 is a peak element and your function should return the index number 2.
     *
     * Example 2:
     * Input: nums = [1,2,1,3,5,6,4]
     * Output: 5
     * Explanation: Your function can return either index number 1 where the peak element is 2, or index number 5 where the peak element is 6.
     *
     */
    @Test
    public void test1(){
        int[] arr1={1,2,3,1};
        System.out.println(findPeakElement(arr1));
    }
    @Test
    public void test2(){
        int[] arr1={1,2,3,4};
        System.out.println(findPeakElement(arr1));
    }


    public int findPeakElement(int[] nums) {
        int size = nums.length;
        int l = 0;
        int r = size-1;
        while(l<r){
            int m = (l+r)/2;
            System.out.println("1  l:"+l+"  r:"+r+"  m:"+m);
            if(nums[m+1]>nums[m]){
                l = m+1;
            } else {
                r = m;
            }
            System.out.println("2  l:"+l+"  r:"+r);
        }
        return l;
    }



}
