package com.example.testc2.array;


import org.junit.Test;

import java.util.HashMap;

/**
 * 请你判断一个 9 x 9 的数独是否有效。只需要 根据以下规则 ，验证已经填入的数字是否有效即可。
 *
 *     数字 1-9 在每一行只能出现一次。
 *     数字 1-9 在每一列只能出现一次。
 *     数字 1-9 在每一个以粗实线分隔的 3x3 宫内只能出现一次。（请参考示例图）
 *
 * 注意：
 *
 *     一个有效的数独（部分已被填充）不一定是可解的。
 *     只需要根据以上规则，验证已经填入的数字是否有效即可。
 *     空白格用 '.' 表示。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/valid-sudoku
 *
 *
 */
public class Sudoku {

    @Test
    public void test1(){
        char[][] c = {
                {'.', '.', '.', '.', '5', '.', '.', '1', '.'},
                {'.', '4', '.', '3', '.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.', '3', '.', '.', '1'},
                {'8', '.', '.', '.', '.', '.', '.', '2', '.'},
                {'.', '.', '2', '.', '7', '.', '.', '.', '.'},
                {'.', '1', '5', '.', '.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.', '2', '.', '.', '.'},
                {'.', '2', '.', '9', '.', '.', '.', '.', '.'},
                {'.', '.', '4', '.', '.', '.', '.', '.', '.'}};

        System.out.println(isValidSudoku(c));
    }

    public boolean isValidSudoku(char[][] board) {
        HashMap<Character,Integer>[] mapsY = new HashMap[9];
        for(int i=0;i<9;i++){
            mapsY[i] = new HashMap();
        }
        for (int i=0;i<9;i++){
            HashMap<Character,Integer> map1 = new HashMap();
            for(int j=0;j<9;j++){
                char c = board[i][j];
                if(c!='.'){
                    int count1 = map1.getOrDefault(c,0)+1;
                    if(count1>1){
                        return false;
                    }
                    map1.put(c,count1);
                    int count2 = mapsY[j].getOrDefault(c,0)+1;
                    if(count2>1){
                        return false;
                    }
                    mapsY[j].put(c,count2);
                }
            }
        }
        for (int i=0;i<=6;i=i+3){
            for(int j=0;j<=6;j=j+3){
                if(!valid(i,j,board)){
                    return false;
                }
            }
        }
        return true;
    }


    public boolean valid(int x,int y,char[][] board){
        HashMap<Character,Integer> m = new HashMap();
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                char c = board[y+i][x+j];
                if(c!='.'){
                    int count = m.getOrDefault(c,0)+1;
                    if(count>1){
                        return false;
                    }
                    m.put(c,count);
                }
            }
        }
        System.out.println("x:"+x+" y:"+y+"  m:"+m);
        return true;
    }

}
