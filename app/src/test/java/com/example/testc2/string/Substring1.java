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
 *
 */
public class Substring1 {

    @Test
    public void case2() {
        String s = "barfoothefoobarman";
        System.out.println("s:"+s.substring(0,1));
    }


    @Test
    public void case1() {
        String s = "barfoothefoobarman";
        String[] words = {"foo","bar"};
        List<Integer> a = findSubstring(s, words);
        System.out.println(a);
    }


    /**
     * "wordgoodgoodgoodbestword"
     * ["word","good","best","good"]
     */
    @Test
    public void case3() {
        String s = "wordgoodgoodgoodbestword";
        String[] words = {"word","good","best","good"};
        List<Integer> a = findSubstring(s, words);
        System.out.println(a);
    }



    public List<Integer> findSubstring(String s, String[] words) {
        Map<String,Integer> dict = new HashMap();
        for (String str:words){
            dict.put(str,dict.getOrDefault(str,0)+1);
        }
        int wordLength = words[0].length();
        int wordSize = words.length;
        int size = wordLength*wordSize;
        int maxIndex = s.length() - size;
        List<Integer> list = new ArrayList();
        for (int i=0;i<=maxIndex;i++){
            if(match(s.substring(i,i+size),dict,wordSize,wordLength)){
                list.add(i);
            } else {

            }
        }
        return list;
    }


    private boolean match(String str,Map<String,Integer> dict,
                          int wordSize,int wordLength){
        Map<String,Integer> copy = new HashMap(dict);
        for(int i=0;i<wordSize;i++){
            int s = i*wordLength;
            int e = s+wordLength;
            String t = str.substring(s,e);
            int count = copy.getOrDefault(t,0)-1;
            if(count<=0){
                copy.remove(t);
            } else {
                copy.put(t,count);  // !!! don't forget to put !!!!s
            }
        }
        return (copy.size()==0);
    }


}
