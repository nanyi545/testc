//
// Created by wei wang on 2022-01-25.
//

#include <malloc.h>
#include "test1.h"

int call1(int a,int b){
    __android_log_print(ANDROID_LOG_VERBOSE, "call1","1111 :%d",5);
    return a-b;
}



void testLinkedList1(){

    /* This will be the unchanging first node */
    mnode *root;
    __android_log_print(ANDROID_LOG_VERBOSE, "ll1","----:%d   ", (root==0)); // ??? todo always not null ???
    if(root==0){
        __android_log_print(ANDROID_LOG_VERBOSE, "ll1","111  " );
    } else {
        __android_log_print(ANDROID_LOG_VERBOSE, "ll1","222 " );
    }
    /**
     * https://stackoverflow.com/questions/3825668/checking-for-null-pointer-in-c-c
     *
     *
     *
     */


    /* Now root points to a node struct */
    /**
     * https://stackoverflow.com/questions/1609163/what-is-the-difference-between-static-cast-and-c-style-casting
     *
 In short:
static_cast<>() gives you a compile time checking ability, C-Style cast doesn't.
static_cast<>() is more readable and can be spotted easily anywhere inside a C++ source code, C_Style cast is'nt.
Intentions are conveyed much better using C++ casts.


More Explanation:
The static cast performs conversions between compatible types. It is similar to the C-style cast, but is more restrictive. For example, the C-style cast would allow an integer pointer to point to a char.

char c = 10;       // 1 byte
int *p = (int*)&c; // 4 bytes

     *
     */
    __android_log_print(ANDROID_LOG_VERBOSE, "ll1","node size:%d  struct node size:%d",sizeof(mnode), sizeof(struct mnode));

    root = (malloc(sizeof(struct mnode)));
    root->val = 4;
    root->next = (malloc(sizeof(struct mnode)));
    root->next->val = 3;
    root->next->next = (malloc(sizeof(struct mnode)));
    root->next->next->val = 2;

    struct mnode *p;

    p = root;
//    while(p){
//        __android_log_print(ANDROID_LOG_VERBOSE, "ll1","node value:%d ", p->val);
//        p = root->next;
//    }



}


