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

package android.text.style.cts;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;
import dalvik.annotation.ToBeFixed;

import android.os.Parcel;
import android.text.TextPaint;
import android.text.style.SuperscriptSpan;

import junit.framework.TestCase;

@TestTargetClass(SuperscriptSpan.class)
public class SuperscriptSpanTest extends TestCase {
    @TestTargets({
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "SuperscriptSpan",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "SuperscriptSpan",
            args = {android.os.Parcel.class}
        )
    })
    public void testConstructor() {
        SuperscriptSpan superscriptSpan = new SuperscriptSpan();

        Parcel p = Parcel.obtain();
        try {
            superscriptSpan.writeToParcel(p, 0);
            p.setDataPosition(0);
            new SuperscriptSpan(p);
        } finally {
            p.recycle();
        }
    }

    @TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "updateMeasureState",
        args = {android.text.TextPaint.class}
    )
    @ToBeFixed(bug="1695243", explanation="miss javadoc")
    public void testUpdateMeasureState() {
        // the expected result is: tp.baselineShift += (int) (tp.ascent() / 2)
        SuperscriptSpan superscriptSpan = new SuperscriptSpan();

        TextPaint tp = new TextPaint();
        float ascent = tp.ascent();
        int baselineShift = 100;
        tp.baselineShift = baselineShift;

        superscriptSpan.updateMeasureState(tp);
        assertEquals(baselineShift + (int) (ascent / 2), tp.baselineShift);

        try {
            superscriptSpan.updateMeasureState(null);
            fail("should throw NullPointerException.");
        } catch (NullPointerException e) {
            // expected, test success.
        }
    }

    @TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "updateDrawState",
        args = {android.text.TextPaint.class}
    )
    @ToBeFixed(bug="1695243", explanation="miss javadoc")
    public void testUpdateDrawState() {
        // the expected result is: tp.baselineShift += (int) (tp.ascent() / 2)
        SuperscriptSpan superscriptSpan = new SuperscriptSpan();

        TextPaint tp = new TextPaint();
        float ascent = tp.ascent();
        int baselineShift = 50;
        tp.baselineShift = baselineShift;

        superscriptSpan.updateDrawState(tp);
        assertEquals(baselineShift + (int) (ascent / 2), tp.baselineShift);

        try {
            superscriptSpan.updateDrawState(null);
            fail("should throw NullPointerException.");
        } catch (NullPointerException e) {
            // expected, test success.
        }
    }

    @TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "describeContents",
        args = {}
    )
    public void testDescribeContents() {
        SuperscriptSpan superscriptSpan = new SuperscriptSpan();
        superscriptSpan.describeContents();
    }

    @TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "getSpanTypeId",
        args = {}
    )
    public void testGetSpanTypeId() {
        SuperscriptSpan superscriptSpan = new SuperscriptSpan();
        superscriptSpan.getSpanTypeId();
    }

    @TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "writeToParcel",
        args = {Parcel.class, int.class}
    )
    public void testWriteToParcel() {
        Parcel p = Parcel.obtain();
        try {
            SuperscriptSpan superscriptSpan = new SuperscriptSpan();
            superscriptSpan.writeToParcel(p, 0);
            p.setDataPosition(0);
            new SuperscriptSpan(p);
        } finally {
            p.recycle();
        }
    }
}
