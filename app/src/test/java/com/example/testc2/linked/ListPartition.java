package com.example.testc2.linked;


import com.example.testc2.treemap.MaxBook;

import org.junit.Test;

/**
 * Given the head of a linked list and a value x, partition it such that all nodes less than x come before nodes greater than or equal to x.
 *
 * You should preserve the original relative order of the nodes in each of the two partitions.
 *
 *https://leetcode-cn.com/problems/partition-list
 *
 */
public class ListPartition {


    /**
     * Input: head = [1,4,3,2,5,2], x = 3
     * Output: [1,2,2,4,3,5]
     */
    @Test
    public void test1(){
        ListNode head = new ListNode(1);
        head.next = new ListNode(4);
        head.next.next = new ListNode(3);
        head.next.next.next = new ListNode(2);
        head.next.next.next.next = new ListNode(5);
        head.next.next.next.next.next = new ListNode(2);
        ListNode newHead = partition(head,3);
        ListNode.print(newHead);
    }



    public ListNode partition(ListNode head, int x) {
        ListNode lHead=null;
        ListNode lTail=null;
        ListNode sHead=null;
        ListNode sTail=null;
        ListNode t = head;
        while(t!=null){
            ListNode temp = t;
            t = t.next;
            if(temp.val<x){
                if(sHead==null){
                    sHead = temp;
                    sTail = temp;
                } else {
                    sTail.next = temp;
                    sTail = temp;
                }
            } else {
                if(lHead==null){
                    lHead = temp;
                    lTail = temp;
                } else {
                    lTail.next = temp;
                    lTail = temp;
                }
            }
            temp.next = null;
        }
        if(sTail==null){
            return lHead;
        }
        sTail.next = lHead;
        return sHead;
    }


    private static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }

        public static void print(ListNode head){
            StringBuilder sb = new StringBuilder();
            ListNode t = head;
            while(t!=null){
                sb.append(t.val);
                sb.append("___");
                t = t.next;
            }
            System.out.println(sb.toString());
        }
    }

}
