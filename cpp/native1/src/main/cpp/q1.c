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


void q1(){
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


int getFewestCoins(int* coins, int coinsSize, int amount){
    int dp[amount+1];
    dp[0] = 0;
    for (int i=1;i<=amount;i++){
        dp[i] = 10000;
        for (int j=0;j<coinsSize;j++){
            int preIndex = i - coins[j];
            if(preIndex>=0){
                int t = dp[preIndex] + 1;
                if(t<dp[i]){
                    dp[i] = t;
                }
            }
        }
    }
    return dp[amount];  // 如果10000 表示没有解
}


void q2(){
    int coins[2];
    coins[0] = 1;
    coins[1] = 5;
    getFewestCoins(coins,2,5);
}




int change(int amount, int* coins, int coinsSize) {
    int dp[amount + 1];
    memset(dp, 0, sizeof(dp));
    dp[0] = 1;
    for (int i = 0; i < coinsSize; i++) {
        //因此需要遍历 coins，对于其中的每一种面额的硬币，更新数组
        //dp 中的每个大于或等于该面额的元素的值。

        //  由于顺序确定，因此不会重复计算不同的排列     免于重复计算 ...
        for (int j = coins[i]; j <= amount; j++) {
            dp[j] += dp[j - coins[i]];
//            dp[j] = dp[j] + dp[j - coins[i]];
        }
//        for( int k = 0 ; k<=amount ; k++){
//            __android_log_print(ANDROID_LOG_VERBOSE, "q3","i:%d k:%d  dp[k]:%d",i,k,dp[k]);
//        }
    }
    return dp[amount];
}



void q3(){
    int coins[3];
    coins[0] = 5;
    coins[1] = 2;
    coins[2] = 1;
    for (int i=1;i<=10;i++){
        int r = change(i,coins,3);
        __android_log_print(ANDROID_LOG_VERBOSE, "q3"," target:%d  r:%d",i,r);
    }

//    int r = change(5,coins,3);

}