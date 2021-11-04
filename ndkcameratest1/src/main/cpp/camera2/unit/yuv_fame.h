//
// Created by wei wang on 2021-11-04.
//

#ifndef TESTC2_YUV_FAME_H
#define TESTC2_YUV_FAME_H


typedef struct YuvFrame {


    /**
     *  0  yStride
     *  1  uvStride
     *  2  yLen
     *  3  uLen
     *  4  vLen
     *  5  window_w
     *  6  window_h
     *  7  window_w_offset
     *  8  window_h_offset
     *  9  stride
     *  10
     */
    int32_t data[10];

    uint8_t *yPixel, *uPixel, *vPixel;


};


typedef void (*YuvFrameHandler)(YuvFrame f);


#endif //TESTC2_YUV_FAME_H
