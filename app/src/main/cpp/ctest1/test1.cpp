//
// Created by wei wang on 2021-07-02.
//


#include "test1.h"
#include <android/log.h>



test1::test1() {
    action2();
}
test1::~test1() {

}


/**
 * if a method is envoked, you will have to implement it....
 */

void test1::action1() {
    __android_log_print(ANDROID_LOG_VERBOSE, "test1","--test1   action1--");

    // struct with no init
    Point p;
    p.x = 100;
    p.y = 200;
    __android_log_print(ANDROID_LOG_VERBOSE, "test1","p:[%d,%d]",p.x, p.y);

    // struct with initialization
    Point p1 = { 99 };
    __android_log_print(ANDROID_LOG_VERBOSE, "test1","p1:[%d,%d]",p1.x, p1.y);

}

//void test1::action2() {
//}