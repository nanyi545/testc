/*****************************************************************************
 * ijksdl_thread.c
 *****************************************************************************
 *
 * Copyright (c) 2013 Bilibili
 * copyright (c) 2013 Zhang Rui <bbcallen@gmail.com>
 *
 * This file is part of ijkPlayer.
 *
 * ijkPlayer is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * ijkPlayer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with ijkPlayer; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */

#include "ijksdl_thread.h"
#include "../native_debug.h"


//   SDL_CreateThreadEx|call1|SDL_RunThread

#if !defined(__APPLE__)
// using ios implement for autorelease
void *SDL_RunThread(void *data)
{
    SDL_Thread *thread = data;
    LOGI("SDL_RunThread  1");
//    ALOGI("davidww-sdl-SDL_RunThread: [%d] %s\n", (int)gettid(), thread->name);
    pthread_setname_np(pthread_self(), thread->name);
    LOGI("SDL_RunThread  2");
    usleep(1000);
    int aa = (thread->func==NULL);
    int bb = (thread->data==NULL);
    LOGI("SDL_RunThread  2-2  aa:%d  bb%d   ttid:%d",aa,bb, gettid());
    thread->retval = thread->func(thread->data);
    LOGI("SDL_RunThread  3");

    return NULL;
}

SDL_Thread *SDL_CreateThreadEx(SDL_Thread *thread, int (*fn)(void *), void *data, const char *name)
{
    LOGI("SDL_CreateThreadEx  1   ttid:%d",gettid());
    thread->func = fn;
    LOGI("SDL_CreateThreadEx  2");
    thread->data = data;
    LOGI("SDL_CreateThreadEx  3");
    strlcpy(thread->name, name, sizeof(thread->name) - 1);
    LOGI("SDL_CreateThreadEx  4");
    int retval = pthread_create(&thread->id, NULL, SDL_RunThread, thread);
    LOGI("SDL_CreateThreadEx  5:%d",retval);
    if (retval)
        return NULL;

    return thread;
}
#endif

int SDL_SetThreadPriority(SDL_ThreadPriority priority)
{
    struct sched_param sched;
    int policy;
    pthread_t thread = pthread_self();

    if (pthread_getschedparam(thread, &policy, &sched) < 0) {
//        ALOGE("pthread_getschedparam() failed");
        return -1;
    }
    if (priority == SDL_THREAD_PRIORITY_LOW) {
        sched.sched_priority = sched_get_priority_min(policy);
    } else if (priority == SDL_THREAD_PRIORITY_HIGH) {
        sched.sched_priority = sched_get_priority_max(policy);
    } else {
        int min_priority = sched_get_priority_min(policy);
        int max_priority = sched_get_priority_max(policy);
        sched.sched_priority = (min_priority + (max_priority - min_priority) / 2);
    }
    if (pthread_setschedparam(thread, policy, &sched) < 0) {
//        ALOGE("pthread_setschedparam() failed");
        return -1;
    }
    return 0;
}

void SDL_WaitThread(SDL_Thread *thread, int *status)
{
    assert(thread);
    if (!thread)
        return;

    pthread_join(thread->id, NULL);

    if (status)
        *status = thread->retval;
}

void SDL_DetachThread(SDL_Thread *thread)
{
    assert(thread);
    if (!thread)
        return;

    pthread_detach(thread->id);
}
