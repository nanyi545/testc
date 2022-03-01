package com.example.testc2.sort;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * jackisnumberone
 * jackiksnumberone
 */
public class Test1 {


    /**
     * lexical order
     */
    @Test
    public void test1(){
        String[] arr1 = new String[5];
        arr1[0] = "zzz";
        arr1[1] = "cba";
        arr1[2] = "abc";
        arr1[3] = "a";
        arr1[4] = "xyz";
        Arrays.sort(arr1);
        for (int i=0;i<arr1.length;i++){
            System.out.println("i:"+i+"    "+arr1[i]);
        }
    }

    /**
     * lexical order
     */
    @Test
    public void test2(){
        List<String> list = new ArrayList<>();
        list.add("zzz");
        list.add("cba");
        list.add("abc");
        list.add("a");
        list.add("A");
        list.add("BB");
        list.add("xyz");
        Collections.sort(list);
        for (int i=0;i<list.size();i++){
            System.out.println("i:"+i+"    "+list.get(i));
        }
    }


    /**
     * array list usage
     */
    @Test
    public void testlist(){
        List<Integer> li = new ArrayList<>(10);
        li.add(1);
        li.add(2);
        li.add(3);
        for (int i=0;i<li.size();i++){
            System.out.println("i:"+i+"    "+li.get(i));
        }
    }


    /**
     * merge order
     */
    @Test
    public void testMerge(){
        List<Integer> l0 = new ArrayList<>(4+1);
        l0.add(10);
        l0.add(20);
        l0.add(30);
        l0.add(40);
        l0.add(Integer.MAX_VALUE);

        List<Integer> l1 = new ArrayList<>(4+1);
        l1.add(5);
        l1.add(9);
        l1.add(13);
        l1.add(17);
        l1.add(Integer.MAX_VALUE);

        List<Integer> l2 = new ArrayList<>(4+1);
        l2.add(15);
        l2.add(39);
        l2.add(43);
        l2.add(57);
        l2.add(Integer.MAX_VALUE);

        int i0=0,i1=0,i2=0;
        List<Integer> l3 = new ArrayList();

        for(int i=0;i<12;i++){
            int v0 = l0.get(i0);
            int v1 = l1.get(i1);
            int v2 = l2.get(i2);

            if(v0<=v1 && v0<=v2) {
                l3.add(l0.get(i0));
                i0++;
                System.out.println("i0:"+i0+"  i1:"+i1+"  i2:"+i2);
                continue;
            }

            if(v1<=v0 && v1<=v2) {
                l3.add(l1.get(i1));
                i1++;
                System.out.println("i0:"+i0+"  i1:"+i1+"  i2:"+i2);
                continue;
            }

            if(v2<=v1 && v2<=v0) {
                l3.add(l2.get(i2));
                i2++;
                System.out.println("i0:"+i0+"  i1:"+i1+"  i2:"+i2);
                continue;
            }

        }

        System.out.println(l3);



    }



}
