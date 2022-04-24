package com.example.testc2.array;

import org.junit.Test;


/**
 *
 *
 * 在一个 n * m 的二维数组中，每一行都按照从左到右递增的顺序排序，每一列都按照从上到下递增的顺序排序。请完成一个高效的函数，输入这样的一个二维数组和一个整数，判断数组中是否含有该整数。
 *
 *  
 *
 * 示例:
 *
 * 现有矩阵 matrix 如下：
 *
 * [
 *   [1,   4,  7, 11, 15],
 *   [2,   5,  8, 12, 19],
 *   [3,   6,  9, 16, 22],
 *   [10, 13, 14, 17, 24],
 *   [18, 21, 23, 26, 30]
 * ]
 * 给定 target = 5，返回 true。
 *
 * 给定 target = 20，返回 false。
 *
 *
 *
 * https://leetcode-cn.com/problems/er-wei-shu-zu-zhong-de-cha-zhao-lcof
 *
 *
 *
 *
 *
 */
public class D2ArrayFind {

    @Test
    public void test1(){
        int[][] matrix = {{-5}};
        System.out.println(findNumberIn2DArray(matrix,-5));
    }

    @Test
    public void test2(){
        int[][] matrix = {{1,1}};
        System.out.println(findNumberIn2DArray(matrix,0));
    }

    @Test
    public void test3(){
        int[][] matrix = {{5},{6}};
        System.out.println(findNumberIn2DArray(matrix,5));
    }

    @Test
    public void test4(){
        int[][] matrix = {{1,2,3,4,5},{6,7,8,9,10}};
        System.out.println(findNumberIn2DArray(matrix,1));
    }


    // simple version ... this also works ...
    public boolean findNumberIn2DArray2(int[][] matrix, int target) {
        int Y = matrix.length;
        if(Y==0){
            return false;
        }
        int X = matrix[0].length;
        if(X==0){
            return false;
        }
        for(int i=0;i<Y;i++){
            boolean b = search(matrix[i],target,0,X-1);
            if(b){
                return true;
            }
        }
        return false;
    }


    public boolean findNumberIn2DArray(int[][] matrix, int target) {
        int Y = matrix.length;
        if(Y==0){
            return false;
        }
        int X = matrix[0].length;
        if(X==0){
            return false;
        }
        int x1=0;
        int x2=X-1;
        int y1=0;
        int y2=Y-1;
        if(x2>0){
            while(matrix[Y-1][x1]<target){
                x1++;
                if(x1>=X){
                    return false;
                }
            }
            while(matrix[0][x2-1]>target){
                x2--;
                if(x2<=0){
                    return false;
                }
            }
        }
        if(y2>0){
            while(matrix[y1][X-1]<target){
                y1++;
                if(y1>=Y){
                    return false;
                }
            }
            while(matrix[y2-1][0]>target){
                y2--;
                if(y2<=0){
                    return false;
                }
            }
        }
        for(int i=y1;i<=y2;i++){
            boolean b = search(matrix[i],target,x1,x2);
            if(b){
                return true;
            }
        }
        return false;
    }

    boolean search(int[] arr,int target,int i1, int i2){
        int s = i1;
        int e = i2;
        if(s>e){
            return false;
        }
        if(s==e){
            return arr[s]==target;
        }
        int m = (s+e)/2;
        if(arr[m]==target){
            return true;
        }
        if(arr[m]>target){
            return search(arr,target,i1,m-1);
        }else{
            return search(arr,target,m+1,i2);
        }
    }

}
