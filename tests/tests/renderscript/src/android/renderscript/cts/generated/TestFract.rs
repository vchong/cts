/*
 * Copyright (C) 2016 The Android Open Source Project
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

// Don't edit this file!  It is auto-generated by frameworks/rs/api/generate.sh.

#pragma version(1)
#pragma rs java_package_name(android.renderscript.cts)

rs_allocation gAllocOutFloor;

float __attribute__((kernel)) testFractFloatFloatFloat(float inV, unsigned int x) {
    float outFloor = 0;
    float out = fract(inV, &outFloor);
    rsSetElementAt_float(gAllocOutFloor, outFloor, x);
    return out;
}

float2 __attribute__((kernel)) testFractFloat2Float2Float2(float2 inV, unsigned int x) {
    float2 outFloor = 0;
    float2 out = fract(inV, &outFloor);
    rsSetElementAt_float2(gAllocOutFloor, outFloor, x);
    return out;
}

float3 __attribute__((kernel)) testFractFloat3Float3Float3(float3 inV, unsigned int x) {
    float3 outFloor = 0;
    float3 out = fract(inV, &outFloor);
    rsSetElementAt_float3(gAllocOutFloor, outFloor, x);
    return out;
}

float4 __attribute__((kernel)) testFractFloat4Float4Float4(float4 inV, unsigned int x) {
    float4 outFloor = 0;
    float4 out = fract(inV, &outFloor);
    rsSetElementAt_float4(gAllocOutFloor, outFloor, x);
    return out;
}

float __attribute__((kernel)) testFractFloatFloat(float inV) {
    return fract(inV);
}

float2 __attribute__((kernel)) testFractFloat2Float2(float2 inV) {
    return fract(inV);
}

float3 __attribute__((kernel)) testFractFloat3Float3(float3 inV) {
    return fract(inV);
}

float4 __attribute__((kernel)) testFractFloat4Float4(float4 inV) {
    return fract(inV);
}
