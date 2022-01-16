package com.example.testc2.graph;

import org.junit.Test;

import java.util.HashSet;

/**
 *  1 water
 *  0 land
 *
 *
 *  Given a 2D grid consists of 0s (land) and 1s (water).  An island is a maximal 4-directionally connected group of 0s and a closed island is an island
 *  totally (all left, top, right, bottom) surrounded by 1s.
 *
 * Return the number of closed islands.
 *
 * 链接：https://leetcode-cn.com/problems/number-of-closed-islands
 *
 */
public class NumberOfIslands {

    /**
     * [[1,1,1,1,1,1,1,0],[1,0,0,0,0,1,1,0],[1,0,1,0,1,1,1,0],[1,0,0,0,0,1,0,1],[1,1,1,1,1,1,1,0]]
     */
    @Test
    public void test3(){
        int[][] arr1 = { { 1,1,1,1,1,1,1,0 }, { 1,0,0,0,0,1,1,0 } , {1,0,1,0,1,1,1,0},{1,0,0,0,0,1,0,1},{1,1,1,1,1,1,1,0} };
        Solution s = new Solution();
        int islands = s.findCircleNum(arr1);
        System.out.println("p:"+islands);
    }

    /**
     *[[1,0,1,1,1,1,0,0,1,0],
     * [1,0,1,1,0,0,0,1,1,1],
     * [0,1,1,0,0,0,1,0,0,0],
     * [1,0,1,1,0,1,0,0,1,0],
     * [0,1,1,1,0,1,0,1,0,0],
     * [1,0,0,1,0,0,1,0,0,0],
     * [1,0,1,1,1,0,0,1,1,0],
     * [1,1,0,1,1,0,1,0,1,1],
     * [0,0,1,1,1,0,1,0,1,1],
     * [1,0,0,1,1,1,1,0,1,1]]
     */



    static class Solution {
        public int findCircleNum(int[][] grid) {
            prepareHash(grid);
            int count = 0;
            for (int i=0;i<width;i++){
                for (int j=0;j<height;j++){
                    int y=j;
                    int x=i;
                    if((!checked(x,y)) && grid[y][x]==0){
                        setChecked(x,y);
                        int[] bounds = new int[4];
                        bounds[0] = width;
                        bounds[1] = height;
                        bounds[2] = 0;
                        bounds[3] = 0;
                        flood(x,y,grid, bounds);
                        if(bounds[0]>0 && bounds[1]>0 && bounds[2]<(width-1) &&  bounds[3]<(height-1) ){
                            System.out.println("bounds    0:"+bounds[0]+" 1:"+bounds[1]+"  2:"+bounds[2]+"  3:"+bounds[3]);
                            count++;
                        }
                    } else {
                        setChecked(x,y);
                    }
                }
            }
            return count;
        }


        int height=0,width=0;
        HashSet<Integer> set;
        private void prepareHash(int[][] isConnected){
            height = isConnected.length;
            width  = isConnected[0].length;
            set = new HashSet();
        }

        private int getHash(int x,int y){
            return width*y+x;
        }
        private boolean checked(int x,int y){
            return set.contains(getHash(x,y));
        }
        private void setChecked(int x,int y){
            set.add(getHash(x,y));
        }


        /**
         *  totally (all left, top, right, bottom) surrounded by water !!!!!
         *
         * @param x
         * @param y
         * @param grid
         * @param bounds
         */
        private void flood(int x,int y,int[][] grid,int[] bounds){

            if(x<bounds[0]){
                bounds[0] = x;
            }
            if(x>bounds[2]){
                bounds[2] = x;
            }

            if(y<bounds[1]){
                bounds[1] = y;
            }
            if(y>bounds[3]){
                bounds[3] = y;
            }


            if( (x-1)>=0 ){
                int x1 = x-1;
                int y1 = y;
                if(checked(x1,y1)){

                } else {
                    if( grid[y1][x1]==0 ){
                        setChecked(x1,y1);
                        flood(x1,y1,grid,bounds);
                    } else {
                        setChecked(x1,y1);
                    }
                }
            }
            if( (y+1)<height ){
                int x1 = x;
                int y1 = y+1;
                if(checked(x1,y1)){

                } else {
                    if( grid[y1][x1]==0 ){
                        setChecked(x1,y1);
                        flood(x1,y1,grid,bounds);
                    } else {
                        setChecked(x1,y1);
                    }
                }
            }
            if( (x+1)<width ){
                int x1 = x+1;
                int y1 = y;
                if(checked(x1,y1)){

                } else {
                    if( grid[y1][x1]==0 ){
                        setChecked(x1,y1);
                        flood(x1,y1,grid,bounds);
                    } else {
                        setChecked(x1,y1);
                    }
                }
            }
            if( (y-1)>=0 ){
                int x1 = x;
                int y1 = y-1;
                if(checked(x1,y1)){

                } else {
                    if( grid[y1][x1]==0 ){
                        setChecked(x1,y1);
                        flood(x1,y1,grid,bounds);
                    } else {
                        setChecked(x1,y1);
                    }
                }
            }

        }

    }
}
