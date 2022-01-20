package com.example.testc2.treemap;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * A k-booking happens when k events have some non-empty intersection (i.e., there is some time that is common to all k events.)
 *
 * You are given some events [start, end), after each given event, return an integer k representing the maximum k-booking between all the previous events.
 *
 * https://leetcode-cn.com/problems/my-calendar-iii
 *
 *
 */

/**
 * Your MyCalendarThree object will be instantiated and called as such:
 * MyCalendarThree obj = new MyCalendarThree();
 * int param_1 = obj.book(start,end);
 */

public class MaxBook {

    @Test
    public void test1(){
        MyCalendarThree c = new MyCalendarThree();
        c.book(5,10);
        c.book(10,15);
    }

    @Test
    public void treeMapDemo1(){
        TreeMap<Integer, Integer> m = new TreeMap<>();
        m.put(3,10);
        m.put(2,10);
        m.put(1,10);
        m.put(6,10);
        m.put(7,10);
        System.out.println(m);
        System.out.println(m.values());

        /**
         *
         *     //返回从fromKey到toKey的集合：含头不含尾
         *     java.util.SortedMap<K,V> subMap(K fromKey, K toKey);
         *
         *     //返回从头到toKey的集合：不包含toKey
         *     java.util.SortedMap<K,V> headMap(K toKey);
         *
         *     //返回从fromKey到结尾的集合：包含fromKey
         *     java.util.SortedMap<K,V> tailMap(K fromKey);
         *
         *     //返回集合中的第一个元素：
         *     K firstKey();
         *
         *     //返回集合中的最后一个元素：
         *     K lastKey();
         *
         */
        System.out.println(m.headMap(2));
        System.out.println(m.tailMap(2));
        System.out.println(m.subMap(2,7));
        System.out.println(m.firstKey());

//        HashMap<Integer, Integer> hashMap = new HashMap<>();
//        hashMap.put(3,10);
//        hashMap.put(2,10);
//        hashMap.put(1,10);
//        System.out.println(hashMap);
//        System.out.println(hashMap.values()); // values based on implementation

        Iterator<Integer> it = m.keySet().iterator();
        StringBuilder sb = new StringBuilder();
        while(it.hasNext()){
            sb.append(it.next());
            sb.append(" ");
        }
        System.out.println("ascend:"+sb.toString());

        Iterator<Integer> it2 = m.descendingKeySet().iterator();
        StringBuilder sb2 = new StringBuilder();
        while(it2.hasNext()){
            sb2.append(it2.next());
            sb2.append(" ");
        }
        System.out.println("descend:"+sb2.toString());
    }

    static class MyCalendarThree {
        TreeMap<Integer, Integer> delta;
        public MyCalendarThree() {
            delta = new TreeMap();
        }
        public int book(int start, int end) {
            delta.put(start, delta.getOrDefault(start, 0) + 1);
            delta.put(end, delta.getOrDefault(end, 0) - 1);
            int active = 0, ans = 0;
            for (int d: delta.values()) {
                active += d;
                if (active > ans) {
                    ans = active;
                }
            }
            return ans;
        }
    }

}
