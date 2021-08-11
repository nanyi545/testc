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
#include <utility>
#include <queue>
#include <unistd.h>
#include <cinttypes>
#include <camera/NdkCameraManager.h>
#include "camera_manager.h"
#include "native_debug.h"
#include "camera_utils.h"

/**
 * Range of Camera Exposure Time:
 *     Camera's capability range have a very long range which may be disturbing
 *     on camera. For this sample purpose, clamp to a range showing visible
 *     video on preview: 100000ns ~ 250000000ns
 */
static const uint64_t kMinExposureTime = static_cast<uint64_t>(1000000);
static const uint64_t kMaxExposureTime = static_cast<uint64_t>(250000000);

NDKCamera::NDKCamera()
        : cameraMgr_(nullptr),
          activeCameraId_(""),
          cameraFacing_(ACAMERA_LENS_FACING_BACK),
//      cameraFacing_(ACAMERA_LENS_FACING_FRONT),
          cameraOrientation_(0),
          outputContainer_(nullptr),
          captureSessionState_(CaptureSessionState::MAX_STATE),
          exposureTime_(static_cast<int64_t>(0)) {
    valid_ = false;
    LOGI("NDKCamera constructor: i1:%d  i2:%d i3:%d i4:%d", CAPTURE_REQUEST_COUNT, JPG_CAPTURE_REQUEST_IDX,PREVIEW_REQUEST_IDX,test_4);

    requests_.resize(CAPTURE_REQUEST_COUNT);
    memset(requests_.data(), 0, requests_.size() * sizeof(requests_[0]));
    cameras_.clear();

    LOGI("camflow ACameraManager_create ----> NDKCamera constructor");
    cameraMgr_ = ACameraManager_create();

    ASSERT(cameraMgr_, "Failed to create cameraManager");

    // Pick up a back-facing camera to preview
    EnumerateCamera();
    ASSERT(activeCameraId_.size(), "Unknown ActiveCameraIdx");

    // Create back facing camera device
//    CALL_MGR(openCamera(cameraMgr_, activeCameraId_.c_str(), GetDeviceListener(),
//                        &cameras_[activeCameraId_].device_));  // equal to below  ...
    ACameraManager_openCamera(cameraMgr_, activeCameraId_.c_str(), GetDeviceListener(),
                              &cameras_[activeCameraId_].device_);
    // std:string.c_str() --> char *



//    CALL_MGR(registerAvailabilityCallback(cameraMgr_, GetManagerListener()));
    ACameraManager_registerAvailabilityCallback(cameraMgr_, GetManagerListener());


    // Initialize camera controls(exposure time and sensitivity), pick
    // up value of 2% * range + min as starting value (just a number, no magic)
    ACameraMetadata *metadataObj;
    CALL_MGR(getCameraCharacteristics(cameraMgr_, activeCameraId_.c_str(),
                                      &metadataObj));
    ACameraMetadata_const_entry val = {
            0,
    };
    camera_status_t status = ACameraMetadata_getConstEntry(
            metadataObj, ACAMERA_SENSOR_INFO_EXPOSURE_TIME_RANGE, &val);
    if (status == ACAMERA_OK) {
        exposureRange_.min_ = val.data.i64[0];
        if (exposureRange_.min_ < kMinExposureTime) {
            exposureRange_.min_ = kMinExposureTime;
        }
        exposureRange_.max_ = val.data.i64[1];
        if (exposureRange_.max_ > kMaxExposureTime) {
            exposureRange_.max_ = kMaxExposureTime;
        }
        exposureTime_ = exposureRange_.value(2);
    } else {
        LOGW("Unsupported ACAMERA_SENSOR_INFO_EXPOSURE_TIME_RANGE");
        exposureRange_.min_ = exposureRange_.max_ = 0l;
        exposureTime_ = 0l;
    }
    status = ACameraMetadata_getConstEntry(
            metadataObj, ACAMERA_SENSOR_INFO_SENSITIVITY_RANGE, &val);

    if (status == ACAMERA_OK) {
        sensitivityRange_.min_ = val.data.i32[0];
        sensitivityRange_.max_ = val.data.i32[1];

        sensitivity_ = sensitivityRange_.value(2);
    } else {
        LOGW("failed for ACAMERA_SENSOR_INFO_SENSITIVITY_RANGE");
        sensitivityRange_.min_ = sensitivityRange_.max_ = 0;
        sensitivity_ = 0;
    }
    valid_ = true;
}

/**
 * A helper class to assist image size comparison, by comparing the absolute
 * size
 * regardless of the portrait or landscape mode.
 */
class DisplayDimension {
public:
    DisplayDimension(int32_t w, int32_t h) : w_(w), h_(h), portrait_(false) {
        if (h > w) {
            // make it landscape
            w_ = h;
            h_ = w;
            portrait_ = true;
        }
    }

    DisplayDimension(const DisplayDimension &other) {
        w_ = other.w_;
        h_ = other.h_;
        portrait_ = other.portrait_;
    }

    DisplayDimension(void) {
        w_ = 0;
        h_ = 0;
        portrait_ = false;
    }

    DisplayDimension &operator=(const DisplayDimension &other) {
        w_ = other.w_;
        h_ = other.h_;
        portrait_ = other.portrait_;

        return (*this);
    }

    bool IsSameRatio(DisplayDimension &other) {
//        if(w_ * other.h_ == h_ * other.w_) {
//            return true;
//        }
        return false;
//        int diff = (w_ * other.h_ - h_ * other.w_);
//        if(diff<0){
//            diff = 0-diff;
//        }
//        return (diff * 100 < (other.h_ * other.w_) );
    }

    bool operator>(DisplayDimension &other) {
        return (w_ >= other.w_ & h_ >= other.h_);
    }

    bool operator==(DisplayDimension &other) {
        return (w_ == other.w_ && h_ == other.h_ && portrait_ == other.portrait_);
    }

    DisplayDimension operator-(DisplayDimension &other) {
        DisplayDimension delta(w_ - other.w_, h_ - other.h_);
        return delta;
    }

    void Flip(void) { portrait_ = !portrait_; }

    bool IsPortrait(void) { return portrait_; }

    int32_t width(void) { return w_; }

    int32_t height(void) { return h_; }

    int32_t org_width(void) { return (portrait_ ? h_ : w_); }

    int32_t org_height(void) { return (portrait_ ? w_ : h_); }

private:
    int32_t w_, h_;
    bool portrait_;
};

/**
 * Find a compatible camera modes:
 *    1) the same aspect ration as the native display window, which should be a
 *       rotated version of the physical device
 *    2) the smallest resolution in the camera mode list
 * This is to minimize the later color space conversion workload.
 */
bool NDKCamera::MatchCaptureSizeRequest(ANativeWindow *display,
                                        ImageFormat *resView,
                                        ImageFormat *resCap) {
    DisplayDimension disp(ANativeWindow_getWidth(display),
                          ANativeWindow_getHeight(display));

    LOGI(" -------- disp width:%d   disp height:%d ",ANativeWindow_getWidth(display),ANativeWindow_getHeight(display));


    if (cameraOrientation_ == 90 || cameraOrientation_ == 270) {
        disp.Flip();
    }

    ACameraMetadata *metadata;

//    CALL_MGR(getCameraCharacteristics(cameraMgr_, activeCameraId_.c_str(), &metadata));
    ACameraManager_getCameraCharacteristics(cameraMgr_, activeCameraId_.c_str(), &metadata);

    ACameraMetadata_const_entry entry;

//    CALL_METADATA(getConstEntry(metadata, ACAMERA_SCALER_AVAILABLE_STREAM_CONFIGURATIONS, &entry));
    ACameraMetadata_getConstEntry(metadata, ACAMERA_SCALER_AVAILABLE_STREAM_CONFIGURATIONS, &entry);


    // format of the data: format, width, height, input?, type int32


    bool foundIt = false;
    DisplayDimension foundRes(4000, 4000);
    DisplayDimension maxJPG(0, 0);

    LOGI(" total count:%d",entry.count);
    for (int i = 0; i < entry.count; i += 4) {

        /**
         * input:
         * 1 output
         * 0 input
         */
        int32_t input = entry.data.i32[i + 3];
        int32_t format = entry.data.i32[i + 0];
        LOGI(" input:%d  format:%d   x:%d  y:%d", input, format, entry.data.i32[i + 1], entry.data.i32[i + 2] );
        if(format == AIMAGE_FORMAT_YUV_420_888){
            LOGI(" input---AIMAGE_FORMAT_YUV_420_888");  // 35
        }
        if(format == AIMAGE_FORMAT_JPEG){
            LOGI(" input---AIMAGE_FORMAT_JPEG "); // 256
        }

    }


    for (int i = 0; i < entry.count; i += 4) {
        int32_t input = entry.data.i32[i + 3];
        int32_t format = entry.data.i32[i + 0];
        if (input){
            continue;
        }  else {
        }

        if (format == AIMAGE_FORMAT_YUV_420_888 || format == AIMAGE_FORMAT_JPEG) {
            DisplayDimension res(entry.data.i32[i + 1],
                                 entry.data.i32[i + 2]);
            if (!disp.IsSameRatio(res)) continue;
            if (format == AIMAGE_FORMAT_YUV_420_888 && foundRes > res) {
                foundIt = true;
                foundRes = res;
            } else if (format == AIMAGE_FORMAT_JPEG && res > maxJPG) {
                maxJPG = res;
            }
        }
    }

    if (foundIt) {
        resView->width = foundRes.org_width();
        resView->height = foundRes.org_height();
        resCap->width = maxJPG.org_width();
        resCap->height = maxJPG.org_height();
        LOGW("found  w:%d  h:%d" ,resView->width ,resView->height );

    } else {
        LOGW("Did not find any compatible camera resolution, taking 640x480");
        if (disp.IsPortrait()) {
            LOGW("w480    h640");
            resView->width = 480;
            resView->height = 640;
        } else {
            LOGW("w640    h480");
            resView->width = 640;
            resView->height = 480;
        }
        *resCap = *resView;
    }
    resView->format = AIMAGE_FORMAT_YUV_420_888;
    resCap->format = AIMAGE_FORMAT_JPEG;
    return foundIt;
}


/**
 *
 *
 * AImageReader，ANativeWindow和ANativeWindow_Buffer是预览阶段的关键因素。
 *
其中AImageReader起到一个承上启下的作用。
在发送请求阶段，绑定请求（ACaptureRequest）和输出（ACaptureSessionOutput），以获取实时视频数据。
在预览阶段，作为数据源，用于数据转换（YUV数据转ARGB等图像数据类型）。

ANativeWindow做为画布（由外部传入，并非由AImageReader，与发送请求阶段的ANativeWindow对象不同），用于显示预览图像。

ANativeWindow_Buffer则可以看作是画布（ANativeWindow）的显存。当数据以某一图片格式写入ANativeWindow_Buffer时，图像便呈现在画布（ANativeWindow）上。


]

 *
 *
 * 请求设置
 *
 * https://blog.csdn.net/daihuimaozideren/article/details/101235393
 *
 *
 * 对于整个发送请求（ACaptureRequest）过程，可以理解为，
（1）生成一个向ACameraDevice发送的，类型为ACameraDevice_request_template的ACaptureRequest。
（2）该ACaptureRequest被不断重复发送。
（3）ACaptureRequest所返回的响应数据，由ACaptureSessionOutputContainer中的ACaptureSessionOutput接收。
（4）ACaptureRequest通过ACameraOutputTarget，将ACaptureSessionOutput与AImageReader绑定。

 在预览阶段，AImageReader将用于数据的读取。
不断的发送请求，是如何完成的？只需要调用ACameraCaptureSession_setRepeatingRequest方法即可。


 因此，一个ACameraCaptureSession包含了以下信息：
（1）与哪个ACameraDevice进行会话。
（2）用哪些ACaptureSessionOutput来接收数据。

 *
 */
void NDKCamera::CreateSession(ANativeWindow *previewWindow,
                              ANativeWindow *jpgWindow, int32_t imageRotation) {
    // Create output from this app's ANativeWindow, and add into output container
    requests_[PREVIEW_REQUEST_IDX].outputNativeWindow_ = previewWindow;

    /**
     *  preview or record ??
     */
//    requests_[PREVIEW_REQUEST_IDX].template_ = TEMPLATE_PREVIEW;
    requests_[PREVIEW_REQUEST_IDX].template_ = TEMPLATE_RECORD;


    requests_[JPG_CAPTURE_REQUEST_IDX].outputNativeWindow_ = jpgWindow;
    requests_[JPG_CAPTURE_REQUEST_IDX].template_ = TEMPLATE_STILL_CAPTURE;

    //  ACaptureSessionOutputContainer是一个可以容纳多个ACaptureSessionOutput的集合。
//    CALL_CONTAINER(create(&outputContainer_));
    ACaptureSessionOutputContainer_create(&outputContainer_);

    for (auto &req : requests_) {
        ANativeWindow_acquire(req.outputNativeWindow_);

        //  ANativeWindow对象通过ACaptureSessionOutput_create方法，获得一个ACaptureSessionOutput对象
        //  ACaptureRequest所返回的响应数据，由ACaptureSessionOutputContainer中的ACaptureSessionOutput接收。
//        CALL_OUTPUT(create(req.outputNativeWindow_, &req.sessionOutput_));
        ACaptureSessionOutput_create(req.outputNativeWindow_, &req.sessionOutput_);


//        CALL_CONTAINER(add(outputContainer_, req.sessionOutput_));
        ACaptureSessionOutputContainer_add(outputContainer_, req.sessionOutput_);


        // ANativeWindow对象通过ACameraOutputTarget_create方法，创建一个ACameraOutputTarget对象。
        //
//        CALL_TARGET(create(req.outputNativeWindow_, &req.target_));
        ACameraOutputTarget_create(req.outputNativeWindow_, &req.target_);


        // 创建请求  一个ACaptureRequest
//        CALL_DEV(createCaptureRequest(cameras_[activeCameraId_].device_, req.template_, &req.request_));
        ACameraDevice_createCaptureRequest(cameras_[activeCameraId_].device_, req.template_, &req.request_);


        // 通过ACaptureRequest_addTarget方法，将ACameraOutputTarget与AImageReader建立关联。
//        CALL_REQUEST(addTarget(req.request_, req.target_));
        ACaptureRequest_addTarget(req.request_, req.target_);

    }

    // Create a capture session for the given preview request
    captureSessionState_ = CaptureSessionState::READY;
    // ACameraCaptureSession是一个会话。它也是由ACameraDevice生成，同时还绑定来一个ACaptureSessionOutputContainer。
//    CALL_DEV(createCaptureSession(cameras_[activeCameraId_].device_, outputContainer_, GetSessionListener(), &captureSession_));
    ACameraDevice_createCaptureSession(cameras_[activeCameraId_].device_, outputContainer_, GetSessionListener(), &captureSession_);
    /**
 因此，一个ACameraCaptureSession包含了以下信息：
（1）与哪个ACameraDevice进行会话。
（2）用哪些ACaptureSessionOutput来接收数据。
     */


    ACaptureRequest_setEntry_i32(requests_[JPG_CAPTURE_REQUEST_IDX].request_, ACAMERA_JPEG_ORIENTATION, 1, &imageRotation);

    /*
     * Only preview request is in manual mode, JPG is always in Auto mode
     * JPG capture mode could also be switch into manual mode and control
     * the capture parameters, this sample leaves JPG capture to be auto mode
     * (auto control has better effect than author's manual control)
     */
//    uint8_t aeModeOff = ACAMERA_CONTROL_AE_MODE_OFF;
//    CALL_REQUEST(setEntry_u8(requests_[PREVIEW_REQUEST_IDX].request_, ACAMERA_CONTROL_AE_MODE, 1, &aeModeOff));
//    CALL_REQUEST(setEntry_i32(requests_[PREVIEW_REQUEST_IDX].request_, ACAMERA_SENSOR_SENSITIVITY, 1, &sensitivity_));
//    CALL_REQUEST(setEntry_i64(requests_[PREVIEW_REQUEST_IDX].request_, ACAMERA_SENSOR_EXPOSURE_TIME, 1, &exposureTime_));


}

NDKCamera::~NDKCamera() {
    valid_ = false;
    // stop session if it is on:
    if (captureSessionState_ == CaptureSessionState::ACTIVE) {
        ACameraCaptureSession_stopRepeating(captureSession_);
    }
    ACameraCaptureSession_close(captureSession_);

    for (auto &req : requests_) {
        CALL_REQUEST(removeTarget(req.request_, req.target_));
        ACaptureRequest_free(req.request_);
        ACameraOutputTarget_free(req.target_);

        CALL_CONTAINER(remove(outputContainer_, req.sessionOutput_));
        ACaptureSessionOutput_free(req.sessionOutput_);

        ANativeWindow_release(req.outputNativeWindow_);
    }

    requests_.resize(0);
    ACaptureSessionOutputContainer_free(outputContainer_);

    for (auto &cam : cameras_) {
        if (cam.second.device_) {
            CALL_DEV(close(cam.second.device_));
        }
    }
    cameras_.clear();
    if (cameraMgr_) {
        CALL_MGR(unregisterAvailabilityCallback(cameraMgr_, GetManagerListener()));
        ACameraManager_delete(cameraMgr_);
        cameraMgr_ = nullptr;
    }
}

/**
 * EnumerateCamera()
 *     Loop through cameras on the system, pick up
 *     1) back facing one if available
 *     2) otherwise pick the first one reported to us
 */
void NDKCamera::EnumerateCamera() {
    ACameraIdList *cameraIds = nullptr;


    LOGI("camflow ACameraManager_getCameraIdList ----> camera_manager.cpp / NDKCamera constructor -EnumerateCamera");
//  CALL_MGR(getCameraIdList(cameraMgr_, &cameraIds));  // equal to below call ....
    ACameraManager_getCameraIdList(cameraMgr_, &cameraIds);

    for (int i = 0; i < cameraIds->numCameras; ++i) {
        const char *id = cameraIds->cameraIds[i];
        LOGI("camflow camid ind:%d -%s", i, id);
        ACameraMetadata *metadataObj;


//    CALL_MGR(getCameraCharacteristics(cameraMgr_, id, &metadataObj)); // // equal to below call ....
        ACameraManager_getCameraCharacteristics(cameraMgr_, id, &metadataObj);

        int32_t count = 0;
        const uint32_t *tags = nullptr;

        /**
         *  get all tags ....
         */
        ACameraMetadata_getAllTags(metadataObj, &count, &tags);

        LOGI("camflow ????  count:%d  tags:%d",count, *tags);

        for (int tagIdx = 0; tagIdx < count; ++tagIdx) {


            if(ACAMERA_JPEG_ORIENTATION == tags[tagIdx]) {
                LOGI("camflow ----ACAMERA_JPEG_ORIENTATION   tagIdx:%d  d:%d",tagIdx,(tags[tagIdx] - ACAMERA_JPEG_START));

            }
            if(ACAMERA_JPEG_THUMBNAIL_QUALITY == tags[tagIdx]) {
                LOGI("camflow ----ACAMERA_JPEG_THUMBNAIL_QUALITY   tagIdx:%d  d:%d",tagIdx,(tags[tagIdx] - ACAMERA_JPEG_START));
            }


            if(ACAMERA_LENS_POSE_ROTATION == tags[tagIdx]) {
                LOGI("camflow ----ACAMERA_LENS_POSE_ROTATION   tagIdx:%d  d:%d",tagIdx,(tags[tagIdx] - ACAMERA_LENS_START));

            }

            if(ACAMERA_LENS_INFO_MINIMUM_FOCUS_DISTANCE == tags[tagIdx]) {

                ACameraMetadata_const_entry minmumFocus = {
                        0,
                };
                ACameraMetadata_getConstEntry(metadataObj, tags[tagIdx], &minmumFocus);
                LOGI("camflow ----ACAMERA_LENS_INFO_MINIMUM_FOCUS_DISTANCE   tagIdx:%d  d:%d   minmumFocus:%f",
                        tagIdx,(tags[tagIdx] - ACAMERA_LENS_INFO_START), *minmumFocus.data.f);

            }

            if(ACAMERA_SCALER_AVAILABLE_STREAM_CONFIGURATIONS == tags[tagIdx]) {
                LOGI("streamconfig ----ACAMERA_SCALER_AVAILABLE_STREAM_CONFIGURATIONS   tagIdx:%d  d:%d",tagIdx,(tags[tagIdx] - ACAMERA_SCALER_START));

                ACameraMetadata_const_entry streamConfig = {
                        0,
                };
                ACameraMetadata_getConstEntry(metadataObj, tags[tagIdx], &streamConfig);

                for (int i=0;i<streamConfig.count/4;i++){
                    LOGI("streamconfig ---- config%d :[%d,%d,%d,%d]",i,streamConfig.data.i32[0+i*4],streamConfig.data.i32[1+i*4],streamConfig.data.i32[2+i*4],streamConfig.data.i32[3+i*4]);
                    switch(streamConfig.data.i32[0+i*4]){
                        case AIMAGE_FORMAT_YUV_420_888:
                            LOGI("streamconfig ---- AIMAGE_FORMAT_YUV_420_888");
                            break;
                        case AIMAGE_FORMAT_JPEG:
                            LOGI("streamconfig ---- AIMAGE_FORMAT_JPEG");
                            break;
                        case AIMAGE_FORMAT_RGBA_FP16:
                            LOGI("streamconfig ---- AIMAGE_FORMAT_RGBA_FP16");
                            break;
                        case AIMAGE_FORMAT_PRIVATE:
                            LOGI("streamconfig ---- AIMAGE_FORMAT_PRIVATE");
                            break;
                    }
                }
            }






            if (ACAMERA_LENS_FACING == tags[tagIdx]) {


                ACameraMetadata_const_entry lensInfo = {
                        0,
                };

//                CALL_METADATA(getConstEntry(metadataObj, tags[tagIdx], &lensInfo));
                ACameraMetadata_getConstEntry(metadataObj, tags[tagIdx], &lensInfo);

                CameraId cam(id);
                cam.facing_ = static_cast<acamera_metadata_enum_android_lens_facing_t>(
                        lensInfo.data.u8[0]);
                cam.owner_ = false;
                cam.device_ = nullptr;
                cameras_[cam.id_] = cam;

                LOGI("camflow ----ACAMERA_LENS_FACING   tagIdx:%d  d:%d   facing:%d",tagIdx,(tags[tagIdx] - ACAMERA_LENS_START),cam.facing_);

                if (cam.facing_ == ACAMERA_LENS_FACING_FRONT) {
                    activeCameraId_ = cam.id_;
                }

//                if (cam.facing_ == ACAMERA_LENS_FACING_BACK) {
//                    activeCameraId_ = cam.id_;
//                }

//                break;
            }
        }
        ACameraMetadata_free(metadataObj);
    }

    ASSERT(cameras_.size(), "No Camera Available on the device");
    if (activeCameraId_.length() == 0) {
        // if no back facing camera found, pick up the first one to use...
        activeCameraId_ = cameras_.begin()->second.id_;
    }
    ACameraManager_deleteCameraIdList(cameraIds);
}

/**
 * GetSensorOrientation()
 *     Retrieve current sensor orientation regarding to the phone device
 * orientation
 *     SensorOrientation is NOT settable.
 */
bool NDKCamera::GetSensorOrientation(int32_t *facing, int32_t *angle) {
    if (!cameraMgr_) {
        return false;
    }

    ACameraMetadata *metadataObj;
    ACameraMetadata_const_entry face, orientation;
    CALL_MGR(getCameraCharacteristics(cameraMgr_, activeCameraId_.c_str(),
                                      &metadataObj));
    CALL_METADATA(getConstEntry(metadataObj, ACAMERA_LENS_FACING, &face));
    cameraFacing_ = static_cast<int32_t>(face.data.u8[0]);
    LOGI("====Current cameraFacing_: %d", cameraFacing_);

    CALL_METADATA(
            getConstEntry(metadataObj, ACAMERA_SENSOR_ORIENTATION, &orientation));

    LOGI("====Current SENSOR_ORIENTATION: %8d", orientation.data.i32[0]);

    ACameraMetadata_free(metadataObj);
    cameraOrientation_ = orientation.data.i32[0];

    if (facing) *facing = cameraFacing_;
    if (angle) *angle = cameraOrientation_;
    return true;
}

/**
 * StartPreview()
 *   Toggle preview start/stop
 */
void NDKCamera::StartPreview(bool start) {
    if (start) {

//        CALL_SESSION(setRepeatingRequest(captureSession_, nullptr, 1,
//                                         &requests_[PREVIEW_REQUEST_IDX].request_,
//                                         nullptr));
        // same as above
        ACameraCaptureSession_setRepeatingRequest(captureSession_, nullptr, 1,
                                                  &requests_[PREVIEW_REQUEST_IDX].request_,
                                                  nullptr);

    } else if (!start && captureSessionState_ == CaptureSessionState::ACTIVE) {
        ACameraCaptureSession_stopRepeating(captureSession_);
    } else {
        ASSERT(false, "Conflict states(%s, %d)", (start ? "true" : "false"),
               captureSessionState_);
    }
}

/**
 * Capture one jpg photo into
 *     /sdcard/DCIM/Camera
 * refer to WriteFile() for details
 */
bool NDKCamera::TakePhoto(void) {
    if (captureSessionState_ == CaptureSessionState::ACTIVE) {
        ACameraCaptureSession_stopRepeating(captureSession_);
    }


    ACameraCaptureSession_capture(captureSession_, GetCaptureCallback(), 1,
                                  &requests_[JPG_CAPTURE_REQUEST_IDX].request_,
                                  &requests_[JPG_CAPTURE_REQUEST_IDX].sessionSequenceId_);
    // same as above ...
//    CALL_SESSION(capture(captureSession_, GetCaptureCallback(), 1,
//                         &requests_[JPG_CAPTURE_REQUEST_IDX].request_,
//                         &requests_[JPG_CAPTURE_REQUEST_IDX].sessionSequenceId_));

    return true;
}

void NDKCamera::UpdateCameraRequestParameter(int32_t code, int64_t val) {
    ACaptureRequest *request = requests_[PREVIEW_REQUEST_IDX].request_;
    switch (code) {
        case ACAMERA_SENSOR_EXPOSURE_TIME:
            if (exposureRange_.Supported()) {
                exposureTime_ = val;
                CALL_REQUEST(setEntry_i64(request, ACAMERA_SENSOR_EXPOSURE_TIME, 1,
                                          &exposureTime_));
            }
            break;

        case ACAMERA_SENSOR_SENSITIVITY:
            if (sensitivityRange_.Supported()) {
                sensitivity_ = val;
                CALL_REQUEST(
                        setEntry_i32(request, ACAMERA_SENSOR_SENSITIVITY, 1, &sensitivity_));
            }
            break;
        default:
            ASSERT(false, "==ERROR==: error code for CameraParameterChange: %d",
                   code);
            return;
    }

    uint8_t aeModeOff = ACAMERA_CONTROL_AE_MODE_OFF;
    CALL_REQUEST(setEntry_u8(request, ACAMERA_CONTROL_AE_MODE, 1, &aeModeOff));
    CALL_SESSION(
            setRepeatingRequest(captureSession_, nullptr, 1, &request,
                                &requests_[PREVIEW_REQUEST_IDX].sessionSequenceId_));
}

/**
 * Retrieve Camera Exposure adjustable range.
 *
 * @param min Camera minimium exposure time in nanoseconds
 * @param max Camera maximum exposure tiem in nanoseconds
 *
 * @return true  min and max are loaded with the camera's exposure values
 *         false camera has not initialized, no value available
 */
bool NDKCamera::GetExposureRange(int64_t *min, int64_t *max, int64_t *curVal) {
    if (!exposureRange_.Supported() || !exposureTime_ || !min || !max || !curVal) {
        return false;
    }
    *min = exposureRange_.min_;
    *max = exposureRange_.max_;
    *curVal = exposureTime_;

    return true;
}

/**
 * Retrieve Camera sensitivity range.
 *
 * @param min Camera minimium sensitivity
 * @param max Camera maximum sensitivity
 *
 * @return true  min and max are loaded with the camera's sensitivity values
 *         false camera has not initialized, no value available
 */
bool NDKCamera::GetSensitivityRange(int64_t *min, int64_t *max,
                                    int64_t *curVal) {
    if (!sensitivityRange_.Supported() || !sensitivity_ || !min || !max || !curVal) {
        return false;
    }
    *min = static_cast<int64_t>(sensitivityRange_.min_);
    *max = static_cast<int64_t>(sensitivityRange_.max_);
    *curVal = sensitivity_;
    return true;
}
