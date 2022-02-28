package com.example.testc2.disjointset;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * ·题目：假如已知有n个人和m对好友关系（存于数组r）如果两个人是直接或间接的好友（好友的好友的好友…），
 * 则认为他们属于同一个朋友圈，请写程序求出这n个人里一共有多少个朋友圈。
 *
 *  4
 *  2
 *  0 1
 *  2 3  ---> 2个圈子 
 */
public class Friends {

    public static void print(int[] arr){
        int size = arr.length;
        StringBuilder sb= new StringBuilder();
        for(int i=0;i<size;i++){
            sb.append(arr[i]);
            sb.append("_");
        }
        System.out.println("arr:"+sb);
    }


    @Test
    public void test(){
        List<int[]> friends = new ArrayList<>();

//        int[] arr0 = {0,3};
//        int[] arr1 = {2,3};
//        int[] arr2 = {1,2};
//        friends.add(arr0);
//        friends.add(arr1);
//        friends.add(arr2);
//        test1(4,3,friends);
        // ---> 1 group : 3

        int[] arr0 = {0,4};
        int[] arr1 = {0,3};//  0-->4  0-->3   !!!!!  how to solve !!!
        int[] arr2 = {1,2};
        // 2 groups : 2,3

        friends.add(arr0);
        friends.add(arr1);
        friends.add(arr2);
        test1(5,3,friends);

    }



    public void test1(int n, int m, List<int[]> friends){

        int[] diset = new int[n];
        for(int i=0;i<n;i++){
            // 初始化, 每一个id[i]指向自己, 没有合并的元素
            diset[i] = i;
        }

        for (int[] t:friends) {
            // 合并操作也是很简单的，先找到两个集合的代表元素，然后将前者的父节点设为后者即可。
            //  diset[t[0]] = t[1];   // compare with this !!!!!!!!!!!

            int root0 = getRoot(t[0],diset);
            int root1 = getRoot(t[1],diset);
            if(root0!=root1) {
                System.out.println("root0:"+root0+"  root1:"+root1);
                print(diset);
                diset[root0] = root1;
//                diset[root1] = root0;  // this 2 will be the same !!!
                print(diset);
                System.out.println("after --------------------");
            }

        }

        HashSet<Integer> groups = new HashSet<>();


        for(int i=0;i<n;i++){
            int root = getRoot(i,diset);
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
