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
#ifndef MESH_H
#define MESH_H

#include <GLES2/gl2.h>

// Meshes act as holders for geometry data etc. This is accessed by the MeshNodes to render them.
// This allows a mesh to appear multiple times in a SceneGraph without duplication.
class Mesh {
public:
    Mesh(const float* vertices, const float* normals, const float* texCoords,
            const int numVertices, const GLuint textureId);
    virtual ~Mesh() {};
    const float* mVertices;
    const float* mNormals;
    const float* mTexCoords;
    const int mNumVertices;
    const GLuint mTextureId;
};

#endif
