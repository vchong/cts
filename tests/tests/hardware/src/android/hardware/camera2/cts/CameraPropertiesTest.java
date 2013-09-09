/*
 * Copyright (C) 2013 The Android Open Source Project
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

/**
 * ! Do not edit this file directly !
 *
 * Generated automatically from system/media/camera/docs/CameraPropertiesTest.mako.
 * This file contains only the auto-generated CameraProperties CTS tests; it does
 * not contain any additional manual tests, which would be in a separate file.
 */

package android.hardware.camera2.cts;

import android.content.Context;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraProperties;
import android.test.AndroidTestCase;

/**
 * Auto-generated CTS test for CameraProperties fields.
 */
public class CameraPropertiesTest extends AndroidTestCase {
    private CameraManager mCameraManager;

    @Override
    public void setContext(Context context) {
        super.setContext(context);
        mCameraManager = (CameraManager)context.getSystemService(Context.CAMERA_SERVICE);
        assertNotNull("Can't connect to camera manager", mCameraManager);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testCameraPropertiesAndroidControlAeAvailableAntibandingModes() throws Exception {
        String[] ids = mCameraManager.getCameraIdList();
        for (int i = 0; i < ids.length; i++) {
            CameraDevice camera = mCameraManager.openCamera(ids[i]);
            assertNotNull("Failed to open camera", camera);
            CameraProperties props;
            try {
                props = camera.getProperties();
            }
            finally {
                camera.close();
            }
            assertNotNull(String.format("Can't get camera properties from: ID %s", ids[i]),
                                        props);
            assertNotNull("Invalid property: android.control.aeAvailableAntibandingModes",
                    props.get(CameraProperties.CONTROL_AE_AVAILABLE_ANTIBANDING_MODES));
        }
    }

    public void testCameraPropertiesAndroidControlAeAvailableTargetFpsRanges() throws Exception {
        String[] ids = mCameraManager.getCameraIdList();
        for (int i = 0; i < ids.length; i++) {
            CameraDevice camera = mCameraManager.openCamera(ids[i]);
            assertNotNull("Failed to open camera", camera);
            CameraProperties props;
            try {
                props = camera.getProperties();
            }
            finally {
                camera.close();
            }
            assertNotNull(String.format("Can't get camera properties from: ID %s", ids[i]),
                                        props);
            assertNotNull("Invalid property: android.control.aeAvailableTargetFpsRanges",
                    props.get(CameraProperties.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES));
        }
    }

    public void testCameraPropertiesAndroidControlAeCompensationRange() throws Exception {
        String[] ids = mCameraManager.getCameraIdList();
        for (int i = 0; i < ids.length; i++) {
            CameraDevice camera = mCameraManager.openCamera(ids[i]);
            assertNotNull("Failed to open camera", camera);
            CameraProperties props;
            try {
                props = camera.getProperties();
            }
            finally {
                camera.close();
            }
            assertNotNull(String.format("Can't get camera properties from: ID %s", ids[i]),
                                        props);
            assertNotNull("Invalid property: android.control.aeCompensationRange",
                    props.get(CameraProperties.CONTROL_AE_COMPENSATION_RANGE));
        }
    }

    public void testCameraPropertiesAndroidControlAeCompensationStep() throws Exception {
        String[] ids = mCameraManager.getCameraIdList();
        for (int i = 0; i < ids.length; i++) {
            CameraDevice camera = mCameraManager.openCamera(ids[i]);
            assertNotNull("Failed to open camera", camera);
            CameraProperties props;
            try {
                props = camera.getProperties();
            }
            finally {
                camera.close();
            }
            assertNotNull(String.format("Can't get camera properties from: ID %s", ids[i]),
                                        props);
            assertNotNull("Invalid property: android.control.aeCompensationStep",
                    props.get(CameraProperties.CONTROL_AE_COMPENSATION_STEP));
        }
    }

    public void testCameraPropertiesAndroidControlAfAvailableModes() throws Exception {
        String[] ids = mCameraManager.getCameraIdList();
        for (int i = 0; i < ids.length; i++) {
            CameraDevice camera = mCameraManager.openCamera(ids[i]);
            assertNotNull("Failed to open camera", camera);
            CameraProperties props;
            try {
                props = camera.getProperties();
            }
            finally {
                camera.close();
            }
            assertNotNull(String.format("Can't get camera properties from: ID %s", ids[i]),
                                        props);
            assertNotNull("Invalid property: android.control.afAvailableModes",
                    props.get(CameraProperties.CONTROL_AF_AVAILABLE_MODES));
        }
    }

    public void testCameraPropertiesAndroidControlAvailableEffects() throws Exception {
        String[] ids = mCameraManager.getCameraIdList();
        for (int i = 0; i < ids.length; i++) {
            CameraDevice camera = mCameraManager.openCamera(ids[i]);
            assertNotNull("Failed to open camera", camera);
            CameraProperties props;
            try {
                props = camera.getProperties();
            }
            finally {
                camera.close();
            }
            assertNotNull(String.format("Can't get camera properties from: ID %s", ids[i]),
                                        props);
            assertNotNull("Invalid property: android.control.availableEffects",
                    props.get(CameraProperties.CONTROL_AVAILABLE_EFFECTS));
        }
    }

    public void testCameraPropertiesAndroidControlAvailableSceneModes() throws Exception {
        String[] ids = mCameraManager.getCameraIdList();
        for (int i = 0; i < ids.length; i++) {
            CameraDevice camera = mCameraManager.openCamera(ids[i]);
            assertNotNull("Failed to open camera", camera);
            CameraProperties props;
            try {
                props = camera.getProperties();
            }
            finally {
                camera.close();
            }
            assertNotNull(String.format("Can't get camera properties from: ID %s", ids[i]),
                                        props);
            assertNotNull("Invalid property: android.control.availableSceneModes",
                    props.get(CameraProperties.CONTROL_AVAILABLE_SCENE_MODES));
        }
    }

    public void testCameraPropertiesAndroidControlAvailableVideoStabilizationModes() throws Exception {
        String[] ids = mCameraManager.getCameraIdList();
        for (int i = 0; i < ids.length; i++) {
            CameraDevice camera = mCameraManager.openCamera(ids[i]);
            assertNotNull("Failed to open camera", camera);
            CameraProperties props;
            try {
                props = camera.getProperties();
            }
            finally {
                camera.close();
            }
            assertNotNull(String.format("Can't get camera properties from: ID %s", ids[i]),
                                        props);
            assertNotNull("Invalid property: android.control.availableVideoStabilizationModes",
                    props.get(CameraProperties.CONTROL_AVAILABLE_VIDEO_STABILIZATION_MODES));
        }
    }

    public void testCameraPropertiesAndroidControlAwbAvailableModes() throws Exception {
        String[] ids = mCameraManager.getCameraIdList();
        for (int i = 0; i < ids.length; i++) {
            CameraDevice camera = mCameraManager.openCamera(ids[i]);
            assertNotNull("Failed to open camera", camera);
            CameraProperties props;
            try {
                props = camera.getProperties();
            }
            finally {
                camera.close();
            }
            assertNotNull(String.format("Can't get camera properties from: ID %s", ids[i]),
                                        props);
            assertNotNull("Invalid property: android.control.awbAvailableModes",
                    props.get(CameraProperties.CONTROL_AWB_AVAILABLE_MODES));
        }
    }

    public void testCameraPropertiesAndroidControlMaxRegions() throws Exception {
        String[] ids = mCameraManager.getCameraIdList();
        for (int i = 0; i < ids.length; i++) {
            CameraDevice camera = mCameraManager.openCamera(ids[i]);
            assertNotNull("Failed to open camera", camera);
            CameraProperties props;
            try {
                props = camera.getProperties();
            }
            finally {
                camera.close();
            }
            assertNotNull(String.format("Can't get camera properties from: ID %s", ids[i]),
                                        props);
            assertNotNull("Invalid property: android.control.maxRegions",
                    props.get(CameraProperties.CONTROL_MAX_REGIONS));
        }
    }

    public void testCameraPropertiesAndroidFlashInfoAvailable() throws Exception {
        String[] ids = mCameraManager.getCameraIdList();
        for (int i = 0; i < ids.length; i++) {
            CameraDevice camera = mCameraManager.openCamera(ids[i]);
            assertNotNull("Failed to open camera", camera);
            CameraProperties props;
            try {
                props = camera.getProperties();
            }
            finally {
                camera.close();
            }
            assertNotNull(String.format("Can't get camera properties from: ID %s", ids[i]),
                                        props);
            assertNotNull("Invalid property: android.flash.info.available",
                    props.get(CameraProperties.FLASH_INFO_AVAILABLE));
        }
    }

    public void testCameraPropertiesAndroidJpegAvailableThumbnailSizes() throws Exception {
        String[] ids = mCameraManager.getCameraIdList();
        for (int i = 0; i < ids.length; i++) {
            CameraDevice camera = mCameraManager.openCamera(ids[i]);
            assertNotNull("Failed to open camera", camera);
            CameraProperties props;
            try {
                props = camera.getProperties();
            }
            finally {
                camera.close();
            }
            assertNotNull(String.format("Can't get camera properties from: ID %s", ids[i]),
                                        props);
            assertNotNull("Invalid property: android.jpeg.availableThumbnailSizes",
                    props.get(CameraProperties.JPEG_AVAILABLE_THUMBNAIL_SIZES));
        }
    }

    public void testCameraPropertiesAndroidLensFacing() throws Exception {
        String[] ids = mCameraManager.getCameraIdList();
        for (int i = 0; i < ids.length; i++) {
            CameraDevice camera = mCameraManager.openCamera(ids[i]);
            assertNotNull("Failed to open camera", camera);
            CameraProperties props;
            try {
                props = camera.getProperties();
            }
            finally {
                camera.close();
            }
            assertNotNull(String.format("Can't get camera properties from: ID %s", ids[i]),
                                        props);
            assertNotNull("Invalid property: android.lens.facing",
                    props.get(CameraProperties.LENS_FACING));
        }
    }

    public void testCameraPropertiesAndroidLensInfoAvailableApertures() throws Exception {
        String[] ids = mCameraManager.getCameraIdList();
        for (int i = 0; i < ids.length; i++) {
            CameraDevice camera = mCameraManager.openCamera(ids[i]);
            assertNotNull("Failed to open camera", camera);
            CameraProperties props;
            try {
                props = camera.getProperties();
            }
            finally {
                camera.close();
            }
            assertNotNull(String.format("Can't get camera properties from: ID %s", ids[i]),
                                        props);
            assertNotNull("Invalid property: android.lens.info.availableApertures",
                    props.get(CameraProperties.LENS_INFO_AVAILABLE_APERTURES));
        }
    }

    public void testCameraPropertiesAndroidLensInfoAvailableFilterDensities() throws Exception {
        String[] ids = mCameraManager.getCameraIdList();
        for (int i = 0; i < ids.length; i++) {
            CameraDevice camera = mCameraManager.openCamera(ids[i]);
            assertNotNull("Failed to open camera", camera);
            CameraProperties props;
            try {
                props = camera.getProperties();
            }
            finally {
                camera.close();
            }
            assertNotNull(String.format("Can't get camera properties from: ID %s", ids[i]),
                                        props);
            assertNotNull("Invalid property: android.lens.info.availableFilterDensities",
                    props.get(CameraProperties.LENS_INFO_AVAILABLE_FILTER_DENSITIES));
        }
    }

    public void testCameraPropertiesAndroidLensInfoAvailableFocalLengths() throws Exception {
        String[] ids = mCameraManager.getCameraIdList();
        for (int i = 0; i < ids.length; i++) {
            CameraDevice camera = mCameraManager.openCamera(ids[i]);
            assertNotNull("Failed to open camera", camera);
            CameraProperties props;
            try {
                props = camera.getProperties();
            }
            finally {
                camera.close();
            }
            assertNotNull(String.format("Can't get camera properties from: ID %s", ids[i]),
                                        props);
            assertNotNull("Invalid property: android.lens.info.availableFocalLengths",
                    props.get(CameraProperties.LENS_INFO_AVAILABLE_FOCAL_LENGTHS));
        }
    }

    public void testCameraPropertiesAndroidLensInfoAvailableOpticalStabilization() throws Exception {
        String[] ids = mCameraManager.getCameraIdList();
        for (int i = 0; i < ids.length; i++) {
            CameraDevice camera = mCameraManager.openCamera(ids[i]);
            assertNotNull("Failed to open camera", camera);
            CameraProperties props;
            try {
                props = camera.getProperties();
            }
            finally {
                camera.close();
            }
            assertNotNull(String.format("Can't get camera properties from: ID %s", ids[i]),
                                        props);
            assertNotNull("Invalid property: android.lens.info.availableOpticalStabilization",
                    props.get(CameraProperties.LENS_INFO_AVAILABLE_OPTICAL_STABILIZATION));
        }
    }

    public void testCameraPropertiesAndroidLensInfoHyperfocalDistance() throws Exception {
        String[] ids = mCameraManager.getCameraIdList();
        for (int i = 0; i < ids.length; i++) {
            CameraDevice camera = mCameraManager.openCamera(ids[i]);
            assertNotNull("Failed to open camera", camera);
            CameraProperties props;
            try {
                props = camera.getProperties();
            }
            finally {
                camera.close();
            }
            assertNotNull(String.format("Can't get camera properties from: ID %s", ids[i]),
                                        props);
            assertNotNull("Invalid property: android.lens.info.hyperfocalDistance",
                    props.get(CameraProperties.LENS_INFO_HYPERFOCAL_DISTANCE));
        }
    }

    public void testCameraPropertiesAndroidLensInfoMinimumFocusDistance() throws Exception {
        String[] ids = mCameraManager.getCameraIdList();
        for (int i = 0; i < ids.length; i++) {
            CameraDevice camera = mCameraManager.openCamera(ids[i]);
            assertNotNull("Failed to open camera", camera);
            CameraProperties props;
            try {
                props = camera.getProperties();
            }
            finally {
                camera.close();
            }
            assertNotNull(String.format("Can't get camera properties from: ID %s", ids[i]),
                                        props);
            assertNotNull("Invalid property: android.lens.info.minimumFocusDistance",
                    props.get(CameraProperties.LENS_INFO_MINIMUM_FOCUS_DISTANCE));
        }
    }

    public void testCameraPropertiesAndroidLensInfoShadingMapSize() throws Exception {
        String[] ids = mCameraManager.getCameraIdList();
        for (int i = 0; i < ids.length; i++) {
            CameraDevice camera = mCameraManager.openCamera(ids[i]);
            assertNotNull("Failed to open camera", camera);
            CameraProperties props;
            try {
                props = camera.getProperties();
            }
            finally {
                camera.close();
            }
            assertNotNull(String.format("Can't get camera properties from: ID %s", ids[i]),
                                        props);
            assertNotNull("Invalid property: android.lens.info.shadingMapSize",
                    props.get(CameraProperties.LENS_INFO_SHADING_MAP_SIZE));
        }
    }

    public void testCameraPropertiesAndroidRequestMaxNumOutputStreams() throws Exception {
        String[] ids = mCameraManager.getCameraIdList();
        for (int i = 0; i < ids.length; i++) {
            CameraDevice camera = mCameraManager.openCamera(ids[i]);
            assertNotNull("Failed to open camera", camera);
            CameraProperties props;
            try {
                props = camera.getProperties();
            }
            finally {
                camera.close();
            }
            assertNotNull(String.format("Can't get camera properties from: ID %s", ids[i]),
                                        props);
            assertNotNull("Invalid property: android.request.maxNumOutputStreams",
                    props.get(CameraProperties.REQUEST_MAX_NUM_OUTPUT_STREAMS));
        }
    }

    public void testCameraPropertiesAndroidScalerAvailableFormats() throws Exception {
        String[] ids = mCameraManager.getCameraIdList();
        for (int i = 0; i < ids.length; i++) {
            CameraDevice camera = mCameraManager.openCamera(ids[i]);
            assertNotNull("Failed to open camera", camera);
            CameraProperties props;
            try {
                props = camera.getProperties();
            }
            finally {
                camera.close();
            }
            assertNotNull(String.format("Can't get camera properties from: ID %s", ids[i]),
                                        props);
            assertNotNull("Invalid property: android.scaler.availableFormats",
                    props.get(CameraProperties.SCALER_AVAILABLE_FORMATS));
        }
    }

    public void testCameraPropertiesAndroidScalerAvailableJpegMinDurations() throws Exception {
        String[] ids = mCameraManager.getCameraIdList();
        for (int i = 0; i < ids.length; i++) {
            CameraDevice camera = mCameraManager.openCamera(ids[i]);
            assertNotNull("Failed to open camera", camera);
            CameraProperties props;
            try {
                props = camera.getProperties();
            }
            finally {
                camera.close();
            }
            assertNotNull(String.format("Can't get camera properties from: ID %s", ids[i]),
                                        props);
            assertNotNull("Invalid property: android.scaler.availableJpegMinDurations",
                    props.get(CameraProperties.SCALER_AVAILABLE_JPEG_MIN_DURATIONS));
        }
    }

    public void testCameraPropertiesAndroidScalerAvailableJpegSizes() throws Exception {
        String[] ids = mCameraManager.getCameraIdList();
        for (int i = 0; i < ids.length; i++) {
            CameraDevice camera = mCameraManager.openCamera(ids[i]);
            assertNotNull("Failed to open camera", camera);
            CameraProperties props;
            try {
                props = camera.getProperties();
            }
            finally {
                camera.close();
            }
            assertNotNull(String.format("Can't get camera properties from: ID %s", ids[i]),
                                        props);
            assertNotNull("Invalid property: android.scaler.availableJpegSizes",
                    props.get(CameraProperties.SCALER_AVAILABLE_JPEG_SIZES));
        }
    }

    public void testCameraPropertiesAndroidScalerAvailableMaxDigitalZoom() throws Exception {
        String[] ids = mCameraManager.getCameraIdList();
        for (int i = 0; i < ids.length; i++) {
            CameraDevice camera = mCameraManager.openCamera(ids[i]);
            assertNotNull("Failed to open camera", camera);
            CameraProperties props;
            try {
                props = camera.getProperties();
            }
            finally {
                camera.close();
            }
            assertNotNull(String.format("Can't get camera properties from: ID %s", ids[i]),
                                        props);
            assertNotNull("Invalid property: android.scaler.availableMaxDigitalZoom",
                    props.get(CameraProperties.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM));
        }
    }

    public void testCameraPropertiesAndroidScalerAvailableProcessedMinDurations() throws Exception {
        String[] ids = mCameraManager.getCameraIdList();
        for (int i = 0; i < ids.length; i++) {
            CameraDevice camera = mCameraManager.openCamera(ids[i]);
            assertNotNull("Failed to open camera", camera);
            CameraProperties props;
            try {
                props = camera.getProperties();
            }
            finally {
                camera.close();
            }
            assertNotNull(String.format("Can't get camera properties from: ID %s", ids[i]),
                                        props);
            assertNotNull("Invalid property: android.scaler.availableProcessedMinDurations",
                    props.get(CameraProperties.SCALER_AVAILABLE_PROCESSED_MIN_DURATIONS));
        }
    }

    public void testCameraPropertiesAndroidScalerAvailableProcessedSizes() throws Exception {
        String[] ids = mCameraManager.getCameraIdList();
        for (int i = 0; i < ids.length; i++) {
            CameraDevice camera = mCameraManager.openCamera(ids[i]);
            assertNotNull("Failed to open camera", camera);
            CameraProperties props;
            try {
                props = camera.getProperties();
            }
            finally {
                camera.close();
            }
            assertNotNull(String.format("Can't get camera properties from: ID %s", ids[i]),
                                        props);
            assertNotNull("Invalid property: android.scaler.availableProcessedSizes",
                    props.get(CameraProperties.SCALER_AVAILABLE_PROCESSED_SIZES));
        }
    }

    public void testCameraPropertiesAndroidSensorBaseGainFactor() throws Exception {
        String[] ids = mCameraManager.getCameraIdList();
        for (int i = 0; i < ids.length; i++) {
            CameraDevice camera = mCameraManager.openCamera(ids[i]);
            assertNotNull("Failed to open camera", camera);
            CameraProperties props;
            try {
                props = camera.getProperties();
            }
            finally {
                camera.close();
            }
            assertNotNull(String.format("Can't get camera properties from: ID %s", ids[i]),
                                        props);
            assertNotNull("Invalid property: android.sensor.baseGainFactor",
                    props.get(CameraProperties.SENSOR_BASE_GAIN_FACTOR));
        }
    }

    public void testCameraPropertiesAndroidSensorMaxAnalogSensitivity() throws Exception {
        String[] ids = mCameraManager.getCameraIdList();
        for (int i = 0; i < ids.length; i++) {
            CameraDevice camera = mCameraManager.openCamera(ids[i]);
            assertNotNull("Failed to open camera", camera);
            CameraProperties props;
            try {
                props = camera.getProperties();
            }
            finally {
                camera.close();
            }
            assertNotNull(String.format("Can't get camera properties from: ID %s", ids[i]),
                                        props);
            assertNotNull("Invalid property: android.sensor.maxAnalogSensitivity",
                    props.get(CameraProperties.SENSOR_MAX_ANALOG_SENSITIVITY));
        }
    }

    public void testCameraPropertiesAndroidSensorOrientation() throws Exception {
        String[] ids = mCameraManager.getCameraIdList();
        for (int i = 0; i < ids.length; i++) {
            CameraDevice camera = mCameraManager.openCamera(ids[i]);
            assertNotNull("Failed to open camera", camera);
            CameraProperties props;
            try {
                props = camera.getProperties();
            }
            finally {
                camera.close();
            }
            assertNotNull(String.format("Can't get camera properties from: ID %s", ids[i]),
                                        props);
            assertNotNull("Invalid property: android.sensor.orientation",
                    props.get(CameraProperties.SENSOR_ORIENTATION));
        }
    }

    public void testCameraPropertiesAndroidSensorInfoActiveArraySize() throws Exception {
        String[] ids = mCameraManager.getCameraIdList();
        for (int i = 0; i < ids.length; i++) {
            CameraDevice camera = mCameraManager.openCamera(ids[i]);
            assertNotNull("Failed to open camera", camera);
            CameraProperties props;
            try {
                props = camera.getProperties();
            }
            finally {
                camera.close();
            }
            assertNotNull(String.format("Can't get camera properties from: ID %s", ids[i]),
                                        props);
            assertNotNull("Invalid property: android.sensor.info.activeArraySize",
                    props.get(CameraProperties.SENSOR_INFO_ACTIVE_ARRAY_SIZE));
        }
    }

    public void testCameraPropertiesAndroidSensorInfoSensitivityRange() throws Exception {
        String[] ids = mCameraManager.getCameraIdList();
        for (int i = 0; i < ids.length; i++) {
            CameraDevice camera = mCameraManager.openCamera(ids[i]);
            assertNotNull("Failed to open camera", camera);
            CameraProperties props;
            try {
                props = camera.getProperties();
            }
            finally {
                camera.close();
            }
            assertNotNull(String.format("Can't get camera properties from: ID %s", ids[i]),
                                        props);
            assertNotNull("Invalid property: android.sensor.info.sensitivityRange",
                    props.get(CameraProperties.SENSOR_INFO_SENSITIVITY_RANGE));
        }
    }

    public void testCameraPropertiesAndroidSensorInfoExposureTimeRange() throws Exception {
        String[] ids = mCameraManager.getCameraIdList();
        for (int i = 0; i < ids.length; i++) {
            CameraDevice camera = mCameraManager.openCamera(ids[i]);
            assertNotNull("Failed to open camera", camera);
            CameraProperties props;
            try {
                props = camera.getProperties();
            }
            finally {
                camera.close();
            }
            assertNotNull(String.format("Can't get camera properties from: ID %s", ids[i]),
                                        props);
            assertNotNull("Invalid property: android.sensor.info.exposureTimeRange",
                    props.get(CameraProperties.SENSOR_INFO_EXPOSURE_TIME_RANGE));
        }
    }

    public void testCameraPropertiesAndroidSensorInfoMaxFrameDuration() throws Exception {
        String[] ids = mCameraManager.getCameraIdList();
        for (int i = 0; i < ids.length; i++) {
            CameraDevice camera = mCameraManager.openCamera(ids[i]);
            assertNotNull("Failed to open camera", camera);
            CameraProperties props;
            try {
                props = camera.getProperties();
            }
            finally {
                camera.close();
            }
            assertNotNull(String.format("Can't get camera properties from: ID %s", ids[i]),
                                        props);
            assertNotNull("Invalid property: android.sensor.info.maxFrameDuration",
                    props.get(CameraProperties.SENSOR_INFO_MAX_FRAME_DURATION));
        }
    }

    public void testCameraPropertiesAndroidSensorInfoPhysicalSize() throws Exception {
        String[] ids = mCameraManager.getCameraIdList();
        for (int i = 0; i < ids.length; i++) {
            CameraDevice camera = mCameraManager.openCamera(ids[i]);
            assertNotNull("Failed to open camera", camera);
            CameraProperties props;
            try {
                props = camera.getProperties();
            }
            finally {
                camera.close();
            }
            assertNotNull(String.format("Can't get camera properties from: ID %s", ids[i]),
                                        props);
            assertNotNull("Invalid property: android.sensor.info.physicalSize",
                    props.get(CameraProperties.SENSOR_INFO_PHYSICAL_SIZE));
        }
    }

    public void testCameraPropertiesAndroidStatisticsInfoAvailableFaceDetectModes() throws Exception {
        String[] ids = mCameraManager.getCameraIdList();
        for (int i = 0; i < ids.length; i++) {
            CameraDevice camera = mCameraManager.openCamera(ids[i]);
            assertNotNull("Failed to open camera", camera);
            CameraProperties props;
            try {
                props = camera.getProperties();
            }
            finally {
                camera.close();
            }
            assertNotNull(String.format("Can't get camera properties from: ID %s", ids[i]),
                                        props);
            assertNotNull("Invalid property: android.statistics.info.availableFaceDetectModes",
                    props.get(CameraProperties.STATISTICS_INFO_AVAILABLE_FACE_DETECT_MODES));
        }
    }

    public void testCameraPropertiesAndroidStatisticsInfoMaxFaceCount() throws Exception {
        String[] ids = mCameraManager.getCameraIdList();
        for (int i = 0; i < ids.length; i++) {
            CameraDevice camera = mCameraManager.openCamera(ids[i]);
            assertNotNull("Failed to open camera", camera);
            CameraProperties props;
            try {
                props = camera.getProperties();
            }
            finally {
                camera.close();
            }
            assertNotNull(String.format("Can't get camera properties from: ID %s", ids[i]),
                                        props);
            assertNotNull("Invalid property: android.statistics.info.maxFaceCount",
                    props.get(CameraProperties.STATISTICS_INFO_MAX_FACE_COUNT));
        }
    }

    public void testCameraPropertiesAndroidTonemapMaxCurvePoints() throws Exception {
        String[] ids = mCameraManager.getCameraIdList();
        for (int i = 0; i < ids.length; i++) {
            CameraDevice camera = mCameraManager.openCamera(ids[i]);
            assertNotNull("Failed to open camera", camera);
            CameraProperties props;
            try {
                props = camera.getProperties();
            }
            finally {
                camera.close();
            }
            assertNotNull(String.format("Can't get camera properties from: ID %s", ids[i]),
                                        props);
            assertNotNull("Invalid property: android.tonemap.maxCurvePoints",
                    props.get(CameraProperties.TONEMAP_MAX_CURVE_POINTS));
        }
    }

    public void testCameraPropertiesAndroidInfoSupportedHardwareLevel() throws Exception {
        String[] ids = mCameraManager.getCameraIdList();
        for (int i = 0; i < ids.length; i++) {
            CameraDevice camera = mCameraManager.openCamera(ids[i]);
            assertNotNull("Failed to open camera", camera);
            CameraProperties props;
            try {
                props = camera.getProperties();
            }
            finally {
                camera.close();
            }
            assertNotNull(String.format("Can't get camera properties from: ID %s", ids[i]),
                                        props);
            assertNotNull("Invalid property: android.info.supportedHardwareLevel",
                    props.get(CameraProperties.INFO_SUPPORTED_HARDWARE_LEVEL));
        }
    }
}

