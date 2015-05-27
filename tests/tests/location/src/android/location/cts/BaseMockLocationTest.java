/*
 * Copyright (C) 2015 The Android Open Source Project
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
 *
 */

package android.location.cts;

import android.os.ParcelFileDescriptor;
import android.test.InstrumentationTestCase;
import android.util.Log;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Base class for instrumentations tests that use mock location.
 */
public abstract class BaseMockLocationTest extends InstrumentationTestCase {
    private static final String LOG_TAG = "BaseMockLocationTest";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setAsMoskLocationProvider(true);
    }

    @Override
    protected void tearDown() throws Exception {
        setAsMoskLocationProvider(false);
        super.tearDown();
    }

    private void setAsMoskLocationProvider(boolean enable) {
        StringBuilder command = new StringBuilder();
        command.append("appops set ");
        command.append(getInstrumentation().getContext().getPackageName());
        command.append(" android:mock_location ");
        command.append(enable ? "allow" : "deny");

        ParcelFileDescriptor pfd = getInstrumentation().getUiAutomation()
                .executeShellCommand(command.toString());

        InputStream is = new FileInputStream(pfd.getFileDescriptor());
        try {
            final byte[] buffer = new byte[8192];
            while ((is.read(buffer)) != -1);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error managing mock locaiton app", e);
        }
    }
}
