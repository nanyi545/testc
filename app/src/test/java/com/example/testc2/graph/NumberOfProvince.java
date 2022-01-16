package com.example.testc2.graph;

import org.junit.Test;

import java.util.HashSet;


/**
 *
 *
 * There are n cities. Some of them are connected, while some are not. If city a is connected directly with city b, and city b is connected directly with city c, then city a is connected indirectly with city c.
 *
 * A province is a group of directly or indirectly connected cities and no other cities outside of the group.
 *
 * You are given an n x n matrix isConnected where isConnected[i][j] = 1 if the ith city and the jth city are directly connected, and isConnected[i][j] = 0 otherwise.
 *
 * Return the total number of provinces.
 *
    https://leetcode-cn.com/problems/number-of-provinces
 *
 *
 *
 * Example 1:
 * Input: isConnected = [[1,1,0],[1,1,0],[0,0,1]]    ---->  1,2 connected   3  :2provinces
 * Output: 2
 *
 * Example 2:
 * Input: isConnected = [[1,0,0],[0,1,0],[0,0,1]]    ---->  1  2  3  : 3 provinces
 * Output: 3
 *
 *
 */
public class NumberOfProvince {

    @Test
    public void test1(){
        int[][] arr1 = { { 1, 1, 0 }, { 1, 1, 0 } , {0,0,1} };
        Solution s = new Solution();
        int p = s.findCircleNum(arr1);
        System.out.println("p:"+p);
    }


    static class Solution {
        public int findCircleNum(int[][] isConnected) {
            int size = isConnected.length;
            boolean[] visit = new boolean[size];
            int count = 0;
            for (int i=0;i<size;i++){
                if(!visit[i]){
                    count++;
                    visit[i] = true;
                    flood(size,isConnected,i,visit);
                }
            }
            return count;
        }

        private void flood(int size, int[][] isConnected,int i,boolean[] visit){
            for(int j=0;j<size;j++){
                if((!visit[j])&&isConnected[i][j]==1){
                    visit[j] = true;
                    flood(size,isConnected,j,visit);
                } else {

                }
            }
        }

    }


}
