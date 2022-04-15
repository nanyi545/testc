package com.example.testc2.disjointset;

import org.junit.Test;

/**
 * https://leetcode-cn.com/problems/find-if-path-exists-in-graph/
 *
 */
public class ValidPath {

    @Test
    public void test1(){
        int n=10;
        int[][] edges = {{0,7},{0,8},{6,1},{2,0},{0,4},{5,8},{4,7},{1,3},{3,5},{6,5}};
        int sour = 5;
        int dest = 7;
        System.out.println(validPath(n,edges,sour,dest));
    }

    public boolean validPath(int n, int[][] edges, int source, int destination) {
        if(source==destination){
            return true;
        }
        int[] arr = new int[n];
        for(int i=0;i<n;i++){
            arr[i]=i;
        }
        for(int i=0;i<edges.length;i++){
            int p1 = edges[i][0];
            int p2 = edges[i][1];
            // !!!!!!!!!! join !!!!!!!!!!!!!
            int r1 = getR(p1,arr);
            int r2 = getR(p2,arr);
            if(r1!=r2){
                arr[r2]=r1;
            }
        }
        // how to check if they are the same group !!!!!
        // not !!!! getR(source,arr)==destination
        return ((getR(source,arr)==getR(destination,arr)));
    }

    int getR(int i,int[] arr){
        if(arr[i]==i){
            return arr[i];
        } else {
            return getR(arr[i],arr);
        }
    }

}
