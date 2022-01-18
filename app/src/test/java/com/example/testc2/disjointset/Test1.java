package com.example.testc2.disjointset;

import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Test1 {

    @Test
    public void demo2(){
        int[] a = {3,4,6,7,8,9};
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
        for (int i=0;i<size;i++){
            int ind = get(i,diset);
            int count = counter.getOrDefault(ind,0)+1;
            counter.put(ind,count);
            if(count>max){
                max = count;
            }
        }

        System.out.println("max:"+max);


    }



    @Test
    /**
     * basic demo !
     */
    public void demo1(){
        int[] a = {3,4,6,7,8,9};
        int size = a.length;
        int[] diset = new int[size];
        Set<Integer> set = new HashSet<>();

        // init
        for (int i=0;i<size;i++){
            set.add(a[i]);
            diset[i] = i;
        }

        diset[0] = 1;
        diset[1] = 1;
        diset[2] = 3;
        diset[3] = 5;
        diset[4] = 5;
        diset[5] = 5;

        System.out.println(get(0,diset));
        System.out.println(get(1,diset));
        System.out.println(get(2,diset));
        System.out.println(get(3,diset));
        System.out.println(get(4,diset));
        System.out.println(get(5,diset));

    }

    int get(int i,int[] diset){
        if(diset[i]==i){
            return i;
        } else {
            return get(diset[i],diset);
        }
    }

}
