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
package com.android.cts.deviceowner;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.test.AndroidTestCase;

/**
 * Base class for device-owner based tests.
 *
 * This class handles making sure that the test is the device owner
 * and that it has an active admin registered, so that all tests may
 * assume these are done. The admin component can be accessed through
 * {@link #getWho()}.
 */
public class BaseDeviceOwnerTest extends AndroidTestCase {

    public static class BasicAdminReceiver extends DeviceAdminReceiver {
    }

    public static final String PACKAGE_NAME = BaseDeviceOwnerTest.class.getPackage().getName();

    protected DevicePolicyManager mDevicePolicyManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mDevicePolicyManager = (DevicePolicyManager)
                mContext.getSystemService(Context.DEVICE_POLICY_SERVICE);
        assertTrue(mDevicePolicyManager.isAdminActive(getWho()));
        assertTrue(mDevicePolicyManager.isDeviceOwnerApp(PACKAGE_NAME));
    }

    public static ComponentName getWho() {
        return new ComponentName(PACKAGE_NAME, BasicAdminReceiver.class.getName());
    }
}