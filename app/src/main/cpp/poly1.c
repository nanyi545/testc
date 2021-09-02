//
// Created by wei wang on 2021-09-02.
//
//   1 way of polymorphism in c ....

#include <malloc.h>
#include "poly1.h"


/**
 *
 * https://stackoverflow.com/questions/56533288/why-is-typedef-struct-x-x-allowed
 *
 *
The line typedef struct point point; does two things:

It creates a forward declaration of struct point
It creates a type alias for struct point called point.

 */

typedef struct poly1 poly1;


/**
 *
 * https://cs50.stackexchange.com/questions/6070/why-do-i-get-an-undefined-identifier-error-even-though-the-variable-is-defined
 *
 *   In C all variable names have to be declared before they are used.
 */
int func1(poly1 *item);
int func2(poly1 *item);
int func3(poly1 *item);



/**
 * https://www.geeksforgeeks.org/difference-between-malloc-and-calloc-with-examples/
 *
 *  malloc() and calloc()
 *
 *
 */
poly1* getPoly1(int type){
    poly1* p;
    switch (type){
        case 1:
             p = (poly1*) calloc(1,sizeof(poly1));
             p->compute = func1;
             p->w = 10;
             p->h = 10;
            return p;
        case 2:
            p = (poly1*) calloc(1,sizeof(poly1));
            p->compute = func2;
            p->w = 10;
            p->h = 10;
            return p;
        case 3:
            p = (poly1*) calloc(1,sizeof(poly1));
            p->compute = func3;
            p->w = 10;
            p->h = 10;
            return p;
        default:
            return p;
    }
}

int func1(poly1 *item){
    return item->h + item->w;
}

int func2(poly1 *item){
    return item->h * item->w;
}

int func3(poly1 *item){
    return item->h - item->w;
}
