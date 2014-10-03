/*
 * Copyright (C) 2014 The Android Open Source Project
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
package com.android.cts.managedprofile.crossprofilecontent;

import static com.android.cts.managedprofile.BaseManagedProfileTest.ADMIN_RECEIVER_COMPONENT;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

public class CrossProfileContentTest extends
        ActivityInstrumentationTestCase2<IntentSenderActivity> {

    private static final String MESSAGE = "Sample Message";

    public CrossProfileContentTest() {
        super(IntentSenderActivity.class);
    }

    @Override
    protected void tearDown() throws Exception {
        DevicePolicyManager dpm = (DevicePolicyManager)
               getActivity().getSystemService(Context.DEVICE_POLICY_SERVICE);
        dpm.clearCrossProfileIntentFilters(ADMIN_RECEIVER_COMPONENT);
        super.tearDown();
    }

    public void testReceiverCanRead() {
        String response = getActivity().testReceiverCanRead(MESSAGE);
        assertEquals(response, MESSAGE);
    }

    public void testReceiverCanWrite() {
        String response = getActivity().testReceiverCanWrite(MESSAGE);
        assertEquals(response, MESSAGE);
    }
}
