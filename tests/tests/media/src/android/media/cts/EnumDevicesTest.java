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

package android.media.cts;

import android.content.Context;
import android.content.pm.PackageManager;

import android.media.AudioDeviceCallback;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;

import android.os.Handler;
import android.os.Looper;

import android.test.AndroidTestCase;

/**
 * TODO: Insert description here. (generated by pmclean)
 */
public class EnumDevicesTest extends AndroidTestCase {
    private AudioManager mAudioManager;

    boolean mAddCallbackCalled = false;
    boolean mRemoveCallbackCalled = false;

    static {
        // We're going to use a Handler
        Looper.prepare();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // get the AudioManager
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        assertNotNull(mAudioManager);
    }

    public void test_getDevices() {
        AudioDeviceInfo[] deviceList;

        // test an empty flags set
        deviceList = mAudioManager.getDevices(0);
        assertTrue(deviceList != null);
        assertTrue(deviceList.length == 0);

        int numOutputDevices = 0;
        if (mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_AUDIO_OUTPUT)) {
            // test OUTPUTS
            deviceList = mAudioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS);
            assertTrue(deviceList != null);
            numOutputDevices = deviceList.length;
            assertTrue(numOutputDevices != 0);

            // all should be "sinks"
            for(int index = 0; index < numOutputDevices; index++) {
                assertTrue(deviceList[index].isSink());
            }
        }

        int numInputDevices = 0;
        if (mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)) {
            // test INPUTS
            deviceList = mAudioManager.getDevices(AudioManager.GET_DEVICES_INPUTS);
            assertTrue(deviceList != null);
            numInputDevices = deviceList.length;
            assertTrue(numInputDevices != 0);

            // all should be "sources"
            for(int index = 0; index < numInputDevices; index++) {
                assertTrue(deviceList[index].isSource());
            }
        }

        // INPUTS & OUTPUTS
        if (mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_AUDIO_OUTPUT) &&
                mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)) {
            deviceList = mAudioManager.getDevices(AudioManager.GET_DEVICES_ALL);
            assertTrue(deviceList.length == (numOutputDevices + numInputDevices));
        }
    }

    private class EmptyDeviceCallback extends AudioDeviceCallback {
        public void onAudioDevicesAdded(AudioDeviceInfo[] addedDevices) {
            mAddCallbackCalled = true;
        }

        public void onAudioDevicesRemoved(AudioDeviceInfo[] removedDevices) {
            mRemoveCallbackCalled = true;
        }
    }

    public void test_deviceCallback() {
        mAudioManager.registerAudioDeviceCallback(null,null);
        assertTrue(true);

        AudioDeviceCallback callback =  new EmptyDeviceCallback();
        mAudioManager.registerAudioDeviceCallback(callback, null);
        assertTrue(true);

        mAudioManager.registerAudioDeviceCallback(callback, new Handler());
        assertTrue(true);
    }

    //TODO - Need tests for device connect/disconnect callbacks
}
