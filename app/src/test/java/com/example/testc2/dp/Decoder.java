package com.example.testc2.dp;

import org.junit.Test;

import java.util.HashMap;

/**
 *
 * 一条包含字母 A-Z 的消息通过以下映射进行了 编码 ：
 *
 * 'A' -> "1"
 * 'B' -> "2"
 * ...
 * 'Z' -> "26"
 *
 *
 *
 * 示例 1：
 * 输入：s = "12"
 * 输出：2
 * 解释：它可以解码为 "AB"（1 2）或者 "L"（12）。
 *
 * 示例 2：
 * 输入：s = "226"
 * 输出：3
 * 解释：它可以解码为 "BZ" (2 26), "VF" (22 6), 或者 "BBF" (2 2 6) 。
 *
 * 示例 3：
 * 输入：s = "0"
 * 输出：0
 * 解释：没有字符映射到以 0 开头的数字。
 * 含有 0 的有效映射是 'J' -> "10" 和 'T'-> "20" 。
 * 由于没有字符，因此没有有效的方法对此进行解码，因为所有数字都需要映射。
 *

 * 链接：https://leetcode-cn.com/problems/decode-ways
 */
public class Decoder {

    @Test
    public void test1(){
        System.out.println(numDecodings("11106")); // 2
        System.out.println(numDecodings("12"));
        System.out.println(numDecodings("226"));
    }


    public int numDecodings(String s) {
        map = new HashMap<>();
        return decode(s,0);
    }

    HashMap<Integer,Integer> map = new HashMap();

    int decode(String s,int ind){
        if(map.containsKey(ind)){
            return map.get(ind);
        }
        if(ind>(s.length()-1)){
            map.put(ind,1);
            return 1;
        }
        if(ind==(s.length()-1)){
            char c = s.charAt(ind);
            if(c!='0'){
                map.put(ind,1);
                return 1;
            } else {
                return 0;
            }
        }

        int v = 0;
        if(ind<s.length()){
            char c = s.charAt(ind);
            if(c!='0'){
                v+=decode(s,ind+1);
            }
        }
        if(ind<(s.length()-1)){
            char c1 = s.charAt(ind);
            char c2 = s.charAt(ind+1);
            boolean b1 = (c1=='1');
            boolean b2 = ((c1=='2')&&(c2=='0'||c2=='1'||c2=='2'||c2=='3'||c2=='4'||c2=='5'||c2=='6'));
            if(b1||b2){
                v+=decode(s,ind+2);
            }
        }
        map.put(ind,v);
        return v;
    }



}
