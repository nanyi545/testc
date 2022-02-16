//
// Created by wei wang on 2022-02-11.
//

#include "sb.h"
#include <string.h>
#include <stdlib.h>
#include <stdio.h>

int SB_init(Sb* sb){
    if(sb==NULL){
        return -1;
    }
    sb->len = 0;
    sb->size = SB_DEFAUL_SIZE;
    sb->str = malloc(sizeof(char)*SB_DEFAUL_SIZE);
    *(sb->str) = 0;
    return 1;
}

int SB_append(Sb* sb, char* str){
    int new_leng  = strlen(str) + sb->len - 1;
    int new_size = new_leng+1;
    int need_resize = 0;
    while ( new_size > sb->size ){
        sb->size = sb->size*2;
        need_resize = 1;
    }
    if(need_resize){
        sb->str = realloc(sb->str,sb->size * sizeof(char));
        if(sb->str==NULL){
            return -1;
        }
    }
    strcat( (sb->str)+sb->len , str);
    sb->len = strlen(sb->str);
    return 1;
}

char* SB_print(Sb* p,int print){
    if(print){
        printf("str:%s  len:%d size:%d\n",p->str,p->len,p->size);
    }
    return p->str;
}

void SB_free(Sb* p,int onHeap){
    free(p->str);
    p->str = NULL;
    if(onHeap){
        free(p);
        p = NULL;
    }
}
