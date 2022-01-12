package com.example.testc2.string;

import com.example.testc2.basics.A;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
 *
 * hash code collision ?????????//
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
    public void case2() {
        String[] strs = {"Aa","BB"};
        System.out.println(groupAnagrams(strs));
    }

    @Test
    public void case3() {
        String[] strs = {"aa","bB"};
        System.out.println(groupAnagrams(strs));
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
        System.out.println((0+'A')); // 65
    }

    @Test
    public void colli(){
//        hash code collision examples
        String str1 = "Aa";
        String str2 = "BB";
        String str3 = "aA";
        System.out.println("str1:"+str1.hashCode()+"  str2:"+str2.hashCode()+" str3:"+str3.hashCode());
        int i1 = str1.charAt(0)*31+str1.charAt(1);
        int i2=  str2.charAt(0)*31+str2.charAt(1);
        int i3 = str3.charAt(0)*31+str3.charAt(1);
        System.out.println("i1:"+i1+"  i2:"+i2+" i3:"+i3);

        System.out.println("A:"+(0+'A')+"  a:"+(0+'a')+" B:"+(0+'B'));
        // 65*31+97
        // 66*31+66

        System.out.println("Aax:"+"Aax".hashCode()+"  BBx:"+"BBx".hashCode());  // collide
    }


    @Test
    public void colli2(){
//        hash code collision examples
        String str1 = "aa";
        String str2 = "bB";
        System.out.println("str1:"+str1.hashCode()+"  str2:"+str2.hashCode());
        // 97*31+97  aa
        // 98*31+66  bB

        String str3 = "aa1234";
        String str4 = "bB1234";
        System.out.println("str3:"+str3.hashCode()+"  str4:"+str4.hashCode());


        String str5 = "Siblings";
        String str6 = "Teheran";
        System.out.println("str5:"+str5.hashCode()+"  str6:"+str6.hashCode());

    }


    @Test
    /**
     * search for collision ...
     */
    public void colli3(){
        HashMap<String,Integer> map = new HashMap<>();
        for (int i=-1000000;i<200000;i++){
            String t = ""+i;
            int h = t.hashCode();
            if(map.containsKey(t)){
                int count = map.get(t);
                map.put(t,count+1);
                System.out.println("collision:"+t+"  count:"+(count+1));
            } else {
                map.put(t,1);
            }
        }
    }



    // http://www.mieliestronk.com/wordlist.html
    // list of all english words
    //  "Siblings" and "Teheran"




    // this also works
    int getCode2(String str){
        char[] arr = str.toCharArray();
        Arrays.sort(arr);
        /**
         * change sort order does not help with the hashcode collision ....
         */
//        char[] arr1 = new char[arr.length];
//        for(int i=0;i<arr.length;i++){
//            arr1[i]=arr[arr.length-1-i];
//        }
//        arr = arr1;
        String t = new String(arr);
        return t.hashCode();
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
        // !!!  this is simpler !!!!
        List<List<String>> list = new ArrayList(map.values());
        return list;
    }
}
