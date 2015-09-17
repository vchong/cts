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

package com.android.compatibility.common.tradefed.targetprep;

import com.android.tradefed.build.DeviceBuildInfo;
import com.android.tradefed.build.IDeviceBuildInfo;
import com.android.tradefed.config.OptionSetter;
import com.android.tradefed.device.ITestDevice;
import com.android.tradefed.targetprep.TargetSetupError;

import junit.framework.TestCase;

import org.easymock.EasyMock;

public class BuildCheckTest extends TestCase {

    private BuildCheck mBuildCheck;
    private IDeviceBuildInfo mMockBuildInfo;
    private ITestDevice mMockDevice;
    private OptionSetter mOptionSetter;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mBuildCheck = new BuildCheck();
        mMockDevice = EasyMock.createMock(ITestDevice.class);
        mMockBuildInfo = new DeviceBuildInfo("0", "", "");
        mOptionSetter = new OptionSetter(mBuildCheck);
        EasyMock.expect(mMockDevice.executeShellCommand(
                "getprop ro.build.type")).andReturn("\nuser\n").anyTimes();
    }

    public void testWarningMatch() throws Exception {
        mOptionSetter.setOptionValue("expected-build-type", "user");
        mOptionSetter.setOptionValue("throw-error", "false");
        EasyMock.replay(mMockDevice);
        mBuildCheck.run(mMockDevice, mMockBuildInfo); // no warnings or errors
    }

    public void testWarningMismatch() throws Exception {
        mOptionSetter.setOptionValue("expected-build-type", "eng");
        mOptionSetter.setOptionValue("throw-error", "false");
        EasyMock.replay(mMockDevice);
        mBuildCheck.run(mMockDevice, mMockBuildInfo); // should only print a warning
    }

    public void testErrorMatch() throws Exception {
        mOptionSetter.setOptionValue("expected-build-type", "user");
        mOptionSetter.setOptionValue("throw-error", "true");
        EasyMock.replay(mMockDevice);
        mBuildCheck.run(mMockDevice, mMockBuildInfo); // no warnings or errors
    }

    public void testErrorMismatch() throws Exception {
        mOptionSetter.setOptionValue("expected-build-type", "userdebug");
        mOptionSetter.setOptionValue("throw-error", "true");
        EasyMock.replay(mMockDevice);
        try {
            mBuildCheck.run(mMockDevice, mMockBuildInfo); // expecting TargetSetupError
            fail("TargetSetupError expected");
        } catch (TargetSetupError e) {
            // Expected
        }
    }

}
