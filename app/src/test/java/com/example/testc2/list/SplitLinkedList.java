package com.example.testc2.list;

import com.example.testc2.list.ReverseLinkedList.ListNode ;

import org.junit.Test;

/**
 * Given the head of a singly linked list and an integer k, split the linked list into k consecutive linked list parts.
 *
 * https://leetcode-cn.com/problems/split-linked-list-in-parts
 *
 * Input: head = [1,2,3], k = 5
 * Output: [[1],[2],[3],[],[]]
 *
 * Input: head = [1,2,3,4,5,6,7,8,9,10], k = 3
 * Output: [[1,2,3,4],[5,6,7],[8,9,10]]
 *
 *
 */
public class SplitLinkedList {

    @Test
    public void test0(){
        ReverseLinkedList.ListNode root = new ReverseLinkedList.ListNode(1);
        root.next = new ReverseLinkedList.ListNode(2);
        root.next.next = new ReverseLinkedList.ListNode(3);
        splitListToParts(root,5);
    }


    public ReverseLinkedList.ListNode[] splitListToParts(ListNode head, int k) {
        int size = 0;
        ListNode t = head;
        while(t!=null){
            size++;
            t = t.next;
        }
        int itemSize = size / k;
        int left = size-(k*itemSize);
        ListNode[] ret = new ListNode[k];

        t = head;
        for(int i = 0;i<k;i++){
            int count = 0;
            if(i<left){
                count = itemSize+1;
            } else {
                count = itemSize;
            }
            ListNode[] temp = splitList(t,count);
            ret[i] = temp[0];
            t=temp[1];
        }
        return ret;
    }

    /**
     * split count items from LinkedList (head) ...
     *
     * @param head
     * @param count
     * @return
     */
    ListNode[] splitList(ListNode head,int count){
        ListNode[] arr = new ListNode[2];
        arr[0] = head;
        if(count==0){
            arr[1] = null;
            return arr;
        }
        ListNode t = head;
        int size = 0;
        for(int i=0;i<count;i++){
            size++;
            ListNode temp = t;
            t=t.next;
            if(size==count){   //
                temp.next = null;
            }
        }
        arr[1] = t;
        return arr;
    }

}
