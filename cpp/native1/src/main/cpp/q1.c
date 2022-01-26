//
// Created by ww on 2022/1/26.
//
#include "q1.h"

/**
 * https://troydhanson.github.io/uthash/userguide.html
 * https://cxymm.net/article/lijianyi0219/109343993
 */
#include "uthash.h"


int* twoSum(int* nums, int numsSize, int target, int* returnSize){
    int* ptr = malloc(2*sizeof(int));
    *returnSize = 2;
    for (int i=0;i<numsSize-1;i++){
        for (int j=i+1;j<numsSize;j++){
            if((nums[i]+nums[j])==target){
                ptr[0]=i;
                ptr[1]=j;
                return ptr;
            }
        }
    }
    return ptr;
}


struct item {
    int key;
    int val;
    UT_hash_handle hh;
};
struct item* table=NULL;

struct item* find(int ikey) {
    struct item* tmp = malloc(sizeof(struct item));;
    HASH_FIND_INT(table, &ikey, tmp);
    return tmp;
}

void insert(int ikey, int ival) {
    struct item* it = find(ikey);
    if (it == NULL) {
        struct item* tmp = malloc(sizeof(struct item));
        tmp->key = ikey, tmp->val = ival;
        HASH_ADD_INT(table, key, tmp);
    } else {
        it->val = ival;
    }
}

int* twoSum2(int* nums, int numsSize, int target, int* returnSize){
    table = NULL;
    for (int i = 0; i < numsSize; i++) {
        struct item* it = find(target - nums[i]);
        if (it != NULL) {
            int* ret = malloc(sizeof(int) * 2);
            ret[0] = it->val, ret[1] = i;
            *returnSize = 2;
            return ret;
        }
        insert(nums[i], i);
    }
    *returnSize = 0;
    return NULL;
}




int q1(){
    int nums[4];
    nums[0] = 2;
    nums[1] = 7;
    nums[2] = 11;
    nums[3] = 4;
    int a;
//    int* r = twoSum(nums,4,13,&a);
    int* r = twoSum2(nums,4,13,&a);
    __android_log_print(ANDROID_LOG_VERBOSE, "q1"," i1:%d  i2:%d size:%d",r[0],r[1],a);
}
