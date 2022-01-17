package com.example.testc2.treemap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 *
 * Given an integer array nums and an integer k, return the k most frequent elements. You may return the answer in any order.
 *
 * https://leetcode-cn.com/problems/top-k-frequent-elements/
 *
 * Example 1:
 * Input: nums = [1,1,1,2,2,3], k = 2
 * Output: [1,2]
 *
 * Example 2:
 * Input: nums = [1], k = 1
 * Output: [1]
 *
 */
public class TopKFrequent {



    public int[] topKFrequent(int[] nums, int k) {
        Map<Integer,Integer> count = new HashMap();
        for (int i:nums){
            count.put(i,count.getOrDefault(i,0)+1);
        }
        TreeMap<Integer, List<Integer>> sorter = new TreeMap();
        Iterator<Integer> keys = count.keySet().iterator();
        while(keys.hasNext()){
            int key = keys.next();
            int v = count.get(key);
            List<Integer> list = sorter.getOrDefault(v,new ArrayList());
            list.add(key);
            sorter.put(v,list);
        }
        int[] ret = new int[k];
        for(int i=0;i<k;i++){
            //  lastKey : the largest key
            //  firstkey: the smallest key
            int maxCount = sorter.lastKey();
            List<Integer> list = sorter.get(maxCount);
            if(list.size()>1){
                ret[i] = list.remove(0);
            } else {
                ret[i] = list.remove(0);
                sorter.remove(maxCount);
            }
        }
        return ret;
    }

}
