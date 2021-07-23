//
// Created by wei wang on 2021-07-02.
//

#ifndef TESTC2_TEST1_H
#define TESTC2_TEST1_H

#include <functional>


struct Point {
    int x;
    int y;
    void* context;
};

typedef struct {
    int x;
    int y;
} Point2;


/**
 *
 * function pointers :
 *
 * https://www.learncpp.com/cpp-tutorial/function-pointers/
 *
 */


/**
 *  Compare  --> a function pointer that takes 2 points, and returns a bool
 */
typedef bool (*Compare)(Point* p1,Point* p2);

/**
 * OperatorFun --> a function pointer that take 2 int, and return a int
 */
using OperatorFun = int(*)(int, int);


class test1 {
private:
    int a;
    int b;
public:
    test1();
    ~test1();
    void action1();
    void action2();
    void order(Point* p, int size, Compare comparator );
    int doOp(int i1, int i2, OperatorFun op );

};

#endif //TESTC2_TEST1_H
