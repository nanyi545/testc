package com.example.testc2.dp;

import com.example.testc2.basics.A;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Change {


    @Test
    public void test1(){
        int[] coins = {1,2,5};
        List<List<Integer>> ret = getCombinations1(4,coins);
        System.out.println(ret);
    }


    /**
     * dp method 1
     */
    public List<List<Integer>> getCombinations1(int target,int[] coins){
        List<List<List<Integer>>> dp = new ArrayList<>();
        // dp.get(target) --> list of coins that sums to target

        List<List<Integer>> list = new ArrayList<>();
        list.add(new ArrayList<>());

        dp.add(list); // dp[0]

        for (int i=1;i<=target;i++){

            List<List<Integer>> r_i = new ArrayList<>();
            dp.add(r_i); // dp[i]

            for(int j:coins){

//                System.out.println("i:"+i+" coin:"+j);

                int preSum = i-j;
                if(preSum>=0){
                    List<List<Integer>> preResults = dp.get(preSum);

                    // dp 状态转移步骤 ...
                    for (List<Integer> preResult:preResults){
                        List<Integer> newResult = new ArrayList<>(preResult);
                        newResult.add(j);

                        // remove duplicates ...
                        sort(newResult);
                        if(!contains(newResult,r_i)){
                            r_i.add(newResult);
                        }

                    }
                }
            }
        }
        return dp.get(target);
    }

    private static void sort(List<Integer> list){
        Collections.sort(list);
    }

    private static boolean equal(List<Integer> list1,List<Integer> list2){
        if(list1.size()!=list2.size()){
            return false;
        }
        for (int i=0;i<list1.size();i++){
            if(list1.get(i)!=list2.get(i)){
                return false;
            }
        }
        return true;
    }

    private static boolean contains(List<Integer> list1,List<List<Integer>> group){
        for (List<Integer> t:group){
            if(equal(t,list1)){
                return true;
            }
        }
        return false;
    }



    @Test
    public void testArr1(){
        int[] arr =new int[5];
        List<Integer>[] arr1 = new ArrayList[3];
        arr1[0] = new ArrayList<>();
        arr1[0].add(8);
        arr1[0].add(9);
        arr1[0].add(10);

        arr1[1] = new ArrayList<>();
        arr1[1].add(1);
        arr1[1].add(2);
        arr1[1].add(3);

        arr1[2] = new ArrayList<>();
        arr1[2].add(4);
        arr1[2].add(5);
        arr1[2].add(6);

        for (int i=0;i<3;i++){
            System.out.println(arr1[i]);
        }

    }
}
