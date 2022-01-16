package com.example.testc2.list;
import com.example.testc2.list.ReverseLinkedList.ListNode;

/**
 * Given the head of a linked list, rotate the list to the right by k places.
 *
 *
 * Input: head = [1,2,3,4,5], k = 2
 *
 * k=1:[5,1,2,3,4]  ---> pick 4th as start
 * k=2:[4,5,1,2,3]  ---> pick 3rd as start
 *
 * Output: [4,5,1,2,3]
 *
 * https://leetcode-cn.com/problems/rotate-list/
 *
 */
public class RotateList {

    public ListNode rotateRight(ListNode head, int k) {
        if(head==null){
            return head;
        }
        int size = 0;
        ListNode t = head;
        ListNode tail=null;
        while(t!=null){  // first loop : find list size
            size++;
            if(t.next==null){
                tail = t;
            }
            t=t.next;
        }
        int steps = ((size-k)%size+size)%size;   // make sure steps > 0  !!!!
        if(steps==0){
            return head;
        }

        int i = 0;
        t = head;
        ListNode targetHead=null;
        ListNode targetHeadPre=null;
        while(t!=null){
            if(i==(steps-1)){
                targetHeadPre = t;
                targetHead = t.next;
                break;
            }
            i++;
            t = t.next;
        }
        tail.next = head;
        targetHeadPre.next=null;
        return targetHead;
    }

}
