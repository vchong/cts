/*
 * Copyright (C) 2016 The Android Open Source Project
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
package com.android.cts.comp;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.os.Process;
import android.os.UserHandle;
import android.os.UserManager;
import android.test.AndroidTestCase;

import java.util.List;

/**
 * Base class for profile-owner based tests.
 * <p>
 * This class handles making sure that the test is the profile owner and that it has an active admin
 * registered, so that all tests may assume these are done.
 */
public class BaseManagedProfileCompTest extends AndroidTestCase {

    protected DevicePolicyManager mDevicePolicyManager;
    protected UserHandle mPrimaryUserHandle;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mDevicePolicyManager = (DevicePolicyManager)
                mContext.getSystemService(Context.DEVICE_POLICY_SERVICE);
        assertNotNull(mDevicePolicyManager);

        assertManagedProfile();

        // Lookup the primary user handle.
        UserManager userManager = mContext.getSystemService(UserManager.class);
        List<UserHandle> userProfiles = userManager.getUserProfiles();
        assertTrue(userProfiles.size() > 1);
        // STOPSHIP(b/32301911): Replace this hack once we have proper API.
        for (UserHandle userHandle : userProfiles) {
            if (!userHandle.equals(Process.myUserHandle())) {
                mPrimaryUserHandle = userHandle;
                break;
            }
        }
        assertNotNull("Cannot find primary user", mPrimaryUserHandle);
    }

    private void assertManagedProfile() {
        assertTrue(mDevicePolicyManager.isAdminActive(
                AdminReceiver.getComponentName(getContext())));
        assertTrue(mDevicePolicyManager.isProfileOwnerApp(getContext().getPackageName()));
        assertTrue(mDevicePolicyManager.isManagedProfile(
                AdminReceiver.getComponentName(getContext())));
    }
}
