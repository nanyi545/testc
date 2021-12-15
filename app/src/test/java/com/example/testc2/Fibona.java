package com.example.testc2;

import org.junit.Test;

import java.util.HashMap;

public class Fibona {

    // https://cloud.tencent.com/developer/article/1817113

    /**
     * 自底向上的动态规划
     *
     * 动态规划跟带备忘录的递归解法基本思想是一致的，都是减少重复计算，时间复杂度也都是差不多。但是呢：
     *
     *     带备忘录的递归，是从f(10)往f(1）方向延伸求解的，所以也称为自顶向下的解法。
     *     动态规划从较小问题的解，由交叠性质，逐步决策出较大问题的解，它是从f(1)往f(10）方向，往上推求解，所以称为自底向上的解法。
     *
     * 动态规划有几个典型特征，最优子结构、状态转移方程、边界、重叠子问题。在青蛙跳阶问题中：
     *
     *     f(n-1)和f(n-2) 称为 f(n) 的最优子结构
     *     f(n)= f（n-1）+f（n-2）就称为状态转移方程
     *     f(1) = 1, f(2) = 2 就是边界啦
     *     比如f(10)= f(9)+f(8),f(9) = f(8) + f(7) ,f(8)就是重叠子问题。
     */

    @Test
    public void test1(){
        long t1 = System.currentTimeMillis();
        for (int i = 1;i<30;i++){
            System.out.println("f"+i+" ="+getFib(i));
        }
        long t2 = System.currentTimeMillis();
        System.out.println("total time:"+(t2-t1));
    }

    private static HashMap<Integer,Integer> map = new HashMap<>();
    private static int getFib(int i){
        if(i==1){
            return 1;
        }
        if(i==2){
            return 2;
        }
        if(map.containsKey(i)){
            return map.get(i);
        } else {
            map.put(i,getFib(i-1) + getFib(i-2 ) );
            return map.get(i);
        }
    }
}
