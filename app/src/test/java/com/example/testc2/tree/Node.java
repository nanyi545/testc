package com.example.testc2.tree;

import androidx.annotation.NonNull;

class Node {
    public int val;
    public Node left;
    public Node right;
    public Node next;

    public Node() {}

    public Node(int _val) {
        val = _val;
    }

    public Node(int _val, Node _left, Node _right, Node _next) {
        val = _val;
        left = _left;
        right = _right;
        next = _next;
    }

    interface Visitor {
        void visit(Node n);
    }

    @NonNull
    @Override
    public String toString() {
        return "node value:"+val;
    }
}
