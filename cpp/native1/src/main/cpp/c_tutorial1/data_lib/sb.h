//
// Created by wei wang on 2022-02-11.
//

/**
 *  simble string builder ....
 */

#ifndef TESTC2_SB_H
#define TESTC2_SB_H

#define SB_DEFAUL_SIZE 8

typedef struct Sb
{
    char *str;
    int len;
    int size;
} Sb;

int SB_init(Sb* p);
int SB_append(Sb* sb, char* str);
void SB_print(Sb* p);
void SB_free(Sb* p,int onHeap);

#endif //TESTC2_SB_H
