#include <jni.h>
#include <android/log.h>

extern "C" {
#include "ijksdl/android/ijksdl_android_jni.h"
}

//  ..... cpp tests .....

// Next is the prototype of the function
int mult ( int x, int y );
void testPointer1 ();

//  ..... end of cpp tests .....



JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    __android_log_print(ANDROID_LOG_VERBOSE, "jni_onload","jni onload---native lib1");
    SDL_JNI_OnLoad(vm,reserved);
    __android_log_print(ANDROID_LOG_VERBOSE, "jni_onload","print a int :%d",mult(3,4));
    testPointer1();
    return JNI_VERSION_1_4;
}




//  ..... cpp tests .....
int mult (int x, int y)
{
    return x * y;
}


// A note about terms: the word pointer can refer either to a memory address itself, or to a variable that stores a memory address.
// Usually, the distinction isn't really that important:

// address operator (&) to get the address of the variable.
// Using the ampersand is a bit like looking at the label on the safety deposit box to see its number rather than looking inside the box, to get what it stores.
void testPointer1(){
    int a = 1;
    int* p  = &a;
    __android_log_print(ANDROID_LOG_VERBOSE, "pointer1","pointer address :%d",p);
    __android_log_print(ANDROID_LOG_VERBOSE, "pointer1","pointer address +1 :%d",(p+1));
    __android_log_print(ANDROID_LOG_VERBOSE, "pointer1","pointer address :%p",p);
    __android_log_print(ANDROID_LOG_VERBOSE, "pointer1","pointer address +1 :%p",(p+1));
    __android_log_print(ANDROID_LOG_VERBOSE, "pointer1","pointer sizeof int pointer size:%d",(sizeof(p)));
    __android_log_print(ANDROID_LOG_VERBOSE, "pointer1","pointer sizeof int size:%d",(sizeof(a)));


    // %p hex form
    // %d int
    //
    //  https://stackoverflow.com/questions/5610298/why-does-int-pointer-increment-by-4-rather-than-1
    //  When you increment a T*, it moves sizeof(T) bytes.†



    __android_log_print(ANDROID_LOG_VERBOSE, "pointer1","pointer value :%d",(*p));

}




//  ..... end of cpp tests .....
