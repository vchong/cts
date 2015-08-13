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
 * limitations under the License.
 */
package com.android.compatibility.common.deviceinfo;

import android.test.ActivityInstrumentationTestCase2;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Test for {@link DeviceInfoActivity}.
 */
public class DeviceInfoActivityTest extends ActivityInstrumentationTestCase2<SampleDeviceInfo> {

    private static final String EXPECTED_FILE_PATH =
            "/storage/emulated/0/device-info-files/SampleDeviceInfo.deviceinfo.json";

    private SampleDeviceInfo mActivity;

    public DeviceInfoActivityTest() {
        super(SampleDeviceInfo.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // Start the activity and get a reference to it.
        mActivity = getActivity();
        // Wait for the activity to finish.
        mActivity.waitForActivityToFinish();
    }

    @Override
    protected void tearDown() throws Exception {
        mActivity = null;
        super.tearDown();
    }

    public void testJsonFile() throws IOException {
        String errorMessage = mActivity.getErrorMessage();
        // Check no errors
        assertNull("Expected no errors", errorMessage);
        String resultFilePath = mActivity.getResultFilePath();
        // Check file path exist
        assertNotNull("Expected a non-null resultFilePath", resultFilePath);
        // Check file path location
        assertEquals("Incorrect file path", EXPECTED_FILE_PATH, resultFilePath);
        // Check json file content
        String jsonContent = readFile(resultFilePath);
        assertEquals("Incorrect json output", ExampleObjects.sampleDeviceInfoJson(), jsonContent);
    }

    private String readFile(String filePath) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(
                new FileInputStream(filePath), "UTF-8");
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line;
        while((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append('\n');
        }
        bufferedReader.close();
        return stringBuilder.toString();
    }
}