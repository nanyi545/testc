package com.example.testc2.string;

import com.example.testc2.basics.A;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Given an array of strings strs, group the anagrams together. You can return the answer in any order.
 *
 * An Anagram is a word or phrase formed by rearranging the letters of a different word or phrase, typically using all the original letters exactly once.
 *
 * https://leetcode-cn.com/problems/group-anagrams
 *
 */
public class Anagrams {

    @Test
    public void case1() {
        String[] strs = {"eat","tea","tan","ate","nat","bat"};
        System.out.println(groupAnagrams(strs));
        // [["bat"],["nat","tan"],["ate","eat","tea"]]
    }

    @Test
    public void testChar() {
        char a = 'a';
        System.out.println(a);  // a
        System.out.println((0+a)); // 97
        System.out.println((a+a)); // 194 = 97+97
        System.out.println((a*a)); // 9409= 97*s97
        System.out.println((97*97));
        System.out.println((a/a)); // 1

    }



    int getCode(String str){
        int code = 0;
        for (int i=0;i<str.length();i++){
            char c = str.charAt(i);
            code += (c*c + c*c*c + c*c*c*c);
        }
        return code;
    }

    public List<List<String>> groupAnagrams(String[] strs) {
        HashMap<Integer,List<String>> map = new HashMap<>();
        for (String str:strs){
            int code = getCode(str);
            if(map.containsKey(code)){
                map.get(code).add(str);
            } else {
                List<String> list = new ArrayList<>();
                list.add(str);
                map.put(code,list);
            }
        }
        Iterator<Integer> it = map.keySet().iterator();
        List<List<String>> list = new ArrayList<>();
        while(it.hasNext()){
            list.add(map.get(it.next()));
        }
        return list;
    }
}
