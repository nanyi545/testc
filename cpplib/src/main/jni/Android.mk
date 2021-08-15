LOCAL_PATH:=$(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := NDKUtils
LOCAL_SRC_FILES := test1.cpp
include $(BUILD_SHARED_LIBRARY)
