package com.example.testc2.tree;


import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Given the root of a binary tree, determine if it is a valid binary search tree (BST).
 *
 * A valid BST is defined as follows:
 *
 * The left subtree of a node contains only nodes with keys less than the node's key.
 * The right subtree of a node contains only nodes with keys greater than the node's key.
 * Both the left and right subtrees must also be binary search trees.
 *  
 *
 * Example 1:
 *
 *
 * Input: root = [2,1,3]
 * Output: true
 * Example 2:
 *
 *
 * Input: root = [5,1,4,null,null,3,6]
 * Output: false
 * Explanation: The root node's value is 5 but its right child's value is 4.
 *
 *
 * https://leetcode-cn.com/problems/validate-binary-search-tree
 *
 *
 */
public class ValidBst {

    @Test
    public void test1(){
        Node root = new Node(2);
        root.left = new Node(2);
        root.right = new Node(2);
        assertEquals(false, isValidBST(root));
    }


    @Test
    public void test2(){
        Node root = new Node(5);
        root.left = new Node(1);
        root.right = new Node(6);
        root.right.left = new Node(3);
        root.right.right = new Node(7);
        assertEquals(false, isValidBST(root));
    }


    public boolean isValidBST(Node root) {
        if(root==null){
            return true;
        }
        if(root.left==null&&root.right==null){
            return true;
        }
        long[] range = new long[2];
        range[0] = Long.MIN_VALUE;
        range[1] = Long.MAX_VALUE;
        return valid(root,range);
    }


    /**
     * 
     * use long !!!
     * in case tree has Integer.MAX_VALUE / Integer.MIN_VALUE
     *
     */
    private boolean valid(Node root,long[] range){
        if(root==null){
            return true;
        }
        if(root.left==null&&root.right==null){
            return true;
        }
        if(root.left!=null && (root.left.val>=root.val))
        {
            return false;
        }
        if(root.left!=null && ( (root.left.val<=range[0])|| root.left.val>=range[1]))
        {
            return false;
        }
        if(root.right!=null && (root.right.val<=root.val)){
            return false;
        }
        if(root.right!=null && ( (root.right.val<=range[0])|| root.right.val>=range[1]))
        {
            return false;
        }
        long[] rangeLeft = new long[2];
        rangeLeft[0] = range[0];
        rangeLeft[1] = root.val;
        long[] rangeRight = new long[2];
        rangeRight[0] = root.val;
        rangeRight[1] = range[1];
        return (valid(root.left,rangeLeft) && valid(root.right,rangeRight));

    }

}
