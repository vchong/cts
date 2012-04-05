/*
 * Copyright (C) 2012 The Android Open Source Project
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


#include <jni.h>
#include <android/log.h>

#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include "attach_shader_one.h"
#include "attach_shader_two.h"
#include "attach_shader_three.h"
#include "attach_shader_four.h"
#include "attach_shader_five.h"
#include "attach_shader_six.h"

#define  LOG_TAG    "gl2_jni_libone"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

static GLint errorAttachShader = -1;
static GLint shaderCount = -1;


extern "C" JNIEXPORT void JNICALL Java_android_opengl_cts_GL2JniLibOne_init
  (JNIEnv *, jclass pClass, jint pCategory, jint pSubCategory)  {
    LOGI("Category :  %d\n", pCategory);

    if(pCategory == 1) {
        if(pSubCategory == 1) {
            Data data = attachShaderOne();
            LOGI("Attach Shader Error :  %d\n", data.mShaderError);
            LOGI("Shader Count :  %d\n", data.mShaderCount);
            errorAttachShader = data.mShaderError;
            shaderCount = data.mShaderCount;
        }else if(pSubCategory == 2) {
            Data data = attachShaderTwo();
            LOGI("Attach Shader Error :  %d\n", data.mShaderError);
            errorAttachShader = data.mShaderError;
        }else if(pSubCategory == 3) {
            Data data = attachShaderThree();
            LOGI("Attach Shader Error :  %d\n", data.mShaderError);
            errorAttachShader = data.mShaderError;
        }else if(pSubCategory == 4) {
            Data data = attachShaderFour();
            LOGI("Attach Shader Error :  %d\n", data.mShaderError);
            LOGI("Shader Count :  %d\n", data.mShaderCount);
            errorAttachShader = data.mShaderError;
            shaderCount = data.mShaderCount;
        }else if(pSubCategory == 5) {
            Data data = attachShaderFive();
            LOGI("Attach Shader Error :  %d\n", data.mShaderError);
            errorAttachShader = data.mShaderError;
        }else if(pSubCategory == 6) {
            Data data = attachShaderSix();
            LOGI("Attach Shader Error :  %d\n", data.mShaderError);
            errorAttachShader = data.mShaderError;
        }
    }
}

extern "C" JNIEXPORT void JNICALL Java_android_opengl_cts_GL2JniLibOne_step(JNIEnv * env, jclass obj)
{
    //implement later
}

extern "C" JNIEXPORT jint JNICALL Java_android_opengl_cts_GL2JniLibOne_getAttachShaderError(JNIEnv * env, jclass obj){
    return errorAttachShader;
}

extern "C" JNIEXPORT jint JNICALL Java_android_opengl_cts_GL2JniLibOne_getLoadShaderError(JNIEnv * env, jclass obj){
     return -1;
}

extern "C" JNIEXPORT jint JNICALL Java_android_opengl_cts_GL2JniLibOne_getProgramError(JNIEnv * env, jclass obj){
     return -2;
}

extern "C" JNIEXPORT jint JNICALL Java_android_opengl_cts_GL2JniLibOne_getAttachedShaderCount(JNIEnv * env, jclass obj){
    return shaderCount;
}


