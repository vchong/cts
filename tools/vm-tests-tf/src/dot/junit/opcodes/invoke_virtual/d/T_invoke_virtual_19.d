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

.source T_invoke_virtual_19.java
.class public dot.junit.opcodes.invoke_virtual.d.T_invoke_virtual_19
.super dot/junit/opcodes/invoke_virtual/d/TSuper


.method public <init>()V
.limit regs 2

       invoke-direct {v1}, dot/junit/opcodes/invoke_virtual/d/TSuper/<init>()V
       return-void
.end method

.method public run()I
.limit regs 4

       const/4 v2, 1
       invoke-virtual {v3, v2}, dot/junit/opcodes/invoke_virtual/d/T_invoke_virtual_19/toInt(I)I
       move-result v0
       return v0
.end method


