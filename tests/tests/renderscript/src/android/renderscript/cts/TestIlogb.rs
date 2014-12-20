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


int __attribute__((kernel)) testIlogbFloatInt(float inV) {
    return ilogb(inV);
}

int2 __attribute__((kernel)) testIlogbFloat2Int2(float2 inV) {
    return ilogb(inV);
}

int3 __attribute__((kernel)) testIlogbFloat3Int3(float3 inV) {
    return ilogb(inV);
}

int4 __attribute__((kernel)) testIlogbFloat4Int4(float4 inV) {
    return ilogb(inV);
}
