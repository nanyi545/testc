package com.example.testc2.array;


/**
 *
 * https://leetcode-cn.com/problems/maximum-subarray/
 *
 * 给你一个整数数组 nums ，请你找出一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。
 * 子数组 是数组中的一个连续部分。
 *
 *
 *
 * 示例 1：
 * 输入：nums = [-2,1,-3,4,-1,2,1,-5,4]
 * 输出：6
 * 解释：连续子数组 [4,-1,2,1] 的和最大，为 6 。
 *
 * 示例 2：
 * 输入：nums = [1]
 * 输出：1
 *
 * 示例 3：
 * 输入：nums = [5,4,-1,7,8]
 * 输出：23
 *
 */
public class MaxSubArray {


    /**
     * ## 2.贪心法的思想
     * 本题还可以利用贪心法来实现, **贪心的思想**是: 从左向右迭代, 一个个数字加过去如果sum<0, 那说明加上它只会变得越来越小, 所以我们将sum置零后重新开始找子序串.
     * 在迭代的过程中要注意, 我们需要**用result来不断维持当前的最大子序和**, 因为sum的值是在不断更新的, 所以我们要及时记录下它的最大值.
     */
    public int maxSubArray(int[] nums) {
        int s = 0;
        int e = 0;
        int t = nums[s];
        int l=nums.length;
        int max = t;
        while(true){
            if(t>0){
                e++;
                if(e>=l){
                    break;
                }
                t+=nums[e];
            } else {
                s = e+1;
                e = s;
                if(e>=l){
                    break;
                }
                t = nums[s];
            }
            if(t>max){
                max = t;
            }
        }
        return max;
    }

}
