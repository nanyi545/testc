//
// Created by wei wang on 2022-02-11.
//

#include <string.h>
#include "arraylist.h"
#include "sb.h"


char* get_element_string(Element e){
    int ENOUGH = (int)((ceil(log10(e))+1)*sizeof(char));
    char* str = (char*) malloc(ENOUGH * sizeof(char));
    sprintf(str, "%d", e);
    return str;
}

char c1 = ',';
char* pc1 = &c1;

void AL_print(ArrayList *lst){
    if (lst == NULL){
        printf("empty");
        return;
    }
    Sb sb = {0};
    SB_init(&sb);
    for (int i=0;i<lst->length;i++){
        Element e = lst->container[i];
        char* s = get_element_string(e);
        SB_append(&sb,s);
        SB_append(&sb,pc1);
//        printf("i:%d  s:%s size:%lu",i, s, strlen(s));
    }
    printf("arr:%s \n",SB_print(&sb,0));
}


int AL_init(ArrayList *lst){
    if (lst == NULL){
        return ERROR;
    }
    lst->length = 0;
    lst->capacity = DEFAULT_CAPACITY;
    lst->container = calloc(DEFAULT_CAPACITY,sizeof(int));
    if (lst->container == NULL){
        return ERROR;
    }
    return 0;
}

int AL_add(ArrayList *lst,Element element){
    if (lst == NULL){
        return ERROR;
    }
    if (lst->length < lst->capacity){
        lst->container[lst->length] = element;

        lst->length ++;
    }else{
        int newSize = lst->capacity*2;
        int *tmp = realloc(lst->container, newSize*sizeof(int));
        if (tmp == NULL){
            printf("realloc error\n");
            return ERROR;
        }
        lst->capacity = newSize;
        lst->container = tmp;
        lst->container[lst->length] = element;
        lst->length ++;
    }
    return OK;
}

int AL_remove(ArrayList *lst,int position){
    if (lst == NULL || position >= lst->length){
        return ERROR;
    }

    for (int i = position; i < lst->length-1; i++){
        lst->container[i] = lst->container[i+1];
    }
    lst->length --;
    return OK;
}

int AL_get(ArrayList *lst,int position,Element *element){
    if (position < lst->length){
        *element = lst->container[position];
        return OK;
    }else{
        return ERROR;
    }
}
