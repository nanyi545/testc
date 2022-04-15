package com.example.testc2.backtrack;


import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * back-track to get combinations
 */
public class Combination {

    @Test
    public void test(){
        int[] arr = {1,2,3};
        List<List<Integer>> combinations = permute(arr);
        System.out.println(combinations);
    }



    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> permutations = new ArrayList();
        int[] used = new int[nums.length];
        for(int i=0;i<nums.length;i++){
            used[i]=0;
        }
        for(int i=0;i<nums.length;i++){
            backTrack(i,used,nums,new ArrayList(),permutations);
        }
        return permutations;
    }

    void backTrack(int i,int[] used,int[] nums, List<Integer> l1,List<List<Integer>> permutations){
        if(used[i]==0){
            used[i]=1;
            l1.add(nums[i]);
            if(l1.size()==nums.length){
                permutations.add(new ArrayList(l1));
            }
        }
        for(int j=0;j<nums.length;j++){
            if(used[j]==0){
                backTrack(j,used,nums,l1,permutations);
            }
        }
        used[i]=0;
        if(l1.size()>0){
            l1.remove(l1.size()-1);
        }
    }



}
