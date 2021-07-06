//
// Created by wei wang on 2021-07-02.
//

#ifndef TESTC2_TEST1_H
#define TESTC2_TEST1_H

#include <functional>


struct Point {
    int x;
    int y;
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
typedef bool (*Compare)(Point* p1,Point* p2);


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
};

#endif //TESTC2_TEST1_H
