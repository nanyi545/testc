package com.example.testc2.tree;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Given the root of a binary tree, return the level order traversal of its nodes' values. (i.e., from left to right, level by level).
 *
 *  
 *
 * Example 1:
 *
 *
 * Input: root = [3,9,20,null,null,15,7]
 * Output: [[3],[9,20],[15,7]]
 * Example 2:
 *
 * Input: root = [1]
 * Output: [[1]]
 * Example 3:
 *
 * Input: root = []
 * Output: []
 *
 *
 * 链接：https://leetcode-cn.com/problems/binary-tree-level-order-traversal
 *
 */
public class LevelOrderTraversal {

    public List<List<Integer>> levelOrder(Node root) {
        List<List<Integer>> ret = new ArrayList();
        if(root==null){
            return ret;
        }
        // use BFS ...
        Queue<Node> que = new LinkedList();
        que.add(root);
        while(que.size()>0){
            int qSize=que.size();
            List<Integer> temp = new ArrayList();
            for (int i=0;i<qSize;i++){
                Node node = que.poll();
                temp.add(node.val);
                if(node.left!=null){
                    que.add(node.left);
                }
                if(node.right!=null){
                    que.add(node.right);
                }
                if(i==(qSize-1)){
                    ret.add(temp);
                }
            }
        }
        return ret;
    }

}
