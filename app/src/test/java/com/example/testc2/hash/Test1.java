package com.example.testc2.hash;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * 小明是一个互联网公司的老板，需要招聘员工。现在有k个学校的学生前来应聘。
 *
 * 由于特殊原因，要求最后入职的员工学校的人数应该都不一样。
 *
 * 比如我们可以A大学录取5人，B大学录取4人。但是不允许A大学和B大学都录取5人。
 *
 * 请问最后最多录取多少人呢？
 *
 *
 *
 * 输入描述
 * 第一行一个整数k，表示学校的数量。
 *
 * 第二行k个整数ai，表示这个学校有ai个人前来应聘。
 *
 * 满足 1<=k<=100000,1<=ai<=100000
 *
 * 输出描述
 * 输出最多录取人数
 *
 *
 * 样例输入
 * 3
 * 3 3 2
 * 样例输出
 * 6
 *
 */
public class Test1 {


    /**
     *   why only 67% ??
     * @param args
     */
    public static void main(String[] args){
        Scanner s = new Scanner(System.in);
        int n = s.nextInt();
        Set<Integer> set = new HashSet();
        long total = 0;
        while(s.hasNext()){
            int i = s.nextInt();
            while (i>0){
                if(!set.contains(i)){
                    set.add(i);
                    total = total+i;
                    break;
                }
                i--;
            }
        }
        System.out.println(total);
    }

}
