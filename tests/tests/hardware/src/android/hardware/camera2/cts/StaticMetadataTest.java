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

package android.hardware.camera2.cts;

import static android.hardware.camera2.CameraCharacteristics.*;

import android.graphics.ImageFormat;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.cts.helpers.StaticMetadata;
import android.hardware.camera2.cts.helpers.StaticMetadata.CheckLevel;
import android.hardware.camera2.cts.testcases.Camera2AndroidTestCase;
import android.util.Log;
import android.util.Size;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * This class covers the {@link CameraCharacteristics} tests that are not
 * covered by {@link CaptureRequestTest} and {@link CameraCharacteristicsTest}
 * (auto-generated tests that only do the non-null checks).
 * </p>
 * <p>
 * Note that most of the tests in this class don't require camera open.
 * </p>
 */
public class StaticMetadataTest extends Camera2AndroidTestCase {
    private static final String TAG = "StaticMetadataTest";
    private static final boolean VERBOSE = Log.isLoggable(TAG, Log.VERBOSE);
    private static final float MIN_FPS_FOR_FULL_DEVICE = 20.0f;
    private String mCameraId;

    /**
     * Test the available capability for different hardware support level devices.
     */
    public void testHwSupportedLevel() throws Exception {
        for (String id : mCameraIds) {
            initStaticMetadata(id);
            List<Integer> availableCaps = mStaticInfo.getAvailableCapabilitiesChecked();

            mCollector.expectTrue("All device must contains BACKWARD_COMPATIBLE capability",
                    availableCaps.contains(REQUEST_AVAILABLE_CAPABILITIES_BACKWARD_COMPATIBLE));

            if (mStaticInfo.isHardwareLevelFull()) {
                // Capability advertisement must be right.
                mCollector.expectTrue("Full device must contains MANUAL_SENSOR capability",
                        availableCaps.contains(REQUEST_AVAILABLE_CAPABILITIES_MANUAL_SENSOR));
                mCollector.expectTrue("Full device must contains MANUAL_POST_PROCESSING capability",
                        availableCaps.contains(
                                REQUEST_AVAILABLE_CAPABILITIES_MANUAL_POST_PROCESSING));

                // Max resolution fps must be >= 20.
                mCollector.expectTrue("Full device must support at least 20fps for max resolution",
                        getFpsForMaxSize(id) >= MIN_FPS_FOR_FULL_DEVICE);

                // Need support per frame control
                mCollector.expectTrue("Full device must support per frame control",
                        mStaticInfo.isPerFrameControlSupported());
            }

            // TODO: test all the keys mandatory for all capability devices.
        }
    }

    /**
     * Test max number of output stream reported by device
     */
    public void testMaxNumOutputStreams() throws Exception {
        for (String id : mCameraIds) {
            initStaticMetadata(id);
            int maxNumStreamsRaw = mStaticInfo.getMaxNumOutputStreamsRawChecked();
            int maxNumStreamsProc = mStaticInfo.getMaxNumOutputStreamsProcessedChecked();
            int maxNumStreamsProcStall = mStaticInfo.getMaxNumOutputStreamsProcessedStallChecked();

            mCollector.expectTrue("max number of raw output streams must be a non negative number",
                    maxNumStreamsRaw >= 0);
            mCollector.expectTrue("max number of processed (stalling) output streams must be >= 1",
                    maxNumStreamsProcStall >= 1);

            if (mStaticInfo.isHardwareLevelFull()) {
                mCollector.expectTrue("max number of processed (non-stalling) output streams" +
                        "must be >= 3 for FULL device",
                        maxNumStreamsProc >= 3);
            } else {
                mCollector.expectTrue("max number of processed (non-stalling) output streams" +
                        "must be >= 2 for LIMITED device",
                        maxNumStreamsProc >= 2);
            }
        }

    }

    /**
     * Test advertised capability does match available keys and vice versa
     */
    public void testCapabilities() throws Exception {
        for (String id : mCameraIds) {
            initStaticMetadata(id);
            List<Integer> availableCaps = mStaticInfo.getAvailableCapabilitiesChecked();

            for (Integer capability = REQUEST_AVAILABLE_CAPABILITIES_BACKWARD_COMPATIBLE;
                    capability <= REQUEST_AVAILABLE_CAPABILITIES_RAW; capability++) {
                boolean isCapabilityAvailable = availableCaps.contains(capability);
                validateCapability(capability, isCapabilityAvailable);
            }
            // Note: Static metadata for capabilities is tested in ExtendedCameraCharacteristicsTest
        }
    }

    private void validateRequestKeysPresence(String capabilityName,
            List<CaptureRequest.Key<?>> requestKeys, boolean expectedPresence) {
        boolean actualPresence = mStaticInfo.areRequestKeysAvailable(requestKeys);
        if (expectedPresence != actualPresence) {
            if (expectedPresence) {
                for (CaptureRequest.Key<?> key : requestKeys) {
                    if (!mStaticInfo.areKeysAvailable(key)) {
                        mCollector.addMessage(String.format(
                                "Camera %s list capability %s but doesn't contain key %s",
                                mCameraId, capabilityName, key.getName()));
                    }
                }
            } else {
                mCollector.addMessage(String.format(
                        "Camera %s doesn't list capability %s but contain all required keys",
                        mCameraId, capabilityName));
            }
        }
    }

    private void validateCapability(Integer capability, boolean isCapabilityAvailable) {
        List<CaptureRequest.Key<?>> requestKeys = new ArrayList<>();
        /* Only test request keys in this test
           Characteristics keys are tested in CameraCharacteristicsTest
           Result keys are tested in CaptureResultTest */
        String capabilityName;
        switch (capability) {
            case REQUEST_AVAILABLE_CAPABILITIES_BACKWARD_COMPATIBLE:
                capabilityName = "REQUEST_AVAILABLE_CAPABILITIES_BACKWARD_COMPATIBLE";
                requestKeys.add(CaptureRequest.CONTROL_AE_ANTIBANDING_MODE);
                requestKeys.add(CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION);
                requestKeys.add(CaptureRequest.CONTROL_AE_LOCK);
                requestKeys.add(CaptureRequest.CONTROL_AE_MODE);
                requestKeys.add(CaptureRequest.CONTROL_AE_REGIONS);
                requestKeys.add(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE);
                requestKeys.add(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER);
                requestKeys.add(CaptureRequest.CONTROL_AF_MODE);
                requestKeys.add(CaptureRequest.CONTROL_AF_REGIONS);
                requestKeys.add(CaptureRequest.CONTROL_AF_TRIGGER);
                requestKeys.add(CaptureRequest.CONTROL_AWB_LOCK);
                requestKeys.add(CaptureRequest.CONTROL_AWB_MODE);
                requestKeys.add(CaptureRequest.CONTROL_AWB_REGIONS);
                requestKeys.add(CaptureRequest.CONTROL_CAPTURE_INTENT);
                requestKeys.add(CaptureRequest.CONTROL_EFFECT_MODE);
                requestKeys.add(CaptureRequest.CONTROL_MODE);
                requestKeys.add(CaptureRequest.CONTROL_SCENE_MODE);
                requestKeys.add(CaptureRequest.CONTROL_VIDEO_STABILIZATION_MODE);
                requestKeys.add(CaptureRequest.FLASH_MODE);
                requestKeys.add(CaptureRequest.JPEG_GPS_LOCATION);
                requestKeys.add(CaptureRequest.JPEG_ORIENTATION);
                requestKeys.add(CaptureRequest.JPEG_QUALITY);
                requestKeys.add(CaptureRequest.JPEG_THUMBNAIL_QUALITY);
                requestKeys.add(CaptureRequest.JPEG_THUMBNAIL_SIZE);
                requestKeys.add(CaptureRequest.LENS_FOCUS_DISTANCE);
                requestKeys.add(CaptureRequest.SCALER_CROP_REGION);
                requestKeys.add(CaptureRequest.STATISTICS_FACE_DETECT_MODE);
                break;
            case REQUEST_AVAILABLE_CAPABILITIES_MANUAL_POST_PROCESSING:
                capabilityName = "REQUEST_AVAILABLE_CAPABILITIES_MANUAL_POST_PROCESSING";
                requestKeys.add(CaptureRequest.TONEMAP_MODE);
                requestKeys.add(CaptureRequest.COLOR_CORRECTION_GAINS);
                requestKeys.add(CaptureRequest.COLOR_CORRECTION_TRANSFORM);
                requestKeys.add(CaptureRequest.SHADING_MODE);
                requestKeys.add(CaptureRequest.STATISTICS_LENS_SHADING_MAP_MODE);
                if (mStaticInfo.isHardwareLevelFull()) {
                    requestKeys.add(CaptureRequest.TONEMAP_CURVE);
                    requestKeys.add(CaptureRequest.COLOR_CORRECTION_ABERRATION_MODE);
                }

                break;
            case REQUEST_AVAILABLE_CAPABILITIES_MANUAL_SENSOR:
                capabilityName = "REQUEST_AVAILABLE_CAPABILITIES_MANUAL_SENSOR";
                requestKeys.add(CaptureRequest.SENSOR_FRAME_DURATION);
                requestKeys.add(CaptureRequest.SENSOR_EXPOSURE_TIME);
                requestKeys.add(CaptureRequest.SENSOR_SENSITIVITY);
                if (mStaticInfo.hasFocuser()) {
                    requestKeys.add(CaptureRequest.LENS_APERTURE);
                    requestKeys.add(CaptureRequest.LENS_FILTER_DENSITY);
                    requestKeys.add(CaptureRequest.LENS_OPTICAL_STABILIZATION_MODE);
                }
                requestKeys.add(CaptureRequest.BLACK_LEVEL_LOCK);
                break;
            case REQUEST_AVAILABLE_CAPABILITIES_RAW:
                capabilityName = "REQUEST_AVAILABLE_CAPABILITIES_RAW";
                mCollector.expectGreater(
                        "REQUEST_AVAILABLE_CAPABILITIES_RAW should support RAW_SENSOR output",
                        /*expected*/0, mStaticInfo.getRawOutputSizesChecked().length);
                requestKeys.add(CaptureRequest.HOT_PIXEL_MODE);
                requestKeys.add(CaptureRequest.STATISTICS_HOT_PIXEL_MAP_MODE);
                break;
            default:
                capabilityName = "Unknown";
                Assert.fail(String.format("Unknown capability: %d", capability));
        }
        validateRequestKeysPresence(capabilityName, requestKeys, isCapabilityAvailable);
    }

    /**
     * Test lens facing.
     */
    public void testLensFacing() throws Exception {
        for (String id : mCameraIds) {
            initStaticMetadata(id);
            mStaticInfo.getLensFacingChecked();
        }
    }

    private float getFpsForMaxSize(String cameraId) throws Exception {
        HashMap<Size, Long> minFrameDurationMap =
                mStaticInfo.getAvailableMinFrameDurationsForFormatChecked(ImageFormat.YUV_420_888);

        Size[] sizes = CameraTestUtils.getSupportedSizeForFormat(ImageFormat.YUV_420_888,
                cameraId, mCameraManager);
        Size maxSize = CameraTestUtils.getMaxSize(sizes);
        Long minDuration = minFrameDurationMap.get(maxSize);
        if (VERBOSE) {
            Log.v(TAG, "min frame duration for size " + maxSize + " is " + minDuration);
        }
        assertTrue("min duration for max size must be postive number",
                minDuration != null && minDuration > 0);

        return 1e9f / minDuration;
    }

    /**
     * Initialize static metadata for a given camera id.
     */
    private void initStaticMetadata(String cameraId) throws Exception {
        mCameraId = cameraId;
        mCollector.setCameraId(cameraId);
        mStaticInfo = new StaticMetadata(mCameraManager.getCameraCharacteristics(cameraId),
                CheckLevel.COLLECT, /* collector */mCollector);
    }
}
