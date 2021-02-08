#include <jni.h>
#include <string>
#include <android/log.h>

struct Person{
    int age;
    std::string name;
};

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_testc2_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    Person *p1 = static_cast<Person *>(malloc(sizeof(Person)));
    std::string hello = "Hello from C++";

    //std::string to char*
    //   https://stackoverflow.com/questions/7352099/stdstring-to-char
    const char *cstr = hello.c_str();
    const char *cstr2 = "99999";
    int a = 123;
    __android_log_print(ANDROID_LOG_VERBOSE, "hehe", "s1:%s  s2:%s  int:%d",cstr,cstr2,a);

    return env->NewStringUTF(hello.c_str());
}
