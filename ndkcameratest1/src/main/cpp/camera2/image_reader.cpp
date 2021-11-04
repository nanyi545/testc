/*
 * Copyright (C) 2017 The Android Open Source Project
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
#include <string>
#include <functional>
#include <thread>
#include <cstdlib>
#include <dirent.h>
#include <ctime>
#include "image_reader.h"
#include "native_debug.h"
#include <errno.h>
#include <string.h>
#include "util.cpp"
#include "fileWriter.h"
#include <unistd.h>
#include <libyuv.h>

/*
 * For JPEG capture, captured files are saved under
 *     DirName
 * File names are incrementally appended an index number as
 *     capture0.jpg, capture1.jpg, capture2.jpg
 */
static const char *kDirName = "/sdcard/DCIM/Camera/";
static const char *kFileName = "capture";

/**
 * MAX_BUF_COUNT:
 *   Max buffers in this ImageReader.
 */
#define MAX_BUF_COUNT 4

/**
 * ImageReader listener: called by AImageReader for every frame captured
 * We pass the event to ImageReader class, so it could do some housekeeping
 * about
 * the loaded queue. For example, we could keep a counter to track how many
 * buffers are full and idle in the queue. If camera almost has no buffer to
 * capture
 * we could release ( skip ) some frames by AImageReader_getNextImage() and
 * AImageReader_delete().
 */
void OnImageCallback(void *ctx, AImageReader *reader) {
  reinterpret_cast<ImageReader *>(ctx)->ImageCallback(reader);
}

/**
 * Constructor
 */
ImageReader::ImageReader(ImageFormat *res, enum AIMAGE_FORMATS format)
    : presentRotation_(0), reader_(nullptr) {
  LOGI("img--reader--constructor");

  callback_ = nullptr;
  callbackCtx_ = nullptr;

  /**
   * 在创建AImageReader时，需要提供四个参数：图像宽度，高度，图像格式，最大图片存储量。
   *
   * 其中AImageReader起到一个承上启下的作用。
在发送请求阶段，绑定请求（ACaptureRequest）和输出（ACaptureSessionOutput），以获取实时视频数据。
在预览阶段，作为数据源，用于数据转换（YUV数据转ARGB等图像数据类型）。
   *
   */
  media_status_t status = AImageReader_new(res->width, res->height, format,
                                           MAX_BUF_COUNT, &reader_);
  ASSERT(reader_ && status == AMEDIA_OK, "Failed to create AImageReader");

  AImageReader_ImageListener listener{
      .context = this, .onImageAvailable = OnImageCallback,
  };
  AImageReader_setImageListener(reader_, &listener);
}

ImageReader::~ImageReader() {
  ASSERT(reader_, "NULL Pointer to %s", __FUNCTION__);
  AImageReader_delete(reader_);
}

void ImageReader::RegisterCallback(void* ctx,
                      std::function<void(void* ctx, const char*fileName)> func) {
  callbackCtx_ = ctx;
  callback_ = func;
}

void ImageReader::ImageCallback(AImageReader *reader) {
  int32_t format;
  media_status_t status = AImageReader_getFormat(reader, &format);
  ASSERT(status == AMEDIA_OK, "Failed to get the media format");
//  LOGI("ImageCallback  format:%d   status:%d",format,status);
  if (format == AIMAGE_FORMAT_JPEG) {
    AImage *image = nullptr;
    media_status_t status = AImageReader_acquireNextImage(reader, &image);
    ASSERT(status == AMEDIA_OK && image, "Image is not available");

    // Create a thread and write out the jpeg files
    std::thread writeFileHandler(&ImageReader::WriteFile, this, image);
    writeFileHandler.detach();
  }
}


/**
 * AImageReader通过AImageReader_getWindow方法，获得一个ANativeWindow对象
 */
ANativeWindow *ImageReader::GetNativeWindow(void) {
  if (!reader_) return nullptr;
  ANativeWindow *nativeWindow;
  media_status_t status = AImageReader_getWindow(reader_, &nativeWindow);
  ASSERT(status == AMEDIA_OK, "Could not get ANativeWindow");

  return nativeWindow;
}

/**
 * GetNextImage()
 *   Retrieve the next image in ImageReader's bufferQueue, NOT the last image so
 * no image is skipped. Recommended for batch/background processing.
 */
AImage *ImageReader::GetNextImage(void) {
  AImage *image;
  media_status_t status = AImageReader_acquireNextImage(reader_, &image);
  if (status != AMEDIA_OK) {
    return nullptr;
  }
  return image;
}

/**
 * GetLatestImage()
 *   Retrieve the last image in ImageReader's bufferQueue, deleting images in
 * in front of it on the queue. Recommended for real-time processing.
 */
AImage *ImageReader::GetLatestImage(void) {
    AImage *image;
    media_status_t status = AImageReader_acquireLatestImage(reader_, &image);
    if (status != AMEDIA_OK) {
        return nullptr;
    }
    return image;
}

/**
 * Delete Image
 * @param image {@link AImage} instance to be deleted
 */
void ImageReader::DeleteImage(AImage *image) {
  if (image) AImage_delete(image);
}

/**
 * Helper function for YUV_420 to RGB conversion. Courtesy of Tensorflow
 * ImageClassifier Sample:
 * https://github.com/tensorflow/tensorflow/blob/master/tensorflow/examples/android/jni/yuv2rgb.cc
 * The difference is that here we have to swap UV plane when calling it.
 */
#ifndef MAX
#define MAX(a, b)           \
  ({                        \
    __typeof__(a) _a = (a); \
    __typeof__(b) _b = (b); \
    _a > _b ? _a : _b;      \
  })
#define MIN(a, b)           \
  ({                        \
    __typeof__(a) _a = (a); \
    __typeof__(b) _b = (b); \
    _a < _b ? _a : _b;      \
  })
#endif

// This value is 2 ^ 18 - 1, and is used to clamp the RGB values before their
// ranges
// are normalized to eight bits.
static const int kMaxChannelValue = 262143;

static inline uint32_t YUV2RGB(int nY, int nU, int nV) {
  nY -= 16;
  nU -= 128;
  nV -= 128;
  if (nY < 0) nY = 0;

  // This is the floating point equivalent. We do the conversion in integer
  // because some Android devices do not have floating point in hardware.
  // nR = (int)(1.164 * nY + 1.596 * nV);
  // nG = (int)(1.164 * nY - 0.813 * nV - 0.391 * nU);
  // nB = (int)(1.164 * nY + 2.018 * nU);

  int nR = (int)(1192 * nY + 1634 * nV);
  int nG = (int)(1192 * nY - 833 * nV - 400 * nU);
  int nB = (int)(1192 * nY + 2066 * nU);

  nR = MIN(kMaxChannelValue, MAX(0, nR));
  nG = MIN(kMaxChannelValue, MAX(0, nG));
  nB = MIN(kMaxChannelValue, MAX(0, nB));

  nR = (nR >> 10) & 0xff;
  nG = (nG >> 10) & 0xff;
  nB = (nB >> 10) & 0xff;

  return 0xff000000 | (nR << 16) | (nG << 8) | nB;
}

FILE *fp_out = NULL;

/**
 * Convert yuv image inside AImage into ANativeWindow_Buffer
 * ANativeWindow_Buffer format is guaranteed to be
 *      WINDOW_FORMAT_RGBX_8888
 *      WINDOW_FORMAT_RGBA_8888
 * @param buf a {@link ANativeWindow_Buffer } instance, destination of
 *            image conversion
 * @param image a {@link AImage} instance, source of image conversion.
 *            it will be deleted via {@link AImage_delete}
 */
YuvFrame ImageReader::DisplayImage(ANativeWindow_Buffer *buf, AImage *image) {
  ASSERT(buf->format == WINDOW_FORMAT_RGBX_8888 ||
             buf->format == WINDOW_FORMAT_RGBA_8888,
         "Not supported buffer format");

  int32_t srcFormat = -1;
  AImage_getFormat(image, &srcFormat);
  ASSERT(AIMAGE_FORMAT_YUV_420_888 == srcFormat, "Failed to get format");
  int32_t srcPlanes = 0;
  AImage_getNumberOfPlanes(image, &srcPlanes);
  ASSERT(srcPlanes == 3, "Is not 3 planes");

//  LOGI("DisplayImage:   gettid:%d",(int)gettid());


  /**
   * srcFormat  --->  AIMAGE_FORMAT_YUV_420_888   35
   */
//  LOGI("DisplayImage  srcPlanes:%d  srcFormat:%d   presentRotation_:%d",srcPlanes,srcFormat, presentRotation_ );

  /**
   *  test write file
   */
//  int* p;
//  if(NULL == fp_out){
//    fp_out = fopen("/sdcard/Download/hehe/out_1","w+");
//    if(NULL == fp_out) {
//        LOGI("null fd");
//        LOGI("null -----  fd");
//        LOGI("fopen erro: %s \n",strerror(errno));
//
//    } else {
//        LOGI("not null fd");
//    }
//    int a1 = 1;
//    p = &a1;
//  }


// 前置摄像头
// 0 -----> 需要PresentImage270，正常显示 ...
//  PresentImage270(buf, image);


  YuvFrame yuvFrame = PresentImage(buf, image);


//  switch (presentRotation_) {
//    case 0:
//      PresentImage(buf, image);
//      break;
//    case 90:
//          PresentImage90(buf, image);
//          break;
//    case 180:
//      PresentImage180(buf, image);
//      break;
//    case 270:
//      PresentImage270(buf, image);
//      break;
//    default:
//      ASSERT(0, "NOT recognized display rotation: %d", presentRotation_);
//  }

  AImage_delete(image);

  return yuvFrame;
}




/*
 * PresentImage()
 *   Converting yuv to RGB
 *   No rotation: (x,y) --> (x, y)
 *   Refer to:
 * https://mathbits.com/MathBits/TISection/Geometry/Transformations2.htm
 */

fileWriter* yuvWriter;
int32_t width2 = 0;
int32_t height2;
uint8_t* yPixe2,*uPixel2, *vPixel2;
int32_t ylen2,ulen2,vlen2;

//
uint8_t  *uPixel_, *vPixel_, *out1_;
uint8_t *preYPixel;


YuvFrame ImageReader::PresentImage(ANativeWindow_Buffer *buf, AImage *image) {
  AImageCropRect srcRect;
  AImage_getCropRect(image, &srcRect);


//    int32_t format;
//    AImage_getFormat(image, &format);
//    // no format ???
//    LOGI("  ----- format:%" PRId32  , (format) );


  int32_t yStride, uvStride , stride3 = 100;
  uint8_t *yPixel, *uPixel, *vPixel;
  int32_t yLen, uLen, vLen;
  AImage_getPlaneRowStride(image, 0, &yStride);
  AImage_getPlaneRowStride(image, 1, &uvStride);
  AImage_getPlaneRowStride(image, 2, &stride3);

  AImage_getPlaneData(image, 0, &yPixel, &yLen);
  AImage_getPlaneData(image, 1, &vPixel, &vLen);
  AImage_getPlaneData(image, 2, &uPixel, &uLen);
//    LOGI("stride1:%d   stride2:%d   stride3:%d",yStride,uvStride,stride3);

  // https://docs.microsoft.com/en-us/windows/win32/medfound/image-stride
  // stride : width + padding ...


  int32_t uvPixelStride,yPixStride , pixStride3 = 100;
    AImage_getPlanePixelStride(image, 0, &yPixStride);
    AImage_getPlanePixelStride(image, 1, &uvPixelStride);
    AImage_getPlanePixelStride(image, 2, &pixStride3);
//    LOGI("pstride1:%d   pstride2:%d   pstride3:%d",yPixStride,uvPixelStride,pixStride3);


//    LOGI("***  yLen:%d  uLen:%d,  vLen:%d  yStride:%d  uvStride:%d  yPixStride:%d   uvPixelStride:%d   buf->stride:%d  buf->width:%d buf->height:%d",yLen ,uLen ,vLen ,yStride , uvStride, yPixStride, uvPixelStride, buf->stride, buf->width,buf->height);
    //  yLen:307200  uLen:153599,  vLen:153599  yStride:640  uvStride:640  yPixStride:1   uvPixelStride:2   buf->stride:1088  buf->width:1080  buf->height:2400


//    LOGI("v0:%d  v1:%d,  v2:%d,  v3:%d,  4:%d,  v5:%d",vPixel[0],vPixel[1],vPixel[2],vPixel[3],vPixel[4],vPixel[5]);

//    LOGI("srcRect.top:%d , srcRect.right:%d, srcRect.bottom%d, srcRect.left%d ",srcRect.top,srcRect.right,srcRect.bottom,srcRect.left);
    // srcRect.top:0 , srcRect.right:640, srcRect.bottom480, srcRect.left0


    int32_t height = MIN(buf->height, (srcRect.bottom - srcRect.top));
  int32_t width = MIN(buf->width, (srcRect.right - srcRect.left));

  bool first = false;


    if(yuvWriter == NULL){

        first = true;

      // write to file
//        yuvWriter = new fileWriter("/sdcard/Download/aaa/t3.yuv", true);
//        yuvWriter->writeYuv((srcRect.right - srcRect.left), (srcRect.bottom - srcRect.top) , yPixel,yLen,vPixel,vLen,uPixel,uLen);

// read from file ....
//      yuvWriter = new fileWriter("/sdcard/Download/aaa/t2.yuv", false);
//      yuvWriter->readYuv(&width2,&height2,&yPixe2,&ylen2,&uPixel2,&ulen2,&vPixel2,&vlen2);
//      LOGI("width2:%d  height2:%d  ylen2:%d ", width2 , height2 , ylen2 );
//      for (int i=0;i<ulen2;i++){
//          if((i%2)==0){
//              if((i+1)<ulen2){
//                  uPixel2[i] = uPixel2[i+1];
//                  vPixel2[i] = vPixel2[i+1];
//              }
//          }
//      }

    } else {
        first = false;

    }

    //  The actual bits
  uint32_t *out = static_cast<uint32_t *>(buf->bits);
  uint8_t *out1 = static_cast<uint8_t *>(buf->bits);

  // convert to rgb lib YUV
if(yuvWriter != NULL){
//    int ret = libyuv::I422ToRGBA(yPixe2,width2,uPixel2,width2,vPixel2,width2,out1,1088*4, width2, height2);
//    int ret = libyuv::I422ToABGR(yPixe2,width2,uPixel2,width2,vPixel2,width2,out1,1088*4, 640, 480);
//    int ret = libyuv::I422ToABGR(yPixe2,width2,vPixel2,width2,uPixel2,width2,out1,1088*4, 640, 480);

// YUV 420 --> RGB use this !!
//    timeStart();
// int ret = libyuv::I420ToABGR(yPixe2,width2,uPixel2,width2>>1,vPixel2,width2>>1,out1,1088*4, 640, 480);
//    int ret = libyuv::I420ToABGR(yPixe2,width2,uPixel2,width2,vPixel2,width2,out1,1088*4, 640, 480);

//    double elapse = timeEnd();
//    LOGI("elapse:%f",elapse);   // ~ 0.3 - 0.4

}




// use saved yuv data ...
//  if(yPixe2!=NULL){
//      timeStart();
//
//    for (int32_t y = 0 ; y < height; y++) {
//      const uint8_t *pY = yPixe2 + yStride * (y + srcRect.top) + srcRect.left;
//
//      int32_t uv_row_start = uvStride * ((y + srcRect.top) >> 1);
//      const uint8_t *pU = uPixel2 + uv_row_start + (srcRect.left >> 1);
//      const uint8_t *pV = vPixel2 + uv_row_start + (srcRect.left >> 1);
//
//      for (int32_t x = 0; x < width; x++) {
//        const int32_t uv_offset = (x >> 1) * uvPixelStride;
//        out[x] = YUV2RGB(pY[x], pU[uv_offset], pV[uv_offset]);
//        if( first && y==4 ){
//            LOGI("---------    x:%d  y:%d  uv_offset:%d    uv_row_start:%d",x ,y ,uv_offset,uv_row_start );
//        }
//      }
//      out += buf->stride;
//    }
//      double elapse = timeEnd();
//      LOGI("elapse:%f",elapse);   // ~ 9 - 14 ....
//  }



  // convert to rgb ----
//  timeStart();
//  for (int32_t y = 0; y < height; y++) {
//    const uint8_t *pY = yPixel + yStride * (y + srcRect.top) + srcRect.left;
//
//    int32_t uv_row_start = uvStride * ((y + srcRect.top) >> 1);
//    const uint8_t *pU = uPixel + uv_row_start + (srcRect.left >> 1);
//    const uint8_t *pV = vPixel + uv_row_start + (srcRect.left >> 1);
//
//    for (int32_t x = 0; x < width; x++) {
//      const int32_t uv_offset = (x >> 1) * uvPixelStride;
//      out[x] = YUV2RGB(pY[x], pU[uv_offset], pV[uv_offset]);
//    }
//    out += buf->stride;
//  }
//  double elapse = timeEnd();
//  LOGI("elapse:%f",elapse); //  // ~ 9 - 14



// libyuv convert to rgb ....
    timeStart();
  int shiftX;
  int shiftY;

    if(uvPixelStride!=1){
        if(uPixel_==NULL){
            uPixel_ = (uint8_t*)(malloc( vLen/uvPixelStride ));
            vPixel_ = (uint8_t*)(malloc( vLen/uvPixelStride ));
            out1_ =  (uint8_t*)(malloc( buf->stride * 4 * buf->height ));
        }

        for (int j = 0;j<vLen/uvPixelStride;j++){
            uPixel_[j]=uPixel[j*uvPixelStride];
            vPixel_[j]=vPixel[j*uvPixelStride];
        }

        int ret = libyuv::I420ToARGB(yPixel,yStride,
                uPixel_,uvStride/uvPixelStride,
                vPixel_,uvStride/uvPixelStride,
                out1_,buf->stride*4, width, height);

        shiftX = 200;
        shiftY = 500;
        int shift = shiftY*buf->stride*4 + shiftX*4;


      libyuv::ARGBRotate(out1_,buf->stride*4, out1+shift, buf->stride*4,width, height,libyuv::kRotate270);


//      if(preYPixel != NULL){
//        uint64_t sse = libyuv::ComputeSumSquareError(preYPixel,yPixel,yLen);
//        uint64_t sseReduced = sse / width / height;
//        LOGI("sseReduced:   ---%" PRId64, sseReduced);
//      }

      if(preYPixel == NULL){
          preYPixel = (uint8_t*)(malloc( yLen ));
      }
      memcpy(preYPixel,yPixel,yLen);

    }

  // sleep 1000000 ---> for 1 sec
//  usleep(100000);


    double elapse = timeEnd();
//    LOGI("elapse:   --- %f",elapse);

    // I420ToARGB  ~~   0.652000 -- 0.803693
    // I420ToARGB + ARGBRotate  ~~  1.4-1.7



// custom rgb framing....
//    for (int32_t y = 0; y < height; y++) {
//        for (int32_t x = 0; x < width; x++) {
//          int b = 0;
//          int g = 255;
//          int r = 0;
//          int a = 255;
//          out[x] = (a<<24) | (b << 16) | (g << 8) | r;
//        }
//      out += buf->stride;
//    }



/**
 *  int32 -->
 *  p[0] 最低8位数
 *  p[3] 最高8位数
 */
//  int a = 253;
//  int b = a<<24;
//  void* p = &b;
//    uint8_t *o1 = static_cast<uint8_t *>(p);
//    LOGI("----- a:%x   b:%x ------  0:%d  1:%d  2:%d  3:%d ",a,b, o1[0],o1[1] ,o1[2] ,o1[3]  );


// custom rgb framing.... using a different format ...
//    for (int32_t y = 0; y < 1000; y++) {
//        for (int32_t x = 0; x < 100; x++) {
//            uint8_t b = 0;
//            uint8_t g = 255;
//            uint8_t r = 0;
//            uint8_t a = 255;
//            out1[x*4] = r;
//            out1[x*4+1] = g;
//            out1[x*4+2] = b;
//            out1[x*4+3] = a;
//        }
//        out1 += (buf->stride * 4);
//    }

  YuvFrame f;
  f.data[0] = yLen;
  f.data[5] = buf->width;
  f.data[6] = buf->height;
  f.data[7] = shiftX;
  f.data[8] = shiftY;
  f.data[9] = buf->stride;

  return f;
}

/*
 * PresentImage90()
 *   Converting YUV to RGB
 *   Rotation image anti-clockwise 90 degree -- (x, y) --> (-y, x)
 */
void ImageReader::PresentImage90(ANativeWindow_Buffer *buf, AImage *image) {
  AImageCropRect srcRect;
  AImage_getCropRect(image, &srcRect);

  int32_t yStride, uvStride;
  uint8_t *yPixel, *uPixel, *vPixel;
  int32_t yLen, uLen, vLen;
  AImage_getPlaneRowStride(image, 0, &yStride);
  AImage_getPlaneRowStride(image, 1, &uvStride);
  AImage_getPlaneData(image, 0, &yPixel, &yLen);
  AImage_getPlaneData(image, 1, &vPixel, &vLen);
  AImage_getPlaneData(image, 2, &uPixel, &uLen);
  int32_t uvPixelStride;
  AImage_getPlanePixelStride(image, 1, &uvPixelStride);
  LOGI("yLen:%d  uLen:%d,  vLen:%d" ,yLen ,uLen ,vLen );

  int32_t height = MIN(buf->width, (srcRect.bottom - srcRect.top));
  int32_t width = MIN(buf->height, (srcRect.right - srcRect.left));

  // width:1560  height:702,  buf->width:1080 ,  buf->height:2400,    crop: l:0  t:0  r:1560  b:702

  LOGI("-------- width:%d  height:%d,  buf->width:%d ,  buf->height:%d,    crop: l:%d  t:%d  r:%d  b:%d "
          ,width ,height     ,buf->width     ,buf->height ,srcRect.left,srcRect.top,srcRect.right,srcRect.bottom);


  uint32_t *out = static_cast<uint32_t *>(buf->bits);
  out += height - 1;

  // offset display to center ....
//  int32_t deltaX= (buf->height - height)/2 ;
//  int32_t deltaY= (buf->width - width)/2 ;
    int32_t deltaX= (buf->height - width)/2 ;
    int32_t deltaY= (buf->width - height)/2 ;


  for (int32_t y = 0; y < height; y++) {
    const uint8_t *pY = yPixel + yStride * (y + srcRect.top) + srcRect.left;

    int32_t uv_row_start = uvStride * ((y + srcRect.top) >> 1);
    const uint8_t *pU = uPixel + uv_row_start + (srcRect.left >> 1);
    const uint8_t *pV = vPixel + uv_row_start + (srcRect.left >> 1);

    for (int32_t x = 0; x < width; x++) {
      const int32_t uv_offset = (x >> 1) * uvPixelStride;
      // [x, y]--> [-y, x]
      out[ (x+deltaX) * buf->stride + deltaY ] = YUV2RGB(pY[x], pU[uv_offset], pV[uv_offset]);
    }
    out -= 1;  // move to the next column
  }
}

/*
 * PresentImage180()
 *   Converting yuv to RGB
 *   Rotate image 180 degree: (x, y) --> (-x, -y)
 */
void ImageReader::PresentImage180(ANativeWindow_Buffer *buf, AImage *image) {
  AImageCropRect srcRect;
  AImage_getCropRect(image, &srcRect);

  int32_t yStride, uvStride;
  uint8_t *yPixel, *uPixel, *vPixel;
  int32_t yLen, uLen, vLen;
  AImage_getPlaneRowStride(image, 0, &yStride);
  AImage_getPlaneRowStride(image, 1, &uvStride);
  AImage_getPlaneData(image, 0, &yPixel, &yLen);
  AImage_getPlaneData(image, 1, &vPixel, &vLen);
  AImage_getPlaneData(image, 2, &uPixel, &uLen);
  int32_t uvPixelStride;
  AImage_getPlanePixelStride(image, 1, &uvPixelStride);

  int32_t height = MIN(buf->height, (srcRect.bottom - srcRect.top));
  int32_t width = MIN(buf->width, (srcRect.right - srcRect.left));

  uint32_t *out = static_cast<uint32_t *>(buf->bits);
  out += (height - 1) * buf->stride;
  for (int32_t y = 0; y < height; y++) {
    const uint8_t *pY = yPixel + yStride * (y + srcRect.top) + srcRect.left;

    int32_t uv_row_start = uvStride * ((y + srcRect.top) >> 1);
    const uint8_t *pU = uPixel + uv_row_start + (srcRect.left >> 1);
    const uint8_t *pV = vPixel + uv_row_start + (srcRect.left >> 1);

    for (int32_t x = 0; x < width; x++) {
      const int32_t uv_offset = (x >> 1) * uvPixelStride;
      // mirror image since we are using front camera
      out[width - 1 - x] = YUV2RGB(pY[x], pU[uv_offset], pV[uv_offset]);
      // out[x] = YUV2RGB(pY[x], pU[uv_offset], pV[uv_offset]);
    }
    out -= buf->stride;
  }
}

/*
 * PresentImage270()
 *   Converting image from YUV to RGB
 *   Rotate Image counter-clockwise 270 degree: (x, y) --> (y, x)
 */
void ImageReader::PresentImage270(ANativeWindow_Buffer *buf, AImage *image) {
  AImageCropRect srcRect;
  AImage_getCropRect(image, &srcRect);

  int32_t yStride, uvStride;
  uint8_t *yPixel, *uPixel, *vPixel;
  int32_t yLen, uLen, vLen;
  AImage_getPlaneRowStride(image, 0, &yStride);
  AImage_getPlaneRowStride(image, 1, &uvStride);
  AImage_getPlaneData(image, 0, &yPixel, &yLen);
  AImage_getPlaneData(image, 1, &vPixel, &vLen);
  AImage_getPlaneData(image, 2, &uPixel, &uLen);
  int32_t uvPixelStride;
  AImage_getPlanePixelStride(image, 1, &uvPixelStride);

  int32_t height = MIN(buf->width, (srcRect.bottom - srcRect.top));
  int32_t width = MIN(buf->height, (srcRect.right - srcRect.left));

  uint32_t *out = static_cast<uint32_t *>(buf->bits);
  for (int32_t y = 0; y < height; y++) {
    const uint8_t *pY = yPixel + yStride * (y + srcRect.top) + srcRect.left;

    int32_t uv_row_start = uvStride * ((y + srcRect.top) >> 1);
    const uint8_t *pU = uPixel + uv_row_start + (srcRect.left >> 1);
    const uint8_t *pV = vPixel + uv_row_start + (srcRect.left >> 1);

    for (int32_t x = 0; x < width; x++) {
      const int32_t uv_offset = (x >> 1) * uvPixelStride;
      out[(width - 1 - x) * buf->stride] =
          YUV2RGB(pY[x], pU[uv_offset], pV[uv_offset]);
    }
    out += 1;  // move to the next column
  }
}
void ImageReader::SetPresentRotation(int32_t angle) {
  presentRotation_ = angle;
}

/**
 * Write out jpeg files to kDirName directory
 * @param image point capture jpg image
 */
void ImageReader::WriteFile(AImage* image) {

  int planeCount;
  media_status_t status = AImage_getNumberOfPlanes(image, &planeCount);
  ASSERT(status == AMEDIA_OK && planeCount == 1,
         "Error: getNumberOfPlanes() planeCount = %d", planeCount);
  uint8_t *data = nullptr;
  int len = 0;
  AImage_getPlaneData(image, 0, &data, &len);

  DIR *dir = opendir(kDirName);
  if (dir) {
    closedir(dir);
  } else {
    std::string cmd = "mkdir -p ";
    cmd += kDirName;
    system(cmd.c_str());
  }

  struct timespec ts {
      0, 0
  };
  clock_gettime(CLOCK_REALTIME, &ts);
  struct tm localTime;
  localtime_r(&ts.tv_sec, &localTime);

  std::string fileName = kDirName;
  std::string dash("-");
  fileName += kFileName + std::to_string(localTime.tm_mon) +
              std::to_string(localTime.tm_mday) + dash +
              std::to_string(localTime.tm_hour) +
              std::to_string(localTime.tm_min) +
              std::to_string(localTime.tm_sec) + ".jpg";
  FILE *file = fopen(fileName.c_str(), "wb");
  if (file && data && len) {
    fwrite(data, 1, len, file);
    fclose(file);

    if (callback_) {
      callback_(callbackCtx_, fileName.c_str());
    }
  } else {
    if (file)
      fclose(file);
  }
  AImage_delete(image);
}
