//
// Created by wei wang on 2021-08-17.
//

#ifndef TESTC2_FILEWRITER_H
#define TESTC2_FILEWRITER_H

#include <cstdio>
#include <stdlib.h>


class fileWriter {
public:
    fileWriter(const char* __path,bool write);
    void writeYuv(int32_t width,int32_t height,
            uint8_t *yPixel, int32_t yLen,
            uint8_t *uPixel, int32_t uLen,
            uint8_t *vPixel, int32_t vLen );
    bool isWrite();

    void readYuv(int32_t* width,int32_t* height,
                  uint8_t** yPixel, int32_t* yLen,
                  uint8_t** uPixel, int32_t* uLen,
                  uint8_t** vPixel, int32_t* vLen );

private:
    FILE *f;
    bool write = 0;   // true evaluates to 1
};


#endif //TESTC2_FILEWRITER_H
