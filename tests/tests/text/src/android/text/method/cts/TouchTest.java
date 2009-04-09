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

package android.text.method.cts;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.ToBeFixed;

import android.app.Activity;
import android.os.SystemClock;
import android.test.ActivityInstrumentationTestCase2;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.Touch;
import android.view.MotionEvent;
import android.widget.TextView;

@TestTargetClass(Touch.class)
public class TouchTest extends ActivityInstrumentationTestCase2<StubActivity> {
    private Activity mActivity;
    private static final String LONG_TEXT = "Scrolls the specified widget to the specified " +
            "coordinates, except constrains the X scrolling position to the horizontal regions " +
            "of the text that will be visible after scrolling to the specified Y position.";
    private boolean mReturnFromTouchEvent;

    public TouchTest() {
        super("com.android.cts.stub", StubActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = getActivity();
    }

    @TestTargetNew(
        level = TestLevel.COMPLETE,
        notes = "Test scrollTo(TextView widget, Layout layout, int x, int y).",
        method = "scrollTo",
        args = {TextView.class, Layout.class, int.class, int.class}
    )
    public void testScrollTo() {
        final TextView tv = new TextView(mActivity);
        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                mActivity.setContentView(tv);
                tv.setSingleLine(true);
                tv.setLines(2);
            }
        });
        getInstrumentation().waitForIdleSync();
        TextPaint paint = tv.getPaint();
        final Layout layout = tv.getLayout();

        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                tv.setText(LONG_TEXT);
            }
        });
        getInstrumentation().waitForIdleSync();

        // get the total length of string
        final int width = getTextWidth(LONG_TEXT, paint);

        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                Touch.scrollTo(tv, layout, width - tv.getWidth() - 1, 0);
            }
        });
        getInstrumentation().waitForIdleSync();
        assertEquals(width - tv.getWidth() - 1, tv.getScrollX());
        assertEquals(0, tv.getScrollY());

        // the X to which scroll is greater than the total length of string.
        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                Touch.scrollTo(tv, layout, width + 100, 5);
            }
        });
        getInstrumentation().waitForIdleSync();
        assertEquals(width - tv.getWidth(), tv.getScrollX());
        assertEquals(5, tv.getScrollY());

        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                Touch.scrollTo(tv, layout, width - 10, 5);
            }
        });
        getInstrumentation().waitForIdleSync();
        assertEquals(width - tv.getWidth(), tv.getScrollX());
        assertEquals(5, tv.getScrollY());
    }

    @TestTargetNew(
        level = TestLevel.COMPLETE,
        notes = "Test onTouchEvent(TextView widget, Spannable buffer, MotionEvent event).",
        method = "onTouchEvent",
        args = {TextView.class, Spannable.class, MotionEvent.class}
    )
    @ToBeFixed(bug = "1695243", explanation = "Android API javadocs are incomplete, " +
            "should add @throws clause into javadoc.")
    public void testOnTouchEvent() {
        final SpannableString spannable = new SpannableString(LONG_TEXT);
        final TextView tv = new TextView(mActivity);
        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                mActivity.setContentView(tv);
                tv.setSingleLine(true);
                tv.setText(LONG_TEXT);
            }
        });
        getInstrumentation().waitForIdleSync();

        TextPaint paint = tv.getPaint();
        final int width = getTextWidth(LONG_TEXT, paint);
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();
        int x = width >> 1;
        final MotionEvent event1 = MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_DOWN, x, 0, 0);
        final MotionEvent event2 = MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_MOVE, 0, 0, 0);
        final MotionEvent event3 = MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_UP, 0, 0, 0);
        assertEquals(0, tv.getScrollX());
        assertEquals(0, tv.getScrollY());
        mReturnFromTouchEvent = false;
        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                mReturnFromTouchEvent = Touch.onTouchEvent(tv, spannable, event1);
            }
        });
        getInstrumentation().waitForIdleSync();
        assertTrue(mReturnFromTouchEvent);
        // TextView has not been scrolled.
        assertEquals(0, tv.getScrollX());
        assertEquals(0, tv.getScrollY());

        mReturnFromTouchEvent = false;
        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                mReturnFromTouchEvent = Touch.onTouchEvent(tv, spannable, event2);
            }
        });
        getInstrumentation().waitForIdleSync();
        assertTrue(mReturnFromTouchEvent);
        // TextView has been scrolled.
        assertEquals(x, tv.getScrollX());
        assertEquals(0, tv.getScrollY());

        mReturnFromTouchEvent = false;
        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                mReturnFromTouchEvent = Touch.onTouchEvent(tv, spannable, event3);
            }
        });
        getInstrumentation().waitForIdleSync();
        assertTrue(mReturnFromTouchEvent);
        // TextView has not been scrolled.
        assertEquals(x, tv.getScrollX());
        assertEquals(0, tv.getScrollY());
    }

    private int getTextWidth(String str, TextPaint paint) {
        float totalWidth = 0f;
        float[] widths = new float[str.length()];
        paint.getTextWidths(str, widths);
        for (float f : widths) {
            totalWidth += f;
        }
        return (int) totalWidth;
    }
}
