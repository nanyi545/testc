package com.example.testc2.math;


import org.junit.Test;

/**
 * 现在你有三个数，a,b,c。
 *
 * 想问是否能够由若干个a加上若干个b，组成c。
 *
 */
public class Combine {

    @Test
    public void case2(){
        System.out.println(test2(3,2,5));
        System.out.println(test2(3,2,1));
    }


    @Test
    public void case1(){
        System.out.println(test1(3,2,5));
        System.out.println(test1(3,2,1));
    }

    public boolean test2(int a,int b,int c){
        if(c%a==0){
            return true;
        }
        if(c%b==0){
            return true;
        }
        int x = c/a;
        for (int i=0;i<=x;i++){
            int diff = c - a*i;
            if(diff<0) {
                continue;
            } else {
                if(diff%b==0){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * how to do faster ??
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public boolean test1(int a,int b,int c){
        if(c%a==0){
            return true;
        }
        if(c%b==0){
            return true;
        }
        int x = c/a;
        int y = c/b;
        for (int i=0;i<=x;i++){
            for(int j=0;j<=y;j++){
                if((a*i+b*j)==c){
                    return true;
                }
            }
        }
        return false;
    }
}
