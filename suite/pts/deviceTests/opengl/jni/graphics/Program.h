/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
#ifndef PROGRAM_H
#define PROGRAM_H

#include "Matrix.h"

#include <GLES2/gl2.h>

class Program {
public:
    Program(GLuint programId);
    virtual ~Program() {};
    virtual void before(Matrix& model, Matrix& view, Matrix& projection);
    virtual void after(Matrix& model, Matrix& view, Matrix& projection);
private:
    GLuint mProgramId;
};

#endif
