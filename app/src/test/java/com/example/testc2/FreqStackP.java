package com.example.testc2;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

public class FreqStackP {

    @Test
    public void case1(){
        FreqStack s = new FreqStack();
        s.push(5);
        s.push(7);
        s.push(5);
        s.push(7);
        s.push(4);
        s.push(5);
        System.out.println(s.pop());
        System.out.println(s.pop());
        System.out.println(s.pop());
        System.out.println(s.pop());
        System.out.println(s.pop());
        System.out.println(s.pop());
    }

    /**
     * ["FreqStack","push","push","push","push","pop", "pop", "push", "push", "push", "pop", "pop", "pop"]
     *        [[],[1],    [1],    [1],   [2],   [],    [],    [2],   [2],    [1],     [], [], []]
     */

    @Test
    public void case2(){
        FreqStack s = new FreqStack();
        s.push(1);
        s.push(1);
        s.push(1);
        s.push(2);
        System.out.println(s.pop());
        System.out.println(s.pop());

        s.push(2);
        s.push(2);
        s.push(1);

        System.out.println(s.pop());
        System.out.println(s.pop());
        System.out.println(s.pop());

    }


    static class FreqStack {

        public FreqStack() {
            counter = new HashMap();
            stack = new TreeMap();
        }

        int maxCount = 0;
        Map<Integer,Integer> counter;
        TreeMap<Integer, Stack<Integer>> stack;

        public void push(int val) {
            counter.put(val,counter.getOrDefault(val,0)+1);
            int newCount = counter.get(val);
            if(newCount>maxCount){
                maxCount = newCount;
            }
            Stack s = stack.getOrDefault(newCount,new Stack());
            s.push(val);
            stack.put(newCount,s);
        }

        public int pop() {
            Stack s = stack.get(maxCount);
            int maxVal = (int)(s.pop());
            if(s.size()==0){
                stack.remove(maxCount);
                if(stack.size()>0){
                    maxCount = stack.lastKey();
                }else {
                    maxCount = 0;
                }

            } else {

            }
            int countAfterPop  = counter.getOrDefault(maxVal,0) - 1;
            if(countAfterPop>0){
                counter.put(maxVal,countAfterPop);
            }else {
                counter.remove(maxVal);
            }
            return maxVal;
        }
    }

}
