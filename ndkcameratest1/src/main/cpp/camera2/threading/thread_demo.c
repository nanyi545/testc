//
// Created by wei wang on 2021-11-08.
//

#include "thread_demo.h"


long iii = 0;
pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;
int call1(void * parm) {
//    LOGI("call1  gettid:%d",(int)gettid());
//    for (long i=0;i<100000000;i++){
//        pthread_mutex_lock(&mutex);
//        iii++;
//        pthread_mutex_unlock(&mutex);
//    }
//    LOGI("call1  --long:%d",iii);
    return 1;
}

int call2(void * parm) {
    LOGI("call2  gettid:%d",(int)gettid());
    for (long i=0;i<100000000;i++){
        pthread_mutex_lock(&mutex);
        iii++;
        pthread_mutex_unlock(&mutex);
    }
    LOGI("call2  --long:%d",iii);
    return 2;
}

void* work(void * parm) {
    while(1){
        sleep(2);
        LOGI("worker  gettid:%d",(int)gettid());
    }
}


void test1() {
    LOGI("thread_demo  test1");


    // demo 1 of pthreading...
//    pthread_t id;
//    pthread_create(&id, NULL, work, NULL);


    // demo 2 of pthreading... more elegant way + sync with mutex_lock
    SDL_Thread tid1;
    SDL_CreateThreadEx(&tid1, call1, &iii, "calll1_thread");


}
