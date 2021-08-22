//
// Created by wei wang on 2021-08-17.
//

#include "fileWriter.h"
#include "native_debug.h"
#include <cstring>

fileWriter::fileWriter(const char *__path , bool write) {
    if(write){
        f = fopen(__path,"wb");
    } else {
        f = fopen(__path,"r");
    }
}

bool fileWriter::isWrite() {
    return write;
}

void fileWriter::writeYuv(int32_t width,int32_t height,uint8_t *yPixel, int32_t yLen, uint8_t *uPixel, int32_t uLen,
                          uint8_t *vPixel, int32_t vLen) {
    fwrite(&yLen,4,1,f);
    fwrite(&uLen,4,1,f);
    fwrite(&vLen,4,1,f);
    fwrite(&width,4,1,f);
    fwrite(&height,4,1,f);

    fwrite(yPixel,1,yLen,f);
    fwrite(uPixel,1,uLen,f);
    fwrite(vPixel,1,vLen,f);
    fflush(f);
    fclose(f);

}


void fileWriter::readYuv(int32_t* width,int32_t* height,uint8_t** yPixel, int32_t* yLen, uint8_t** uPixel, int32_t* uLen,
                         uint8_t** vPixel, int32_t* vLen) {

    fread(yLen,4,1,f);
    fread(uLen,4,1,f);
    fread(vLen,4,1,f);
    fread(width,4,1,f);
    fread(height,4,1,f);

    *yPixel = (uint8_t*)(malloc(*yLen));

    int32_t t  = (*yLen)>>1;

    *uPixel = (uint8_t*)(malloc(t));
    *vPixel = (uint8_t*)(malloc(t));

    fread(*yPixel,1,*yLen,f);

    // make it gray ....
//    memset(*uPixel,128,t);
//    memset(*vPixel,128,t);


//    fread(*uPixel,1,t,f);
//    fread(*vPixel,1,t,f);


    fread(*uPixel,1,*uLen,f);
    fread(*vPixel,1,*vLen,f);


}