package com.example.testc2.tree;

import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class Traverse {

    static Node root;

    /**
     *           1
     *      2         3
     *    4   5    6    7
     *       8 9  0
     *
     *
     * 前序  1,2,4,5,8,9,3,6,0,7,
     * 中序  4,2,8,5,9,1,0,6,3,7,
     * 后序  4,8,9,5,2,0,6,7,3,1,
     * bfs  1,2,3,4,5,6,7,8,9,0,
     *
     */
    static {
        root = new Node(1);
        root.left = new Node(2);
        root.right = new Node(3);
        root.left.left = new Node(4);
        root.left.right= new Node(5);
        root.right.left = new Node(6);
        root.right.right= new Node(7);
        root.left.right.left = new Node(8);
        root.left.right.right = new Node(9);
        root.right.left.left = new Node(0);
    }

    /**
     * bfs
     */
    @Test
    public void test0(){
        bfs(root, new Node.Visitor() {
            @Override
            public void visit(Node n) {
                System.out.print(n.val+",");
            }
        });
    }

    private void bfs(Node root,Node.Visitor visitor){
        Queue<Node> q = new LinkedList();
        q.add(root);
        while(q.size()>0){
            int qSize = q.size();
            for(int i=0;i<qSize;i++) {
                Node t = q.poll();
                visitor.visit(t);
                if (t.left != null) {
                    q.add(t.left);
                }
                if (t.right != null) {
                    q.add(t.right);
                }
            }
        }
    }

    /**
     * 递归，前序
     */
    @Test
    public void test1(){
        preOrder1(root, new Node.Visitor() {
            @Override
            public void visit(Node n) {
                System.out.print(n.val+",");
            }
        });
        /**
         * 1,2,4,5,8,9,3,6,0,7,
         */
    }

    /**
     * 递归，中序
     */
    @Test
    public void test11(){
        midOrder1(root, new Node.Visitor() {
            @Override
            public void visit(Node n) {
                System.out.print(n.val+",");
            }
        });
        /**
         * 4,2,8,5,9,1,0,6,3,7,
         */
    }

    /**
     * 递归，后序
     */
    @Test
    public void test111(){
        posOrder1(root, new Node.Visitor() {
            @Override
            public void visit(Node n) {
                System.out.print(n.val+",");
            }
        });
    }


    /**
     * 非递归，前序
     */
    @Test
    public void test2(){
        preOrder2(root, new Node.Visitor() {
            @Override
            public void visit(Node n) {
                System.out.print(n.val+",");
            }
        });
        /**
         * 1,2,4,5,8,9,3,6,0,7,
         */
    }

    /**
     * 非递归，mid序
     */
    @Test
    public void test22(){
        midOrder2(root, new Node.Visitor() {
            @Override
            public void visit(Node n) {
                System.out.print(n.val+",");
            }
        });
        /**
         */
    }


    private void preOrder1(Node root, Node.Visitor visitor){
        visitor.visit(root);
        if(root.left!=null){
            preOrder1(root.left,visitor);
        }
        if(root.right!=null){
            preOrder1(root.right,visitor);
        }
    }


    private void preOrder2(Node root, Node.Visitor visitor){
        Stack<Node> s = new Stack();
        s.push(root);
        while(!s.isEmpty()){
            Node temp = s.pop();
            visitor.visit(temp);
            if(temp.right!=null){
                s.push(temp.right);
            }
            if(temp.left!=null){
                s.push(temp.left);
            }
        }
    }


    private void midOrder1(Node root, Node.Visitor visitor){
        if(root.left!=null){
            midOrder1(root.left,visitor);
        }
        visitor.visit(root);
        if(root.right!=null){
            midOrder1(root.right,visitor);
        }
    }


    /**
     *           1
     *      2         3
     *    4   5    6    7
     *       8 9  0
     * 中序  4,2,8,5,9,1,0,6,3,7,
     */

    private void midOrder2(Node root, Node.Visitor visitor){
        Stack<Node> s = new Stack();
        Node t = root;
        while(true){
            // switch to the most left ...
            while(t.left!=null){
                s.push(t);
                t = t.left;
            }
            visitor.visit(t);
            while(true){
                // switch right branch
                if(t.right!=null){
                    t = t.right;  // break ---> means to switch to most left again ...
                    break;
                } else {
                    // has item in stack ---> back track
                    if(!s.isEmpty()){
                        t = s.pop();
                        visitor.visit(t);
                    } else {
                        // no item in stack
                        // break --->
                        System.out.print("end");
                        break;
                    }
                }
            }

            // this is the end condition ...
            if(s.isEmpty() && t.left==null && t.right==null){
                break;
            }
        }
    }




    private void posOrder1(Node root, Node.Visitor visitor){
        if(root.left!=null){
            posOrder1(root.left,visitor);
        }
        if(root.right!=null){
            posOrder1(root.right,visitor);
        }
        visitor.visit(root);
    }



    private void posOrder2(Node root, Node.Visitor visitor){
        Stack<Node> s = new Stack();
        Node t = root;

        // move to most left
        while (t.left!=null){
            s.push(t);
            t = t.left;
        }

        while(true){
            if(t.right==null){
                visitor.visit(t);
                if(!s.isEmpty()){
                    t = s.pop();
                }

            }else{
                s.push(t);
                t = t.right;

            }
        }




    }


}
