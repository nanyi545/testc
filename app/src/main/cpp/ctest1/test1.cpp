//
// Created by wei wang on 2021-07-02.
//


#include "test1.h"

// mac
//#include "../../../../../../../Library/Android/sdk/ndk/22.0.7026061/toolchains/llvm/prebuilt/darwin-x86_64/sysroot/usr/include/android/log.h"

// ubuntu
#include "../../../../../../../../../../home/ww/Android/Sdk/ndk/21.0.6113669/toolchains/llvm/prebuilt/linux-x86_64/sysroot/usr/include/android/log.h"

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




}

//void test1::action2() {
//}