package com.example.testc2.disjointset;

import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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




    public int longestConsecutive(int[] a) {
        int size = a.length;
        int[] diset = new int[size];
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
        Set<Integer> set = new HashSet<>();
        for (int i=0;i<size;i++){
//            if(set.contains(a[i])){
//                continue;
//            }
            set.add(a[i]);
            int ind = get(i,diset);
            System.out.println("i:"+i+"  ind:"+ind);
            int count = counter.getOrDefault(ind,0)+1;
            counter.put(ind,count);
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
            return get(diset[i],diset);
        }
    }

}
