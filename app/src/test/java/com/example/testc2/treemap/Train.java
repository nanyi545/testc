package com.example.testc2.treemap;


import org.junit.Test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

/**
 *
 * 题目描述：
 * 在Z省，有若干个个城市坐落在一条直线上，从左到右依次标号1,2,3,…，其中每个城市有一个火车站点，现今已经开放了n条火车路线，第i条火车路线是从第Yi个城市到第Xi个城市，因为Z省地势奇特，标号小的城市地势较低，
 * 所以火车是从高往低开的，再通过神秘力量传送回高地，即Yi一定大于Xi，它在沿途的所有城市都会停靠（显然不包括起点Yi，但是包括终点Xi），火车停靠就需要火车站台来运营维护。火车站台随着运营线路的数量不同，
 * 其损耗速度、维护成本也各异，现在我们想知道所有站台中，所运营的线路数最大是多少。
 *
 *
 *
 * 输入描述
 * 第一行一个数n。（1≤n≤100000）
 *
 * 接下来n行每行两个数Xi和Yi，分别代表第i条火车路线的终点和起点。（1≤Xi<Yi≤1e5）
 *
 * 输出描述
 * 共一行，一个非负整数，表示最大运营路线数。
 *
 *
 * 样例输入
 * 4
 * 4 7
 * 2 6
 * 2 5
 * 1 2
 * 样例输出
 * 3
 *
 * 提示
 * 第一条涉及站台6，5，4
 * 第二条涉及站台5，4，3，2
 * 第三条涉及站台4，3，2
 * 第四条涉及站台1
 *
 * 所以显然站台4运营的线路最多，有3条
 */
public class Train {

    @Test
    public void test1(){
        long l1 = System.currentTimeMillis();
        byTreeMap(5000,3000);
        long l2 = System.currentTimeMillis();
        System.out.println("t1:"+(l2-l1));

        long l3 = System.currentTimeMillis();
        byHashMap(5000,3000);
        long l4 = System.currentTimeMillis();
        System.out.println("t2:"+(l4-l3));

    }


    private int getRandInt(int max){
        return (int) (Math.random()*max);
    }

    // n^log(n)
    private int byTreeMap(int lines, int maxV){
        TreeMap<Integer,Integer> counter = new TreeMap();
        for (int i=0;i<lines;i++){
            int a = getRandInt(maxV);
            int b = getRandInt(maxV);
            int x = a<b?a:b;
            int y = a>b?a:b;
            counter.put(x,counter.getOrDefault(x,0)+1);
            counter.put(y,counter.getOrDefault(y,0)-1);
        }
        Iterator<Integer> it = counter.keySet().iterator();
        int temp = 0;
        int max = 0;
        int maxKey = 0;
        while(it.hasNext()){
            int k=it.next();
            int v = counter.get(k);
            temp+=v;
            if(temp>max){
                max = temp;
                maxKey = k;
            }
        }
        System.out.println("max:"+max);
        return max;
    }


    // n^2
    private int byHashMap(int lines, int maxV){
        HashMap<Integer,Integer> counter = new HashMap();
        for (int i=0;i<lines;i++){
            int a = getRandInt(maxV);
            int b = getRandInt(maxV);
            int x = a<b?a:b;
            int y = a>b?a:b;
            for (int j=x;j<=y;j++){
                counter.put(j,counter.getOrDefault(j,0)+1);
            }
        }
        Iterator<Integer> it = counter.keySet().iterator();
        int max = 0;
        int maxKey = 0;
        while(it.hasNext()){
            int k = it.next();
            int v = counter.get(k);
            if(v>max){
                max = v;
                maxKey = k;
            }
        }
        System.out.println("max:"+max);
        return max;
    }

}
