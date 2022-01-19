package com.example.testc2.tree;


import com.example.testc2.TestUtil;

import org.junit.Test;

/**
 *
 * Given two integer arrays preorder and inorder where preorder is the preorder traversal of a binary tree and inorder
 * is the inorder traversal of the same tree, construct and return the binary tree.
 *
 *  
 *
 * Example 1:
 * Input: preorder = [3,9,20,15,7], inorder = [9,3,15,20,7]
 * Output: [3,9,20,null,null,15,7]
 * Example 2:
 *
 * Input: preorder = [-1], inorder = [-1]
 * Output: [-1]
 *  
 *
 *
 *
 * Constraints:
 *
 * 1 <= preorder.length <= 3000
 * inorder.length == preorder.length
 * -3000 <= preorder[i], inorder[i] <= 3000
 * preorder and inorder consist of unique values.
 * Each value of inorder also appears in preorder.
 * preorder is guaranteed to be the preorder traversal of the tree.
 * inorder is guaranteed to be the inorder traversal of the tree.
 *
 *
 * 链接：https://leetcode-cn.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal
 *
 *
 * [3,9,20,15,7]
 * [9,3,15,20,7]
 *
 *
 */
public class ConstructTree {


    @Test
    public void test1(){
        int[] pre = {3,9,20,15,7};
        int[] in = {9,3,15,20,7};
        TreeNode r = buildTree(pre,in);
    }

    static class TreeNode extends Node {
        public TreeNode(int _val) {
            super(_val);
        }
    }

    public TreeNode buildTree(int[] preorder, int[] inorder) {
        return buildTreeRecurse(preorder,inorder);
    }

    public TreeNode buildTreeRecurse(int[] preorder, int[] inorder) {
        TestUtil.printArr(preorder,"pre");
        TestUtil.printArr(inorder,"in");
        if(preorder.length==0){
            return new TreeNode(preorder[0]);
        }
        int r = preorder[0];
        TreeNode root = new TreeNode(r);
        int ind = 0;
        for (int i=0;i<inorder.length;i++){
            if(inorder[i]==r){
                ind = i;
                break;
            }
        }
        if(ind>0){
            int leftSize = ind;
            int[] leftInOrder = new int[leftSize];
            copy(inorder,leftInOrder,0,leftSize);

            int[] leftPreOrder = new int[leftSize];
            copy(preorder,leftPreOrder,1,leftSize);

            root.left = buildTreeRecurse(leftPreOrder,leftInOrder);
        }

        if(ind<(inorder.length-1)){
            int rightSize = inorder.length - 1 - ind ;
            int[] rightInOrder = new int[rightSize];
            copy(inorder,rightInOrder,preorder.length-rightSize,rightSize);

            int[] rightPreOrder = new int[rightSize];
            copy(preorder,rightPreOrder,preorder.length-rightSize,rightSize);
            root.right = buildTreeRecurse(rightPreOrder,rightInOrder);
        }
        return root;
    }

    private void copy(int[] target, int[] dest, int targetStart, int count){
        for (int i=0;i<count;i++){
            dest[i] = target[targetStart+i];
        }
    }

}
