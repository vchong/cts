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

#include "shared.rsh"

short red[256];
short green[256];
short blue[256];
short alpha[256];

uchar4 __attribute__((kernel)) root(uchar4 in) {
    uchar4 tmp;
    tmp.r = red[in.r];
    tmp.g = green[in.g];
    tmp.b = blue[in.b];
    tmp.a = alpha[in.a];
    return tmp;
}

