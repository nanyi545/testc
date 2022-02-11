//
// Created by wei wang on 2022-02-11.
//

/**
 * gcc test.c arraylist.c -o test
 *
 * gcc test.c sb.c -o test
 *
 */

#include <stdio.h>
#include "arraylist.h"
#include "sb.h"

int main() {
//    ArrayList list = {0};
//    // 初始化列表
//    int r = AL_init(&list);
//    AL_add(&list, 100);
//    AL_print(&list);


// no need to free ---> char arr[3] = "bo";
// https://stackoverflow.com/questions/21513666/how-to-free-memory-from-char-array-in-c
//
// Local variables are automatically freed when the function ends, you don't need to free them by yourself.
// You only free dynamically allocated memory (e.g using malloc) as it's allocated on the heap:
//
// A simple rule for you to follow is that you must only every call free() on a pointer that was returned by a call to malloc, calloc or realloc.
    Sb sb = {0};
    SB_init(&sb);
    SB_append(&sb,"12");
    SB_print(&sb);
    SB_append(&sb,"345");
    SB_print(&sb);
    SB_append(&sb,"6789");
    SB_print(&sb);
    SB_append(&sb,"abcde");
    SB_print(&sb);
    SB_append(&sb,"fghijklmnopqrstuvwxyzlmnopqrstuvwxyzlmnopqrstuvwxyzlmnopqrstuvwxyz");
    SB_print(&sb);
    SB_free(&sb,0);
}