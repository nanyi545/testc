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


