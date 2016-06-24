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

package android.view.cts;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.test.filters.SmallTest;
import android.test.InstrumentationTestCase;
import android.view.AbsSavedState;
import android.view.View.BaseSavedState;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SmallTest
public class View_BaseSavedStateTest extends InstrumentationTestCase {

    public void testConstructors() {
        try {
            new BaseSavedState((Parcelable) null);
            fail("Expected NullPointerException");
        } catch (IllegalArgumentException e) {
            // Expected.
        }

        try {
            new BaseSavedState((Parcel) null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected.
        }

        try {
            new BaseSavedState(null, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected.
        }

        BaseSavedState superState = new BaseSavedState(Parcel.obtain());
        assertEquals(AbsSavedState.EMPTY_STATE, superState.getSuperState());

        BaseSavedState s = new BaseSavedState(superState);
        assertEquals(superState, s.getSuperState());

        Parcel source = Parcel.obtain();
        source.writeParcelable(superState, 0);
        source.setDataPosition(0);
        s = new BaseSavedState(source);
        assertTrue(s.getSuperState() instanceof BaseSavedState);

        ClassLoader loader = BaseSavedState.class.getClassLoader();
        source = Parcel.obtain();
        source.writeParcelable(superState, 0);
        source.setDataPosition(0);
        s = new BaseSavedState(source, loader);
        assertTrue(s.getSuperState() instanceof BaseSavedState);
    }

    public void testCreator() {
        int size = 10;
        BaseSavedState[] array = BaseSavedState.CREATOR.newArray(size);
        assertNotNull(array);
        assertEquals(size, array.length);
        for (BaseSavedState state : array) {
            assertNull(state);
        }

        BaseSavedState state = new BaseSavedState(AbsSavedState.EMPTY_STATE);
        Parcel parcel = Parcel.obtain();
        state.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        BaseSavedState unparceled = BaseSavedState.CREATOR.createFromParcel(parcel);
        assertNotNull(unparceled);
        assertEquals(AbsSavedState.EMPTY_STATE, unparceled.getSuperState());
    }

    public void testWriteToParcel() {
        Parcelable superState = mock(Parcelable.class);
        BaseSavedState savedState = new BaseSavedState(superState);
        Parcel dest = Parcel.obtain();
        int flags = 2;
        savedState.writeToParcel(dest, flags);
        verify(superState).writeToParcel(eq(dest), eq(flags));
    }
}
