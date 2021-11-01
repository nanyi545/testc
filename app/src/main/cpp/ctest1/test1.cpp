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
 *
 * function pointers :
 *
 *
 * https://www.learncpp.com/cpp-tutorial/function-pointers/
 *
 *
 */

int foo(){
    return 5;
}
const int foo1(){ return 5;}


// fcnPtr is a pointer to a function that takes no arguments and returns an integer
int (*fcnPtr)();
// The parenthesis around *fcnPtr are necessary for precedence reasons,
//
// as int *fcnPtr() would be interpreted as a forward declaration for a function named fcnPtr
// that takes no parameters and returns a pointer to an integer.



//  To make a const function pointer, the const goes after the asterisk:
int (*const fcnPtr2)() = nullptr;

//  function being pointed to would return a const int
const int (* fcnPtr3)();





/**
 * if a method is envoked, you will have to implement it....
 */

void test1::action1() {
    __android_log_print(ANDROID_LOG_VERBOSE, "test1","--test1   action1--");

    // struct with no init
    Point p;
    p.x = 100;
    p.y = 200;
    p.context = this;
    __android_log_print(ANDROID_LOG_VERBOSE, "test1","p:[%d,%d]",p.x, p.y);

    /**
     *   use of this ....  refers to class instance ....
     *
     */
    reinterpret_cast<test1*>(p.context)->action2();


    // struct with initialization
    Point p1 = { 99 };
    __android_log_print(ANDROID_LOG_VERBOSE, "test1","p1:[%d,%d]",p1.x, p1.y);

    Point2 p2 = {88,99};
    __android_log_print(ANDROID_LOG_VERBOSE, "test1","p2:[%d,%d]",p2.x, p2.y);


    __android_log_print(ANDROID_LOG_VERBOSE, "test1","function pointer1:%d",foo);
    __android_log_print(ANDROID_LOG_VERBOSE, "test1","function pointer2:%d", reinterpret_cast<void*>(foo));
    __android_log_print(ANDROID_LOG_VERBOSE, "test1","function pointer3:%x",foo);
    __android_log_print(ANDROID_LOG_VERBOSE, "test1","function pointer4:%x", reinterpret_cast<void*>(foo));


    // assign function pointers
    fcnPtr = &foo;

    fcnPtr3= &foo1;


    // function pointer with initialization
    int (*fcnPtr4)(){ &foo };



    /**
     *
     * One common mistake is to do this:
     *
fcnPtr = goo();

This would actually assign the return value from a call to function goo() to fcnPtr

     */


    //  call by explicit dereference:
    __android_log_print(ANDROID_LOG_VERBOSE, "test1","function pointer by explicit dereference:%d",(*fcnPtr)());

    //  call by implicit dereference
    __android_log_print(ANDROID_LOG_VERBOSE, "test1","function pointer by implicit dereference:%d",fcnPtr());

}

//void test1::action2() {
//}