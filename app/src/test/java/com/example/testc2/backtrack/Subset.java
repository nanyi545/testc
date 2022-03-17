package com.example.testc2.backtrack;

import com.example.testc2.basics.A;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


/**
 *  back-track to get subsets
 *  ---------
 *  When the order doesn't matter, it is a Combination.
 *  When the order does matter it is a Permutation.
 *
 **/
public class Subset {


    @Test
    public void test(){
        List<List<Integer>> subSets = getSubsets(4);
        System.out.println(subSets);
    }



    public List<List<Integer>> getSubsets(int count){
        List<List<Integer>> subSets = new ArrayList<>();

        int[] used = new int[count];
        for(int i=0;i<count;i++){
            used[i] = 0;
        }

        for (int i=0;i<count;i++){
            List<Integer> set = new ArrayList<>();
            backtrack(used,i,set,subSets,count);
        }
        return subSets;
    }


    public void backtrack(int[] used, int ind, List<Integer> set,List<List<Integer>> subSets,int size){
        if(used[ind]==0){
            used[ind]=1;
            set.add(ind);
            subSets.add(set);
        }
        for(int i=ind+1;i<size;i++){
            List<Integer> tt = new ArrayList<>(set);
            backtrack(used,i,tt,subSets,size);
        }
        //  back track !
        used[ind]=0;
    }

}
