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


JavaVM *javaVM;

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    javaVM = vm;
    __android_log_print(ANDROID_LOG_VERBOSE, "jni_onload","jni onload");
    return JNI_VERSION_1_4;
}


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


    std::string hello = "Hello -- from C++";

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





/**
 *  ... below is rtmp ....
 *
 *  start of RTMP
**/


extern "C"{
#include "librtmp/rtmp.h"
}
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,"David",__VA_ARGS__)
typedef struct {
    int16_t sps_len;
    int16_t pps_len;
    int8_t *sps;
    int8_t *pps;
    RTMP *rtmp;
} Live;
Live *live = nullptr;
void prepareVideo(int8_t *data, int len, Live *live) {
    for (int i = 0; i < len; i++) {
        //0x00 0x00 0x00 0x01
        if (i + 4 < len) {
            if (data[i] == 0x00 && data[i + 1] == 0x00
                && data[i + 2] == 0x00
                && data[i + 3] == 0x01) {
                //0x00 0x00 0x00 0x01 7 sps 0x00 0x00 0x00 0x01 8 pps
                //将sps pps分开
                //找到pps
                if (data[i + 4]  == 0x68) {
                    //去掉界定符
                    live->sps_len = i - 4;
                    live->sps = static_cast<int8_t *>(malloc(live->sps_len));
                    memcpy(live->sps, data + 4, live->sps_len);

                    live->pps_len = len - (4 + live->sps_len) - 4;
                    live->pps = static_cast<int8_t *>(malloc(live->pps_len));
                    memcpy(live->pps, data + 4 + live->sps_len + 4, live->pps_len);
                    LOGI("sps:%d pps:%d", live->sps_len, live->pps_len);
                    break;
                }
            }
        }
    }
}



/**
 *   first byte/0x01/0x00/0x00/0x00/ 4bytes h264 length  / ------ h264 data ------/
 *
 *   iframe: 0x17
 *   non-iframe : 0x27
 *
 *
 * @param buf
 * @param len
 * @param tms
 * @param live
 * @return
 */
RTMPPacket *createVideoPackage(int8_t *buf, int len, const long tms, Live *live) {
//    分隔符被抛弃了      --buf指的是651
    buf += 4;  // skip first 4 bytes in a h264 frame :   0001
    len -= 4;
    int body_size = len + 9;
    RTMPPacket *packet = (RTMPPacket *) malloc(sizeof(RTMPPacket));
    RTMPPacket_Alloc(packet, len + 9);

    packet->m_body[0] = 0x27;
    if (buf[0] == 0x65) { //关键帧
        packet->m_body[0] = 0x17;
        LOGI("发送关键帧 data");
    }
    packet->m_body[1] = 0x01;
    packet->m_body[2] = 0x00;
    packet->m_body[3] = 0x00;
    packet->m_body[4] = 0x00;
    //长度
    packet->m_body[5] = (len >> 24) & 0xff;
    packet->m_body[6] = (len >> 16) & 0xff;
    packet->m_body[7] = (len >> 8) & 0xff;
    packet->m_body[8] = (len) & 0xff;

    //数据
    memcpy(&packet->m_body[9], buf, len);


    packet->m_packetType = RTMP_PACKET_TYPE_VIDEO;
    packet->m_nBodySize = body_size;
    packet->m_nChannel = 0x04;
    packet->m_nTimeStamp = tms;
    packet->m_hasAbsTimestamp = 0;
    packet->m_headerType = RTMP_PACKET_SIZE_LARGE;
    packet->m_nInfoField2 = live->rtmp->m_stream_id;
    return packet;

}

/**
 * video package sps/pps
 *
 * 0x17|0x00|0x00|0x00|0x00|0x01|----|----|----|0xFF|0xE1|----|----|-------- sps data ------|0x01|----|----|--------- pps data -------|
 *                          configurationVersion 版本号
 *                               sps[1] profile 如baseline、main、 high
 *                                    sps[2] profile_compatibility 兼容性
 *                                         sps[3] profile level
 *
 *                                                        sps length 2 bytes                      pps length 2 bytes
 *
 *
 *
 * @param live
 * @return
 */
RTMPPacket *createVideoPackage(Live *live) {
    int body_size = 13 + live->sps_len + 3 + live->pps_len;
    RTMPPacket *packet = (RTMPPacket *) malloc(sizeof(RTMPPacket));
    RTMPPacket_Alloc(packet, body_size);
    int i = 0;
    //AVC sequence header 与IDR一样
    packet->m_body[i++] = 0x17;
    //AVC sequence header 设置为0x00
    packet->m_body[i++] = 0x00;
    //CompositionTime
    packet->m_body[i++] = 0x00;
    packet->m_body[i++] = 0x00;
    packet->m_body[i++] = 0x00;
    //AVC sequence header
    packet->m_body[i++] = 0x01;   //configurationVersion 版本号 1
    packet->m_body[i++] = live->sps[1]; //profile 如baseline、main、 high

    packet->m_body[i++] = live->sps[2]; //profile_compatibility 兼容性
    packet->m_body[i++] = live->sps[3]; //profile level
    packet->m_body[i++] = 0xFF; // reserved（111111） + lengthSizeMinusOne（2位 nal 长度） 总是0xff
    //sps
    packet->m_body[i++] = 0xE1; //reserved（111） + lengthSizeMinusOne（5位 sps 个数） 总是0xe1
    //sps length 2字节
    packet->m_body[i++] = (live->sps_len >> 8) & 0xff; //第0个字节
    packet->m_body[i++] = live->sps_len & 0xff;        //第1个字节
    memcpy(&packet->m_body[i], live->sps, live->sps_len);
    i += live->sps_len;

    /*pps*/
    packet->m_body[i++] = 0x01; //pps number
    //pps length
    packet->m_body[i++] = (live->pps_len >> 8) & 0xff;
    packet->m_body[i++] = live->pps_len & 0xff;
    memcpy(&packet->m_body[i], live->pps, live->pps_len);

    packet->m_packetType = RTMP_PACKET_TYPE_VIDEO;
    packet->m_nBodySize = body_size;
    packet->m_nChannel = 0x04;
    packet->m_nTimeStamp = 0;
    packet->m_hasAbsTimestamp = 0;
    packet->m_headerType = RTMP_PACKET_SIZE_LARGE;
    packet->m_nInfoField2 = live->rtmp->m_stream_id;
    return packet;
}
int sendPacket(RTMPPacket *packet) {
    int r = RTMP_SendPacket(live->rtmp, packet, 1);
    RTMPPacket_Free(packet);
    free(packet);
    return r;
}

int sendVideo(int8_t *buf, int len, long tms) {
    int ret;
    if (buf[4] == 0x67) {//sps pps
        if (live && (!live->pps || !live->sps)) {
            LOGI("ffff prepareVideo sps/pps ");
            prepareVideo(buf, len, live);
        }
    } else {
        if (buf[4] == 0x65) {//关键帧
            RTMPPacket *packet = createVideoPackage(live);
            LOGI("ffff i-frame ------------- create/send sps/pps");
            if (!(ret = sendPacket(packet))) {
            }
        }
        LOGI("ffff non-i-framesend ----------------  create/send frame data");
        RTMPPacket *packet = createVideoPackage(buf, len, tms, live);
        ret = sendPacket(packet);
    }
    return ret;
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_com_example_testc2_rtmp_ScreenLive_connect(JNIEnv *env, jobject thiz, jstring url_) {
    const char *url = env->GetStringUTFChars(url_, 0);
    int ret;
    do {
        live = (Live*)malloc(sizeof(Live));
        memset(live, 0, sizeof(Live));
        live->rtmp = RTMP_Alloc();
        RTMP_Init(live->rtmp);
        live->rtmp->Link.timeout = 10;
        LOGI("connect %s", url);
        if (!(ret = RTMP_SetupURL(live->rtmp, (char*)url))) break;
        RTMP_EnableWrite(live->rtmp);
        LOGI("RTMP_Connect");
        if (!(ret = RTMP_Connect(live->rtmp, 0))) break;
        LOGI("RTMP_ConnectStream ");
        if (!(ret = RTMP_ConnectStream(live->rtmp, 0))) break;
        LOGI("connect success");
    } while (0);
    if (!ret && live) {
        free(live);
        live = nullptr;
    }

    env->ReleaseStringUTFChars(url_, url);
    return ret;
}

/**
 *
 * video packet:
 *
 * |0xAF|0x00|AAC DATA|   first packet ...
 * |0xAF|0x01|AAC DATA|   later packet ...
 *
 * @param buf
 * @param len
 * @param type
 * @param tms
 * @param live
 * @return
 */
RTMPPacket *createAudioPacket(int8_t *buf, const int len, const int type, const long tms,
                              Live *live) {

//    组装音频包  两个字节    是固定的   af    如果是第一次发  你就是 01       如果后面   00  或者是 01  aac
    int body_size = len + 2;
    RTMPPacket *packet = (RTMPPacket *) malloc(sizeof(RTMPPacket));
    RTMPPacket_Alloc(packet, body_size);
//         音频头
    packet->m_body[0] = 0xAF;
    if (type == 1) {
//        头
        packet->m_body[1] = 0x00;
    }else{
        packet->m_body[1] = 0x01;
    }
    memcpy(&packet->m_body[2], buf, len);
    packet->m_packetType = RTMP_PACKET_TYPE_AUDIO;
    packet->m_nChannel = 0x05;
    packet->m_nBodySize = body_size;
    packet->m_nTimeStamp = tms;
    packet->m_hasAbsTimestamp = 0;
    packet->m_headerType = RTMP_PACKET_SIZE_LARGE;
    packet->m_nInfoField2 = live->rtmp->m_stream_id;
    return packet;
}
int sendAudio(int8_t *buf, int len, int type, int tms) {
//    创建音频包   如何组装音频包
    RTMPPacket *packet = createAudioPacket(buf, len, type, tms, live);
    int ret=sendPacket(packet);
    return ret;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_example_testc2_rtmp_ScreenLive_sendData(JNIEnv *env, jobject thiz, jbyteArray data_,
                                                 jint len, jlong tms,jint type) {
    int ret;
    jbyte *data = env->GetByteArrayElements(data_, NULL);
//视频  音频
    switch (type) {
        case 0: //video
            ret = sendVideo(data, len, tms);

            break;
        default: //audio
            ret = sendAudio(data, len, type, tms);
            LOGI("send Audio  lenght :%d",len);
            break;
    }
    env->ReleaseByteArrayElements(data_, data, 0);
    return ret;
}

/**
*  end of RTMP
**/




/**
 *  start -- x264
**/

#include <x264.h>
#include <x264_config.h>

int mWidth;
int mHeight;
int mFps;
int mBitrate;

// 容器 x264_picture_t ---->   bytebuffer
x264_picture_t *pic_in = 0;

int ySize;
int uvSize;

//    编码器
x264_t *videoCodec = 0;


void setVideoEncInfo(int width, int height, int fps, int bitrate){
//    实例化X264
    mWidth = width;
    mHeight = height;
    mFps = fps;
    mBitrate = bitrate;
//
    ySize = width * height;
    uvSize = ySize / 4;
    if (videoCodec) {
        x264_encoder_close(videoCodec);
        videoCodec = 0;
    }
//    定义参数
    x264_param_t param;
//    参数赋值   x264  麻烦  编码器 速度   直播  越快 1  越慢2
    x264_param_default_preset(&param, "ultrafast", "zerolatency");
//编码等级
    param.i_level_idc = 32;
//    选取显示格式
    param.i_csp = X264_CSP_I420;
    param.i_width = width;
    param.i_height = height;
//    B帧
    param.i_bframe = 0;
//折中    cpu   突发情况   ABR 平均
    param.rc.i_rc_method = X264_RC_ABR;
//k为单位
    param.rc.i_bitrate = bitrate / 1024;
//帧率   1s/25帧     40ms  视频 编码      帧时间 ms存储  us   s
    param.i_fps_num = fps;
//    帧率 时间  分子  分母
    param.i_fps_den = 1;
//    分母
    param.i_timebase_den = param.i_fps_num;
//    分子
    param.i_timebase_num = param.i_fps_den;

//单位 分子/分母
    //用fps而不是时间戳来计算帧间距离
    param.b_vfr_input = 0;
//I帧间隔     2s  15*2
    param.i_keyint_max = fps * 2;

    // 是否复制sps和pps放在每个关键帧的前面 该参数设置是让每个关键帧(I帧)都附带sps/pps。
    param.b_repeat_headers = 1;
//    sps  pps  赋值及裙楼
    //多线程
    param.i_threads = 1;
    x264_param_apply_profile(&param, "baseline");
//    打开编码器  宽 高一定是交换的
    videoCodec = x264_encoder_open(&param);
//容器
    pic_in = new x264_picture_t;
//设置初始化大小  容器大小就确定的
    x264_picture_alloc(pic_in, X264_CSP_I420, width, height);

}




jobject jobj;
jmethodID jmid_postData;

void postH264(JNIEnv *env,char *data, int length){
    jbyteArray array = env->NewByteArray(length);
    env->SetByteArrayRegion(array, 0, length, reinterpret_cast<const jbyte *>(data));
    env->CallVoidMethod(jobj, jmid_postData,array);
}


void doEncode(int8_t *data,JNIEnv *env){
//    容器   y的数据
    memcpy(pic_in->img.plane[0], data, ySize);
    for (int i = 0; i < uvSize; ++i) {
        //v数据
        *(pic_in->img.plane[2] + i) = *(data + ySize + i * 2);
        //间隔1个字节取一个数据
        //u数据
        *(pic_in->img.plane[1] + i) = *(data + ySize + i * 2+1 );
    }
//编码成H264码流

    //编码出了几个 nalu （暂时理解为帧）  1   pi_nal  1  永远是1
    int pi_nal;
//     编码出了几帧
    //编码出的数据 H264
    x264_nal_t *pp_nals;
//    pp_nal[]
//编码出的参数  BufferInfo
    x264_picture_t pic_out;
//关键的一句话   yuv  数据 转化 H  sendPacket
    x264_encoder_encode(videoCodec, &pp_nals, &pi_nal, pic_in, &pic_out);


    //sps数据   30
    uint8_t sps[100];
    uint8_t pps[100];


    int sps_len, pps_len;
    LOGI("编码出的帧数  %d",pi_nal);
    if (pi_nal > 0) {
        for (int i = 0; i < pi_nal; ++i) {
            LOGI("输出索引:  %d  输出长度 %d",i,pi_nal);
                postH264(env,reinterpret_cast<char *>(pp_nals[i].p_payload), pp_nals[i].i_payload);
            if (pp_nals[i].i_type == NAL_SPS) {
//        sps
                sps_len = pp_nals[i].i_payload - 4;
                memcpy(sps, pp_nals[i].p_payload + 4, sps_len);
            }  else if (pp_nals[i].i_type == NAL_PPS) {
//        到了pps
                pps_len = pp_nals[i].i_payload - 4;
                memcpy(pps, pp_nals[i].p_payload + 4, pps_len);
//                发送出去
//                sendSpsPps(sps, pps, sps_len, pps_len);
                LOGI("sps_len:  %d  pps_len %d",sps_len,pps_len);
            } else{
                LOGI("关键帧、非关键帧");
                //关键帧、非关键帧
//                sendFrame(pp_nals[i].i_type,pp_nals[i].i_payload,pp_nals[i].p_payload);
            }
        }
    }
    LOGI("pi_nal  %d",pi_nal);
//pp_nal  输出来
//    H264码流
    return;

}


extern "C"
JNIEXPORT void JNICALL
Java_com_example_testc2_x264_X264Encoder_init(JNIEnv *env, jobject thiz, jint width, jint height, jint fps, jint bitrate) {
    jobj = env->NewGlobalRef(thiz);
    jclass jclazz = env->GetObjectClass(jobj);
    jmid_postData = env->GetMethodID(jclazz, "postData", "([B)V");
    setVideoEncInfo(width, height,fps,bitrate);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_testc2_x264_X264Encoder_encode(JNIEnv *env, jobject thiz, jbyteArray frame) {
    jbyte *data = env->GetByteArrayElements(frame, NULL);
    doEncode(data,env);
    env->ReleaseByteArrayElements(frame, data, 0);
}



/**
 *  end of -- x264
**/
