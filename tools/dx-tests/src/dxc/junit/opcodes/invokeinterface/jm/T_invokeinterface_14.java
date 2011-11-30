/*
 * Copyright (C) 2008 The Android Open Source Project
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

package dxc.junit.opcodes.invokeinterface.jm;

public class T_invokeinterface_14 {

    public int run(ITest test) {
        int a = 123;
        int b = 345;
        if(test.testArgsOrder(64, 2) == 32)
        {
            if(a == 123)
                if(b == 345)
                    return 1;
        }
        return 0;
    }
}
