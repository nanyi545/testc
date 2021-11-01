/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include "looper.h"

#include <assert.h>
#include <jni.h>
#include <pthread.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <errno.h>
#include <limits.h>
#include <semaphore.h>
#include "native_debug.h"

// for __android_log_print(ANDROID_LOG_INFO, "YourApp", "formatted message");


struct loopermessage;
typedef struct loopermessage loopermessage;

struct loopermessage {
    int what;
    void *obj;
    loopermessage *next;
    bool quit;
};



void* looper::trampoline(void* p) {
    ((looper*)p)->loop();
    return NULL;
}

looper::looper() {
    sem_init(&headdataavailable, 0, 0);
    sem_init(&headwriteprotect, 0, 1);
    pthread_attr_t attr;
    pthread_attr_init(&attr);

//    LOGI("Looper constructor  worker:%l", worker);
    unsigned char *ptc1 = (unsigned char*)(void*)(&worker);
    for (size_t i=0; i<sizeof(worker); i++) {
//        LOGI("Looper constructor called  worker    %02x", (unsigned)(ptc1[i]));
    }

    pthread_create(&worker, &attr, trampoline, this);
//    LOGI("Looper constructor call ------------ ");


    unsigned char *ptc = (unsigned char*)(void*)(&worker);
    for (size_t i=0; i<sizeof(worker); i++) {
//        LOGI("Looper constructor called  worker    %02x", (unsigned)(ptc[i]));
    }


    running = true;
}


looper::~looper() {
    if (running) {
//        LOGI("Looper deleted while still running. Some messages will not be processed");
        quit();
    }
}

void looper::post(int what, void *data, bool flush) {
    loopermessage *msg = new loopermessage();
    msg->what = what;
    msg->obj = data;
    msg->next = NULL;
    msg->quit = false;
    addmsg(msg, flush);
}

void looper::addmsg(loopermessage *msg, bool flush) {
    sem_wait(&headwriteprotect);
    loopermessage *h = head;

    if (flush) {
        while(h) {
            loopermessage *next = h->next;
            delete h;
            h = next;
        }
        h = NULL;
    }
    if (h) {
        while (h->next) {
            h = h->next;
        }
        h->next = msg;
    } else {
        head = msg;
    }
//    LOGI("Looper post msg %d", msg->what);
    sem_post(&headwriteprotect);
    sem_post(&headdataavailable);
}

void looper::loop() {
//    LOGI("Looper start ");

    while(true) {
        // wait for available message
//        LOGI("Looper wait 1 ");
        sem_wait(&headdataavailable);

//        LOGI("Looper wait 2 ");
        // get next available message
        sem_wait(&headwriteprotect);
        loopermessage *msg = head;
        if (msg == NULL) {
//            LOGI("Looper no msg");
            sem_post(&headwriteprotect);
            continue;
        }
        head = msg->next;
        sem_post(&headwriteprotect);

        if (msg->quit) {
//            LOGI("Looper quitting");
            delete msg;
            return;
        }
//        LOGI("Looper processing msg %d", msg->what);
        handle(msg->what, msg->obj);
        delete msg;
    }
}

void looper::quit() {
//    LOGI("Looper quit");
    loopermessage *msg = new loopermessage();
    msg->what = 0;
    msg->obj = NULL;
    msg->next = NULL;
    msg->quit = true;
    addmsg(msg, false);
    void *retval;
    pthread_join(worker, &retval);
    sem_destroy(&headdataavailable);
    sem_destroy(&headwriteprotect);
    running = false;
}

void looper::handle(int what, void* obj) {
//    LOGI("Looper dropping msg %d %p", what, obj);
}

