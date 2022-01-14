package com.example.testc2.tree;

import java.util.ArrayList;
import java.util.List;
/**
 * You are given a perfect binary tree where all leaves are on the same level, and every parent has two children. The binary tree has the following definition:
 *
 * struct Node {
 *   int val;
 *   Node *left;
 *   Node *right;
 *   Node *next;
 * }
 *
 * Populate each next pointer to point to its next right node. If there is no next right node, the next pointer should be set to NULL.
 *
 * Initially, all next pointers are set to NULL.
 *
 * 链接：https://leetcode-cn.com/problems/populating-next-right-pointers-in-each-node
 *
 * https://assets.leetcode.com/uploads/2019/02/14/116_sample.png
 *
 **/

public class PerfectBinaryTree {


    public Node connectHorizontal(Node root) {
        if(root==null){
            return null;
        }
        List<Node> list = new ArrayList();
        list.add(root);
        while(list.size()>0){
            int size = list.size();
            List<Node> t = new ArrayList();
            for (int i=0;i<size;i++){
                if(i<(size-1)){
                    list.get(i).next = list.get(i+1);
                }else{
                    list.get(i).next = null;
                }
                if(list.get(i).left!=null){
                    t.add(list.get(i).left);
                    t.add(list.get(i).right);
                }
            }
            list = t;
        }
        return root;
    }

}
