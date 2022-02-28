package com.example.testc2.nk;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;


/**
 *
 * io
 * https://blog.csdn.net/Pandafz1997/article/details/120517721?spm=1001.2014.3001.5501
 *
 * nk io
 * https://blog.csdn.net/qq_42403042/article/details/107533785
 *
 * saima io
 *
 *
 * import java.util.*;
 *
 * class Main {
 *   public static void main(String[] args){
 *     Scanner s = new Scanner(System.in);
 *     int c = s.nextInt();
 *     HashSet<Integer> m = new HashSet();
 *     for (int i=0;i<c;i++){
 *       int n = s.nextInt();
 *       while(true){
 *         if( n>0 &&(!m.contains(n))) {
 *           m.add(n);
 *           break;
 *         }
 *         if(n==0){
 *           break;
 *         }
 *         n=n-1;
 *       }
 *     }
 *     int t = 0;
 *     Iterator<Integer> i = m.iterator();
 *     while(i.hasNext()){
 *       t+=i.next();
 *     }
 *     System.out.println(t);
 *   }s
 * }
 *
 *
 */
public class Nk1 {  // use class Main ....

    public static void main(String[] args){
        Scanner s = new Scanner(System.in);
        String str = s.nextLine();
        String[] strs = str.split(" ");
        System.out.println(strs[strs.length-1].length());
    }


    @Test
    public void test2(){
        Scanner s = new Scanner(System.in);
        while(s.hasNext()){
            int a = s.nextInt();
            int b = s.nextInt();
            System.out.println("a:"+a+"  b:"+b);
        }
    }

    private void testImport(){
        List list = new ArrayList();
        Map<Character,Integer> counter = new HashMap();
    }


    /**
     *  写出一个程序，接受一个由字母、数字和空格组成的字符串，和一个字符，然后输出输入字符串中该字符的出现次数。（不区分大小写字母）
     *
     * 数据范围： 1≤n≤1000 1 \le n \le 1000 \ 1≤n≤1000
     * 输入描述：
     *
     * 第一行输入一个由字母和数字以及空格组成的字符串，第二行输入一个字符。
     * 输出描述：
     *
     * 输出输入字符串中含有该字符的个数。（不区分大小写字母）
     * 示例1
     * 输入：
     *
     * ABCabc
     * A
     *
     * 输出：
     * 2
     */
    public static void test3(String[] str){
        Scanner s = new Scanner(System.in);
        String line = s.nextLine();
        String c = s.nextLine();
        char[] arr = line.toLowerCase().toCharArray();
        Map<Character,Integer> counter = new HashMap();
        for (int i=0;i<line.length();i++){
            char mChar = arr[i];
            counter.put(mChar,counter.getOrDefault(mChar,0)+1);
        }
        System.out.println(counter.getOrDefault(c.toLowerCase().charAt(0),0));
    }


    /**
     *  N sets of inputs
     *
     *  --line 1 t: number of lines of data
     *  --t lines
     *  ....
     *  --line 1 t: number of lines of data
     *  --t lines
     *  ....
     *
     *
     *
     */
    public static void test4(String[] args){
        Scanner s = new Scanner(System.in);
        int N = 0;
        List<Set<Integer>> list = new ArrayList();
        while (s.hasNext()){
            int lines = s.nextInt();
            N++;
            Set<Integer> set = new HashSet();
            list.add(set);
            for (int i=0;i<lines;i++){
                int a = s.nextInt();
                set.add(a);
            }
        }
        for (int j=0;j<N;j++){
            int ttt = list.get(j).size();
            int[] arr = new int[ttt];
            Iterator<Integer> it = list.get(j).iterator();
            for(int i=0;i<ttt;i++){
                arr[i] = it.next();
            }
            Arrays.sort(arr);
            for(int i=0;i<ttt;i++){
                System.out.println(arr[i]);
            }
        }
    }


    /**
     * 描述
     *
     * •连续输入字符串，请按长度为8拆分每个输入字符串并进行输出；
     * •长度不是8整数倍的字符串请在后面补数字0，空字符串不处理。
     * （注：本题有多组输入）
     * 输入描述：
     *
     * 连续输入字符串(输入多次,每个字符串长度小于等于100)
     * 输出描述：
     *
     * 依次输出所有分割后的长度为8的新字符串
     *
     *
     * 示例1
     * 输入：
     * abc
     * 123456789
     *
     * 输出：
     * abc00000
     * 12345678
     * 90000000
     */
    public static void test5(String[] args){
        Scanner s = new Scanner(System.in);
        List<String> list = new ArrayList();
        while(s.hasNext()){
            String str = s.nextLine();
            list.add(str);
        }
        List<String> out = new ArrayList();
        String c = "";
        int i = 0;
        while (i<list.size()){
            String ss = list.get(i);
            if(ss.length()==0){
                continue;
            }
            int q = ss.length()/8;
            int r = ss.length()%8;
            String sss;
            if(r!=0){
                q++;
                int append = 8 - r;
                StringBuilder sb = new StringBuilder();
                sb.append(ss);
                for(int tt=0;tt<append;tt++){
                    sb.append("0");
                }
                sss=sb.toString();
            } else {
                sss=ss;
            }
            for (int ii=0;ii<q;ii++){
                out.add(sss.substring(ii*8,ii*8+8));
            }
            i++;
        }
        for (int j=0;j<out.size();j++){
            System.out.println(out.get(j));
        }
    }


}
