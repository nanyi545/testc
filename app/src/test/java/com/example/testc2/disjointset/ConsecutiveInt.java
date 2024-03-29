package com.example.testc2.disjointset;

import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * Given an unsorted array of integers nums, return the length of the longest consecutive elements sequence.
 *
 * You must write an algorithm that runs in O(n) time.
 *
 *  
 *
 * Example 1:
 *
 * Input: nums = [100,4,200,1,3,2]
 * Output: 4
 * Explanation: The longest consecutive elements sequence is [1, 2, 3, 4]. Therefore its length is 4.
 * Example 2:
 *
 * Input: nums = [0,3,7,2,5,8,4,6,0,1]
 * Output: 9
 *
 *
 * https://leetcode-cn.com/problems/longest-consecutive-sequence
 *
 *
 *  !!!!!    key : in this DisJointSet n+1 is parent of n
 */
public class ConsecutiveInt {
    @Test
    public void test1(){
        int[] a = {3,4,6,7,8,9};
        System.out.println("max:"+longestConsecutive(a));
    }

    //[0,3,7,2,5,8,4,6,0,1]
    @Test
    public void test2(){
        int[] a = {0,3,7,2,5,8,4,6,0,1};
        System.out.println("max:"+longestConsecutive(a));
    }

    @Test
    public void test3(){
        int[] a = {0,0,-1};
        System.out.println("max:"+longestConsecutive(a));
    }




    public int longestConsecutive(int[] ints) {
        Set<Integer> temp = new HashSet<>();
        for (int i=0;i<ints.length;i++){
            temp.add(ints[i]);
        }
        int[] a = new int[temp.size()];
        Iterator<Integer> it = temp.iterator();
        int tt = 0;
        while(it.hasNext()){
            a[tt] = it.next();
            tt++;
        }


        int size = a.length;
        int[] diset = new int[size];

        //  orginal value ---> ind
        Map<Integer,Integer> map = new HashMap<>();

        // init
        for (int i=0;i<size;i++){
            map.put(a[i],i);
            diset[i] = i;
        }

        // set parent
        for (int i=0;i<size;i++){
            int t = a[i] + 1;
            if(map.containsKey(t)){
                int tIndex = map.get(t);
                diset[i] = tIndex;
            }
        }

        int max = 0;
        Map<Integer,Integer> counter = new HashMap<>();
        for (int i=0;i<size;i++){
            // root
            int ind = get(i,diset);
            int count = counter.getOrDefault(ind,0)+1;
            counter.put(ind,count);

            // find the most frequent root  .... 
            if(count>max){
                max = count;
            }
        }
        return max;
    }

    int get(int i,int[] diset){
        if(diset[i]==i){
            return i;
        } else {

            // !!! caching !!!
            int a = get(diset[i],diset);
            diset[i] = a;
            return a;
        }
    }

}
