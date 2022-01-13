package com.example.testc2.stack;

import org.junit.Test;

import java.util.Stack;

import static org.junit.Assert.assertEquals;


/**
 * Given two integer arrays pushed and popped each with distinct values, return true if this could have been the result of a sequence of push and pop operations on an initially empty stack, or false otherwise.
 *
 *
 *
 *
 * Example 1:
 *
 * Input: pushed = [1,2,3,4,5], popped = [4,5,3,2,1]
 * Output: true
 * Explanation: We might do the following sequence:
 * push(1), push(2), push(3), push(4),
 * pop() -> 4,
 * push(5),
 * pop() -> 5, pop() -> 3, pop() -> 2, pop() -> 1
 *
 *
 *
 * Example 2:
 *
 * Input: pushed = [1,2,3,4,5], popped = [4,3,5,1,2]
 * Output: false
 * Explanation: 1 cannot be popped before 2.
 *
 *
 * 链接：https://leetcode-cn.com/problems/validate-stack-sequences
 *
 *
 */
public class ValidateStackSequences {

    @Test
    public void case1() {
        int[] pushed = {1,2,3,4,5};
        int[] popped = {4,5,3,2,1};
        assertEquals(true, validateStackSequences(pushed,popped));
    }
    @Test
    public void case2() {
        int[] pushed = {1,2,3,4,5};
        int[] popped = {4,3,5,1,2};
        assertEquals(false, validateStackSequences(pushed,popped));
    }

    public boolean validateStackSequences(int[] pushed, int[] popped) {
        Stack<Integer> stack = new Stack();
        int l = 0;
        int r = 0;
        int size = pushed.length;
        int size2 = popped.length;
        if(size!=size2){
            return false;
        }
        int steps = 0;
        while(steps<(size*2)){
            while(stack.size()>0 && (r<size) && stack.peek()==popped[r]){
                stack.pop();
                r++;
            }
            if(l<size){
                stack.push(pushed[l]);
                l++;
            }
            steps++;
        }
        return stack.size()==0;
    }

}
