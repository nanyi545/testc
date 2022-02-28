package com.example.testc2.string;


import org.junit.Test;

/**
 * 题目描述：
 * 小明很喜欢打字，今天小红给了小明一个字符串。
 * 这个字符串只包含大写和小写字母。
 * 我们知道，按下CapsLock键，可以切换大小写模式。
 * 我们在小写模式时候，同时按下shift+字母键，就能写出大写字母。
 * 在大写模式的时候，按下shift+字母键，就能写出小写字母。
 * 现在问题来了，给你一个字符串，问你最少使用多少个按键，就可以写出这个字符串呢？
 * 注意，按shift和字母键，算两次按键。开始时均为小写状态。
 *
 *
 *
 * 输入描述
 * 第一行一个T，表示有T组输入。
 *
 * 接下来T组数据：
 *
 * 每组数据一个字符串s，s的长度小于等于100。仅包含大小写字母。
 *
 * 输出描述
 * 对于每组数据，输出最少按键次数。
 *
 *
 * 样例输入
 * 3
 * A
 * AA
 * AAAAAA
 * 样例输出
 * 2
 * 3
 * 7
 *
 *
 * // below code only has 9%...
 *
 */
public class Counter {

    @Test
    public void test1() {
//        char[] c1 = "aaAA".toCharArray();
//        System.out.println(getCount(c1,3));

        System.out.println(getCount("AA"));
        System.out.println(getCount("AaAaAaAa"));


//        char[] c1 = "AA".toCharArray();
//        System.out.println(getCount(c1,0));


    }


    static int getCount(String str){
        if(str==null || str.equals("")){
            return 0;
        }
        char[] arr= str.toCharArray();
        int size = str.length();
        boolean inputCase = true;
        int p = 0;
        boolean s = small(arr[0]);
        int count = getCount(arr,p);
        while(count!=0){

            if(count==1){
                if(inputCase && s){
                    p+=1;
                }
                if(!inputCase && s){
                    p+=2;
                }
                if(inputCase && !s){
                    p+=2;
                }
                if(!inputCase && !s){
                    p+=1;
                }

            } else {
                if(inputCase && s){
                    p+=count;
                }
                if(!inputCase && s){
                    p+=count+1;
                }
                if(inputCase && !s){
                    p+=count+1;
                }
                if(!inputCase && !s){
                    p+=count;
                }
                inputCase = !inputCase;
            }
            count = getCount(arr,p);

        }
        return p;
    }

    static boolean small(char c){
        return c>='a' && c<='z';
    }
    static boolean big(char c){
        return c>='A' && c<='Z';
    }
    static int getCount(char[] arr, int ind){
        if(ind>=arr.length){
            return 0;
        }
        boolean s = small(arr[ind]);
        int shift = 0;
        while(true){
            if((ind+shift+1)>=arr.length){
                break;
            }
            if( s == small(arr[ind+shift+1]) )
            {
                shift++;
            } else {
                break;
            }
        }
        return shift+1;
    }


}
