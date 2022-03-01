package com.example.testc2.dp;

import org.junit.Test;

/***
 *
 * 给定一个非负整数数组 nums ，你最初位于数组的 第一个下标 。
 * 数组中的每个元素代表你在该位置可以跳跃的最大长度。
 * 判断你是否能够到达最后一个下标。
 **
 * 示例 1：
 *
 * 输入：nums = [2,3,1,1,4]
 * 输出：true
 * 解释：可以先跳 1 步，从下标 0 到达下标 1, 然后再从下标 1 跳 3 步到达最后一个下标。
 * 示例 2：
 *
 * 输入：nums = [3,2,1,0,4]
 * 输出：false
 * 解释：无论怎样，总会到达下标为 3 的位置。但该下标的最大跳跃长度是 0 ， 所以永远不可能到达最后一个下标。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/jump-game
 */
public class Jump {

    @Test
    public void case1(){
        int[] arr1 = {0};
        System.out.println(canJump(arr1));
    }

    public boolean canJump(int[] nums) {
        if(nums.length==1){
            return true;
        }
        int[] jump = new int[nums.length];
        for(int i=0;i<nums.length;i++){
            jump[i] = 0;
        }
        for(int i=0;i<nums.length;i++){
            int steps = nums[i];
            if(steps==0){
                continue;
            }
            if(i==0){
                jump[i] = 1;
            }
            for (int j=1;j<=steps;j++){
                int ind = i+j;
                if(ind<nums.length){
                    if(jump[ind]==0 && jump[i]==1){
                        jump[ind] = 1;
                    }
                }
            }
        }
        return ( jump[nums.length-1] == 1) ;
    }

}
