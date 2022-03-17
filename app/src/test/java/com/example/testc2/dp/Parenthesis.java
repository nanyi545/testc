package com.example.testc2.dp;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 数字 n 代表生成括号的对数，请你设计一个函数，用于能够生成所有可能的并且 有效的 括号组合。
 *
 *
 *
 * 示例 1：
 *
 * 输入：n = 3
 * 输出：["((()))","(()())","(())()","()(())","()()()"]
 *
 * 示例 2：
 *
 * 输入：n = 1
 * 输出：["()"]
 *
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/generate-parentheses
 *
 *
 * --------------------------------------------------
 * DP:
 *
 *  f(0) --> [""]
 *  f(1)     ["()"]
 *  f(2)     ["(())","()()"]
 *  ...
 *
 *  f(n) depends on f(n-1),...,f(0)
 *
 *  f(n) = "(" + f(0) + ")"  + f(n-1)
 *       + "(" + f(1) + ")"  + f(n-2)
 *       + ...
 *       + "(" + f(n-1) + ")"  + f(0)
 *
 *
 */
public class Parenthesis {
    @Test
    public void test1(){
        System.out.println(generateParenthesis(8));
    }

    public List<String> generateParenthesis(int n) {
        List<List<String>> list = new ArrayList();
        List<String> l0 = new ArrayList();
        l0.add("");
        list.add(l0);
        List<String> l1 = new ArrayList();
        l1.add("()");
        list.add(l1);
        for(int i=2;i<=n;i++){
            List<String> l = new ArrayList();
            for(int j=0;j<i;j++){
                List<String> ll1 = list.get(j);
                List<String> ll2 = list.get(i-1-j);
                for(String s1:ll1){
                    for(String s2:ll2){
                        String temp = "("+s1+")"+s2;
                        l.add(temp);
                    }
                }
            }
            list.add(l);
        }
        return list.get(n);
    }

}
