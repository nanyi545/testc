package com.example.testc2.string;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ModifyString {

    @Test
    public void test1(){
//        String str = "111";
        String str = "a1b988sd1";
        char[] arr = str.toCharArray();
        List<Character> list = new ArrayList<>();
        boolean preNum =false;
        for (int i=0;i<arr.length;i++){
            boolean num = isNum(arr[i]);
            boolean b1 = ((!preNum) && (num));
            boolean b2 = ((preNum) && (!num));
            boolean ending = ((i==(arr.length-1))&&num);
            if (b1||b2){
                list.add('*');
            }
            list.add(arr[i]);
            if (ending){
                list.add('*');
            }
            preNum = num;
        }
        char[] out = new char[list.size()];
        int ind = 0;
        while(ind<list.size()){
            out[ind] = list.get(ind);
            ind++;
        }
        System.out.println(new String(out));
    }


    private boolean isNum(char c){
        return ((c>='0')&&(c<='9'));
    }
}
