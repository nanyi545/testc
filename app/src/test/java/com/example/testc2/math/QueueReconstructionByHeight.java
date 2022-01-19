package com.example.testc2.math;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class QueueReconstructionByHeight {


    @Test
    public void testInsert(){
        List<Integer> list = new ArrayList();
        list.add(1);
        list.add(2);
        System.out.println(list);
        list.add(1,3);
        System.out.println(list);
    }


    @Test
    public void testSort(){
        int[][] people = {{9,0},{7,3},{8,3},{8,2}};
        Arrays.sort(people,comparator);
        for(int i=0;i<people.length;i++){
            System.out.println(i+":"+people[i][0]+"-"+people[i][1]);
        }
    }


    @Test
    public void testSort2(){


        Integer[] arr = {1,3,9,4,5,2,0}; // if you want reverse order you need to use Integer array
        Arrays.sort(arr);
        for(int i=0;i<arr.length;i++){
            System.out.println(i+":"+arr[i]); // 默认生序
        }


        Arrays.sort(arr, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                /**
                 * @return a negative integer, zero, or a positive integer as the
                 *         first argument is less than, equal to, or greater than the
                 *         second.
                 */
                return o1-o2;  // 生序
            }
        });
        for(int i=0;i<arr.length;i++){
            System.out.println(i+":"+arr[i]);
        }


        Arrays.sort(arr, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2-o1;  // 降序
            }
        });
        for(int i=0;i<arr.length;i++){
            System.out.println(i+":"+arr[i]);
        }

    }


    //[[7,0],[4,4],[7,1],[5,0],[6,1],[5,2]]

    @Test
    public void case1(){
        int[][] people = {{7,0},{4,4},{7,1},{5,0},{6,1},{5,2}};
        int[][] re = reconstructQueue(people);
        for(int i=0;i<re.length;i++){
            System.out.println(i+":"+re[i][0]+"-"+re[i][1]);
        }
    }


    public int[][] reconstructQueue(int[][] people) {
        Arrays.sort(people,comparator);
        List<int[]> list = new ArrayList<>();
        for (int i=0;i<people.length;i++){
            int[] t  = people[i];
            if(t[1]==list.size()){
                list.add(t);
            } else {
                list.add(t[1],t);
            }
        }
        int[][] ret = new int[list.size()][2];
        for (int i=0;i<list.size();i++){
            ret[i] = list.get(i);
        }
        return ret;
    }


    Comparator<int[]> comparator = new Comparator<int[]>() {
        @Override
        public int compare(int[] o1, int[] o2) {
            int diff1 = o2[0]-o1[0];
            int diff2 = o1[1]-o2[1];
            if(diff1!=0) {
                return diff1;
            }
            return diff2;
        }
    };


}
