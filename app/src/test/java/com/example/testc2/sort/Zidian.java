package com.example.testc2.sort;


import org.junit.Test;

import java.util.Scanner;

/**
 *
 输入描述
 第一行一个字符串id，表示他的用户名（1≤id长度≤1000），保证只由小写字母组成。

 第二行一个字符c，表示被添加进去的字符，保证为一个小写字母。

 输出描述
 共一行，修改后的字典序最小的字符串。


 样例输入
 jackisnumberone
 k
 样例输出
 jackiksnumberone


 */
public class Zidian {


    @Test
    public void test1()
    {
        String str = "jackisnumberone";
        String str2 = "k";
        if(str.length()==0){
            System.out.println(str2);
            return;
        }
        char c = str2.charAt(0);

        int size = str.length();
        int insertIndex = -1;
        for(int i=0;i<size;i++){
            char ct = str.charAt(i);
            if(ct>c){
                insertIndex = i;
                break;
            }
        }

        if(insertIndex>0){
            String s1 = str.substring(0,insertIndex);
            String s2 = str.substring(insertIndex,size);
            System.out.println(s1+str2+s2);
        } else if(insertIndex == 0){
            System.out.println(str2+str);
        }else {
            System.out.println(str+str2);
        }

    }


}
