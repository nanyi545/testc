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
        List<List<Integer>> combinations = getCombinations(3);
        System.out.println(combinations);
    }



    public List<List<Integer>> getCombinations(int count){
        List<List<Integer>> combinations = new ArrayList<>();

        int[] used = new int[count];
        for(int i=0;i<count;i++){
            used[i] = 0;
        }

        for (int i=0;i<count;i++){
            List<Integer> combination = new ArrayList<>();
            backtrack(used,i,combination,combinations,count);
        }
        return combinations;
    }



    public void backtrack(int[] used, int ind, List<Integer> combination,List<List<Integer>> combinations,int size){
        if(used[ind]==0){
            used[ind]=1;
            combination.add(ind);
            if(combination.size()==size){
                combinations.add(combination);
            }
        }
        for(int i=0;i<size;i++){
            if(used[i]==1){
                continue;
            }
            List<Integer> tt = new ArrayList<>(combination);
            backtrack(used,i,tt,combinations,size);
        }
        used[ind] = 0;
    }



}
