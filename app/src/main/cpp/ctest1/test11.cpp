//
// Created by wei wang on 2021-07-02.
//

#include "test1.h"
#include "../../../../../../../Library/Android/sdk/ndk/22.0.7026061/toolchains/llvm/prebuilt/darwin-x86_64/sysroot/usr/include/android/log.h"


/**
 * you can implement different functions in different files ...
 *
 */

void test1::action2() {
    __android_log_print(ANDROID_LOG_VERBOSE, "test1","--test1_   action2--");
}

