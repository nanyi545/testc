package com.example.testc2;


import com.example.testc2.basics.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * 时间限制： 5000MS
 * 内存限制： 589824KB
 * 题目描述：
 * 一天，小明买了许多积木回家，他想把这些积木拼接在一起。每块积木有两个接口，每个接口我们用一个数字标记，规定只有当两块积木有相同数字标记的接口时，
 * 这两块积木才可以通过该接口拼接在一起。举例，有两块积木，接口数字分别为1，2和3，4，那么这两块积木无法拼接；
 * 若两块积木接口数字分别为1，2和2，3，那么这两块积木可以通过由数字2标记的接口拼接在一起。
 *
 * 现在小明知道所有积木的数量和每块积木接口的数字标记，你能告诉他他可以将所有积木拼接成一个整体么？
 *
 *
 *
 * 输入描述
 * 第一行一个整数t，表示测试数组组数1≤t≤10；
 *
 * 接下来在每组测试数据中：
 * 第一行一个整数n，表示积木的数量1≤n≤100000，
 *
 * 下面n行每行2个整数x，y，表示其中一块积木的两个接口的数字标记；1≤x，y≤100000；
 *
 * 输出描述
 * 对于每组测试数据，输出”YES”，表示该组数据中的所有积木可以拼接成一个整体，”NO”表示不行。（注意输出不包括引号）
 *
 *
 * 样例输入
 * 2
 * 3
 * 1 2
 * 2 3
 * 4 5
 * 6
 * 1 2
 * 2 3
 * 3 5
 * 4 5
 * 4 6
 * 5 1
 * 样例输出
 * NO
 * YES
 *
 *
 *
 * !!!!! 注意环形情况
 *
 */
public class Plugin {

    public static void main(String[] args){
        Scanner s = new Scanner(System.in);
        int t = s.nextInt();
        for(int i=0;i<t;i++){
            int plugins = s.nextInt();
            List<Set<Integer>> list = new ArrayList<>();
            for (int j=0;j<plugins;j++){
                int a = s.nextInt();
                int b = s.nextInt();
                Set<Integer> set = new HashSet<>();
                set.add(a);
                set.add(b);
                List<Set<Integer>> removeList = null;

                boolean contains = false;
                for(Set<Integer> ss:list){
                    if(contains1(ss,a,b)){
                        ss.add(a);
                        ss.add(b);
                        contains = true;
                        if(removeList==null){
                            removeList =new ArrayList<>();
                        } else {
                            removeList.add(ss);
                        }
                    }
                }
                if(!contains){
                    list.add(set);
                }

                if(removeList!=null){
                    list.removeAll(removeList);
                }
            }
            if(list.size()>1){
                System.out.println("NO");
            } else {
                System.out.println("YES");
            }
        }
    }

    public static boolean contains1(Set<Integer> s,int a,int b){
        if(s.contains(a)){
            return true;
        }
        if(s.contains(b)){
            return true;
        }
        return false;
    }

}
