# Copyright (C) 2016 The Android Open Source Project
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

LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_JAVA_RESOURCE_DIRS := assets/$(PLATFORM_SDK_VERSION)/

LOCAL_MODULE_TAGS := tests

LOCAL_MODULE := CtsSysUiHostTestCases

LOCAL_JAVA_LIBRARIES := cts-tradefed_v2 tradefed-prebuilt

LOCAL_STATIC_JAVA_LIBRARIES := cts-migration-lib

LOCAL_CTS_TEST_PACKAGE := android.host.systemui

LOCAL_SDK_VERSION := current

# Tag this module as a cts_v2 test artifact
LOCAL_COMPATIBILITY_SUITE := cts_v2

include $(BUILD_CTS_HOST_JAVA_LIBRARY)

include $(call all-makefiles-under,$(LOCAL_PATH))
