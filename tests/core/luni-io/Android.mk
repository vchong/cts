# Copyright (C) 2009 The Android Open Source Project
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

ifeq ($(BUILD_CTSCORE_PACKAGE),)
    $(error BUILD_CTSCORE_PACKAGE must be defined)
endif

#
# Luni-Io Tests
##########################################################
include $(CLEAR_VARS)

LOCAL_SRC_FILES := $(call all-java-files-under,../../../../libcore/luni/src/test/java/tests/api/java/io) \
	$(call all-java-files-under,../../../../libcore/luni/src/test/java/libcore/java/io) \
	$(call all-java-files-under,../../../../libcore/support/src/test/java/) \
	$(call all-java-files-under,../../../../libcore/luni/src/test/java/org/apache/harmony/luni/tests/pkg1) \
	$(call all-java-files-under,../../../../libcore/luni/src/test/java/org/apache/harmony/luni/tests/pkg2) \
	../../../../libcore/luni/src/test/java/tests/luni/AllTestsIo.java

LOCAL_PACKAGE_NAME := android.core.tests.luni.io

# for java.* javax.* support classes in libcore/support/src/test/java
LOCAL_DX_FLAGS := --core-library

include $(BUILD_CTSCORE_PACKAGE)
