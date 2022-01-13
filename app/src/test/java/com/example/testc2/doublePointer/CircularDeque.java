package com.example.testc2.doublePointer;

import org.junit.Test;

/**
 *
 * Design your implementation of the circular double-ended queue (deque).
 *
 * Implement the CircularDeque class:
 *
 * CircularDeque(int k) Initializes the deque with a maximum size of k.
 * boolean insertFront() Adds an item at the front of Deque. Returns true if the operation is successful, or false otherwise.
 * boolean insertLast() Adds an item at the rear of Deque. Returns true if the operation is successful, or false otherwise.
 * boolean deleteFront() Deletes an item from the front of Deque. Returns true if the operation is successful, or false otherwise.
 * boolean deleteLast() Deletes an item from the rear of Deque. Returns true if the operation is successful, or false otherwise.
 * int getFront() Returns the front item from the Deque. Returns -1 if the deque is empty.
 * int getRear() Returns the last item from Deque. Returns -1 if the deque is empty.
 * boolean isEmpty() Returns true if the deque is empty, or false otherwise.
 * boolean isFull() Returns true if the deque is full, or false otherwise.
 *
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/design-circular-deque
 *
 * Your CircularDeque object will be instantiated and called as such:
 * CircularDeque obj = new CircularDeque(k);
 * boolean param_1 = obj.insertFront(value);
 * boolean param_2 = obj.insertLast(value);
 * boolean param_3 = obj.deleteFront();
 * boolean param_4 = obj.deleteLast();
 * int param_5 = obj.getFront();
 * int param_6 = obj.getRear();
 * boolean param_7 = obj.isEmpty();
 * boolean param_8 = obj.isFull();
 */
public class CircularDeque {

    @Test
    public void test1(){
        /**
         * ["CircularDeque","insertLast","insertLast","insertFront","insertFront","getRear","isFull","deleteLast","insertFront","getFront"]
         * [[3],[1],[2],[3],[4],[],[],[],[4],[]]
         */
        MyDeq o = new MyDeq(3);
        System.out.println(o.insertLast(1));
        o.print();
        System.out.println(o.insertLast(2));
        o.print();
        System.out.println(o.insertFront(3));
        o.print();
        System.out.println(o.insertFront(4));
        o.print();
        System.out.println(o.getRear());
        o.print();
    }


    private static class MyDeq {
        int[] arr;
        int start,size,max;
        public MyDeq(int k) {
            arr = new int[k];
            start = 0;
            size = 0;
            max = k;
        }
        public void print(){
            if(size==0){
                System.out.println("empty");
                return;
            }
            StringBuilder sb = new StringBuilder();
            for(int i=0;i<size;i++){
                int index = getIndex(start,i);
                sb.append(arr[index]);
                sb.append("___");
            }
            System.out.println(sb.toString());
        }

        private int getIndex(int i,int delta){
            return (i+delta+10*max)%max;
        }

        boolean canAdd(){
            return size+1<=max;
        }

        public boolean insertFront(int value) {
            if(canAdd()){
                start = getIndex(start, -1);
                arr[start] = value;
                size++;
                return true;
            }
            return false;
        }

        public boolean insertLast(int value) {
            if(canAdd()){
                int end = getIndex(start, size);
                arr[end] = value;
                size++;
                return true;
            }
            return false;
        }

        public boolean deleteFront() {
            if(size>0){
                start = getIndex(start, 1);
                size--;
                return true;
            }
            return false;
        }

        public boolean deleteLast() {
            if(size>0){
                size--;
                return true;
            }
            return false;
        }

        public int getFront() {
            if(size>0){
                return arr[start];
            }
            return -1;
        }

        public int getRear() {
            if(size>0){
                int end = getIndex(start, size-1);  // size-1 !!!!!!
                return arr[end];
            }
            return -1;
        }

        public boolean isEmpty() {
            return size==0;
        }

        public boolean isFull() {
            return size==max;
        }

    }


}
