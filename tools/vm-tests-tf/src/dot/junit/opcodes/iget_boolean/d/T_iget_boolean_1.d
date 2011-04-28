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

.source T_iget_boolean_1.java
.class public dot.junit.opcodes.iget_boolean.d.T_iget_boolean_1
.super java/lang/Object

.field public  i1 Z
.field protected  p1 Z
.field private  pvt1 Z


.method public <init>()V
.limit regs 2

       invoke-direct {v1}, java/lang/Object/<init>()V
       
       const/4 v0, 1
       iput-boolean v0, v1, dot.junit.opcodes.iget_boolean.d.T_iget_boolean_1.i1 Z

       const/16 v0, 1
       iput-boolean v0, v1, dot.junit.opcodes.iget_boolean.d.T_iget_boolean_1.p1 Z

       const/16 v0, 1
       iput-boolean v0, v1, dot.junit.opcodes.iget_boolean.d.T_iget_boolean_1.pvt1 Z
       
       return-void
.end method

.method public run()Z
.limit regs 3

       iget-boolean v1, v2, dot.junit.opcodes.iget_boolean.d.T_iget_boolean_1.i1 Z
       return v1
.end method


