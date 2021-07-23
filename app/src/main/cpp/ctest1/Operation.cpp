#include "test1.h"
#include <android/log.h>


int test1::doOp(int i1, int i2, OperatorFun op ) {
    return op(i1,i2);
}
