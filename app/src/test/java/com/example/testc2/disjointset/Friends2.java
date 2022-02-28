package com.example.testc2.disjointset;


import com.example.testc2.codec2.Utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 *  多少个朋友圈 ??
 *
 *
 *  0 1
 *  2 3  ---> 2个圈子
 */

public class Friends2 {

    @Test
    public void test(){
        List<int[]> friends = new ArrayList<>();
        int[] arr0 = {0,100};  //
        int[] arr1 = {0,3};    //  0-->100  0-->3 should not work  ....  ??
        int[] arr2 = {1,2};
        friends.add(arr0);
        friends.add(arr1);
        friends.add(arr2);
        test1(friends);
    }


    public void test1( List<int[]> friends ) {
        // friend id --> id
        Map<Integer,Integer> map1 = new HashMap<>();

        // id --> friend id
        Map<Integer,Integer> map2 = new HashMap<>();

        // temp id
        int ind = -1;
        for(int i=0;i<friends.size();i++){
            int[] arr = friends.get(i);
            if(!map1.containsKey(arr[0])){
                ind++;
                map1.put(arr[0],ind);
                map2.put(ind,arr[0]);
            }
            if(!map1.containsKey(arr[1])){
                ind++;
                map1.put(arr[1],ind);
                map2.put(ind,arr[1]);
            }
        }

        System.out.println("ind:"+ind);

        int[] dj = new int[ind+1];
        for(int i=0;i<=ind;i++){
            dj[i] = i;
        }

        for(int i=0;i<friends.size();i++){
            int[] arr = friends.get(i);
            int ind0 = map1.get(arr[0]);
            int ind1 = map1.get(arr[1]);

            int root0 = getRoot(ind0,dj);
            int root1 = getRoot(ind1,dj);

            if(root0!=root1){
                dj[root0] = root1;
            }

        }

        HashSet<Integer> groups = new HashSet<>();


        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();

        for(int i=0;i<=ind;i++){
            sb1.append(dj[i]+"_");
            sb2.append(map2.get(dj[i])+"_");
        }
        System.out.println("friend ids:"+sb2);
        System.out.println("       ids:"+sb1);
        System.out.println("friend id -> id :"+map1);


        for(int i=0;i<=ind;i++){
            int root = getRoot(i,dj);
            // number of roots means --> number of groups
            groups.add(root);
        }

        System.out.println(groups);

    }



    public int getRoot(int i,int[] diset){
        if(diset[i]==i){
            return i;
        } else {
            int a = getRoot( diset[i], diset);
            return a;
        }
    }

}
