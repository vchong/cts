#!/usr/bin/python

# Copyright (C) 2012 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#       http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# consts to be used in signal processing functions

# types of data for input / output
TYPE_I64    = 0
TYPE_DOUBLE = 1
TYPE_MONO   = 2
TYPE_STEREO = 3

# result for the processing
RESULT_OK = 0
RESULT_CONTINUE = 1
RESULT_BREAKONELOOP = 2
RESULT_ERROR = 3
RESULT_FAIL = 4
RESULT_PASS = 5
