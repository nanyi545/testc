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


    @Test
    public void test(){
        List<int[]> friends = new ArrayList<>();
        int[] arr0 = {0,1};
        int[] arr1 = {2,3};
        int[] arr2 = {1,2};
        friends.add(arr0);
        friends.add(arr1);
        friends.add(arr2);
        test1(4,3,friends);

    }


    public void test1(int n, int m, List<int[]> friends){

        int[] diset = new int[n];
        for(int i=0;i<n;i++){
            // 初始化, 每一个id[i]指向自己, 没有合并的元素
            diset[i] = i;
        }

        for (int[] t:friends) {
            // 合并操作也是很简单的，先找到两个集合的代表元素，然后将前者的父节点设为后者即可。
            diset[t[0]] = t[1];
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
