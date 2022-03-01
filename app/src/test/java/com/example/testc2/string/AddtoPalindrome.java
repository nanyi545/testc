package com.example.testc2.string;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 给定一个字符串，问是否能够通过添加一个字母将其变成“回文串”。 “回文串”是指正着和反着读都一样的字符串。如：”aa”,”bob”,”testset”是回文串，”alice”,”time”都不是回文串。
 *
 *
 *
 *结题思路：如果一个字符串能通过添加一个字母变成回文串，那么它去掉一个字母也就可以变成会文串，
 * 于是我们就可以将字符串的每位都去掉一次去判断，如果一次回文串都没出现，说明添加一个字母也不能构成回文串。
 *
 */
public class AddtoPalindrome {

    @Test
    public void caseRun(){
        test("coco");
        test("fbi");
    }

    public void test(String str){
        int size = str.length();
        // !!!!!!!!!
        if(size==0 || size==1) {
            System.out.println("Yes");
            return;
        }
        char[] arr = str.toCharArray();
        boolean a = false;
        for (int i=0;i<size;i++){
            List<Character> list= new ArrayList();
            for(int j=0;j<size;j++){
                if(j==i){

                } else {
                    list.add(arr[j]);
                }
            }
            boolean isP = isP(list);
            if(isP){
                a = true;
                break;
            }
        }
        if(a){
            System.out.println("Yes");
        } else {
            System.out.println("No");
        }
    }



    static boolean isP(List<Character> list){
        int size = list.size();
        int half = size/2;
        for (int i=0;i<=half;i++){
            if(list.get(i)==list.get(size-1-i)){

            } else {
                return false;
            }
        }
        return true;
    }

}
