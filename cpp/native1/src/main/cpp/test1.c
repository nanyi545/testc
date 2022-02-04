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


void mergeSort(int* arr, int start, int end){
    if(start==end){
        return;
    }
    if(start==(end-1)){
        if(arr[end]>=arr[start]){
            return;
        }
        // switch in place
        arr[end] = arr[end]+arr[start];
        arr[start] = arr[end] - arr[start];
        arr[end] = arr[end] - arr[start];
        return;
    }
    int mid = (start+end)/2;
    mergeSort(arr,start,mid);
    mergeSort(arr,mid+1,end);
    int size = end-start+1;
    int* t = malloc(size*sizeof(int));
    int i1 = start;
    int i2 = mid+1;
    int count = 0;
    while(count<(size)){
        if(i1>mid){
            t[count]=arr[i2];
            count++;
            i2++;
            continue;
        }
        if(i2>end){
            t[count]=arr[i1];
            count++;
            i1++;
            continue;
        }
        if(arr[i1]<=arr[i2]){
            t[count]=arr[i1];
            count++;
            i1++;
            continue;
        } else {
            t[count]=arr[i2];
            count++;
            i2++;
            continue;
        }
    }
    for (int i=0;i<size;i++){
        arr[i+start] = t[i];
    }
    free(t);
}


void testArrMergeSort(){
    int arr1[10];
    arr1[0] = 31;arr1[1] = 11;arr1[2] = 22;arr1[3] = 76;arr1[4] = 9;
    arr1[5] = 24;arr1[6] = 88;arr1[7] = 93;arr1[8] = 26;arr1[9] = -33;
    mergeSort(arr1,0,9);
    for (int i=0;i<10;i++){
        __android_log_print(ANDROID_LOG_VERBOSE, "mergesort","int:%d  val:%d",i,arr1[i]);
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


void testReloc(){
    int* ptr = malloc(5 * sizeof(int));

    if(ptr == NULL) {}     // malloc() was unable to allocate the memory, handle the
    // error and DO NOT use this pointer anymore

    __android_log_print(ANDROID_LOG_VERBOSE, "testReloc","malloc at :%p ", ptr);

    for (int i=0;i<5;i++){
        ptr[i] = i;
    }
    for (int i=0;i<5;i++){
        __android_log_print(ANDROID_LOG_VERBOSE, "testReloc","just after malloc i:%d :%p ",ptr[i], ptr);
    }

// suppose 10 ints aren't no more enough:
    ptr = realloc(ptr, 20 * sizeof(int));
    __android_log_print(ANDROID_LOG_VERBOSE, "testReloc","realloc at :%p ", ptr);

    // after realloc  : original values are kept ...
    for (int i=0;i<10;i++){
        __android_log_print(ANDROID_LOG_VERBOSE, "testReloc","just after realloc i:%d :%p ",ptr[i], ptr);
    }
}


void testAlloc(){
    mnode** ptr;
    ptr = malloc(3 * sizeof(mnode*));
    for (int i=0;i<3;i++){
        ptr[i] = malloc(sizeof(mnode));
        ptr[i]->val = i+9 ;
    }
    __android_log_print(ANDROID_LOG_VERBOSE, "testReloc2","v0 :%d ", ptr[0]->val);
    __android_log_print(ANDROID_LOG_VERBOSE, "testReloc2","v1 :%d ", ptr[1]->val);
    __android_log_print(ANDROID_LOG_VERBOSE, "testReloc2","v2 :%d ", ptr[2]->val);
}
