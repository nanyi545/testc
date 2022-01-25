//
// Created by wei wang on 2022-01-25.
//

#ifndef TESTC2_TEST1_H
#define TESTC2_TEST1_H

#include <android/log.h>
#include <functional>

int call1(int a,int b);


// this line adds a type alias mnode in the global name space and thus allows you to just write:
typedef struct mnode mnode;

// define a struct
struct mnode {
    int val;
    mnode* next;
};


/**
 * linked list
 * https://www.cprogramming.com/tutorial/c/lesson15.html
 */

void testLinkedList1 ();


#endif //TESTC2_TEST1_H
