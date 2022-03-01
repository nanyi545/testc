package com.example.testc2.disjointset;


import com.example.testc2.TestUtil;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 输入描述
 * 输入第一行包括一个整数n表示家族成员个数。
 *
 * 接下来n行每行一对整数对a和b表示a是b的父亲，或者b是a的父亲，这需要你来判断。
 *
 * 如果b是-1，那么a就是整个家族的根，也就是辈分最大的人，保证只有一个。
 *
 * 第n+2行是一个整数m表示小红的询问个数。
 *
 * 接下来m行，每行两个正整数A和B。
 *
 * 表示小红想知道A是B的祖先关系。
 *
 * n,m≤40000，每个节点的编号都不超过40000。
 *
 * 输出描述
 * 对于每一个询问。
 *
 * 输出1表示A是B的祖先，输出2表示B是A的祖先，都不是输出0
 *
 *
 * 样例输入
 * 10
 * 1 -1
 * 3 1
 * 4 1
 * 5 1
 * 6 1
 * 7 1
 * 8 1
 * 9 1
 * 10 1
 * 2 10
 * 5
 * 1 2
 * 2 3
 * 2 4
 * 2 5
 * 2 10
 * 样例输出
 * 1
 * 0
 * 0
 * 0
 * 2
 */
public class Family {


    @Test
    public void case1(){
        int n = 10;
        List<int[]> list = new ArrayList<>();
        int[] a1 = {1,-1};
        int[] a2 = {3,1};
        int[] a3 = {4,1};
        int[] a4 = {5,1};
        int[] a5 = {6,1};
        int[] a6 = {7,1};
        int[] a7 = {8,1};
        int[] a8 = {9,1};
        int[] a9 = {10,1};
        int[] a10 = {2,10};
        list.add(a1); list.add(a2); list.add(a3); list.add(a4);list.add(a5);
        list.add(a6); list.add(a7); list.add(a8); list.add(a9);list.add(a10);

        int m = 5;
        List<int[]> query = new ArrayList<>();
        int[] q1 = {1,2};
        int[] q2 = {2,3};
        int[] q3 = {2,4};
        int[] q4 = {2,5};
        int[] q5 = {2,10};
        query.add(q1); query.add(q2); query.add(q3); query.add(q4);query.add(q5);

        test1(n,list,m,query);

    }

    // get path to root
    static List<Integer> get(int ind,int[] arr,List<Integer> list){
        if(ind==arr[ind]){
            list.add(ind);
            return list;
        }
        list.add(ind);
        return get(arr[ind],arr,list);
    }


    public void test1(int n, List<int[]> list, int m, List<int[]> queries){

        int[] dj = new int[n+1];

        // init
        for(int i = 0;i<=n;i++){
            dj[i] = i;
        }

        // set parent
        for(int i = 0;i<n;i++){
            int a = list.get(i)[0];
            int b = list.get(i)[1];
            dj[a] = (b==-1?0:b);
        }

        // print paths ....
        TestUtil.printArr(dj,"dj");
        for(int i=0;i<=n;i++){
            List<Integer> path = new ArrayList<>();
            get(i,dj,path);
            System.out.println("i:"+i+"  p:"+path);
        }


        for(int i = 0;i<m;i++){
            int a = queries.get(i)[0];
            int b = queries.get(i)[1];
            List<Integer> pa = new ArrayList();
            get(a,dj,pa);
            if(pa.contains(b)){
                System.out.println(2);
                continue;
            }

            List<Integer> pb = new ArrayList();
            get(b,dj,pb);
            if(pb.contains(a)){
                System.out.println(1);
                continue;
            }

            System.out.println(0);
        }

    }





}
