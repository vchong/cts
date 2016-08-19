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

package android.util.cts;

import static org.junit.Assert.assertEquals;

import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;
import android.util.AndroidRuntimeException;

import org.junit.Test;
import org.junit.runner.RunWith;

@SmallTest
@RunWith(AndroidJUnit4.class)
public class AndroidRuntimeExceptionTest {
    private static final String NAME = "Test_AndroidRuntimeException";
    private static final Exception CAUSE = new Exception();

    @Test
    public void testAndroidRuntimeException() {
        try {
            throw new AndroidRuntimeException();
        } catch (AndroidRuntimeException e) {
        }

        try {
            throw new AndroidRuntimeException(NAME);
        } catch (AndroidRuntimeException e) {
            assertEquals(NAME, e.getMessage());
        }

        try {
            throw new AndroidRuntimeException(CAUSE);
        } catch (AndroidRuntimeException e) {
            assertEquals(CAUSE, e.getCause());
        }
    }
}
