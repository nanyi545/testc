package com.example.testc2.backtrack;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 给你一个字符串 s，请你将 s 分割成一些子串，使每个子串都是 回文串 。返回 s 所有可能的分割方案。
 *
 * 回文串 是正着读和反着读都一样的字符串。
 *
 *  
 *
 * 示例 1：
 * 输入：s = "aab"
 * 输出：[["a","a","b"],["aa","b"]]
 *
 *
 * 示例 2：
 * 输入：s = "a"
 * 输出：[["a"]]
 *
 *
 * 链接：https://leetcode-cn.com/problems/palindrome-partitioning
 */
public class Polindrome {

    @Test
    public void test(){
        String str = "abc";
        System.out.println(str.substring(0,3));
        System.out.println(isP("a"));
    }


    /**
     * solution:
     *
     * 假设我们当前搜索到字符串的第 i 个字符，且 s[0..i-1] 位置的所有字符已经被分割成若干个回文串，并且分割结果被放入了答案数组中，
     * 那么我们就需要枚举下一个回文串的右边界j，使得 s[i..j] 是一个回文串。
     *
     * 因此，我们可以从i开始，从小到大依次枚举j。对于当前枚举的j值，我们判断s[i..j] 是否为回文串：如果s[i..j] 是回文串，那么就将其加入答案数组中，
     * 并以 j+1 作为新的i 进行下一层搜索，并在未来的回溯时将 s[i..j] 从ans 中移除。
     *
     *
     */
    @Test
    public void test2(){
        String str = "aba";
        System.out.println(getPs(str));
    }


    List<List<String>> getPs(String str){
        int ind = 0;
        List<List<String>> ret = new ArrayList<>();
        List<String> list = new ArrayList<>();
        backTrack(ind,str,ret,list);
        return ret;
    }


    void backTrack(int ind, String str, List<List<String>> ret, List<String> list){
        if(ind==str.length()){
            ret.add(new ArrayList<>(list));
        } else {
            for(int i=ind+1;i<=str.length();i++){
                String t = str.substring(ind, i);
                if(isP(t)){
                    list.add(t);
                    backTrack(i,str,ret,list);
                    list.remove(list.size()-1);
                }
            }
        }
    }



    HashMap<String,Boolean> map = new HashMap<>();
    boolean isP(String str){
        if(map.containsKey(str)){
            return map.get(str);
        }
        for(int i=0;i<(str.length()/2+1);i++){
            int c = str.length() - 1 - i;
            if(str.charAt(c)!=str.charAt(i)){
                map.put(str,false);
                return false;
            }
        }
        map.put(str,true);
        return true;
    }
}
