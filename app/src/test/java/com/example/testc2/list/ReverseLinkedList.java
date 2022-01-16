package com.example.testc2.list;


/**
 * Given the head of a singly linked list, reverse the list, and return the reversed list.
 *
 * https://leetcode-cn.com/problems/reverse-linked-list
 *
 */
public class ReverseLinkedList {

    public ListNode reverseList(ListNode head) {
        if(head==null){
            return head;
        }
        ListNode head2 = null;
        ListNode t = head;
        while(t!=null){
            ListNode t2 = t;
            t = t.next;
            t2.next = head2;
            head2 = t2;
        }
        return head2;
    }

    static class ListNode {
          int val;
          ListNode next;
          ListNode() {}
          ListNode(int val) { this.val = val; }
          ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

}
