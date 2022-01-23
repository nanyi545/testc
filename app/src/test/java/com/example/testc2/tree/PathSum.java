package com.example.testc2.tree;

import org.junit.Test;


/**
 *
 * Given the root of a binary tree and an integer targetSum, return true if the tree has a root-to-leaf path such that adding up all the values along the path equals targetSum.
 *
 * A leaf is a node with no children.
 *
 * https://leetcode-cn.com/problems/path-sum
 *
 *
 *
 * a root-to-leaf path  !!!!!!!!!!s
 *
 *
 */
public class PathSum {


    @Test
    public void test1(){
        Node root = new Node(1);
        root.left = new Node(2);
        System.out.println(hasPathSum(root,0));
    }

    public boolean hasPathSum(Node root, int targetSum) {
        if(root==null){
            return false;
        }
        if( (root.left==null) && (root.right==null) && (root.val==targetSum) ) {
            return true;
        }
        if( (root.left==null) && (root.right==null) && (root.val!=targetSum) ) {
            return false;
        }
        return hasPathSum(root.left, targetSum - root.val)|| hasPathSum(root.right, targetSum-root.val);
    }

}
