//
// Created by wei wang on 2022-02-18.
//
//   https://leetcode-cn.com/problems/find-center-of-star-graph/
//
//   gcc p1791.c -o o1


#include <stdio.h>
#include <stdlib.h>



int findCenter(int** edges, int edgesSize, int* edgesColSize){
    int max = 0;
    for (int i=0;i<edgesSize;i++){
        for(int j=0;j<*edgesColSize;j++){
            int t = *(*(edges+i)+j);
            if(t>max){
                max = t;
            }
        }
    }
    int* arr = malloc(sizeof(int)*max);
    for (int i=0;i<edgesSize;i++){
        arr[i] = 0;
    }
    for (int i=0;i<edgesSize;i++){
        for(int j=0;j<*edgesColSize;j++){
            int t = *(*(edges+i)+j);
            arr[t]++;
        }
    }

    for (int i=0;i<edgesSize;i++){
        for(int j=0;j<*edgesColSize;j++){
            int t = *(*(edges+i)+j);
            if(arr[t]==edgesSize){
                return arr[t];
            }
        }
    }

    return -1;
}

int main() {
    printf("1+2\n");
    return 0;
}

