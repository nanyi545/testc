#include <jni.h>
#include <android/log.h>
#include <stdlib.h>

extern "C" {
#include "ijksdl/android/ijksdl_android_jni.h"
#include "test1.h"
}

//  ..... cpp tests .....

// Next is the prototype of the function
int mult ( int x, int y );
void testPointer1 ();
void testPointer2 ();

// struct declare
struct person {
    int age;
    float salary;
};

void testStruct1 ();
void testArr1 ();
void testStr1 ();


//  ..... end of cpp tests .....



JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    __android_log_print(ANDROID_LOG_VERBOSE, "jni_onload","jni onload---native lib1");
    SDL_JNI_OnLoad(vm,reserved);
    __android_log_print(ANDROID_LOG_VERBOSE, "jni_onload","print a int :%d",mult(3,4));

    call1(10,6);
    testPointer1();
    testPointer2();
    testStruct1();
    testArr1();
    testStr1();
    testLinkedList1();

    return JNI_VERSION_1_4;
}




//  ..... cpp tests .....
int mult (int x, int y)
{
    int a = 9;
    int b = 10;
    int c = 9;
    __android_log_print(ANDROID_LOG_VERBOSE, "testbool","test bool1 :%d",(a==b));
    __android_log_print(ANDROID_LOG_VERBOSE, "testbool","test bool2 :%d",(a==c));
    return x * y;
}


// A note about terms: the word pointer can refer either to a memory address itself, or to a variable that stores a memory address.
// Usually, the distinction isn't really that important:

// address operator (&) to get the address of the variable.
// Using the ampersand is a bit like looking at the label on the safety deposit box to see its number rather than looking inside the box, to get what it stores.

/**
 * https://www.cprogramming.com/tutorial/c/lesson6.html
 */
void testPointer1(){
    int a = 1;
    int* p  = &a;
    __android_log_print(ANDROID_LOG_VERBOSE, "pointer1","pointer address :%d",p);
    __android_log_print(ANDROID_LOG_VERBOSE, "pointer1","pointer address +1 :%d",(p+1));
    __android_log_print(ANDROID_LOG_VERBOSE, "pointer1","pointer address :%p",p);
    __android_log_print(ANDROID_LOG_VERBOSE, "pointer1","pointer address +1 :%p",(p+1));
    __android_log_print(ANDROID_LOG_VERBOSE, "pointer1","pointer sizeof int pointer size:%d",(sizeof(p)));  // 8? 4?
    __android_log_print(ANDROID_LOG_VERBOSE, "pointer1","pointer sizeof int size:%d",(sizeof(a)));
    // %p hex form
    // %d int
    //
    //  https://stackoverflow.com/questions/5610298/why-does-int-pointer-increment-by-4-rather-than-1
    //  When you increment a T*, it moves sizeof(T) bytes.†
    __android_log_print(ANDROID_LOG_VERBOSE, "pointer1","pointer value :%d",(*p));
//    __android_log_print(ANDROID_LOG_VERBOSE, "pointer1","pointer value 2:%d",(*(p+1024*1024)));
    /**
     *  For instance, the operating system will probably prevent you from accessing memory that it knows your program doesn't own: this will cause your program to crash.
     *
     *
2022-01-24 22:56:03.723 22013-22013/com.ww.cppnativeapp A/libc: Fatal signal 11 (SIGSEGV), code 1 (SEGV_MAPERR), fault addr 0x7fffbe660574 in tid 22013 (ww.cppnativeapp), pid 22013 (ww.cppnativeapp)
2022-01-24 22:56:03.776 22040-22040/? A/DEBUG: *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***
2022-01-24 22:56:03.776 22040-22040/? A/DEBUG: Build fingerprint: 'Android/sdk_phone_x86_64/generic_x86_64:9/PSR1.180720.012/4923214:userdebug/test-keys'
2022-01-24 22:56:03.777 22040-22040/? A/DEBUG: Revision: '0'
2022-01-24 22:56:03.777 22040-22040/? A/DEBUG: ABI: 'x86_64'
2022-01-24 22:56:03.777 22040-22040/? A/DEBUG: pid: 22013, tid: 22013, name: ww.cppnativeapp  >>> com.ww.cppnativeapp <<<
2022-01-24 22:56:03.777 22040-22040/? A/DEBUG: signal 11 (SIGSEGV), code 1 (SEGV_MAPERR), fault addr 0x7fffbe660574
2022-01-24 22:56:03.777 22040-22040/? A/DEBUG:     rax 000000000000001b  rbx 0000730876187f60  rcx 9a9f6fab5c1f9afc  rdx 00007fffbe260574
2022-01-24 22:56:03.777 22040-22040/? A/DEBUG:     r8  0000000000000008  r9  00000000ffffffff  r10 00007fffbe25f9d0  r11 0000000000000206
     */

}


/**
 * It is also possible to initialize pointers using free memory. This allows dynamic allocation of memory.
 * It is useful for setting up structures such as linked lists or data trees where you don't know exactly how much memory will be needed at compile time,
 * so you have to get memory during the program's execution.
 *
 *
 * https://www.cprogramming.com/tutorial/c/lesson6.html
 *
 */
void testPointer2(){
    // #include <stdlib.h>
//    int *ptr = static_cast<int *>(malloc(sizeof(int)));

    // Note that it is slightly cleaner to write malloc statements by taking the size of the variable pointed to by using the pointer directly:
    int *ptr = static_cast<int *>(malloc(sizeof(*ptr)));
    int *ptr2 = static_cast<int *>(malloc(sizeof(*ptr)));
    *ptr = 1;
    __android_log_print(ANDROID_LOG_VERBOSE, "pointer2","pointer address1 :%p",(ptr));
    __android_log_print(ANDROID_LOG_VERBOSE, "pointer2","pointer address2 :%p",(ptr2));
    __android_log_print(ANDROID_LOG_VERBOSE, "pointer2","pointer value :%d",(*ptr));
    free( ptr );
    free( ptr2 );

}


void testStruct1(){
    person p1;
    p1.age = 22;
    p1.salary = 12000.21;
    __android_log_print(ANDROID_LOG_VERBOSE, "struct1","p1 age:%d  salary:%f",(p1.age),(p1.salary));
    __android_log_print(ANDROID_LOG_VERBOSE, "struct1","p1 size:%d  float size:%d",sizeof(p1), sizeof(p1.salary));
    __android_log_print(ANDROID_LOG_VERBOSE, "struct1","person size:%d  struct person size:%d",sizeof(person), sizeof(struct person));

    person* p2;
    p2 = static_cast<person *>(malloc(sizeof(person)));
    p2->age = 11;
    p2->salary = 300;
    __android_log_print(ANDROID_LOG_VERBOSE, "struct1","p2 age:%d  salary:%f",(p2->age),(p2->salary));

}

/**
 * https://www.cprogramming.com/tutorial/c/lesson8.html
 *
 */
void testArr1(){
    int arr[100]; /* This declares an array */
    __android_log_print(ANDROID_LOG_VERBOSE, "arr1","arr size:%d ", sizeof(arr) );
    // Let me note again that you should never attempt to write data past the last element of the array,
//    arr[100]= 10;  // may or may not... cause crash ...
/**
01-25 12:46:13.383 3433-3433/? I/DEBUG: signal 6 (SIGABRT), code -6 (SI_TKILL), fault addr --------
01-25 12:46:13.399 3433-3433/? I/DEBUG: Abort message: 'stack corruption detected'
01-25 12:46:13.402 3433-3433/? I/DEBUG:     r0 00000000  r1 00000151  r2 00000006  r3 00000000
 */

//    arr[101]= 11;
//    arr[100000]= 12;  // cause crash ...
}


#include <string.h>   /* for all the new-fangled string functions */

void testStr1(){
    char str[5];
    str[0]='a';
    str[1]='b';
//    str[2]='c';
    str[2]='\0';
//    ('a'-0)  --> 97
//    default char = 0   --> '\0'
    __android_log_print(ANDROID_LOG_VERBOSE, "str1","str size:%d   %s  a:%d  terminator1:%d terminator2:%d", sizeof(str), str,('a'-0) ,
                        (str[2]-0), (str[3]-0));

    char s1[50];
    char s2[50];
    char s3[50];

    s1[0] = 'd';
    s1[1] = 'a';
    s1[2] = 'v';
    s1[3] = 'i';
    s1[4] = 0;

    s2[0] = 'w';
    s2[1] = 'x';
    s2[2] = 'm';
    s2[3] = 0;

    s3[0]=0;
    // you need '\0' at end to get correct string length

    __android_log_print(ANDROID_LOG_VERBOSE, "str1","str length:%d  ", strlen(s1) );


    // string concat
    strcat( s3, s1 );     /* Copy s1 into s3 */
    strcat( s3, "---" );      /* Separate the names by a space */
    strcat( s3, s2 ); /* Copy s2 onto the end of s3 */
    __android_log_print(ANDROID_LOG_VERBOSE, "str1","s3:%s ", s3 );


    //
    int i = strcmp("b","a");
    __android_log_print(ANDROID_LOG_VERBOSE, "str1","string compare :%d ", i );

    char s5[] = "ijkplayer";
    __android_log_print(ANDROID_LOG_VERBOSE, "str1","direct use string:%s ", s5 );

}



//  ..... end of cpp tests .....
