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

package android.hardware.cts;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Area;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.ErrorCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.media.CamcorderProfile;
import android.media.ExifInterface;
import android.media.MediaRecorder;
import android.os.ConditionVariable;
import android.os.Environment;
import android.os.Looper;
import android.test.ActivityInstrumentationTestCase2;
import android.test.MoreAsserts;
import android.test.UiThreadTest;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * This test case must run with hardware. It can't be tested in emulator
 */
@LargeTest
@TestTargetClass(Camera.class)
public class CameraTest extends ActivityInstrumentationTestCase2<CameraStubActivity> {
    private String TAG = "CameraTest";
    private static final String PACKAGE = "com.android.cts.stub";
    private static final boolean LOGV = false;
    private final String JPEG_PATH = Environment.getExternalStorageDirectory().getPath() +
            "/test.jpg";
    private byte[] mJpegData;

    private boolean mPreviewCallbackResult = false;
    private boolean mShutterCallbackResult = false;
    private boolean mRawPictureCallbackResult = false;
    private boolean mJpegPictureCallbackResult = false;
    private boolean mErrorCallbackResult = false;
    private boolean mAutoFocusSucceeded = false;

    private static final int WAIT_FOR_COMMAND_TO_COMPLETE = 1500;  // Milliseconds.
    private static final int WAIT_FOR_FOCUS_TO_COMPLETE = 3000;
    private static final int WAIT_FOR_SNAPSHOT_TO_COMPLETE = 5000;

    private static final int FOCUS_AREA = 0;
    private static final int METERING_AREA = 1;

    private PreviewCallback mPreviewCallback = new PreviewCallback();
    private TestShutterCallback mShutterCallback = new TestShutterCallback();
    private RawPictureCallback mRawPictureCallback = new RawPictureCallback();
    private JpegPictureCallback mJpegPictureCallback = new JpegPictureCallback();
    private TestErrorCallback mErrorCallback = new TestErrorCallback();
    private AutoFocusCallback mAutoFocusCallback = new AutoFocusCallback();

    private Looper mLooper = null;
    private final ConditionVariable mPreviewDone = new ConditionVariable();
    private final ConditionVariable mFocusDone = new ConditionVariable();
    private final ConditionVariable mSnapshotDone = new ConditionVariable();

    Camera mCamera;

    public CameraTest() {
        super(PACKAGE, CameraStubActivity.class);
        if (LOGV) Log.v(TAG, "Camera Constructor");
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // to start CameraStubActivity.
        getActivity();
    }

    @Override
    protected void tearDown() throws Exception {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
        super.tearDown();
    }

    /*
     * Initializes the message looper so that the Camera object can
     * receive the callback messages.
     */
    private void initializeMessageLooper(final int cameraId) {
        final ConditionVariable startDone = new ConditionVariable();
        new Thread() {
            @Override
            public void run() {
                Log.v(TAG, "start loopRun");
                // Set up a looper to be used by camera.
                Looper.prepare();
                // Save the looper so that we can terminate this thread
                // after we are done with it.
                mLooper = Looper.myLooper();
                mCamera = Camera.open(cameraId);
                mCamera.setErrorCallback(mErrorCallback);
                Log.v(TAG, "camera is opened");
                startDone.open();
                Looper.loop(); // Blocks forever until Looper.quit() is called.
                if (LOGV) Log.v(TAG, "initializeMessageLooper: quit.");
            }
        }.start();

        Log.v(TAG, "start waiting for looper");
        if (!startDone.block(WAIT_FOR_COMMAND_TO_COMPLETE)) {
            Log.v(TAG, "initializeMessageLooper: start timeout");
            fail("initializeMessageLooper: start timeout");
        }
    }

    /*
     * Terminates the message looper thread.
     */
    private void terminateMessageLooper() throws Exception {
        mLooper.quit();
        // Looper.quit() is asynchronous. The looper may still has some
        // preview callbacks in the queue after quit is called. The preview
        // callback still uses the camera object (setHasPreviewCallback).
        // After camera is released, RuntimeException will be thrown from
        // the method. So we need to join the looper thread here.
        mLooper.getThread().join();
        mCamera.release();
        mCamera = null;
    }

    //Implement the previewCallback
    private final class PreviewCallback
            implements android.hardware.Camera.PreviewCallback {
        public void onPreviewFrame(byte [] data, Camera camera) {
            assertNotNull(data);
            Size size = camera.getParameters().getPreviewSize();
            int format = camera.getParameters().getPreviewFormat();
            int bitsPerPixel = ImageFormat.getBitsPerPixel(format);
            assertEquals(size.width * size.height * bitsPerPixel / 8, data.length);
            mPreviewCallbackResult = true;
            mCamera.stopPreview();
            if (LOGV) Log.v(TAG, "notify the preview callback");
            mPreviewDone.open();
            if (LOGV) Log.v(TAG, "Preview callback stop");
        }
    }

    //Implement the shutterCallback
    private final class TestShutterCallback implements ShutterCallback {
        public void onShutter() {
            mShutterCallbackResult = true;
            if (LOGV) Log.v(TAG, "onShutter called");
        }
    }

    //Implement the RawPictureCallback
    private final class RawPictureCallback implements PictureCallback {
        public void onPictureTaken(byte [] rawData, Camera camera) {
            if (rawData != null) {
                mRawPictureCallbackResult = true;
            } else {
                mRawPictureCallbackResult = false;
            }
            if (LOGV) Log.v(TAG, "RawPictureCallback callback");
        }
    }

    // Implement the JpegPictureCallback
    private final class JpegPictureCallback implements PictureCallback {
        public void onPictureTaken(byte[] rawData, Camera camera) {
            try {
                mJpegData = rawData;
                if (rawData != null) {
                    // try to store the picture on the SD card
                    File rawoutput = new File(JPEG_PATH);
                    FileOutputStream outStream = new FileOutputStream(rawoutput);
                    outStream.write(rawData);
                    outStream.close();
                    mJpegPictureCallbackResult = true;

                    if (LOGV) {
                        Log.v(TAG, "JpegPictureCallback rawDataLength = " + rawData.length);
                    }
                } else {
                    mJpegPictureCallbackResult = false;
                }
                mSnapshotDone.open();
                if (LOGV) Log.v(TAG, "Jpeg Picture callback");
            } catch (IOException e) {
                // no need to fail here; callback worked fine
                Log.w(TAG, "Error writing picture to sd card.");
            }
        }
    }

    // Implement the ErrorCallback
    private final class TestErrorCallback implements ErrorCallback {
        public void onError(int error, Camera camera) {
            mErrorCallbackResult = true;
            fail("Got camera error from ErrorCallback: " + error);
        }
    }

    private final class AutoFocusCallback
            implements android.hardware.Camera.AutoFocusCallback {
        public void onAutoFocus(boolean success, Camera camera) {
            mAutoFocusSucceeded = success;
            Log.v(TAG, "AutoFocusCallback success=" + success);
            mFocusDone.open();
        }
    }

    private void waitForPreviewDone() {
        if (LOGV) Log.v(TAG, "Wait for preview callback");
        if (!mPreviewDone.block(WAIT_FOR_COMMAND_TO_COMPLETE)) {
            // timeout could be expected or unexpected. The caller will decide.
            Log.v(TAG, "waitForPreviewDone: timeout");
        }
        mPreviewDone.close();
    }

    private boolean waitForFocusDone() {
        boolean result = mFocusDone.block(WAIT_FOR_FOCUS_TO_COMPLETE);
        if (!result) {
            // timeout could be expected or unexpected. The caller will decide.
            Log.v(TAG, "waitForFocusDone: timeout");
        }
        mFocusDone.close();
        return result;
    }

    private void waitForSnapshotDone() {
        if (!mSnapshotDone.block(WAIT_FOR_SNAPSHOT_TO_COMPLETE)) {
            // timeout could be expected or unexpected. The caller will decide.
            Log.v(TAG, "waitForSnapshotDone: timeout");
        }
        mSnapshotDone.close();
    }

    private void checkPreviewCallback() throws Exception {
        mCamera.setPreviewDisplay(getActivity().getSurfaceView().getHolder());
        if (LOGV) Log.v(TAG, "check preview callback");
        mCamera.startPreview();
        waitForPreviewDone();
        mCamera.setPreviewCallback(null);
    }

    /*
     * Test case 1: Take a picture and verify all the callback
     * functions are called properly.
     */
    @TestTargets({
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "startPreview",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setPreviewDisplay",
            args = {android.view.SurfaceHolder.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "open",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "release",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "takePicture",
            args = {android.hardware.Camera.ShutterCallback.class,
                    android.hardware.Camera.PictureCallback.class,
                    android.hardware.Camera.PictureCallback.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "autoFocus",
            args = {android.hardware.Camera.AutoFocusCallback.class}
        )
    })
    @UiThreadTest
    public void testTakePicture() throws Exception {
        int nCameras = Camera.getNumberOfCameras();
        for (int id = 0; id < nCameras; id++) {
            Log.v(TAG, "Camera id=" + id);
            testTakePictureByCamera(id);
        }
    }

    private void testTakePictureByCamera(int cameraId) throws Exception {
        initializeMessageLooper(cameraId);
        Size pictureSize = mCamera.getParameters().getPictureSize();
        mCamera.setPreviewDisplay(getActivity().getSurfaceView().getHolder());
        mCamera.startPreview();
        mCamera.autoFocus(mAutoFocusCallback);
        assertTrue(waitForFocusDone());
        mJpegData = null;
        mCamera.takePicture(mShutterCallback, mRawPictureCallback, mJpegPictureCallback);
        waitForSnapshotDone();
        terminateMessageLooper();
        assertTrue(mShutterCallbackResult);
        assertTrue(mJpegPictureCallbackResult);
        assertNotNull(mJpegData);
        BitmapFactory.Options bmpOptions = new BitmapFactory.Options();
        bmpOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(mJpegData, 0, mJpegData.length, bmpOptions);
        assertEquals(pictureSize.width, bmpOptions.outWidth);
        assertEquals(pictureSize.height, bmpOptions.outHeight);
    }

    @TestTargets({
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "stopPreview",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setPreviewCallback",
            args = {android.hardware.Camera.PreviewCallback.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "open",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "release",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "startPreview",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setPreviewDisplay",
            args = {android.view.SurfaceHolder.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setErrorCallback",
            args = {android.hardware.Camera.ErrorCallback.class}
        )
    })
    @UiThreadTest
    public void testPreviewCallback() throws Exception {
        int nCameras = Camera.getNumberOfCameras();
        for (int id = 0; id < nCameras; id++) {
            Log.v(TAG, "Camera id=" + id);
            testPreviewCallbackByCamera(id);
        }
    }

    private void testPreviewCallbackByCamera(int cameraId) throws Exception {
        initializeMessageLooper(cameraId);
        mCamera.setPreviewCallback(mPreviewCallback);
        checkPreviewCallback();
        terminateMessageLooper();
        assertTrue(mPreviewCallbackResult);

        mPreviewCallbackResult = false;
        initializeMessageLooper(cameraId);
        checkPreviewCallback();
        terminateMessageLooper();
        assertFalse(mPreviewCallbackResult);

        // Test all preview sizes.
        initializeMessageLooper(cameraId);
        Parameters parameters = mCamera.getParameters();
        for (Size size: parameters.getSupportedPreviewSizes()) {
            mPreviewCallbackResult = false;
            mCamera.setPreviewCallback(mPreviewCallback);
            parameters.setPreviewSize(size.width, size.height);
            mCamera.setParameters(parameters);
            assertEquals(size, mCamera.getParameters().getPreviewSize());
            checkPreviewCallback();
            assertTrue(mPreviewCallbackResult);
            try {
                // Wait for a while to throw away the remaining preview frames.
                Thread.sleep(1000);
            } catch(Exception e) {
                // ignore
            }
            mPreviewDone.close();
        }
        terminateMessageLooper();
    }

    @TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "setOneShotPreviewCallback",
        args = {PreviewCallback.class}
    )
    @UiThreadTest
    public void testSetOneShotPreviewCallback() throws Exception {
        int nCameras = Camera.getNumberOfCameras();
        for (int id = 0; id < nCameras; id++) {
            Log.v(TAG, "Camera id=" + id);
            testSetOneShotPreviewCallbackByCamera(id);
        }
    }

    private void testSetOneShotPreviewCallbackByCamera(int cameraId) throws Exception {
        initializeMessageLooper(cameraId);
        mCamera.setOneShotPreviewCallback(mPreviewCallback);
        checkPreviewCallback();
        terminateMessageLooper();
        assertTrue(mPreviewCallbackResult);

        mPreviewCallbackResult = false;
        initializeMessageLooper(cameraId);
        checkPreviewCallback();
        terminateMessageLooper();
        assertFalse(mPreviewCallbackResult);
    }

    @TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "setPreviewDisplay",
        args = {SurfaceHolder.class}
    )
    @UiThreadTest
    public void testSetPreviewDisplay() throws Exception {
        int nCameras = Camera.getNumberOfCameras();
        for (int id = 0; id < nCameras; id++) {
            Log.v(TAG, "Camera id=" + id);
            testSetPreviewDisplayByCamera(id);
        }
    }

    private void testSetPreviewDisplayByCamera(int cameraId) throws Exception {
        SurfaceHolder holder = getActivity().getSurfaceView().getHolder();
        initializeMessageLooper(cameraId);

        // Check the order: startPreview->setPreviewDisplay.
        mCamera.setOneShotPreviewCallback(mPreviewCallback);
        mCamera.startPreview();
        mCamera.setPreviewDisplay(holder);
        waitForPreviewDone();
        terminateMessageLooper();
        assertTrue(mPreviewCallbackResult);

        // Check the order: setPreviewDisplay->startPreview.
        initializeMessageLooper(cameraId);
        mPreviewCallbackResult = false;
        mCamera.setOneShotPreviewCallback(mPreviewCallback);
        mCamera.setPreviewDisplay(holder);
        mCamera.startPreview();
        waitForPreviewDone();
        mCamera.stopPreview();
        assertTrue(mPreviewCallbackResult);

        // Check the order: setting preview display to null->startPreview->
        // setPreviewDisplay.
        mPreviewCallbackResult = false;
        mCamera.setOneShotPreviewCallback(mPreviewCallback);
        mCamera.setPreviewDisplay(null);
        mCamera.startPreview();
        mCamera.setPreviewDisplay(holder);
        waitForPreviewDone();
        terminateMessageLooper();
        assertTrue(mPreviewCallbackResult);
    }

    @TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "setDisplayOrientation",
        args = {int.class}
    )
    @UiThreadTest
    public void testDisplayOrientation() throws Exception {
        int nCameras = Camera.getNumberOfCameras();
        for (int id = 0; id < nCameras; id++) {
            Log.v(TAG, "Camera id=" + id);
            testDisplayOrientationByCamera(id);
        }
    }

    private void testDisplayOrientationByCamera(int cameraId) throws Exception {
        initializeMessageLooper(cameraId);

        // Check valid arguments.
        mCamera.setDisplayOrientation(0);
        mCamera.setDisplayOrientation(90);
        mCamera.setDisplayOrientation(180);
        mCamera.setDisplayOrientation(270);

        // Check invalid arguments.
        try {
            mCamera.setDisplayOrientation(45);
            fail("Should throw exception for invalid arguments");
        } catch (RuntimeException ex) {
            // expected
        }

        // Start preview.
        mCamera.setPreviewDisplay(getActivity().getSurfaceView().getHolder());
        mCamera.startPreview();

        // Check setting orientation during preview is not allowed.
        try {
            mCamera.setDisplayOrientation(90);
            fail("Should throw exception for setting orientation during preview.");
        } catch (RuntimeException ex) {
            // expected
        }

        terminateMessageLooper();
    }

    @TestTargets({
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getParameters",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setParameters",
            args = {android.hardware.Camera.Parameters.class}
        )
    })
    @UiThreadTest
    public void testParameters() throws Exception {
        int nCameras = Camera.getNumberOfCameras();
        for (int id = 0; id < nCameras; id++) {
            Log.v(TAG, "Camera id=" + id);
            testParametersByCamera(id);
        }
    }

    private void testParametersByCamera(int cameraId) throws Exception {
        initializeMessageLooper(cameraId);
        // we can get parameters just by getxxx method due to the private constructor
        Parameters pSet = mCamera.getParameters();
        assertParameters(pSet);
        terminateMessageLooper();
    }

    // Also test Camera.Parameters
    private void assertParameters(Parameters parameters) {
        // Parameters constants
        final int PICTURE_FORMAT = ImageFormat.JPEG;
        final int PREVIEW_FORMAT = ImageFormat.NV21;

        // Before setting Parameters
        final int origPictureFormat = parameters.getPictureFormat();
        final int origPictureWidth = parameters.getPictureSize().width;
        final int origPictureHeight = parameters.getPictureSize().height;
        final int origPreviewFormat = parameters.getPreviewFormat();
        final int origPreviewWidth = parameters.getPreviewSize().width;
        final int origPreviewHeight = parameters.getPreviewSize().height;
        final int origPreviewFrameRate = parameters.getPreviewFrameRate();

        assertTrue(origPictureWidth > 0);
        assertTrue(origPictureHeight > 0);
        assertTrue(origPreviewWidth > 0);
        assertTrue(origPreviewHeight > 0);
        assertTrue(origPreviewFrameRate > 0);

        // The default preview format must be yuv420 (NV21).
        assertEquals(ImageFormat.NV21, origPreviewFormat);

        // The default picture format must be Jpeg.
        assertEquals(ImageFormat.JPEG, origPictureFormat);

        // If camera supports flash, the default flash mode must be off.
        String flashMode = parameters.getFlashMode();
        assertTrue(flashMode == null || flashMode.equals(parameters.FLASH_MODE_OFF));
        String wb = parameters.getWhiteBalance();
        assertTrue(wb == null || wb.equals(parameters.WHITE_BALANCE_AUTO));
        String effect = parameters.getColorEffect();
        assertTrue(effect == null || effect.equals(parameters.EFFECT_NONE));

        // Some parameters must be supported.
        List<Size> previewSizes = parameters.getSupportedPreviewSizes();
        List<Size> pictureSizes = parameters.getSupportedPictureSizes();
        List<Integer> previewFormats = parameters.getSupportedPreviewFormats();
        List<Integer> pictureFormats = parameters.getSupportedPictureFormats();
        List<Integer> frameRates = parameters.getSupportedPreviewFrameRates();
        List<String> focusModes = parameters.getSupportedFocusModes();
        String focusMode = parameters.getFocusMode();
        float focalLength = parameters.getFocalLength();
        float horizontalViewAngle = parameters.getHorizontalViewAngle();
        float verticalViewAngle = parameters.getVerticalViewAngle();
        int jpegQuality = parameters.getJpegQuality();
        int jpegThumnailQuality = parameters.getJpegThumbnailQuality();
        assertTrue(previewSizes != null && previewSizes.size() != 0);
        assertTrue(pictureSizes != null && pictureSizes.size() != 0);
        assertTrue(previewFormats != null && previewFormats.size() >= 2);
        assertTrue(previewFormats.contains(ImageFormat.NV21));
        assertTrue(previewFormats.contains(ImageFormat.YV12));
        assertTrue(pictureFormats != null && pictureFormats.size() != 0);
        assertTrue(frameRates != null && frameRates.size() != 0);
        assertTrue(focusModes != null && focusModes.size() != 0);
        assertNotNull(focusMode);
        assertTrue(focalLength > 0);
        assertTrue(horizontalViewAngle > 0 && horizontalViewAngle <= 360);
        assertTrue(verticalViewAngle > 0 && verticalViewAngle <= 360);
        Size previewSize = previewSizes.get(0);
        Size pictureSize = pictureSizes.get(0);
        assertTrue(jpegQuality >= 1 && jpegQuality <= 100);
        assertTrue(jpegThumnailQuality >= 1 && jpegThumnailQuality <= 100);

        // If a parameter is supported, both getXXX and getSupportedXXX have to
        // be non null.
        if (parameters.getWhiteBalance() != null) {
            assertNotNull(parameters.getSupportedWhiteBalance());
        }
        if (parameters.getSupportedWhiteBalance() != null) {
            assertNotNull(parameters.getWhiteBalance());
        }
        if (parameters.getColorEffect() != null) {
            assertNotNull(parameters.getSupportedColorEffects());
        }
        if (parameters.getSupportedColorEffects() != null) {
            assertNotNull(parameters.getColorEffect());
        }
        if (parameters.getAntibanding() != null) {
            assertNotNull(parameters.getSupportedAntibanding());
        }
        if (parameters.getSupportedAntibanding() != null) {
            assertNotNull(parameters.getAntibanding());
        }
        if (parameters.getSceneMode() != null) {
            assertNotNull(parameters.getSupportedSceneModes());
        }
        if (parameters.getSupportedSceneModes() != null) {
            assertNotNull(parameters.getSceneMode());
        }
        if (parameters.getFlashMode() != null) {
            assertNotNull(parameters.getSupportedFlashModes());
        }
        if (parameters.getSupportedFlashModes() != null) {
            assertNotNull(parameters.getFlashMode());
        }

        // Check if the sizes value contain invalid characters.
        assertNoLetters(parameters.get("preview-size-values"), "preview-size-values");
        assertNoLetters(parameters.get("picture-size-values"), "picture-size-values");
        assertNoLetters(parameters.get("jpeg-thumbnail-size-values"),
                "jpeg-thumbnail-size-values");

        // Set the parameters.
        parameters.setPictureFormat(PICTURE_FORMAT);
        assertEquals(PICTURE_FORMAT, parameters.getPictureFormat());
        parameters.setPictureSize(pictureSize.width, pictureSize.height);
        assertEquals(pictureSize.width, parameters.getPictureSize().width);
        assertEquals(pictureSize.height, parameters.getPictureSize().height);
        parameters.setPreviewFormat(PREVIEW_FORMAT);
        assertEquals(PREVIEW_FORMAT, parameters.getPreviewFormat());
        parameters.setPreviewFrameRate(frameRates.get(0));
        assertEquals(frameRates.get(0).intValue(), parameters.getPreviewFrameRate());
        parameters.setPreviewSize(previewSize.width, previewSize.height);
        assertEquals(previewSize.width, parameters.getPreviewSize().width);
        assertEquals(previewSize.height, parameters.getPreviewSize().height);

        mCamera.setParameters(parameters);
        Parameters paramActual = mCamera.getParameters();

        assertTrue(isValidPixelFormat(paramActual.getPictureFormat()));
        assertEquals(pictureSize.width, paramActual.getPictureSize().width);
        assertEquals(pictureSize.height, paramActual.getPictureSize().height);
        assertTrue(isValidPixelFormat(paramActual.getPreviewFormat()));
        assertEquals(previewSize.width, paramActual.getPreviewSize().width);
        assertEquals(previewSize.height, paramActual.getPreviewSize().height);
        assertTrue(paramActual.getPreviewFrameRate() > 0);

        checkExposureCompensation(parameters);
        checkPreferredPreviewSizeForVideo(parameters);
    }

    private void checkPreferredPreviewSizeForVideo(Parameters parameters) {
        List<Size> videoSizes = parameters.getSupportedVideoSizes();
        Size preferredPreviewSize = parameters.getPreferredPreviewSizeForVideo();

        // If getSupportedVideoSizes() returns null,
        // getPreferredPreviewSizeForVideo() will return null;
        // otherwise, if getSupportedVideoSizes() does not return null,
        // getPreferredPreviewSizeForVideo() will not return null.
        if (videoSizes == null) {
            assertNull(preferredPreviewSize);
        } else {
            assertNotNull(preferredPreviewSize);
        }

        // If getPreferredPreviewSizeForVideo() returns null,
        // getSupportedVideoSizes() will return null;
        // otherwise, if getPreferredPreviewSizeForVideo() does not return null,
        // getSupportedVideoSizes() will not return null.
        if (preferredPreviewSize == null) {
            assertNull(videoSizes);
        } else {
            assertNotNull(videoSizes);
        }

        if (videoSizes != null) {  // implies: preferredPreviewSize != null
            // If getSupportedVideoSizes() does not return null,
            // the returned list will contain at least one size.
            assertTrue(videoSizes.size() > 0);

            // In addition, getPreferredPreviewSizeForVideo() returns a size
            // that is among the supported preview sizes.
            List<Size> previewSizes = parameters.getSupportedPreviewSizes();
            assertNotNull(previewSizes);
            assertTrue(previewSizes.size() > 0);
            assertTrue(previewSizes.contains(preferredPreviewSize));
        }
    }

    private void checkExposureCompensation(Parameters parameters) {
        assertEquals(0, parameters.getExposureCompensation());
        int max = parameters.getMaxExposureCompensation();
        int min = parameters.getMinExposureCompensation();
        float step = parameters.getExposureCompensationStep();
        if (max == 0 && min == 0) {
            assertEquals(0f, step, 0.000001f);
            return;
        }
        assertTrue(step > 0);
        assertTrue(max >= 0);
        assertTrue(min <= 0);
    }

    private boolean isValidPixelFormat(int format) {
        return (format == ImageFormat.RGB_565) || (format == ImageFormat.NV21)
                || (format == ImageFormat.JPEG) || (format == ImageFormat.YUY2);
    }

    @TestTargets({
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setJpegThumbnailSize",
            args = {android.hardware.Camera.Size.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getJpegThumbnailSize",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getJpegSupportedThumbnailSizes",
            args = {}
        )
    })
    @UiThreadTest
    public void testJpegThumbnailSize() throws Exception {
        int nCameras = Camera.getNumberOfCameras();
        for (int id = 0; id < nCameras; id++) {
            Log.v(TAG, "Camera id=" + id);
            testJpegThumbnailSizeByCamera(id);
        }
    }

    private void testJpegThumbnailSizeByCamera(int cameraId) throws Exception {
        initializeMessageLooper(cameraId);
        // Thumbnail size parameters should have valid values.
        Parameters p = mCamera.getParameters();
        Size size = p.getJpegThumbnailSize();
        assertTrue(size.width > 0 && size.height > 0);
        List<Size> sizes = p.getSupportedJpegThumbnailSizes();
        assertTrue(sizes.size() >= 2);
        assertTrue(sizes.contains(size));
        assertTrue(sizes.contains(mCamera.new Size(0, 0)));

        // Test if the thumbnail size matches the setting.
        mCamera.setPreviewDisplay(getActivity().getSurfaceView().getHolder());
        mCamera.startPreview();
        mCamera.takePicture(mShutterCallback, mRawPictureCallback, mJpegPictureCallback);
        waitForSnapshotDone();
        assertTrue(mJpegPictureCallbackResult);
        ExifInterface exif = new ExifInterface(JPEG_PATH);
        assertTrue(exif.hasThumbnail());
        byte[] thumb = exif.getThumbnail();
        BitmapFactory.Options bmpOptions = new BitmapFactory.Options();
        bmpOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(thumb, 0, thumb.length, bmpOptions);
        assertEquals(size.width, bmpOptions.outWidth);
        assertEquals(size.height, bmpOptions.outHeight);

        // Test no thumbnail case.
        p.setJpegThumbnailSize(0, 0);
        mCamera.setParameters(p);
        Size actual = mCamera.getParameters().getJpegThumbnailSize();
        assertEquals(0, actual.width);
        assertEquals(0, actual.height);
        mCamera.startPreview();
        mCamera.takePicture(mShutterCallback, mRawPictureCallback, mJpegPictureCallback);
        waitForSnapshotDone();
        assertTrue(mJpegPictureCallbackResult);
        exif = new ExifInterface(JPEG_PATH);
        assertFalse(exif.hasThumbnail());

        terminateMessageLooper();
    }

    @UiThreadTest
    public void testJpegExif() throws Exception {
        int nCameras = Camera.getNumberOfCameras();
        for (int id = 0; id < nCameras; id++) {
            Log.v(TAG, "Camera id=" + id);
            testJpegExifByCamera(id);
        }
    }

    private void testJpegExifByCamera(int cameraId) throws Exception {
        initializeMessageLooper(cameraId);
        Camera.Parameters parameters = mCamera.getParameters();
        mCamera.setPreviewDisplay(getActivity().getSurfaceView().getHolder());
        mCamera.startPreview();
        double focalLength = (double)parameters.getFocalLength();
        mCamera.takePicture(mShutterCallback, mRawPictureCallback, mJpegPictureCallback);
        waitForSnapshotDone();
        ExifInterface exif = new ExifInterface(JPEG_PATH);
        assertNotNull(exif.getAttribute(ExifInterface.TAG_MAKE));
        assertNotNull(exif.getAttribute(ExifInterface.TAG_MODEL));
        assertNotNull(exif.getAttribute(ExifInterface.TAG_DATETIME));
        assertTrue(exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0) != 0);
        assertTrue(exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0) != 0);
        checkGpsDataNull(exif);
        double exifFocalLength = (double)exif.getAttributeDouble(
                ExifInterface.TAG_FOCAL_LENGTH, -1);
        assertEquals(focalLength, exifFocalLength, 0.001);

        // Test gps exif tags.
        testGpsExifValues(parameters, 37.736071, -122.441983, 21, 1199145600,
            "GPS NETWORK HYBRID ARE ALL FINE.");
        testGpsExifValues(parameters, 0.736071, 0.441983, 1, 1199145601, "GPS");
        testGpsExifValues(parameters, -89.736071, -179.441983, 100000, 1199145602, "NETWORK");

        // Test gps tags do not exist after calling removeGpsData.
        mCamera.startPreview();
        parameters.removeGpsData();
        mCamera.setParameters(parameters);
        mCamera.takePicture(mShutterCallback, mRawPictureCallback, mJpegPictureCallback);
        waitForSnapshotDone();
        exif = new ExifInterface(JPEG_PATH);
        checkGpsDataNull(exif);
        terminateMessageLooper();
    }

    private void testGpsExifValues(Parameters parameters, double latitude,
            double longitude, double altitude, long timestamp, String method)
            throws IOException {
        mCamera.startPreview();
        parameters.setGpsLatitude(latitude);
        parameters.setGpsLongitude(longitude);
        parameters.setGpsAltitude(altitude);
        parameters.setGpsTimestamp(timestamp);
        parameters.setGpsProcessingMethod(method);
        mCamera.setParameters(parameters);
        mCamera.takePicture(mShutterCallback, mRawPictureCallback, mJpegPictureCallback);
        waitForSnapshotDone();
        ExifInterface exif = new ExifInterface(JPEG_PATH);
        assertNotNull(exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE));
        assertNotNull(exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE));
        assertNotNull(exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF));
        assertNotNull(exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF));
        assertNotNull(exif.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP));
        assertNotNull(exif.getAttribute(ExifInterface.TAG_GPS_DATESTAMP));
        assertEquals(method, exif.getAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD));
        float[] latLong = new float[2];
        assertTrue(exif.getLatLong(latLong));
        assertEquals((float)latitude, latLong[0], 0.0001f);
        assertEquals((float)longitude, latLong[1], 0.0001f);
        assertEquals(altitude, exif.getAltitude(-1), 1);
        assertEquals(timestamp, exif.getGpsDateTime() / 1000);
    }

    private void checkGpsDataNull(ExifInterface exif) {
        assertNull(exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE));
        assertNull(exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE));
        assertNull(exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF));
        assertNull(exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF));
        assertNull(exif.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP));
        assertNull(exif.getAttribute(ExifInterface.TAG_GPS_DATESTAMP));
        assertNull(exif.getAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD));
    }

    @TestTargets({
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "lock",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "unlock",
            args = {}
        )
    })
    @UiThreadTest
    public void testLockUnlock() throws Exception {
        int nCameras = Camera.getNumberOfCameras();
        for (int id = 0; id < nCameras; id++) {
            Log.v(TAG, "Camera id=" + id);
            testLockUnlockByCamera(id);
        }
    }

    private void testLockUnlockByCamera(int cameraId) throws Exception {
        initializeMessageLooper(cameraId);
        Camera.Parameters parameters = mCamera.getParameters();
        SurfaceHolder surfaceHolder;
        surfaceHolder = getActivity().getSurfaceView().getHolder();
        CamcorderProfile profile = CamcorderProfile.get(cameraId,
                CamcorderProfile.QUALITY_LOW);

        // Set the preview size.
        if (parameters.getSupportedVideoSizes() == null) {
            parameters.setPreviewSize(profile.videoFrameWidth,
                    profile.videoFrameHeight);
        } else {  // Driver supports separates outputs for preview and video.
            List<Size> sizes = parameters.getSupportedPreviewSizes();
            Size preferred = parameters.getPreferredPreviewSizeForVideo();
            int product = preferred.width * preferred.height;
            for (Size size: sizes) {
                if (size.width * size.height <= product) {
                    parameters.setPreviewSize(size.width, size.height);
                    break;
                }
            }
        }

        mCamera.setParameters(parameters);
        mCamera.setPreviewDisplay(surfaceHolder);
        mCamera.startPreview();
        mCamera.lock();  // Locking again from the same process has no effect.
        try {
            recordVideo(profile, surfaceHolder);
            fail("Recording should not succeed because camera is locked.");
        } catch (Exception e) {
            // expected
        }

        mCamera.unlock();  // Unlock the camera so media recorder can use it.
        try {
            mCamera.setParameters(parameters);
            fail("setParameters should not succeed because camera is unlocked.");
        } catch (RuntimeException e) {
            // expected
        }

        recordVideo(profile, surfaceHolder);  // should not throw exception
        // Media recorder already releases the camera so the test application
        // can lock and use the camera now.
        mCamera.lock();  // should not fail
        mCamera.setParameters(parameters);  // should not fail
        terminateMessageLooper();
    }

    private void recordVideo(CamcorderProfile profile,
            SurfaceHolder holder) throws Exception {
        MediaRecorder recorder = new MediaRecorder();
        try {
            // Pass the camera from the test application to media recorder.
            recorder.setCamera(mCamera);
            recorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
            recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            recorder.setProfile(profile);
            recorder.setOutputFile("/dev/null");
            recorder.setPreviewDisplay(holder.getSurface());
            recorder.prepare();
            recorder.start();
            Thread.sleep(2000);
            recorder.stop();
        } finally {
            recorder.release();
        }
    }

    @TestTargets({
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "addCallbackBuffer",
            args = {byte[].class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setPreviewCallbackWithBuffer",
            args = {android.hardware.Camera.PreviewCallback.class}
        )
    })
    @UiThreadTest
    public void testPreviewCallbackWithBuffer() throws Exception {
        int nCameras = Camera.getNumberOfCameras();
        for (int id = 0; id < nCameras; id++) {
            Log.v(TAG, "Camera id=" + id);
            testPreviewCallbackWithBufferByCamera(id);
        }
    }

    private void testPreviewCallbackWithBufferByCamera(int cameraId) throws Exception {
        initializeMessageLooper(cameraId);
        SurfaceHolder surfaceHolder;
        surfaceHolder = getActivity().getSurfaceView().getHolder();
        mCamera.setPreviewDisplay(surfaceHolder);
        Parameters parameters = mCamera.getParameters();
        PreviewCallbackWithBuffer callback = new PreviewCallbackWithBuffer();
        // Test all preview sizes.
        for (Size size: parameters.getSupportedPreviewSizes()) {
            parameters.setPreviewSize(size.width, size.height);
            mCamera.setParameters(parameters);
            assertEquals(size, mCamera.getParameters().getPreviewSize());
            callback.mNumCbWithBuffer1 = 0;
            callback.mNumCbWithBuffer2 = 0;
            callback.mNumCbWithBuffer3 = 0;
            int format = mCamera.getParameters().getPreviewFormat();
            int bitsPerPixel = ImageFormat.getBitsPerPixel(format);
            callback.mBuffer1 = new byte[size.width * size.height * bitsPerPixel / 8];
            callback.mBuffer2 = new byte[size.width * size.height * bitsPerPixel / 8];
            callback.mBuffer3 = new byte[size.width * size.height * bitsPerPixel / 8];

            // Test if we can get the preview callbacks with specified buffers.
            mCamera.addCallbackBuffer(callback.mBuffer1);
            mCamera.addCallbackBuffer(callback.mBuffer2);
            mCamera.setPreviewCallbackWithBuffer(callback);
            mCamera.startPreview();
            waitForPreviewDone();
            assertEquals(1, callback.mNumCbWithBuffer1);
            assertEquals(1, callback.mNumCbWithBuffer2);
            assertEquals(0, callback.mNumCbWithBuffer3);

            // Test if preview callback with buffer still works during preview.
            mCamera.addCallbackBuffer(callback.mBuffer3);
            waitForPreviewDone();
            assertEquals(1, callback.mNumCbWithBuffer1);
            assertEquals(1, callback.mNumCbWithBuffer2);
            assertEquals(1, callback.mNumCbWithBuffer3);
            mCamera.setPreviewCallbackWithBuffer(null);
            mCamera.stopPreview();
        }
        terminateMessageLooper();
    }

    private final class PreviewCallbackWithBuffer
            implements android.hardware.Camera.PreviewCallback {
        public int mNumCbWithBuffer1, mNumCbWithBuffer2, mNumCbWithBuffer3;
        public byte[] mBuffer1, mBuffer2, mBuffer3;
        public void onPreviewFrame(byte[] data, Camera camera) {
            assertNotNull(data);
            if (data == mBuffer1) {
                mNumCbWithBuffer1++;
            } else if (data == mBuffer2) {
                mNumCbWithBuffer2++;
            } else if (data == mBuffer3) {
                mNumCbWithBuffer3++;
            } else {
                fail("Invalid byte array.");
            }

            if ((mNumCbWithBuffer1 == 1 && mNumCbWithBuffer2 == 1)
                    || mNumCbWithBuffer3 == 1) {
                mPreviewDone.open();
            }
        }
    }

    @TestTargets({
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "startSmoothZoom",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "stopSmoothZoom",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setZoomChangeListener",
            args = {android.hardware.Camera.OnZoomChangeListener.class}
        )
    })
    @UiThreadTest
    public void testZoom() throws Exception {
        int nCameras = Camera.getNumberOfCameras();
        for (int id = 0; id < nCameras; id++) {
            Log.v(TAG, "Camera id=" + id);
            initializeMessageLooper(id);
            testImmediateZoom();
            testSmoothZoom();
            terminateMessageLooper();
        }
    }

    private void testImmediateZoom() throws Exception {
        Parameters parameters = mCamera.getParameters();
        if (!parameters.isZoomSupported()) return;

        mCamera.setPreviewDisplay(getActivity().getSurfaceView().getHolder());

        // Test the zoom parameters.
        assertEquals(0, parameters.getZoom());  // default zoom should be 0.
        for (Size size: parameters.getSupportedPreviewSizes()) {
            parameters = mCamera.getParameters();
            parameters.setPreviewSize(size.width, size.height);
            mCamera.setParameters(parameters);
            parameters = mCamera.getParameters();
            int maxZoom = parameters.getMaxZoom();
            assertTrue(maxZoom >= 0);

            // Zoom ratios should be sorted from small to large.
            List<Integer> ratios = parameters.getZoomRatios();
            assertEquals(maxZoom + 1, ratios.size());
            assertEquals(100, ratios.get(0).intValue());
            for (int i = 0; i < ratios.size() - 1; i++) {
                assertTrue(ratios.get(i) < ratios.get(i + 1));
            }
            mCamera.startPreview();
            waitForPreviewDone();

            // Test each zoom step.
            for (int i = 0; i <= maxZoom; i++) {
                parameters.setZoom(i);
                mCamera.setParameters(parameters);
                assertEquals(i, mCamera.getParameters().getZoom());
            }

            // It should throw exception if an invalid value is passed.
            try {
                parameters.setZoom(maxZoom + 1);
                mCamera.setParameters(parameters);
                fail("setZoom should throw exception.");
            } catch (RuntimeException e) {
                // expected
            }
            assertEquals(maxZoom, mCamera.getParameters().getZoom());

            mCamera.takePicture(mShutterCallback, mRawPictureCallback,
                                mJpegPictureCallback);
            waitForSnapshotDone();
        }
    }

    private void testSmoothZoom() throws Exception {
        Parameters parameters = mCamera.getParameters();
        if (!parameters.isSmoothZoomSupported()) return;
        assertTrue(parameters.isZoomSupported());

        ZoomListener zoomListener = new ZoomListener();
        mCamera.setPreviewDisplay(getActivity().getSurfaceView().getHolder());
        mCamera.setZoomChangeListener(zoomListener);
        mCamera.startPreview();
        waitForPreviewDone();

        // Immediate zoom should not generate callbacks.
        int maxZoom = parameters.getMaxZoom();
        parameters.setZoom(maxZoom);
        mCamera.setParameters(parameters);
        assertEquals(maxZoom, mCamera.getParameters().getZoom());
        parameters.setZoom(0);
        mCamera.setParameters(parameters);
        assertEquals(0, mCamera.getParameters().getZoom());
        assertFalse(zoomListener.mZoomDone.block(500));

        // Nothing will happen if zoom is not moving.
        mCamera.stopSmoothZoom();

        // It should not generate callbacks if zoom value is not changed.
        mCamera.startSmoothZoom(0);
        assertFalse(zoomListener.mZoomDone.block(500));
        assertEquals(0, mCamera.getParameters().getZoom());

        // Test startSmoothZoom.
        mCamera.startSmoothZoom(maxZoom);
        assertEquals(true, zoomListener.mZoomDone.block(5000));
        assertEquals(maxZoom, mCamera.getParameters().getZoom());
        assertEquals(maxZoom, zoomListener.mValues.size());
        for(int i = 0; i < maxZoom; i++) {
            // Make sure we get all the callbacks in order.
            assertEquals(i + 1, zoomListener.mValues.get(i).intValue());
        }

        // Test startSmoothZoom. Make sure we get all the callbacks.
        if (maxZoom > 1) {
            zoomListener.mValues = new ArrayList<Integer>();
            zoomListener.mStopped = false;
            Log.e(TAG, "zoomListener.mStopped = " + zoomListener.mStopped);
            zoomListener.mZoomDone.close();
            mCamera.startSmoothZoom(maxZoom / 2);
            assertTrue(zoomListener.mZoomDone.block(5000));
            assertEquals(maxZoom / 2, mCamera.getParameters().getZoom());
            assertEquals(maxZoom - (maxZoom / 2), zoomListener.mValues.size());
            int i = maxZoom - 1;
            for(Integer value: zoomListener.mValues) {
                assertEquals(i, value.intValue());
                i--;
            }
        }

        // It should throw exception if an invalid value is passed.
        try {
            mCamera.startSmoothZoom(maxZoom + 1);
            fail("startSmoothZoom should throw exception.");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test stopSmoothZoom.
        zoomListener.mValues = new ArrayList<Integer>();
        zoomListener.mStopped = false;
        zoomListener.mZoomDone.close();
        parameters.setZoom(0);
        mCamera.setParameters(parameters);
        assertEquals(0, mCamera.getParameters().getZoom());
        mCamera.startSmoothZoom(maxZoom);
        mCamera.stopSmoothZoom();
        assertTrue(zoomListener.mZoomDone.block(5000));
        assertEquals(zoomListener.mValues.size(), mCamera.getParameters().getZoom());
        for(int i = 0; i < zoomListener.mValues.size() - 1; i++) {
            // Make sure we get all the callbacks in order (except the last).
            assertEquals(i + 1, zoomListener.mValues.get(i).intValue());
        }
    }

    private final class ZoomListener
            implements android.hardware.Camera.OnZoomChangeListener {
        public ArrayList<Integer> mValues = new ArrayList<Integer>();
        public boolean mStopped;
        public final ConditionVariable mZoomDone = new ConditionVariable();

        public void onZoomChange(int value, boolean stopped, Camera camera) {
            mValues.add(value);
            assertFalse(mStopped);
            mStopped = stopped;
            if (stopped) {
                mZoomDone.open();
            }
        }
    }

    @UiThreadTest
    public void testFocusDistances() throws Exception {
        int nCameras = Camera.getNumberOfCameras();
        for (int id = 0; id < nCameras; id++) {
            Log.v(TAG, "Camera id=" + id);
            testFocusDistancesByCamera(id);
        }
    }

    private void testFocusDistancesByCamera(int cameraId) throws Exception {
        initializeMessageLooper(cameraId);
        mCamera.setPreviewDisplay(getActivity().getSurfaceView().getHolder());
        mCamera.startPreview();
        waitForPreviewDone();
        Parameters parameters = mCamera.getParameters();

        // Test every supported focus mode.
        for (String focusMode: parameters.getSupportedFocusModes()) {
            parameters.setFocusMode(focusMode);
            mCamera.setParameters(parameters);
            parameters = mCamera.getParameters();
            assertEquals(focusMode, parameters.getFocusMode());
            checkFocusDistances(parameters);
            if (Parameters.FOCUS_MODE_AUTO.equals(focusMode)
                    || Parameters.FOCUS_MODE_MACRO.equals(focusMode)) {
                mCamera.autoFocus(mAutoFocusCallback);
                assertTrue(waitForFocusDone());
                parameters = mCamera.getParameters();
                checkFocusDistances(parameters);
            }
        }

        // Test if the method throws exception if the argument is invalid.
        try {
            parameters.getFocusDistances(null);
            fail("getFocusDistances should not accept null.");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            parameters.getFocusDistances(new float[2]);
            fail("getFocusDistances should not accept a float array with two elements.");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            parameters.getFocusDistances(new float[4]);
            fail("getFocusDistances should not accept a float array with four elements.");
        } catch (IllegalArgumentException e) {
            // expected
        }
        terminateMessageLooper();
    }

    private void checkFocusDistances(Parameters parameters) {
        float[] distances = new float[3];
        parameters.getFocusDistances(distances);

        // Focus distances should be greater than 0.
        assertTrue(distances[Parameters.FOCUS_DISTANCE_NEAR_INDEX] > 0);
        assertTrue(distances[Parameters.FOCUS_DISTANCE_OPTIMAL_INDEX] > 0);
        assertTrue(distances[Parameters.FOCUS_DISTANCE_FAR_INDEX] > 0);

        // Make sure far focus distance >= optimal focus distance >= near focus distance.
        assertTrue(distances[Parameters.FOCUS_DISTANCE_FAR_INDEX] >=
                   distances[Parameters.FOCUS_DISTANCE_OPTIMAL_INDEX]);
        assertTrue(distances[Parameters.FOCUS_DISTANCE_OPTIMAL_INDEX] >=
                   distances[Parameters.FOCUS_DISTANCE_NEAR_INDEX]);

        // Far focus distance should be infinity in infinity focus mode.
        if (Parameters.FOCUS_MODE_INFINITY.equals(parameters.getFocusMode())) {
            assertEquals(Float.POSITIVE_INFINITY,
                         distances[Parameters.FOCUS_DISTANCE_FAR_INDEX]);
        }
    }

    @TestTargets({
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "cancelAutofocus",
            args = {}
        )
    })
    @UiThreadTest
    public void testCancelAutofocus() throws Exception {
        int nCameras = Camera.getNumberOfCameras();
        for (int id = 0; id < nCameras; id++) {
            Log.v(TAG, "Camera id=" + id);
            testCancelAutofocusByCamera(id);
        }
    }

    private void testCancelAutofocusByCamera(int cameraId) throws Exception {
        initializeMessageLooper(cameraId);
        mCamera.setPreviewDisplay(getActivity().getSurfaceView().getHolder());
        mCamera.startPreview();

        // No op if autofocus is not in progress.
        mCamera.cancelAutoFocus();

        // Try to cancel autofocus immediately.
        mCamera.autoFocus(mAutoFocusCallback);
        mCamera.cancelAutoFocus();
        checkFocusDistanceNotChanging();

        // Try to cancel autofocus after it starts for some time.
        mCamera.autoFocus(mAutoFocusCallback);
        Thread.sleep(500);
        mCamera.cancelAutoFocus();
        checkFocusDistanceNotChanging();

        // Try to cancel autofocus after it completes. It should be no op.
        mCamera.autoFocus(mAutoFocusCallback);
        assertTrue(waitForFocusDone());
        mCamera.cancelAutoFocus();

        // Test the case calling cancelAutoFocus and release in a row.
        mCamera.autoFocus(mAutoFocusCallback);
        mCamera.cancelAutoFocus();
        mCamera.release();

        // Ensure the camera can be opened if release is called right after AF.
        mCamera = Camera.open(cameraId);
        mCamera.setPreviewDisplay(getActivity().getSurfaceView().getHolder());
        mCamera.startPreview();
        mCamera.autoFocus(mAutoFocusCallback);
        mCamera.release();

        terminateMessageLooper();
    }

    private void checkFocusDistanceNotChanging() throws Exception {
        float[] distances1 = new float[3];
        float[] distances2 = new float[3];
        Parameters parameters = mCamera.getParameters();
        parameters.getFocusDistances(distances1);
        Thread.sleep(100);
        parameters = mCamera.getParameters();
        parameters.getFocusDistances(distances2);
        assertEquals(distances1[Parameters.FOCUS_DISTANCE_NEAR_INDEX],
                     distances2[Parameters.FOCUS_DISTANCE_NEAR_INDEX]);
        assertEquals(distances1[Parameters.FOCUS_DISTANCE_OPTIMAL_INDEX],
                     distances2[Parameters.FOCUS_DISTANCE_OPTIMAL_INDEX]);
        assertEquals(distances1[Parameters.FOCUS_DISTANCE_FAR_INDEX],
                     distances2[Parameters.FOCUS_DISTANCE_FAR_INDEX]);
    }

    @TestTargets({
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getNumberOfCameras",
            args = {int.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "getCameraInfo",
            args = {int.class, CameraInfo.class}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "open",
            args = {int.class}
        )
    })
    @UiThreadTest
    public void testMultipleCameras() throws Exception {
        int nCameras = Camera.getNumberOfCameras();
        Log.v(TAG, "total " + nCameras + " cameras");
        assertTrue(nCameras >= 0);

        boolean backCameraExist = false;
        CameraInfo info = new CameraInfo();
        for (int i = 0; i < nCameras; i++) {
            Camera.getCameraInfo(i, info);
            if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
                backCameraExist = true;
                break;
            }
        }
        // Make sure original open still works. It must return a back-facing
        // camera.
        mCamera = Camera.open();
        if (mCamera != null) {
            mCamera.release();
            assertTrue(backCameraExist);
        } else {
            assertFalse(backCameraExist);
        }

        for (int id = -1; id <= nCameras; id++) {
            Log.v(TAG, "testing camera #" + id);

            boolean isBadId = (id < 0 || id >= nCameras);

            try {
                Camera.getCameraInfo(id, info);
                if (isBadId) {
                    fail("getCameraInfo should not accept bad cameraId (" + id + ")");
                }
            } catch (RuntimeException e) {
                if (!isBadId) throw e;
            }

            int facing = info.facing;
            int orientation = info.orientation;
            assertTrue(facing == CameraInfo.CAMERA_FACING_BACK ||
                       facing == CameraInfo.CAMERA_FACING_FRONT);
            assertTrue(orientation == 0 || orientation == 90 ||
                       orientation == 180 || orientation == 270);

            Camera camera = null;
            try {
                camera = Camera.open(id);
                if (isBadId) {
                    fail("open() should not accept bad cameraId (" + id + ")");
                }
            } catch (RuntimeException e) {
                if (!isBadId) throw e;
            } finally {
                if (camera != null) {
                    camera.release();
                }
            }
        }
    }

    @UiThreadTest
    public void testPreviewPictureSizesCombination() throws Exception {
        int nCameras = Camera.getNumberOfCameras();
        for (int id = 0; id < nCameras; id++) {
            Log.v(TAG, "Camera id=" + id);
            testPreviewPictureSizesCombinationByCamera(id);
        }
    }

    private void testPreviewPictureSizesCombinationByCamera(int cameraId) throws Exception {
        initializeMessageLooper(cameraId);
        mCamera.setPreviewDisplay(getActivity().getSurfaceView().getHolder());
        Parameters parameters = mCamera.getParameters();
        PreviewCbForPreviewPictureSizesCombination callback =
            new PreviewCbForPreviewPictureSizesCombination();

        // Test combination of preview sizes and picture sizes. Pick four of each to test.
        // Do not test all combinations because it will timeout. Four is just a small number
        // and the test will not timeout.
        List<Size> previewSizes = parameters.getSupportedPreviewSizes();
        List<Size> pictureSizes = parameters.getSupportedPictureSizes();
        int previewSizeTestCount = Math.min(previewSizes.size(), 4);
        int pictureSizeTestCount = Math.min(pictureSizes.size(), 4);
        // Calculate the step so that the first one and the last one are always tested.
        float previewSizeIndexStep = (float) (previewSizes.size() - 1) / (previewSizeTestCount - 1);
        float pictureSizeIndexStep = (float) (pictureSizes.size() - 1) / (pictureSizeTestCount - 1);
        for (int i = 0; i < previewSizeTestCount; i++) {
            for (int j = 0; j < pictureSizeTestCount; j++) {
                Size previewSize = previewSizes.get(Math.round(previewSizeIndexStep * i));
                Size pictureSize = pictureSizes.get(Math.round(pictureSizeIndexStep * j));
                Log.v(TAG, "Test previewSize=(" + previewSize.width + "," +
                        previewSize.height + ") pictureSize=(" +
                        pictureSize.width + "," + pictureSize.height + ")");
                mPreviewCallbackResult = false;
                mCamera.setPreviewCallback(callback);
                callback.expectedPreviewSize = previewSize;
                parameters.setPreviewSize(previewSize.width, previewSize.height);
                parameters.setPictureSize(pictureSize.width, pictureSize.height);
                mCamera.setParameters(parameters);
                assertEquals(previewSize, mCamera.getParameters().getPreviewSize());
                assertEquals(pictureSize, mCamera.getParameters().getPictureSize());

                // Check if the preview size is the same as requested.
                mCamera.startPreview();
                waitForPreviewDone();
                assertTrue(mPreviewCallbackResult);

                // Check if the picture size is the same as requested.
                mCamera.takePicture(mShutterCallback, mRawPictureCallback, mJpegPictureCallback);
                waitForSnapshotDone();
                assertTrue(mJpegPictureCallbackResult);
                assertNotNull(mJpegData);
                BitmapFactory.Options bmpOptions = new BitmapFactory.Options();
                bmpOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeByteArray(mJpegData, 0, mJpegData.length, bmpOptions);
                assertEquals(pictureSize.width, bmpOptions.outWidth);
                assertEquals(pictureSize.height, bmpOptions.outHeight);
            }
        }
        terminateMessageLooper();
    }

    private final class PreviewCbForPreviewPictureSizesCombination
            implements android.hardware.Camera.PreviewCallback {
        public Size expectedPreviewSize;
        public void onPreviewFrame(byte[] data, Camera camera) {
            assertNotNull(data);
            Size size = camera.getParameters().getPreviewSize();
            assertEquals(expectedPreviewSize, size);
            int format = camera.getParameters().getPreviewFormat();
            int bitsPerPixel = ImageFormat.getBitsPerPixel(format);
            assertEquals(size.width * size.height * bitsPerPixel / 8, data.length);
            camera.setPreviewCallback(null);
            mPreviewCallbackResult = true;
            mPreviewDone.open();
        }
    }

    @UiThreadTest
    public void testPreviewFpsRange() throws Exception {
        int nCameras = Camera.getNumberOfCameras();
        for (int id = 0; id < nCameras; id++) {
            Log.v(TAG, "Camera id=" + id);
            testPreviewFpsRangeByCamera(id);
        }
    }

    private void testPreviewFpsRangeByCamera(int cameraId) throws Exception {
        initializeMessageLooper(cameraId);
        mCamera.setPreviewDisplay(getActivity().getSurfaceView().getHolder());

        // Test if the parameters exists and minimum fps <= maximum fps.
        int[] defaultFps = new int[2];
        Parameters parameters = mCamera.getParameters();
        parameters.getPreviewFpsRange(defaultFps);
        List<int[]> fpsList = parameters.getSupportedPreviewFpsRange();
        assertTrue(fpsList.size() > 0);
        boolean found = false;
        for(int[] fps: fpsList) {
            assertTrue(fps[Parameters.PREVIEW_FPS_MIN_INDEX] > 0);
            assertTrue(fps[Parameters.PREVIEW_FPS_MIN_INDEX] <=
                       fps[Parameters.PREVIEW_FPS_MAX_INDEX]);
            if (!found && Arrays.equals(defaultFps, fps)) {
                found = true;
            }
        }
        assertTrue("Preview fps range must be in the supported list.", found);

        // Test if the list is properly sorted.
        for (int i = 0; i < fpsList.size() - 1; i++) {
            int minFps1 = fpsList.get(i)[Parameters.PREVIEW_FPS_MIN_INDEX];
            int maxFps1 = fpsList.get(i)[Parameters.PREVIEW_FPS_MAX_INDEX];
            int minFps2 = fpsList.get(i + 1)[Parameters.PREVIEW_FPS_MIN_INDEX];
            int maxFps2 = fpsList.get(i + 1)[Parameters.PREVIEW_FPS_MAX_INDEX];
            assertTrue(maxFps1 < maxFps2
                    || (maxFps1 == maxFps2 && minFps1 < minFps2));
        }

        // Test if the actual fps is within fps range.
        Size size = parameters.getPreviewSize();
        int format = mCamera.getParameters().getPreviewFormat();
        int bitsPerPixel = ImageFormat.getBitsPerPixel(format);
        byte[] buffer1 = new byte[size.width * size.height * bitsPerPixel / 8];
        byte[] buffer2 = new byte[size.width * size.height * bitsPerPixel / 8];
        byte[] buffer3 = new byte[size.width * size.height * bitsPerPixel / 8];
        FpsRangePreviewCb callback = new FpsRangePreviewCb();
        int[] readBackFps = new int[2];
        for (int[] fps: fpsList) {
            parameters = mCamera.getParameters();
            parameters.setPreviewFpsRange(fps[Parameters.PREVIEW_FPS_MIN_INDEX],
                                          fps[Parameters.PREVIEW_FPS_MAX_INDEX]);
            callback.reset(fps[Parameters.PREVIEW_FPS_MIN_INDEX] / 1000.0,
                           fps[Parameters.PREVIEW_FPS_MAX_INDEX] / 1000.0);
            mCamera.setParameters(parameters);
            parameters = mCamera.getParameters();
            parameters.getPreviewFpsRange(readBackFps);
            MoreAsserts.assertEquals(fps, readBackFps);
            mCamera.addCallbackBuffer(buffer1);
            mCamera.addCallbackBuffer(buffer2);
            mCamera.addCallbackBuffer(buffer3);
            mCamera.setPreviewCallbackWithBuffer(callback);
            mCamera.startPreview();
            try {
                // Test the frame rate for a while.
                Thread.sleep(3000);
            } catch(Exception e) {
                // ignore
            }
            mCamera.stopPreview();
        }

        // Test the invalid fps cases.
        parameters = mCamera.getParameters();
        parameters.setPreviewFpsRange(-1, -1);
        try {
            mCamera.setParameters(parameters);
            fail("Should throw an exception if fps range is negative.");
        } catch (RuntimeException e) {
            // expected
        }
        parameters.setPreviewFpsRange(10, 5);
        try {
            mCamera.setParameters(parameters);
            fail("Should throw an exception if fps range is invalid.");
        } catch (RuntimeException e) {
            // expected
        }

        terminateMessageLooper();
    }

    private final class FpsRangePreviewCb
            implements android.hardware.Camera.PreviewCallback {
        private double mMinFps, mMaxFps, mMaxFrameInterval, mMinFrameInterval;
        // An array storing the arrival time of the frames in the last second.
        private ArrayList<Long> mFrames = new ArrayList<Long>();
        private long firstFrameArrivalTime;

        public void reset(double minFps, double maxFps) {
            this.mMinFps = minFps;
            this.mMaxFps = maxFps;
            mMaxFrameInterval = 1000.0 / mMinFps;
            mMinFrameInterval = 1000.0 / mMaxFps;
            Log.v(TAG, "Min fps=" + mMinFps + ". Max fps=" + mMaxFps
                    + ". Min frame interval=" + mMinFrameInterval
                    + ". Max frame interval=" + mMaxFrameInterval);
            mFrames.clear();
            firstFrameArrivalTime = 0;
        }

        // This method tests if the actual fps is between minimum and maximum.
        // It also tests if the frame interval is too long.
        public void onPreviewFrame(byte[] data, android.hardware.Camera camera) {
            long arrivalTime = System.currentTimeMillis();
            camera.addCallbackBuffer(data);
            if (firstFrameArrivalTime == 0) firstFrameArrivalTime = arrivalTime;

            // Remove the frames that arrived before the last second.
            Iterator<Long> it = mFrames.iterator();
            while(it.hasNext()) {
                long time = it.next();
                if (arrivalTime - time > 1000 && mFrames.size() > 2) {
                    it.remove();
                } else {
                    break;
                }
            }

            // Start the test after one second.
            if (arrivalTime - firstFrameArrivalTime > 1000) {
                assertTrue(mFrames.size() >= 2);

                // Check the frame interval and fps. The interval check
                // considers the time variance passing frames from the camera
                // hardware to the callback. It should be a constant time, not a
                // ratio. The fps check is more strict because individual
                // variance is averaged out.

                // Check if the frame interval is too large or too small.
                double intervalMargin = 30;  // ms
                long lastArrivalTime = mFrames.get(mFrames.size() - 1);
                double interval = arrivalTime - lastArrivalTime;
                if (LOGV) Log.v(TAG, "Frame interval=" + interval);
                assertTrue("Frame interval (" + interval + "ms) is too large." +
                        " mMaxFrameInterval=" + mMaxFrameInterval + "ms",
                        interval < mMaxFrameInterval + intervalMargin);
                assertTrue("Frame interval (" + interval + "ms) is too small." +
                        " mMinFrameInterval=" + mMinFrameInterval + "ms",
                        interval > mMinFrameInterval - intervalMargin);

                // Check if the fps is within range.
                double fpsMargin = 0.03;
                double avgInterval = (double)(arrivalTime - mFrames.get(0))
                        / mFrames.size();
                double fps = 1000.0 / avgInterval;
                assertTrue("Actual fps (" + fps + ") should be larger than " +
                           "min fps (" + mMinFps + ")",
                           fps >= mMinFps * (1 - fpsMargin));
                assertTrue("Actual fps (" + fps + ") should be smaller than " +
                           "max fps (" + mMaxFps + ")",
                           fps <= mMaxFps * (1 + fpsMargin));
            }
            // Add the arrival time of this frame to the list.
            mFrames.add(arrivalTime);
        }
    }

    private void assertEquals(Size expected, Size actual) {
        assertEquals(expected.width, actual.width);
        assertEquals(expected.height, actual.height);
    }

    private void assertNoLetters(String value, String key) {
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            assertFalse("Parameter contains invalid characters. key,value=("
                    + key + "," + value + ")",
                    Character.isLetter(c) && c != 'x');
        }
    }

    @UiThreadTest
    public void testSceneMode() throws Exception {
        int nCameras = Camera.getNumberOfCameras();
        for (int id = 0; id < nCameras; id++) {
            Log.v(TAG, "Camera id=" + id);
            testSceneModeByCamera(id);
        }
    }

    private class SceneModeSettings {
        public String mScene, mFlash, mFocus, mWhiteBalance;
        public List<String> mSupportedFlash, mSupportedFocus, mSupportedWhiteBalance;

        public SceneModeSettings(Parameters parameters) {
            mScene = parameters.getSceneMode();
            mFlash = parameters.getFlashMode();
            mFocus = parameters.getFocusMode();
            mWhiteBalance = parameters.getWhiteBalance();
            mSupportedFlash = parameters.getSupportedFlashModes();
            mSupportedFocus = parameters.getSupportedFocusModes();
            mSupportedWhiteBalance = parameters.getSupportedWhiteBalance();
        }
    }

    private void testSceneModeByCamera(int cameraId) throws Exception {
        initializeMessageLooper(cameraId);
        Parameters parameters = mCamera.getParameters();
        List<String> supportedSceneModes = parameters.getSupportedSceneModes();
        if (supportedSceneModes != null) {
            assertEquals(Parameters.SCENE_MODE_AUTO, parameters.getSceneMode());
            SceneModeSettings autoSceneMode = new SceneModeSettings(parameters);

            // Store all scene mode affected settings.
            SceneModeSettings[] settings = new SceneModeSettings[supportedSceneModes.size()];
            for (int i = 0; i < supportedSceneModes.size(); i++) {
                parameters.setSceneMode(supportedSceneModes.get(i));
                mCamera.setParameters(parameters);
                parameters = mCamera.getParameters();
                settings[i] = new SceneModeSettings(parameters);
            }

            // Make sure scene mode settings are consistent before preview and
            // after preview.
            mCamera.setPreviewDisplay(getActivity().getSurfaceView().getHolder());
            mCamera.startPreview();
            waitForPreviewDone();
            for (int i = 0; i < supportedSceneModes.size(); i++) {
                String sceneMode = supportedSceneModes.get(i);
                parameters.setSceneMode(sceneMode);
                mCamera.setParameters(parameters);
                parameters = mCamera.getParameters();

                // In auto scene mode, camera HAL will not remember the previous
                // flash, focus, and white-balance. It will just take values set
                // by parameters. But the supported flash, focus, and
                // white-balance should still be restored in auto scene mode.
                if (!Parameters.SCENE_MODE_AUTO.equals(sceneMode)) {
                    assertEquals("Flash is inconsistent in scene mode " + sceneMode,
                            settings[i].mFlash, parameters.getFlashMode());
                    assertEquals("Focus is inconsistent in scene mode " + sceneMode,
                            settings[i].mFocus, parameters.getFocusMode());
                    assertEquals("White balance is inconsistent in scene mode " + sceneMode,
                            settings[i].mWhiteBalance, parameters.getWhiteBalance());
                }
                assertEquals("Suppported flash modes are inconsistent in scene mode " + sceneMode,
                        settings[i].mSupportedFlash, parameters.getSupportedFlashModes());
                assertEquals("Suppported focus modes are inconsistent in scene mode " + sceneMode,
                        settings[i].mSupportedFocus, parameters.getSupportedFocusModes());
                assertEquals("Suppported white balance are inconsistent in scene mode " + sceneMode,
                        settings[i].mSupportedWhiteBalance, parameters.getSupportedWhiteBalance());
            }

            for (int i = 0; i < settings.length; i++) {
                if (Parameters.SCENE_MODE_AUTO.equals(settings[i].mScene)) continue;

                // If the scene mode overrides the flash mode, it should also override
                // the supported flash modes.
                if (autoSceneMode.mSupportedFlash != null) {
                    if (!autoSceneMode.mFlash.equals(settings[i].mFlash)) {
                        assertEquals(1, settings[i].mSupportedFlash.size());
                        assertTrue(settings[i].mSupportedFlash.contains(settings[i].mFlash));
                    }
                }

                // If the scene mode overrides the focus mode, it should also override
                // the supported focus modes.
                if (autoSceneMode.mSupportedFocus != null) {
                    if (!autoSceneMode.mFocus.equals(settings[i].mFocus)) {
                        assertEquals(1, settings[i].mSupportedFocus.size());
                        assertTrue(settings[i].mSupportedFocus.contains(settings[i].mFocus));
                    }
                }

                // If the scene mode overrides the white balance, it should also override
                // the supported white balance.
                if (autoSceneMode.mSupportedWhiteBalance != null) {
                    if (!autoSceneMode.mWhiteBalance.equals(settings[i].mWhiteBalance)) {
                        assertEquals(1, settings[i].mSupportedWhiteBalance.size());
                        assertTrue(settings[i].mSupportedWhiteBalance.contains(settings[i].mWhiteBalance));
                    }
                }
            }
        }
        terminateMessageLooper();
    }

    @UiThreadTest
    public void testInvalidParameters() throws Exception {
        int nCameras = Camera.getNumberOfCameras();
        for (int id = 0; id < nCameras; id++) {
            Log.v(TAG, "Camera id=" + id);
            testInvalidParametersByCamera(id);
        }
    }

    private void testInvalidParametersByCamera(int cameraId) throws Exception {
        initializeMessageLooper(cameraId);
        // Test flash mode.
        Parameters parameters = mCamera.getParameters();
        List<String> list = parameters.getSupportedFlashModes();
        if (list != null && list.size() > 0) {
            String original = parameters.getFlashMode();
            parameters.setFlashMode("invalid");
            try {
                mCamera.setParameters(parameters);
                fail("Should throw exception for invalid parameters");
            } catch (RuntimeException e) {
                // expected
            }
            parameters = mCamera.getParameters();
            assertEquals(original, parameters.getFlashMode());
        }

        // Test focus mode.
        String originalFocus = parameters.getFocusMode();
        parameters.setFocusMode("invalid");
        try {
            mCamera.setParameters(parameters);
            fail("Should throw exception for invalid parameters");
        } catch (RuntimeException e) {
            // expected
        }
        parameters = mCamera.getParameters();
        assertEquals(originalFocus, parameters.getFocusMode());

        // Test preview size.
        Size originalSize = parameters.getPreviewSize();
        parameters.setPreviewSize(-1, -1);
        try {
            mCamera.setParameters(parameters);
            fail("Should throw exception for invalid parameters");
        } catch (RuntimeException e) {
            // expected
        }
        parameters = mCamera.getParameters();
        assertEquals(originalSize, parameters.getPreviewSize());

        terminateMessageLooper();
    }

    @UiThreadTest
    public void testGetParameterDuringFocus() throws Exception {
        int nCameras = Camera.getNumberOfCameras();
        for (int id = 0; id < nCameras; id++) {
            Log.v(TAG, "Camera id=" + id);
            testGetParameterDuringFocusByCamera(id);
        }
    }

    private void testGetParameterDuringFocusByCamera(int cameraId) throws Exception {
        initializeMessageLooper(cameraId);
        mCamera.setPreviewDisplay(getActivity().getSurfaceView().getHolder());
        mCamera.startPreview();
        Parameters parameters = mCamera.getParameters();
        for (String focusMode: parameters.getSupportedFocusModes()) {
            if (focusMode.equals(parameters.FOCUS_MODE_AUTO)
                    || focusMode.equals(parameters.FOCUS_MODE_MACRO)) {
                parameters.setFocusMode(focusMode);
                mCamera.setParameters(parameters);
                mCamera.autoFocus(mAutoFocusCallback);
                // This should not crash or throw exception.
                mCamera.getParameters();
                waitForFocusDone();


                mCamera.autoFocus(mAutoFocusCallback);
                // Add a small delay to make sure focus has started.
                Thread.sleep(100);
                // This should not crash or throw exception.
                mCamera.getParameters();
            }
        }
        terminateMessageLooper();
    }

    @UiThreadTest
    public void testPreviewFormats() throws Exception {
        int nCameras = Camera.getNumberOfCameras();
        for (int id = 0; id < nCameras; id++) {
            Log.v(TAG, "Camera id=" + id);
            testPreviewFormatsByCamera(id);
        }
    }

    private void testPreviewFormatsByCamera(int cameraId) throws Exception {
        initializeMessageLooper(cameraId);
        mCamera.setPreviewDisplay(getActivity().getSurfaceView().getHolder());
        Parameters parameters = mCamera.getParameters();
        for (int format: parameters.getSupportedPreviewFormats()) {
            Log.v(TAG, "Test preview format " + format);
            parameters.setPreviewFormat(format);
            mCamera.setParameters(parameters);
            mCamera.setOneShotPreviewCallback(mPreviewCallback);
            mCamera.startPreview();
            waitForPreviewDone();
            assertTrue(mPreviewCallbackResult);
        }
        terminateMessageLooper();
    }

    @UiThreadTest
    public void testMultiCameraRelease() throws Exception {
        // Verify that multiple cameras exist, and that they can be opened at the same time
        if (LOGV) Log.v(TAG, "testMultiCameraRelease: Checking pre-conditions.");
        int nCameras = Camera.getNumberOfCameras();
        if (nCameras < 2) {
            Log.i(TAG, "Test multi-camera release: Skipping test because only 1 camera available");
            return;
        }

        Camera testCamera0 = Camera.open(0);
        Camera testCamera1 = null;
        try {
            testCamera1 = Camera.open(1);
        } catch (RuntimeException e) {
            // Can't open two cameras at once
            Log.i(TAG, "testMultiCameraRelease: Skipping test because only 1 camera "+
                  "could be opened at once. Second open threw: " + e);
            testCamera0.release();
            return;
        }
        testCamera0.release();
        testCamera1.release();

        // Start first camera
        if (LOGV) Log.v(TAG, "testMultiCameraRelease: Opening camera 0");
        initializeMessageLooper(0);
        mCamera.setPreviewDisplay(getActivity().getSurfaceView().getHolder());
        SimplePreviewStreamCb callback0 = new SimplePreviewStreamCb(0);
        mCamera.setPreviewCallback(callback0);
        if (LOGV) Log.v(TAG, "testMultiCameraRelease: Starting preview on camera 0");
        mCamera.startPreview();
        // Run preview for a bit
        for (int f = 0; f < 100; f++) {
            mPreviewDone.close();
            assertTrue("testMultiCameraRelease: First camera preview timed out on frame " + f + "!",
                       mPreviewDone.block( WAIT_FOR_COMMAND_TO_COMPLETE));
        }
        if (LOGV) Log.v(TAG, "testMultiCameraRelease: Stopping preview on camera 0");
        mCamera.stopPreview();
        // Save message looper and camera to deterministically release them, instead
        // of letting GC do it at some point.
        Camera firstCamera = mCamera;
        Looper firstLooper = mLooper;
        //terminateMessageLooper(); // Intentionally not calling this

        // Start second camera without releasing the first one (will
        // set mCamera and mLooper to new objects)
        if (LOGV) Log.v(TAG, "testMultiCameraRelease: Opening camera 1");
        initializeMessageLooper(1);
        mCamera.setPreviewDisplay(getActivity().getSurfaceView().getHolder());
        SimplePreviewStreamCb callback1 = new SimplePreviewStreamCb(1);
        mCamera.setPreviewCallback(callback1);
        if (LOGV) Log.v(TAG, "testMultiCameraRelease: Starting preview on camera 1");
        mCamera.startPreview();
        // Run preview for a bit - GC of first camera instance should not impact the second's
        // operation.
        for (int f = 0; f < 100; f++) {
            mPreviewDone.close();
            assertTrue("testMultiCameraRelease: Second camera preview timed out on frame " + f + "!",
                       mPreviewDone.block( WAIT_FOR_COMMAND_TO_COMPLETE));
            if (f == 50) {
                // Release first camera mid-preview, should cause no problems
                if (LOGV) Log.v(TAG, "testMultiCameraRelease: Releasing camera 0");
                firstCamera.release();
            }
        }
        if (LOGV) Log.v(TAG, "testMultiCameraRelease: Stopping preview on camera 0");
        mCamera.stopPreview();

        firstLooper.quit();
        terminateMessageLooper();
    }

    // This callback just signals on the condition variable, making it useful for checking that
    // preview callbacks don't stop unexpectedly
    private final class SimplePreviewStreamCb
            implements android.hardware.Camera.PreviewCallback {
        private int mId;
        public SimplePreviewStreamCb(int id) {
            mId = id;
        }
        public void onPreviewFrame(byte[] data, android.hardware.Camera camera) {
            if (LOGV) Log.v(TAG, "Preview frame callback, id " + mId + ".");
            mPreviewDone.open();
        }
    }

    @UiThreadTest
    public void testFocusAreas() throws Exception {
        int nCameras = Camera.getNumberOfCameras();
        for (int id = 0; id < nCameras; id++) {
            Log.v(TAG, "Camera id=" + id);

            initializeMessageLooper(id);
            Parameters parameters = mCamera.getParameters();
            int maxNumFocusAreas = parameters.getMaxNumFocusAreas();
            assertTrue(maxNumFocusAreas >= 0);
            if (maxNumFocusAreas > 0) {
                List<String> focusModes = parameters.getSupportedFocusModes();
                assertTrue(focusModes.contains(Parameters.FOCUS_MODE_AUTO));
                testAreas(FOCUS_AREA, maxNumFocusAreas);
            }
            terminateMessageLooper();
        }
    }

    @UiThreadTest
    public void testMeteringAreas() throws Exception {
        int nCameras = Camera.getNumberOfCameras();
        for (int id = 0; id < nCameras; id++) {
            Log.v(TAG, "Camera id=" + id);
            initializeMessageLooper(id);
            Parameters parameters = mCamera.getParameters();
            int maxNumMeteringAreas = parameters.getMaxNumMeteringAreas();
            assertTrue(maxNumMeteringAreas >= 0);
            if (maxNumMeteringAreas > 0) {
                testAreas(METERING_AREA, maxNumMeteringAreas);
            }
            terminateMessageLooper();
        }
    }

    private void testAreas(int type, int maxNumAreas) throws Exception {
        mCamera.setPreviewDisplay(getActivity().getSurfaceView().getHolder());
        mCamera.startPreview();

        // Test various valid cases.
        testValidAreas(type, null);                                  // the default area
        testValidAreas(type, makeAreas(-1000, -1000, 1000, 1000, 1)); // biggest area
        testValidAreas(type, makeAreas(-500, -500, 500, 500, 1000)); // medium area & biggest weight
        testValidAreas(type, makeAreas(0, 0, 1, 1, 1));              // smallest area

        ArrayList<Area> areas = new ArrayList();
        if (maxNumAreas > 1) {
            // Test overlapped areas.
            testValidAreas(type, makeAreas(-250, -250, 250, 250, 1, 0, 0, 500, 500, 2));
            // Test completely disjoint areas.
            testValidAreas(type, makeAreas(-250, -250, 0, 0, 1, 900, 900, 1000, 1000, 1));
            // Test the maximum number of areas.
            testValidAreas(type, makeAreas(-1000, -1000, 1000, 1000, 1000, maxNumAreas));
        }

        // Test various invalid cases.
        testInvalidAreas(type, makeAreas(-1001, -1000, 1000, 1000, 1));    // left should >= -1000
        testInvalidAreas(type, makeAreas(-1000, -1001, 1000, 1000, 1));    // top should >= -1000
        testInvalidAreas(type, makeAreas(-1000, -1000, 1001, 1000, 1));    // right should <= 1000
        testInvalidAreas(type, makeAreas(-1000, -1000, 1000, 1001, 1));    // bottom should <= 1000
        testInvalidAreas(type, makeAreas(-1000, -1000, 1000, 1000, 0));    // weight should >= 1
        testInvalidAreas(type, makeAreas(-1000, -1000, 1000, 1001, 1001)); // weight should <= 1000
        testInvalidAreas(type, makeAreas(500, -1000, 500, 1000, 1));       // left should < right
        testInvalidAreas(type, makeAreas(-1000, 500, 1000, 500, 1));       // top should < bottom
        testInvalidAreas(type, makeAreas(500, -1000, 499, 1000, 1));       // left should < right
        testInvalidAreas(type, makeAreas(-1000, 500, 100, 499, 1));        // top should < bottom
        testInvalidAreas(type, makeAreas(-250, -250, 250, 250, -1));       // weight should >= 1
        // Test when the number of areas exceeds maximum.
        testInvalidAreas(type, makeAreas(-1000, -1000, 1000, 1000, 1000, maxNumAreas + 1));
    }

    private static ArrayList<Area> makeAreas(int left, int top, int right, int bottom, int weight) {
        ArrayList<Area> areas = new ArrayList<Area>();
        areas.add(new Area(new Rect(left, top, right, bottom), weight));
        return areas;
    }

    private static ArrayList<Area> makeAreas(int left, int top, int right, int bottom,
            int weight, int number) {
        ArrayList<Area> areas = new ArrayList<Area>();
        for (int i = 0; i < number; i++) {
            areas.add(new Area(new Rect(left, top, right, bottom), weight));
        }
        return areas;
    }

    private static ArrayList<Area> makeAreas(int left1, int top1, int right1,
            int bottom1, int weight1, int left2, int top2, int right2,
            int bottom2, int weight2) {
        ArrayList<Area> areas = new ArrayList<Area>();
        areas.add(new Area(new Rect(left1, top1, right1, bottom1), weight1));
        areas.add(new Area(new Rect(left2, top2, right2, bottom2), weight2));
        return areas;
    }

    private void testValidAreas(int areaType, ArrayList<Area> areas) {
        if (areaType == FOCUS_AREA) {
            testValidFocusAreas(areas);
        } else {
            testValidMeteringAreas(areas);
        }
    }

    private void testInvalidAreas(int areaType, ArrayList<Area> areas) {
        if (areaType == FOCUS_AREA) {
            testInvalidFocusAreas(areas);
        } else {
            testInvalidMeteringAreas(areas);
        }
    }

    private void testValidFocusAreas(ArrayList<Area> areas) {
        Parameters parameters = mCamera.getParameters();
        parameters.setFocusAreas(areas);
        mCamera.setParameters(parameters);
        parameters = mCamera.getParameters();
        assertEquals(areas, parameters.getFocusAreas());
        mCamera.autoFocus(mAutoFocusCallback);
        waitForFocusDone();
    }

    private void testInvalidFocusAreas(ArrayList<Area> areas) {
        Parameters parameters = mCamera.getParameters();
        List<Area> originalAreas = parameters.getFocusAreas();
        try {
            parameters.setFocusAreas(areas);
            mCamera.setParameters(parameters);
            fail("Should throw exception when focus area is invalid.");
        } catch (RuntimeException e) {
            parameters = mCamera.getParameters();
            assertEquals(originalAreas, parameters.getFocusAreas());
        }
    }

    private void testValidMeteringAreas(ArrayList<Area> areas) {
        Parameters parameters = mCamera.getParameters();
        parameters.setMeteringAreas(areas);
        mCamera.setParameters(parameters);
        parameters = mCamera.getParameters();
        assertEquals(areas, parameters.getMeteringAreas());
    }

    private void testInvalidMeteringAreas(ArrayList<Area> areas) {
        Parameters parameters = mCamera.getParameters();
        List<Area> originalAreas = parameters.getMeteringAreas();
        try {
            parameters.setMeteringAreas(areas);
            mCamera.setParameters(parameters);
            fail("Should throw exception when metering area is invalid.");
        } catch (RuntimeException e) {
            parameters = mCamera.getParameters();
            assertEquals(originalAreas, parameters.getMeteringAreas());
        }
    }
}
