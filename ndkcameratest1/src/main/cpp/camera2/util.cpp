//
// Created by wei wang on 2021-07-29.
//

#ifndef UTIL_CPP_1
#define UTIL_CPP_1

#include <chrono>

static auto t_start = std::chrono::high_resolution_clock::now();
static auto t_end = std::chrono::high_resolution_clock::now();

/**
 *
 *  duplicate symbols  --->  use inline ...
 *
 *  https://stackoverflow.com/questions/29946517/c-duplicate-symbols
 *
 */

inline void timeStart(){
    t_start = std::chrono::high_resolution_clock::now();
}

inline double timeEnd(){
    t_end = std::chrono::high_resolution_clock::now();
    double elapsed_time_ms = std::chrono::duration<double, std::milli>(t_end-t_start).count();
    return elapsed_time_ms;
}

#endif  // UTIL_CPP_1
