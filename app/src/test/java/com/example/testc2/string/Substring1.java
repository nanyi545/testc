package com.example.testc2.string;


import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 *
 * You are given a string s and an array of strings words of the same length. Return all starting indices of substring(s) in s that is a concatenation of
 * each word in words exactly once, in any order, and without any intervening characters.
 **
 * https://leetcode-cn.com/problems/substring-with-concatenation-of-all-words/
 */
public class Substring1 {

    @Test
    public void case1() {
        String s = "barfoothefoobarman";
        String[] words = {"foo","bar"};
        List<Integer> a = findSubstring(s, words);
        // 0,9
        int ind1 =s.indexOf("foo");
//        System.out.println("ind:"+ind1);
//        System.out.println("substring:"+s.substring(0,2));  //  ba

    }



    public List<Integer> findSubstring(String s, String[] words) {
        int totalSize = s.length();
        int wSize = words.length;
        int wordLength = words[0].length();
        Map<String,List<Integer>> record = new HashMap<>();

        for (int i=0;i<wSize;i++){
            List<Integer> indexes = new ArrayList<>();
            String word = words[i];
            for (int j=0;j<totalSize-wordLength;j++){
                String t = s.substring(j,j+wordLength);
                if(t.equals(word)){
                    indexes.add(j);
                }
            }
            System.out.println("w:"+word+"  pos:"+indexes);
        }
        return null;
    }

}
