#include <jni.h>
#include <string>
#include <android/log.h>
#include <android/bitmap.h>
extern "C"{
#include "giflib/gif_lib.h"
}
#include "abi.h"

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
    Person *p1 = static_cast<Person *>(malloc(sizeof(Person)));
//    Person *p1;
    p1->age=10;
    __android_log_print(ANDROID_LOG_VERBOSE, "hehe", "age:%d",p1->age);


    std::string hello = "Hello from C++";

    //std::string to char*
    //   https://stackoverflow.com/questions/7352099/stdstring-to-char
    const char *cstr = hello.c_str();
    const char *cstr2 = "99999";
    int a = 123;
    __android_log_print(ANDROID_LOG_VERBOSE, "hehe", "s1:%s  s2:%s  int:%d",cstr,cstr2,a);
    __android_log_print(ANDROID_LOG_VERBOSE, "hehe", "abi:%s ",ABI);

    return env->NewStringUTF(hello.c_str());
}


struct GifBean{
    int current_frame;
    int total_frame;
    int *delays;
};



/**
 *   load gif   ----->  a copy of DGifSlurp method ....
 */

int
DGifSlurpCopy(GifFileType *GifFile)
{
    size_t ImageSize;
    GifRecordType RecordType;
    SavedImage *sp;
    GifByteType *ExtData;
    int ExtFunction;

    GifFile->ExtensionBlocks = NULL;
    GifFile->ExtensionBlockCount = 0;

    do {

        __android_log_print(ANDROID_LOG_VERBOSE, "gif", "do ---- ");

        if (DGifGetRecordType(GifFile, &RecordType) == GIF_ERROR)
            return (GIF_ERROR);

        switch (RecordType) {
            case IMAGE_DESC_RECORD_TYPE:
                __android_log_print(ANDROID_LOG_VERBOSE, "gif", "do ---- IMAGE_DESC_RECORD_TYPE");


                // GifFile->ImageCount++
                if (DGifGetImageDesc(GifFile) == GIF_ERROR)
                    return (GIF_ERROR);
                __android_log_print(ANDROID_LOG_VERBOSE, "gif", "do ---- IMAGE_DESC_RECORD_TYPE  ImageCount:%d", GifFile->ImageCount);

                sp = &GifFile->SavedImages[GifFile->ImageCount - 1];
                /* Allocate memory for the image */
                if (sp->ImageDesc.Width < 0 && (sp->ImageDesc.Height < 0)
                    && (sp->ImageDesc.Width > (INT_MAX / sp->ImageDesc.Height))
                        ) {
                    return GIF_ERROR;
                }
                ImageSize = sp->ImageDesc.Width * sp->ImageDesc.Height;

                __android_log_print(ANDROID_LOG_VERBOSE, "gif", "do ---- IMAGE_DESC_RECORD_TYPE  ImageSize:%d", ImageSize);


                if (ImageSize > (SIZE_MAX / sizeof(GifPixelType))) {
                    return GIF_ERROR;
                }
                sp->RasterBits = (unsigned char *)reallocarray(NULL, ImageSize,
                                                               sizeof(GifPixelType));

                if (sp->RasterBits == NULL) {
                    return GIF_ERROR;
                }

                if (sp->ImageDesc.Interlace) {
                    __android_log_print(ANDROID_LOG_VERBOSE, "gif", "do ---- IMAGE_DESC_RECORD_TYPE    Interlace");

                    int i, j;
                    /*
                     * The way an interlaced image should be read -
                     * offsets and jumps...
                     */
                    int InterlacedOffset[] = { 0, 4, 2, 1 };
                    int InterlacedJumps[] = { 8, 8, 4, 2 };
                    /* Need to perform 4 passes on the image */
                    for (i = 0; i < 4; i++)
                        for (j = InterlacedOffset[i];
                             j < sp->ImageDesc.Height;
                             j += InterlacedJumps[i]) {
                            if (DGifGetLine(GifFile,
                                            sp->RasterBits+j*sp->ImageDesc.Width,
                                            sp->ImageDesc.Width) == GIF_ERROR)
                                return GIF_ERROR;
                        }
                }
                else {
                    __android_log_print(ANDROID_LOG_VERBOSE, "gif", "do ---- IMAGE_DESC_RECORD_TYPE  not Interlace");

                    if (DGifGetLine(GifFile,sp->RasterBits,ImageSize)==GIF_ERROR)
                        return (GIF_ERROR);
                }

                if (GifFile->ExtensionBlocks) {
                    sp->ExtensionBlocks = GifFile->ExtensionBlocks;
                    sp->ExtensionBlockCount = GifFile->ExtensionBlockCount;

                    GifFile->ExtensionBlocks = NULL;
                    GifFile->ExtensionBlockCount = 0;
                }
                break;

            case EXTENSION_RECORD_TYPE:
                __android_log_print(ANDROID_LOG_VERBOSE, "gif", "do ---- EXTENSION_RECORD_TYPE");

                if (DGifGetExtension(GifFile,&ExtFunction,&ExtData) == GIF_ERROR)
                    return (GIF_ERROR);
                /* Create an extension block with our data */
                if (ExtData != NULL) {
                    if (GifAddExtensionBlock(&GifFile->ExtensionBlockCount,
                                             &GifFile->ExtensionBlocks,
                                             ExtFunction, ExtData[0], &ExtData[1])
                        == GIF_ERROR)
                        return (GIF_ERROR);
                }
                while (ExtData != NULL) {
                    if (DGifGetExtensionNext(GifFile, &ExtData) == GIF_ERROR)
                        return (GIF_ERROR);
                    /* Continue the extension block */
                    if (ExtData != NULL)
                        if (GifAddExtensionBlock(&GifFile->ExtensionBlockCount,
                                                 &GifFile->ExtensionBlocks,
                                                 CONTINUE_EXT_FUNC_CODE,
                                                 ExtData[0], &ExtData[1]) == GIF_ERROR)
                            return (GIF_ERROR);
                }
                break;

            case TERMINATE_RECORD_TYPE:
                __android_log_print(ANDROID_LOG_VERBOSE, "gif", "do ---- TERMINATE_RECORD_TYPE");

                break;

            default:    /* Should be trapped by DGifGetRecordType */
                break;
        }
    } while (RecordType != TERMINATE_RECORD_TYPE);

    /* Sanity check for corrupted file */
    if (GifFile->ImageCount == 0) {
        GifFile->Error = D_GIF_ERR_NO_IMAG_DSCR;
        return(GIF_ERROR);
    }

    return (GIF_OK);
}


extern "C"
JNIEXPORT jlong JNICALL
Java_com_example_testc2_gif_GifHandler_loadGif(JNIEnv *env, jclass clazz, jstring path_) {
    const char *path = env->GetStringUTFChars(path_, 0);
//    path   java   --
    int Erro;//打开失败还是成功
    GifFileType * gifFileType= DGifOpenFileName(path, &Erro);
    __android_log_print(ANDROID_LOG_VERBOSE, "gif", "init  Erro:%d", Erro);
    __android_log_print(ANDROID_LOG_VERBOSE, "gif", "init  ImageCount:%d", gifFileType->ImageCount);


//初始化缓冲区  数组 SaveImages
    DGifSlurpCopy(gifFileType);
//    DGifSlurp(gifFileType);



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


    //  -------------
    // color map ??
//    ColorMapObject *colorMapObject = frameInfo.ColorMap;

    ColorMapObject *colorMapObject;
    colorMapObject = (gifFileType->Image.ColorMap
                ? gifFileType->Image.ColorMap
                : gifFileType->SColorMap);

    //  -------------


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

ColorMapObject *ColorMap;
int32_t *user_image_data;
int transparentColorIndex = 0;
int disposalMode = DISPOSAL_UNSPECIFIED;

#define  MAKE_COLOR_ABGR(r, g, b) ((0xff) << 24 ) | ((b) << 16 ) | ((g) << 8 ) | ((r) & 0xff)

uint32_t gifColorToColorARGB(const GifColorType &color) {
    return (uint32_t) (MAKE_COLOR_ABGR(color.Red, color.Green, color.Blue));
}

void setColorARGB(uint32_t *sPixels, int imageIndex,
                             ColorMapObject *colorMap, GifByteType colorIndex) {
    if (imageIndex > 0 && disposalMode == DISPOSE_DO_NOT && colorIndex == transparentColorIndex) {
        return;
    }
    if (colorIndex != transparentColorIndex || transparentColorIndex == NO_TRANSPARENT_COLOR) {
        *sPixels = gifColorToColorARGB(colorMap->Colors[colorIndex]);
    } else {
        *sPixels = 0;
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
//    绘制  -------- this way ... has issue  ??  ----------
    drawFrame1(gifFileType, info, pixels);



    //  --------------test call --------------
    // // from ndk gif project ....
//    GifBean *gifBean1 = static_cast<GifBean *>(gifFileType->UserData);
//    SavedImage savedImage = gifFileType->SavedImages[gifBean1->current_frame];
//    GifImageDesc frameInfo=savedImage.ImageDesc;
//    uint32_t *sPixels = (uint32_t *) pixels;
//    user_image_data = (int32_t *) savedImage.RasterBits;
//    transparentColorIndex = user_image_data[1];
//    disposalMode = user_image_data[2];
//
////    ColorMap = (frameInfo.ColorMap
////                ? frameInfo.ColorMap
////                : gifFileType->SColorMap);
//    ColorMap = (gifFileType->Image.ColorMap
//                ? gifFileType->Image.ColorMap
//                : gifFileType->SColorMap);
//    //
//    int pointPixelIdx = sizeof(int32_t) * 3;
//    int dH = frameInfo.Width * frameInfo.Top;
//    for (int h = frameInfo.Top; h < frameInfo.Top + frameInfo.Height; h++) {
//        for (int w = frameInfo.Left; w < frameInfo.Left + frameInfo.Width; w++) {
//            setColorARGB(&sPixels[dH + w],
//                         gifBean1->current_frame,
//                         ColorMap,
//                         savedImage.RasterBits[pointPixelIdx++]);
//        }
//        dH += frameInfo.Width;
//    }
    //  --------------end of test call --------------

    AndroidBitmap_unlockPixels(env, bitmap);
    GifBean *gifBean = static_cast<GifBean *>(gifFileType->UserData);
    gifBean->current_frame++;
    if (gifBean->current_frame >= gifBean->total_frame - 1) {
        gifBean->current_frame = 0;
    }
    return 100;
}


