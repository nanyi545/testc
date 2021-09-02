//
// Created by wei wang on 2021-09-02.
//

#ifndef TESTC2_POLY1_H
#define TESTC2_POLY1_H

typedef struct poly1 poly1;

struct poly1 {
    int w;
    int h;
    int (*compute)(poly1 *item);
};

poly1* getPoly1(int type);

#endif //TESTC2_POLY1_H
