# Copyright (C) 2008 The Android Open Source Project
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

.source "T_sput_2.java"
.class  public Ldot/junit/opcodes/sput/d/T_sput_2;
.super  Ljava/lang/Object;

.field public static st_d1:D

.method public constructor <init>()V
.registers 1

       invoke-direct {v0}, Ljava/lang/Object;-><init>()V
       return-void
.end method

.method public run()V
.registers 4

       const-wide v1, 1000000.000000
       sput v1, Ldot/junit/opcodes/sput/d/T_sput_2;->st_d1:D
       return-void
.end method


