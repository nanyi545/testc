package com.example.testc2.list;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Given a signed 32-bit integer x, return x with its digits reversed. If reversing x causes the value to go outside the signed 32-bit integer range [-231, 231 - 1], then return 0.
 *
 * Assume the environment does not allow you to store 64-bit integers (signed or unsigned).
 *
 * 链接：https://leetcode-cn.com/problems/reverse-integer
 *
 */
public class ReverseInt {

    @Test
    public void test1(){
        int a = -123;
        System.out.println(a%10);  // -3
        System.out.println(a/10);  // -12
    }

    @Test
    /**
     * 1 way to check if int overflow
     */
    public void checkOverFlow(){
        int a = Integer.MAX_VALUE -2000;
        // check if a * 10 overflow
        int t = a*10/10;
        System.out.println("a*10 overflow:"+(!(a==t)));

        int b = Integer.MAX_VALUE /100;
        int t2 = b*10/10;
        System.out.println("b*10 overflow:"+(!(b==t2)));
    }

    @Test
    public void case1(){
        int a = -123;
        assertEquals(-321, reverse(a));
    }


    @Test
    public void case2(){
        int a = Integer.MAX_VALUE;
        assertEquals(0, reverse(a));
    }

    @Test
    public void case3(){
        int a = 123;
        assertEquals(321, reverse(a));
    }


    public int reverse(int x) {
        boolean positive = (x>0);
        int t = x;
        int reversed = 0;
        while(t!=0){
            int remain = t%10;
            t = (t - remain)/10;
            if(positive){
                if(reversed>(Integer.MAX_VALUE/10)){
                    return 0;
                }
            }
            if(!positive){
                if(reversed<(Integer.MIN_VALUE/10)){
                    return 0;
                }
            }
            reversed = reversed*10 + remain;
        }
        return reversed;
    }



}
