//
// Created by wei wang on 2022-01-25.
//

#ifndef TESTC2_TEST1_H
#define TESTC2_TEST1_H

#include <android/log.h>
//#include <stddef.h>

int call1(int a,int b);


// this line adds a type alias mnode in the global name space and thus allows you to just write:
typedef struct mnode mnode;

// define a struct
//   https://stackoverflow.com/questions/12642830/can-i-define-a-function-inside-a-c-structure   //  use function pointer

struct mnode {
    int val;
    mnode* next;
};


typedef struct tnode tnode;

struct tnode {
    int val;
    tnode* left;
    tnode* right;
};


/**
 * linked list
 * https://www.cprogramming.com/tutorial/c/lesson15.html
 */

void testLinkedList1 ();

/**
 * https://www.cprogramming.com/tutorial/c/lesson16.html
 */
void testRecurse (int a);


/**
 * https://www.cprogramming.com/tutorial/c/lesson17.html
 */
double average ( int num, ... );

void recurseTree(tnode* root);

/**
 * test binary tree
 */
void testTree();

void testArrMergeSort();

/**
 * https://stackoverflow.com/questions/4701900/c-vector-arraylist-linkedlist
 */
void testReloc();
void testAlloc();

#endif //TESTC2_TEST1_H
