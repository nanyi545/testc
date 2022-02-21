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
    printf("max:%d \n",max);
//    int* arr = malloc(sizeof(int)*max);
//    for (int i=0;i<edgesSize;i++){
//        arr[i] = 0;
//    }
//    for (int i=0;i<edgesSize;i++){
//        for(int j=0;j<*edgesColSize;j++){
//            int t = *(*(edges+i)+j);
//            arr[t]++;
//        }
//    }
//
//    for (int i=0;i<edgesSize;i++){
//        for(int j=0;j<*edgesColSize;j++){
//            int t = *(*(edges+i)+j);
//            if(arr[t]==edgesSize){
//                return arr[t];
//            }
//        }
//    }

    return -1;
}

int main() {
    int arr[3][2]={{1,2},{3,2},{0,2}};
    int s = 2;

    // 1d array
//    int arr1[3]={1,2,3};
//    int *p1 = arr1;
//    printf("p:%x\n",arr1);
//    printf("p:%x\n",p1);
//    printf("p0:%x\n",&arr1[0]);
//    printf("p1:%x\n",&arr1[1]);
//    printf("p2:%x\n",&arr1[2]);

    // 2d array
//    int **p2 = NULL;
//    int *p1_ = NULL;
//    for (int i=0;i<3;i++){
//        for (int j=0;j<2;j++){
//            p1_ = arr[i];
//            p2 = &p1_;
//            printf("i:%d,j:%d,v:%d\n",i,j,*(*(p2)+j));
//        }
//    }

    int** pp = &arr[0];



    printf("p0-0:%x\n",*(*(&arr[0])+0));
    printf("p0-1:%x\n",*(*(&arr[0])+1));

    printf("p1:%x\n",&arr[1]);
    printf("p2:%x\n",&arr[2]);


//    findCenter( &arr[0] ,3, &s);
    return 0;
}

