/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

#ifndef CTSAUDIO_GENERIC_FACTORY_H
#define CTSAUDIO_GENERIC_FACTORY_H

#include "task/TaskGeneric.h"

class ClientInterface;

/**
 * Factory methods for all abstract classes
 */
class GenericFactory {
public:

    virtual ClientInterface* createClientInterface();
    virtual TaskGeneric* createTask(TaskGeneric::TaskType type);
};


#endif // CTSAUDIO_GENERIC_FACTORY_H
