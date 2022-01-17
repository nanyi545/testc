package com.example.testc2.tree;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class OpenLock {

    @Test
    public void test1(){
        System.out.println(""+turn("0000"));
    }


    @Test
    public void test2(){
        String[] dead = {"3333"};
        System.out.println(""+openLock(dead,"2222"));
    }





    public int openLock(String[] deadends, String target) {
        if(target.equals("0000")){
            return 0;
        }
        HashSet<String> deadSet = new HashSet();
        HashSet<String> turned = new HashSet();
        for (String str:deadends){
            deadSet.add(str);
        }
        if(deadSet.contains("0000")){
            return -1;
        }

        String lock = "0000";
        turned.add(lock);
        Queue<String> que = new LinkedList();
        que.add(lock);
        int turns = 0;
        while(que.size()>0){
            turns++;
            int qSize = que.size();
            for (int i=0;i<qSize;i++){
                String str = que.poll();
                List<String> turnStrs = turn(str);
                for (String tempStr:turnStrs){
                    if(
                            (!turned.contains(tempStr))&&
                                    (!deadSet.contains(tempStr))
                    ){
                        if(tempStr.equals(target)){
                            return turns;
                        }
                        turned.add(tempStr);
                        que.add(tempStr);
                    }
                }
            }
        }
        return -1;
    }


    //   !!!!!
    char startChar = '0';

    List<String> turn(String lock){
        List<String> str = new ArrayList();
        char[] chars = lock.toCharArray();
        for (int i=0;i<8;i++){
            char[] charsCopy = new char[4];
            charsCopy[0] = chars[0];
            charsCopy[1] = chars[1];
            charsCopy[2] = chars[2];
            charsCopy[3] = chars[3];
            int index = i%4;
            int direction = i/4;
            char old = chars[index];
            if(direction==0){
                int diff = (old - startChar);
                int newCharDiff = (diff-1+10)%10;
                char newChar = (char) (startChar+newCharDiff);
                charsCopy[index] = newChar;
            } else {
                int diff = (old -startChar);
                int newCharDiff = (diff+1+10)%10;
                char newChar = (char) (startChar+newCharDiff);
                charsCopy[index] = newChar;
            }
            str.add(new String(charsCopy));
        }
        return str;
    }

}
