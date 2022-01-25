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
    mnode *root=NULL;  // you need to init the pointer as  NULL
    __android_log_print(ANDROID_LOG_VERBOSE, "ll1","----:%d   pointer:%p", (root==NULL),root);


    /**
     * https://stackoverflow.com/questions/3825668/checking-for-null-pointer-in-c-c
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

    root = (malloc(sizeof(mnode)));
    root->val = 4;
    root->next = (malloc(sizeof(mnode)));
    root->next->val = 3;
    root->next->next = (malloc(sizeof(mnode)));
    root->next->next->val = 2;
    root->next->next->next = NULL;

    __android_log_print(ANDROID_LOG_VERBOSE, "ll1","root value:%d ", root->val);


    mnode *p;

    p = root;

    while(p!=NULL){
        __android_log_print(ANDROID_LOG_VERBOSE, "ll1","node value:%d ", p->val);
        p = p->next;
    }

}



void testRecurse(int a){
    __android_log_print(ANDROID_LOG_VERBOSE, "recurse"," recurse:%d ", a);
    if(a<10){
        testRecurse(a+1);
    }
}


double average ( int num, ... )
{
    va_list arguments;
    double sum = 0;

    /* Initializing arguments to store all values after num */
    va_start ( arguments, num );
    /* Sum all the inputs; we still rely on the function caller to tell us how
     * many there are */
    for ( int x = 0; x < num; x++ )
    {
        sum += va_arg ( arguments, double );
    }
    va_end ( arguments );                  // Cleans up the list

    return sum / num;
}


void recurseTree(tnode* root){
    if(root==NULL){
        return;
    }
    __android_log_print(ANDROID_LOG_VERBOSE, "recurseTree"," node :%d ", root->val);
    recurseTree(root->left);
    recurseTree(root->right);
}

void testTree( ){
    tnode* root = malloc(sizeof(tnode));
    root->val = 1;
    root->left = malloc(sizeof(tnode));
    root->left->val = 2;
    root->left->left = NULL;
    root->left->right = NULL;
    root->right = malloc(sizeof(tnode));
    root->right->val = 3;
    root->right->left = NULL;
    root->right->right = NULL;
    recurseTree(root);
}
