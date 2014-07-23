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

rs_allocation gAllocInX;

float __attribute__((kernel)) testNativeAtan2FloatFloatFloat(float inY, unsigned int x) {
    float inX = rsGetElementAt_float(gAllocInX, x);
    return native_atan2(inY, inX);
}

float2 __attribute__((kernel)) testNativeAtan2Float2Float2Float2(float2 inY, unsigned int x) {
    float2 inX = rsGetElementAt_float2(gAllocInX, x);
    return native_atan2(inY, inX);
}

float3 __attribute__((kernel)) testNativeAtan2Float3Float3Float3(float3 inY, unsigned int x) {
    float3 inX = rsGetElementAt_float3(gAllocInX, x);
    return native_atan2(inY, inX);
}

float4 __attribute__((kernel)) testNativeAtan2Float4Float4Float4(float4 inY, unsigned int x) {
    float4 inX = rsGetElementAt_float4(gAllocInX, x);
    return native_atan2(inY, inX);
}
