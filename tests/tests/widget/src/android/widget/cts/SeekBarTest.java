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

package android.widget.cts;

import android.app.Activity;
import android.app.Instrumentation;
import android.os.SystemClock;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.MotionEvent;
import android.widget.SeekBar;

import static org.mockito.Mockito.*;

/**
 * Test {@link SeekBar}.
 */
@SmallTest
public class SeekBarTest extends ActivityInstrumentationTestCase2<SeekBarCtsActivity> {
    private SeekBar mSeekBar;

    private Activity mActivity;

    private Instrumentation mInstrumentation;

    public SeekBarTest() {
        super("android.widget.cts", SeekBarCtsActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mInstrumentation = getInstrumentation();
        mActivity = getActivity();
        mSeekBar = (SeekBar) mActivity.findViewById(R.id.seekBar);
    }

    public void testConstructor() {
        new SeekBar(mActivity);

        new SeekBar(mActivity, null);

        new SeekBar(mActivity, null, android.R.attr.seekBarStyle);

        new SeekBar(mActivity, null, 0, android.R.style.Widget_Material_Light_SeekBar);
    }

    public void testSetOnSeekBarChangeListener() {
        SeekBar.OnSeekBarChangeListener mockChangeListener =
                mock(SeekBar.OnSeekBarChangeListener.class);

        mSeekBar.setOnSeekBarChangeListener(mockChangeListener);
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();
        int seekBarXY[] = new int[2];
        mSeekBar.getLocationInWindow(seekBarXY);
        MotionEvent event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN,
                seekBarXY[0], seekBarXY[1], 0);
        mInstrumentation.sendPointerSync(event);
        mInstrumentation.waitForIdleSync();
        verify(mockChangeListener, times(1)).onStartTrackingTouch(mSeekBar);
        // while starting to track, the progress is changed also
        verify(mockChangeListener, atLeastOnce()).onProgressChanged(eq(mSeekBar), anyInt(),
                eq(true));

        reset(mockChangeListener);
        downTime = SystemClock.uptimeMillis();
        eventTime = SystemClock.uptimeMillis();
        event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE,
                seekBarXY[0] + (mSeekBar.getWidth() >> 1), seekBarXY[1], 0);
        mInstrumentation.sendPointerSync(event);
        mInstrumentation.waitForIdleSync();
        verify(mockChangeListener, atLeastOnce()).onProgressChanged(eq(mSeekBar), anyInt(),
                eq(true));

        reset(mockChangeListener);
        downTime = SystemClock.uptimeMillis();
        eventTime = SystemClock.uptimeMillis();
        event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP,
                seekBarXY[0] + (mSeekBar.getWidth() >> 1), seekBarXY[1], 0);
        mInstrumentation.sendPointerSync(event);
        mInstrumentation.waitForIdleSync();
        verify(mockChangeListener, times(1)).onStopTrackingTouch(mSeekBar);
    }
}
