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

rs_allocation gAllocInB;

float __attribute__((kernel)) testMinFloatFloatFloat(float inA, unsigned int x) {
    float inB = rsGetElementAt_float(gAllocInB, x);
    return min(inA, inB);
}

float2 __attribute__((kernel)) testMinFloat2Float2Float2(float2 inA, unsigned int x) {
    float2 inB = rsGetElementAt_float2(gAllocInB, x);
    return min(inA, inB);
}

float3 __attribute__((kernel)) testMinFloat3Float3Float3(float3 inA, unsigned int x) {
    float3 inB = rsGetElementAt_float3(gAllocInB, x);
    return min(inA, inB);
}

float4 __attribute__((kernel)) testMinFloat4Float4Float4(float4 inA, unsigned int x) {
    float4 inB = rsGetElementAt_float4(gAllocInB, x);
    return min(inA, inB);
}

float2 __attribute__((kernel)) testMinFloat2FloatFloat2(float2 inA, unsigned int x) {
    float inB = rsGetElementAt_float(gAllocInB, x);
    return min(inA, inB);
}

float3 __attribute__((kernel)) testMinFloat3FloatFloat3(float3 inA, unsigned int x) {
    float inB = rsGetElementAt_float(gAllocInB, x);
    return min(inA, inB);
}

float4 __attribute__((kernel)) testMinFloat4FloatFloat4(float4 inA, unsigned int x) {
    float inB = rsGetElementAt_float(gAllocInB, x);
    return min(inA, inB);
}

char __attribute__((kernel)) testMinCharCharChar(char inA, unsigned int x) {
    char inB = rsGetElementAt_char(gAllocInB, x);
    return min(inA, inB);
}

char2 __attribute__((kernel)) testMinChar2Char2Char2(char2 inA, unsigned int x) {
    char2 inB = rsGetElementAt_char2(gAllocInB, x);
    return min(inA, inB);
}

char3 __attribute__((kernel)) testMinChar3Char3Char3(char3 inA, unsigned int x) {
    char3 inB = rsGetElementAt_char3(gAllocInB, x);
    return min(inA, inB);
}

char4 __attribute__((kernel)) testMinChar4Char4Char4(char4 inA, unsigned int x) {
    char4 inB = rsGetElementAt_char4(gAllocInB, x);
    return min(inA, inB);
}

uchar __attribute__((kernel)) testMinUcharUcharUchar(uchar inA, unsigned int x) {
    uchar inB = rsGetElementAt_uchar(gAllocInB, x);
    return min(inA, inB);
}

uchar2 __attribute__((kernel)) testMinUchar2Uchar2Uchar2(uchar2 inA, unsigned int x) {
    uchar2 inB = rsGetElementAt_uchar2(gAllocInB, x);
    return min(inA, inB);
}

uchar3 __attribute__((kernel)) testMinUchar3Uchar3Uchar3(uchar3 inA, unsigned int x) {
    uchar3 inB = rsGetElementAt_uchar3(gAllocInB, x);
    return min(inA, inB);
}

uchar4 __attribute__((kernel)) testMinUchar4Uchar4Uchar4(uchar4 inA, unsigned int x) {
    uchar4 inB = rsGetElementAt_uchar4(gAllocInB, x);
    return min(inA, inB);
}

short __attribute__((kernel)) testMinShortShortShort(short inA, unsigned int x) {
    short inB = rsGetElementAt_short(gAllocInB, x);
    return min(inA, inB);
}

short2 __attribute__((kernel)) testMinShort2Short2Short2(short2 inA, unsigned int x) {
    short2 inB = rsGetElementAt_short2(gAllocInB, x);
    return min(inA, inB);
}

short3 __attribute__((kernel)) testMinShort3Short3Short3(short3 inA, unsigned int x) {
    short3 inB = rsGetElementAt_short3(gAllocInB, x);
    return min(inA, inB);
}

short4 __attribute__((kernel)) testMinShort4Short4Short4(short4 inA, unsigned int x) {
    short4 inB = rsGetElementAt_short4(gAllocInB, x);
    return min(inA, inB);
}

ushort __attribute__((kernel)) testMinUshortUshortUshort(ushort inA, unsigned int x) {
    ushort inB = rsGetElementAt_ushort(gAllocInB, x);
    return min(inA, inB);
}

ushort2 __attribute__((kernel)) testMinUshort2Ushort2Ushort2(ushort2 inA, unsigned int x) {
    ushort2 inB = rsGetElementAt_ushort2(gAllocInB, x);
    return min(inA, inB);
}

ushort3 __attribute__((kernel)) testMinUshort3Ushort3Ushort3(ushort3 inA, unsigned int x) {
    ushort3 inB = rsGetElementAt_ushort3(gAllocInB, x);
    return min(inA, inB);
}

ushort4 __attribute__((kernel)) testMinUshort4Ushort4Ushort4(ushort4 inA, unsigned int x) {
    ushort4 inB = rsGetElementAt_ushort4(gAllocInB, x);
    return min(inA, inB);
}

int __attribute__((kernel)) testMinIntIntInt(int inA, unsigned int x) {
    int inB = rsGetElementAt_int(gAllocInB, x);
    return min(inA, inB);
}

int2 __attribute__((kernel)) testMinInt2Int2Int2(int2 inA, unsigned int x) {
    int2 inB = rsGetElementAt_int2(gAllocInB, x);
    return min(inA, inB);
}

int3 __attribute__((kernel)) testMinInt3Int3Int3(int3 inA, unsigned int x) {
    int3 inB = rsGetElementAt_int3(gAllocInB, x);
    return min(inA, inB);
}

int4 __attribute__((kernel)) testMinInt4Int4Int4(int4 inA, unsigned int x) {
    int4 inB = rsGetElementAt_int4(gAllocInB, x);
    return min(inA, inB);
}

uint __attribute__((kernel)) testMinUintUintUint(uint inA, unsigned int x) {
    uint inB = rsGetElementAt_uint(gAllocInB, x);
    return min(inA, inB);
}

uint2 __attribute__((kernel)) testMinUint2Uint2Uint2(uint2 inA, unsigned int x) {
    uint2 inB = rsGetElementAt_uint2(gAllocInB, x);
    return min(inA, inB);
}

uint3 __attribute__((kernel)) testMinUint3Uint3Uint3(uint3 inA, unsigned int x) {
    uint3 inB = rsGetElementAt_uint3(gAllocInB, x);
    return min(inA, inB);
}

uint4 __attribute__((kernel)) testMinUint4Uint4Uint4(uint4 inA, unsigned int x) {
    uint4 inB = rsGetElementAt_uint4(gAllocInB, x);
    return min(inA, inB);
}

long __attribute__((kernel)) testMinLongLongLong(long inA, unsigned int x) {
    long inB = rsGetElementAt_long(gAllocInB, x);
    return min(inA, inB);
}

long2 __attribute__((kernel)) testMinLong2Long2Long2(long2 inA, unsigned int x) {
    long2 inB = rsGetElementAt_long2(gAllocInB, x);
    return min(inA, inB);
}

long3 __attribute__((kernel)) testMinLong3Long3Long3(long3 inA, unsigned int x) {
    long3 inB = rsGetElementAt_long3(gAllocInB, x);
    return min(inA, inB);
}

long4 __attribute__((kernel)) testMinLong4Long4Long4(long4 inA, unsigned int x) {
    long4 inB = rsGetElementAt_long4(gAllocInB, x);
    return min(inA, inB);
}

ulong __attribute__((kernel)) testMinUlongUlongUlong(ulong inA, unsigned int x) {
    ulong inB = rsGetElementAt_ulong(gAllocInB, x);
    return min(inA, inB);
}

ulong2 __attribute__((kernel)) testMinUlong2Ulong2Ulong2(ulong2 inA, unsigned int x) {
    ulong2 inB = rsGetElementAt_ulong2(gAllocInB, x);
    return min(inA, inB);
}

ulong3 __attribute__((kernel)) testMinUlong3Ulong3Ulong3(ulong3 inA, unsigned int x) {
    ulong3 inB = rsGetElementAt_ulong3(gAllocInB, x);
    return min(inA, inB);
}

ulong4 __attribute__((kernel)) testMinUlong4Ulong4Ulong4(ulong4 inA, unsigned int x) {
    ulong4 inB = rsGetElementAt_ulong4(gAllocInB, x);
    return min(inA, inB);
}
