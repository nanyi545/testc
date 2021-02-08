#include <jni.h>
#include <string>
#include <android/log.h>
#include <android/bitmap.h>
extern "C"{
#include "giflib/gif_lib.h"
}

#define  argb(a,r,g,b) ( ((a) & 0xff) << 24 ) | ( ((b) & 0xff) << 16 ) | ( ((g) & 0xff) << 8 ) | ((r) & 0xff)


struct Person{
    int age;
    std::string name;
};

/**
 * test function to get a string through JNI
 */
extern "C" JNIEXPORT jstring JNICALL
Java_com_example_testc2_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
//    Person *p1 = static_cast<Person *>(malloc(sizeof(Person)));
    Person *p1;
    p1->age=10;
    __android_log_print(ANDROID_LOG_VERBOSE, "hehe", "age:%d",p1->age);


    std::string hello = "Hello from C++";

    //std::string to char*
    //   https://stackoverflow.com/questions/7352099/stdstring-to-char
    const char *cstr = hello.c_str();
    const char *cstr2 = "99999";
    int a = 123;
    __android_log_print(ANDROID_LOG_VERBOSE, "hehe", "s1:%s  s2:%s  int:%d",cstr,cstr2,a);

    return env->NewStringUTF(hello.c_str());
}


struct GifBean{
    int current_frame;
    int total_frame;
    int *delays;
};



/**
 *   load gif
 *
 *
 */

extern "C"
JNIEXPORT jlong JNICALL
Java_com_example_testc2_gif_GifHandler_loadGif(JNIEnv *env, jclass clazz, jstring path_) {
    const char *path = env->GetStringUTFChars(path_, 0);
//    path   java   --
    int Erro;//打开失败还是成功
    GifFileType * gifFileType= DGifOpenFileName(path, &Erro);

//初始化缓冲区  数组 SaveImages
    DGifSlurp(gifFileType);
    GifBean *gifBean = static_cast<GifBean *>(malloc(sizeof(GifBean)));
    memset(gifBean, 0, sizeof(GifBean));
    gifFileType->UserData = gifBean;
    gifBean->current_frame = 0;
//    总帧数
    gifBean->total_frame = gifFileType->ImageCount;

    env->ReleaseStringUTFChars(path_, path);
    return (jlong)(gifFileType);
}



extern "C"
JNIEXPORT jint JNICALL
Java_com_example_testc2_gif_GifHandler_getWidth(JNIEnv *env, jclass clazz, jlong gif_hander) {
    GifFileType *gifFileType = reinterpret_cast<GifFileType *>(gif_hander);
    return gifFileType->SWidth;
}



extern "C"
JNIEXPORT jint JNICALL
Java_com_example_testc2_gif_GifHandler_getHeight(JNIEnv *env, jclass clazz, jlong gif_hander) {
    GifFileType *gifFileType = reinterpret_cast<GifFileType *>(gif_hander);
    return gifFileType->SHeight;
}





void drawFrame1(GifFileType* gifFileType, AndroidBitmapInfo info, void *pixels) {
    GifBean *gifBean = static_cast<GifBean *>(gifFileType->UserData);
    //当前帧 一定要拿到         拿到当前帧   直接
    SavedImage savedImage = gifFileType->SavedImages[gifBean->current_frame];
//    图像分成两部分  像素   一部分是 描述
    GifImageDesc frameInfo=savedImage.ImageDesc;
    ColorMapObject *colorMapObject = frameInfo.ColorMap;
//    像素
//    savedImage.RasterBits[i];
//记录每一行的首地址
//bitmap
    int* px = (int *)pixels;

//    临时 索引
    int *line;
//    索引
    int  pointPixel;
    GifByteType  gifByteType;
//操作   解压
    GifColorType gifColorType;
    for (int y =frameInfo.Top ; y<frameInfo.Top+frameInfo.Height; ++y) {
//每次遍历行    首地址 传给line
        line = px;
        for (int x =  frameInfo.Left; x < frameInfo.Left + frameInfo.Width ; ++x) {
//            定位像素  索引
            pointPixel = (y - frameInfo.Top) * frameInfo.Width + (x - frameInfo.Left);
//            是 1不是2  压缩
            gifByteType=savedImage.RasterBits[pointPixel];
            gifColorType= colorMapObject->Colors[gifByteType];
//line 进行复制   0  255  屏幕有颜色 line
            line[x] = argb(255, gifColorType.Red, gifColorType.Green, gifColorType.Blue);
        }
//遍历条件     转到下一行
        px = (int *) ((char *) px + info.stride);
    }

}



extern "C"
JNIEXPORT jint JNICALL
Java_com_example_testc2_gif_GifHandler_updateFrame(JNIEnv *env, jclass clazz, jlong gif_point,
                                                   jobject bitmap) {
    GifFileType *gifFileType = reinterpret_cast<GifFileType *>(gif_point);
    int width = gifFileType->SWidth;

    int height = gifFileType->SHeight;
//    还有另外一种    根据bitmap 获取
    AndroidBitmapInfo info;
    AndroidBitmap_getInfo(env, bitmap, &info);

//    ------------
    width = info.width;
    height = info.height;

//     bitmap  数组

    void *pixels;
//bitmap--->像素二维数组    ----- 锁住当前bitmap
    AndroidBitmap_lockPixels(env, bitmap, &pixels);
//    绘制
    drawFrame1(gifFileType, info, pixels);
    AndroidBitmap_unlockPixels(env, bitmap);
    GifBean *gifBean = static_cast<GifBean *>(gifFileType->UserData);
    gifBean->current_frame++;
    if (gifBean->current_frame >= gifBean->total_frame - 1) {
        gifBean->current_frame = 0;
    }
    return 100;
}


