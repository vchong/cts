/*
 * Copyright (C) 2014 The Android Open Source Project
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

#pragma version(1)
#pragma rs java_package_name(android.renderscript.cts)

// Don't edit this file!  It is auto-generated by frameworks/rs/api/gen_runtime.

rs_allocation gAllocInRightVector;

float __attribute__((kernel)) testDotFloatFloatFloat(float inLeftVector, unsigned int x) {
    float inRightVector = rsGetElementAt_float(gAllocInRightVector, x);
    return dot(inLeftVector, inRightVector);
}

float __attribute__((kernel)) testDotFloat2Float2Float(float2 inLeftVector, unsigned int x) {
    float2 inRightVector = rsGetElementAt_float2(gAllocInRightVector, x);
    return dot(inLeftVector, inRightVector);
}

float __attribute__((kernel)) testDotFloat3Float3Float(float3 inLeftVector, unsigned int x) {
    float3 inRightVector = rsGetElementAt_float3(gAllocInRightVector, x);
    return dot(inLeftVector, inRightVector);
}

float __attribute__((kernel)) testDotFloat4Float4Float(float4 inLeftVector, unsigned int x) {
    float4 inRightVector = rsGetElementAt_float4(gAllocInRightVector, x);
    return dot(inLeftVector, inRightVector);
}
