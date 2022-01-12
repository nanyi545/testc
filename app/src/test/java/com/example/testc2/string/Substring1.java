package com.example.testc2.string;


import org.junit.Test;

import java.util.List;

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
    }



    public List<Integer> findSubstring(String s, String[] words) {
        return null;
    }

}
