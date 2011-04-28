; Copyright (C) 2008 The Android Open Source Project
;
; Licensed under the Apache License, Version 2.0 (the "License");
; you may not use this file except in compliance with the License.
; You may obtain a copy of the License at
;
;      http://www.apache.org/licenses/LICENSE-2.0
;
; Unless required by applicable law or agreed to in writing, software
; distributed under the License is distributed on an "AS IS" BASIS,
; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
; See the License for the specific language governing permissions and
; limitations under the License.

.source T_iget_wide_5.java
.class public dot.junit.opcodes.iget_wide.d.T_iget_wide_5
.super java/lang/Object

.field public static i1 J

.method public <init>()V
.limit regs 4

       invoke-direct {v3}, java/lang/Object/<init>()V
       return-void
.end method

.method public run()J
.limit regs 3

       iget-wide v1, v2, dot.junit.opcodes.iget_wide.d.T_iget_wide_5.i1 J
       return-wide v1
.end method


