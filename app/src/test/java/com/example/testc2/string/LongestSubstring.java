package com.example.testc2.string;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Given a string s, find the length of the longest substring without repeating characters.
 *
 * https://leetcode-cn.com/problems/longest-substring-without-repeating-character
 */
public class LongestSubstring {


    @Test
    public void testChar() {
        char c1 = '0';
        char c2 = '1';
        int diff = (c2-c1);
        System.out.println("diff:"+diff);

        char c3 = (char) (c1+2);
        System.out.println("c3:"+(c3));  //  use of char !!
        System.out.println("c3:"+(c3+1));
    }


    @Test
    public void case1() {
        int a = lengthOfLongestSubstring("abcabcbb");
        assertEquals(a, 3);
    }
    @Test
    public void case2() {
        int a = lengthOfLongestSubstring("");
        assertEquals(a, 0);
    }
    @Test
    public void case3() {
        int a = lengthOfLongestSubstring("bbbbb");
        assertEquals(a, 1);
    }
    @Test
    public void case4() {
        int a = lengthOfLongestSubstring("pwwkew");
        assertEquals(a, 3);
    }

    public int lengthOfLongestSubstring(String s) {
        int start = 0;
        int end = 0;
        int length = s.length();
        if(s==null||length==0){
            return 0;
        }
        int max = 1;
        int currentMax = 1;
        Set<Character> set = new HashSet();
        set.add(s.charAt(start));
        while(end <length){
            boolean b = ((end+1) < length) ;
            if(b){
                char next = s.charAt(end+1);
                if(!set.contains(next)){
                    set.add(next);
                    currentMax++;
                    if(currentMax>max){
                        max = currentMax;
                    }
                    end = end+1;
                } else {
                    set.remove(s.charAt(start));
                    currentMax --;
                    start = start + 1;
                }
            } else {
                break;
            }
        }
        return max;
    }



}
