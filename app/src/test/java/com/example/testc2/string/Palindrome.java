package com.example.testc2.string;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Given a string s, return the longest palindromic substring in s.
 *
 *  
 *
 * Example 1:
 *
 * Input: s = "babad"
 * Output: "bab"
 * Explanation: "aba" is also a valid answer.
 * Example 2:
 *
 * Input: s = "cbbd"
 * Output: "bb"
 *
 *
 *https://leetcode-cn.com/problems/longest-palindromic-substring
 *
 *
 *
 */
public class Palindrome {
    @Test
    public void test1(){

    }


    /**
     * 中心扩展法
     */
    public String longestPalindrome(String s) {
        List<int[]> seeds = new ArrayList();
        char[] chars = s.toCharArray();
        for (int i=0;i<chars.length;i++){
            int[] seed = new int[2];
            seed[0] = i;
            seed[1] = i;
            seeds.add(seed);
        }
        for (int i=0;i<chars.length-1;i++){
            if(chars[i]==chars[i+1]){
                int[] seed = new int[2];
                seed[0] = i;
                seed[1] = i+1;
                seeds.add(seed);
            }
        }
        String t="";
        for (int i =0 ;i<seeds.size();i++){
            String str = expand(chars,seeds.get(i));
            if(str.length()>t.length()){
                t = str;
            }
        }
        return t;
    }

    String expand(char[] arr,int[] ind){
        int s = ind[0];
        int e = ind[1];
        int changes = 0;
        while(
                (s-1>=0) && (e+1<=(arr.length-1))
        ){
            if(arr[s-1]==arr[e+1]){
                s--;
                e++;
                changes++;
            } else {
                break;
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i=s;i<=e;i++){
            sb.append(arr[i]);
        }
        return sb.toString();
    }

}
