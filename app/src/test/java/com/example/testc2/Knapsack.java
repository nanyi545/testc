package com.example.testc2;


import org.junit.Test;

/**
 * 背包问题  : https://zhuanlan.zhihu.com/p/132702321
 *  我们可以这样来描述它：给你一些物品，这些物品都有它对应的重量 wi和价格 vi，并且每个物品只能选择一次。现在有一个最多能装 W 重量的背包，你要怎样来组合这些物品来使得背包里物品的价格最大。
 *  暴力搜索的方式就是将这个问题的全部组合一一列举出来，即穷举集合 {w1, w2, …, wi, …, wn } 中所有的子集，然后判断重量是否超过了背包上限，如果没有，就分别计算它们的价格，最后选出最高价格的组合。
 *
 * solutions:
 * https://zhuanlan.zhihu.com/p/138328448
 *
 * --------------------------------------
 * https://zhuanlan.zhihu.com/p/93857890
 *
 * dp[i][j]表示将前i件物品装进限重为j的背包可以获得的最大价值, 0<=i<=N, 0<=j<=W
 *
 * 那么我们可以将dp[0][0...W]初始化为0，表示将前0个物品（即没有物品）装入书包的最大价值为0。那么当 i > 0 时dp[i][j]有两种情况：
 *
 *     1 不装入第i件物品，即dp[i−1][j]；
 *     2 装入第i件物品（前提是能装下），即dp[i−1][j−w[i]] + v[i]
 *
 * 即状态转移方程为
 *
 *   dp[i][j] = max(dp[i−1][j], dp[i−1][j−w[i]]+v[i]) // j >= w[i]
 *
 *
 * --------------------------------------
 */
public class Knapsack {

    /**
     *  https://zhuanlan.zhihu.com/p/138328448
     *
     */
    @Test
    public void test1(){

        int[][] values =
                {
                        {7,42},  // w1 v1 ...
                        {3,12},
                        {4,40},
                        {5,25}
                };

        int w = 10;  // max capacity

        int dp_y_count = values.length+1;
        int dp_x_count = w+1;

        int dp[][] = new int[dp_y_count][dp_x_count];
        // dp[i][0]=0    dp[0][j] = 0

        for(int i=0;i<dp_y_count;i++){
            for (int j=0;j<dp_x_count;j++) {
                if(i==0||j==0){
                    dp[i][j] = 0;
                } else {
                    int w_i = values[i-1][0];
                    int v_i = values[i-1][1];
                    if(j<w_i) {
                        dp[i][j] = dp[i-1][j];
                    } else {
                        int p1 = dp[i-1][j];
                        int p2 = dp[i-1][j-w_i]+v_i;
                        dp[i][j] = p2>p1?p2:p1;
                    }
                }
            }
        }

        System.out.println("max:"+dp[dp_y_count-1][dp_x_count-1]);

    }


}
