/*
 * Copyright (C) 2009 The Android Open Source Project
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

package android.widget.cts;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.cts.util.MediaUtils;
import android.media.MediaPlayer;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.util.Log;
import android.view.View.MeasureSpec;
import android.widget.MediaController;
import android.widget.VideoView;

import org.mockito.invocation.InvocationOnMock;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Test {@link VideoView}.
 */
public class VideoViewTest extends ActivityInstrumentationTestCase2<VideoViewCtsActivity> {
    /** Debug TAG. **/
    private static final String TAG = "VideoViewTest";
    /** The maximum time to wait for an operation. */
    private static final long   TIME_OUT = 15000L;
    /** The interval time to wait for completing an operation. */
    private static final long   OPERATION_INTERVAL  = 1500L;
    /** The duration of R.raw.testvideo. */
    private static final int    TEST_VIDEO_DURATION = 11047;
    /** The full name of R.raw.testvideo. */
    private static final String VIDEO_NAME   = "testvideo.3gp";
    /** delta for duration in case user uses different decoders on different
        hardware that report a duration that's different by a few milliseconds */
    private static final int DURATION_DELTA = 100;

    private VideoView mVideoView;
    private Activity mActivity;
    private Instrumentation mInstrumentation;
    private String mVideoPath;

    private boolean hasCodec() {
        return MediaUtils.hasCodecsForResource(mActivity, R.raw.testvideo);
    }

    /**
     * Instantiates a new video view test.
     */
    public VideoViewTest() {
        super("android.widget.cts", VideoViewCtsActivity.class);
    }

    /**
     * Find the video view specified by id.
     *
     * @param id the id
     * @return the video view
     */
    private VideoView findVideoViewById(int id) {
        return (VideoView) mActivity.findViewById(id);
    }

    private String prepareSampleVideo() throws IOException {
        InputStream source = null;
        OutputStream target = null;

        try {
            source = mActivity.getResources().openRawResource(R.raw.testvideo);
            target = mActivity.openFileOutput(VIDEO_NAME, Context.MODE_PRIVATE);

            final byte[] buffer = new byte[1024];
            for (int len = source.read(buffer); len > 0; len = source.read(buffer)) {
                target.write(buffer, 0, len);
            }
        } finally {
            if (source != null) {
                source.close();
            }
            if (target != null) {
                target.close();
            }
        }

        return mActivity.getFileStreamPath(VIDEO_NAME).getAbsolutePath();
    }

    /**
     * Wait for an asynchronous media operation complete.
     * @throws InterruptedException
     */
    private void waitForOperationComplete() throws InterruptedException {
        Thread.sleep(OPERATION_INTERVAL);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = getActivity();
        mInstrumentation = getInstrumentation();
        mVideoPath = prepareSampleVideo();
        assertNotNull(mVideoPath);
        mVideoView = findVideoViewById(R.id.videoview);
    }

    private void makeVideoView() {
        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                MediaController mediaController = new MediaController(mActivity);
                mVideoView.setMediaController(mediaController);
            }
        });
        mInstrumentation.waitForIdleSync();
    }

    @UiThreadTest
    public void testConstructor() {
        new VideoView(mActivity);

        new VideoView(mActivity, null);

        new VideoView(mActivity, null, 0);
    }

    public void testPlayVideo1() throws Throwable {
        makeVideoView();
        // Don't run the test if the codec isn't supported.
        if (!hasCodec()) {
            Log.i(TAG, "SKIPPING testPlayVideo1(): codec is not supported");
            return;
        }

        final MediaPlayer.OnPreparedListener mockPreparedListener =
                mock(MediaPlayer.OnPreparedListener.class);
        final CountDownLatch preparedLatch = new CountDownLatch(1);
        doAnswer((InvocationOnMock invocation) -> {
            preparedLatch.countDown();
            return null;
        }).when(mockPreparedListener).onPrepared(any(MediaPlayer.class));
        mVideoView.setOnPreparedListener(mockPreparedListener);

        final MediaPlayer.OnCompletionListener mockCompletionListener =
                mock(MediaPlayer.OnCompletionListener.class);
        final CountDownLatch completionLatch = new CountDownLatch(1);
        doAnswer((InvocationOnMock invocation) -> {
            completionLatch.countDown();
            return null;
        }).when(mockCompletionListener).onCompletion(any(MediaPlayer.class));
        mVideoView.setOnCompletionListener(mockCompletionListener);

        runTestOnUiThread(() -> mVideoView.setVideoPath(mVideoPath));
        preparedLatch.await(TIME_OUT, TimeUnit.MILLISECONDS);
        verify(mockPreparedListener, times(1)).onPrepared(any(MediaPlayer.class));
        verifyZeroInteractions(mockCompletionListener);

        runTestOnUiThread(mVideoView::start);
        // wait time is longer than duration in case system is sluggish
        completionLatch.await(mVideoView.getDuration() + TIME_OUT, TimeUnit.MILLISECONDS);
        verify(mockCompletionListener, times(1)).onCompletion(any(MediaPlayer.class));
    }

    public void testSetOnErrorListener() throws Throwable {
        makeVideoView();

        final MediaPlayer.OnErrorListener mockErrorListener =
                mock(MediaPlayer.OnErrorListener.class);
        final CountDownLatch errorLatch = new CountDownLatch(1);
        doAnswer((InvocationOnMock invocation) -> {
            errorLatch.countDown();
            return null;
        }).when(mockErrorListener).onError(any(MediaPlayer.class), anyInt(), anyInt());
        mVideoView.setOnErrorListener(mockErrorListener);

        runTestOnUiThread(new Runnable() {
            public void run() {
                String path = "unknown path";
                mVideoView.setVideoPath(path);
                mVideoView.start();
            }
        });
        mInstrumentation.waitForIdleSync();

        errorLatch.await(TIME_OUT, TimeUnit.MILLISECONDS);
        verify(mockErrorListener, times(1)).onError(any(MediaPlayer.class), anyInt(), anyInt());
    }

    public void testGetBufferPercentage() throws Throwable {
        makeVideoView();
        // Don't run the test if the codec isn't supported.
        if (!hasCodec()) {
            Log.i(TAG, "SKIPPING testGetBufferPercentage(): codec is not supported");
            return;
        }

        final MediaPlayer.OnPreparedListener mockPreparedListener =
                mock(MediaPlayer.OnPreparedListener.class);
        final CountDownLatch preparedLatch = new CountDownLatch(1);
        doAnswer((InvocationOnMock invocation) -> {
            preparedLatch.countDown();
            return null;
        }).when(mockPreparedListener).onPrepared(any(MediaPlayer.class));
        mVideoView.setOnPreparedListener(mockPreparedListener);

        runTestOnUiThread(() -> mVideoView.setVideoPath(mVideoPath));
        mInstrumentation.waitForIdleSync();

        preparedLatch.await(TIME_OUT, TimeUnit.MILLISECONDS);
        verify(mockPreparedListener, times(1)).onPrepared(any(MediaPlayer.class));
        int percent = mVideoView.getBufferPercentage();
        assertTrue(percent >= 0 && percent <= 100);
    }

    @UiThreadTest
    public void testResolveAdjustedSize() {
        mVideoView = new VideoView(mActivity);

        final int desiredSize = 100;
        int resolvedSize = mVideoView.resolveAdjustedSize(desiredSize, MeasureSpec.UNSPECIFIED);
        assertEquals(desiredSize, resolvedSize);

        final int specSize = MeasureSpec.getSize(MeasureSpec.AT_MOST);
        resolvedSize = mVideoView.resolveAdjustedSize(desiredSize, MeasureSpec.AT_MOST);
        assertEquals(Math.min(desiredSize, specSize), resolvedSize);

        resolvedSize = mVideoView.resolveAdjustedSize(desiredSize, MeasureSpec.EXACTLY);
        assertEquals(specSize, resolvedSize);
    }

    public void testGetDuration() throws Throwable {
        // Don't run the test if the codec isn't supported.
        if (!hasCodec()) {
            Log.i(TAG, "SKIPPING testGetDuration(): codec is not supported");
            return;
        }

        runTestOnUiThread(() -> mVideoView.setVideoPath(mVideoPath));
        waitForOperationComplete();
        assertTrue(Math.abs(mVideoView.getDuration() - TEST_VIDEO_DURATION) < DURATION_DELTA);
    }

    @UiThreadTest
    public void testSetMediaController() {
        final MediaController ctlr = new MediaController(mActivity);
        mVideoView.setMediaController(ctlr);
    }
}
