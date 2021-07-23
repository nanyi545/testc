//
// Created by wei wang on 2021-07-06.
//

#include "test1.h"
#include <android/log.h>


void test1::order(Point* p, int size, Compare comparator) {
    bool b1 = comparator(p, p+1);
    __android_log_print(ANDROID_LOG_VERBOSE, "test1", "result:%d", b1);
}
