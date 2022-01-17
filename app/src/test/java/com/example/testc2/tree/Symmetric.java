package com.example.testc2.tree;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


/**
 * Given the root of a binary tree, check whether it is a mirror of itself (i.e., symmetric around its center).
 *
 * 链接：https://leetcode-cn.com/problems/symmetric-tree
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [1,2,2,3,4,4,3]
 * Output: true
 * Example 2:
 *
 *
 * Input: root = [1,2,2,null,3,null,3]
 * Output: false
 *
 *
 *
 *
 */
public class Symmetric {

    @Test
    public void test5(){
        int a  = Integer.MIN_VALUE;
        int b  = Integer.MIN_VALUE;
        int c  = Integer.MAX_VALUE;
        System.out.println("eq:"+(a==b));
        System.out.println("not eq:"+(a!=b));

        System.out.println("min:"+a+"  max:"+c);
    }


    @Test
    public void test6(){
        List<Integer> left = new ArrayList();
        List<Integer> right = new ArrayList();
        left.add(Integer.MIN_VALUE);
        right.add(Integer.MIN_VALUE);
        /**
         *
         * Integer.MIN_VALUE might no equal to  Integer.MIN_VALUE
         *
         *
         */
        System.out.println("eq1:"+(left.get(0)==right.get(0)));
        System.out.println("eq2:"+(left.get(0).equals(right.get(0))));
        System.out.println("class:"+(left.get(0).getClass().getName()));  // java.lang.Integer
    }



    @Test
    public void test1(){
        Node root = new Node(1);
        root.left = new Node(2);
        root.left.left = new Node(3);
        root.left.right = new Node(4);
        root.right = new Node(2);
        root.right.left = new Node(4);
        root.right.right = new Node(3);

        List<Integer> left = new ArrayList();
        List<Integer> right = new ArrayList();
        getLeft(root.left,left);
        getRight(root.right,right);
        System.out.println(left);
        System.out.println(right);

        for (int i=0;i<left.size();i++){
            if(left.get(i).equals(right.get(i))){
                System.out.println("eq   l:"+left.get(i)+"  r:"+right.get(i));
            }else {
                System.out.println("no   l:"+left.get(i)+"  r:"+right.get(i));
            }
        }


        System.out.println(isSymmetric(root));

    }



    public boolean isSymmetric(Node root) {
        if(root==null){
            return true;
        }
        List<Integer> left = new ArrayList();
        List<Integer> right = new ArrayList();
        getLeft(root.left,left);
        getRight(root.right,right);
        int sizel = left.size();
        int sizer = right.size();
        if(sizel!=sizer){
            return false;
        }
        for (int i=0;i<sizel;i++){
            if(!(left.get(i).equals(right.get(i)))){
                return false;
            }
        }
        return true;
    }

    public void getLeft(Node leftR, List<Integer> list){
        if(leftR==null){
            return;
        }
        list.add(leftR.val);
        if(leftR.left!=null){
            getLeft(leftR.left,list);
        } else {
            list.add(Integer.MIN_VALUE);
        }
        if(leftR.right!=null){
            getLeft(leftR.right,list);
        }else {
            list.add(Integer.MIN_VALUE);
        }
    }

    public void getRight(Node rightR, List<Integer> list){
        if(rightR==null){
            return;
        }
        list.add(rightR.val);
        if(rightR.right!=null){
            getRight(rightR.right,list);
        }else {
            list.add(Integer.MIN_VALUE);
        }
        if(rightR.left!=null){
            getRight(rightR.left,list);
        }else {
            list.add(Integer.MIN_VALUE);
        }
    }

}
