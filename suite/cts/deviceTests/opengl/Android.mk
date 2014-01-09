# Copyright (C) 2013 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

# don't include this package in any target
LOCAL_MODULE_TAGS := optional

LOCAL_STATIC_JAVA_LIBRARIES := ctsdeviceutil ctstestrunner

LOCAL_JNI_SHARED_LIBRARIES := libctsopengl_jni

LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_PACKAGE_NAME := CtsDeviceOpenGl

LOCAL_SDK_VERSION := 16

include $(BUILD_CTS_PACKAGE)

include $(call all-makefiles-under,$(LOCAL_PATH))
