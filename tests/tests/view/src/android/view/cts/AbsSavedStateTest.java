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

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SmallTest
public class AbsSavedStateTest extends InstrumentationTestCase {

    public void testConstructor() {
        try {
            new AbsSavedStateImpl((Parcelable) null);
            fail("Expected NullPointerException");
        } catch (IllegalArgumentException e) {
            // Expected.
        }

        try {
            new AbsSavedStateImpl((Parcel) null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected.
        }

        try {
            new AbsSavedStateImpl(null, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected.
        }

        AbsSavedState superState = new AbsSavedStateImpl(Parcel.obtain());
        assertNull(superState.getSuperState());

        AbsSavedState s = new AbsSavedStateImpl(superState);
        assertSame(superState, s.getSuperState());

        Parcel source = Parcel.obtain();
        source.writeParcelable(superState, 0);
        source.setDataPosition(0);
        s = new AbsSavedStateImpl(source);
        assertTrue(s.getSuperState() instanceof AbsSavedState);

        source = Parcel.obtain();
        s = new AbsSavedStateImpl(source);
        assertSame(AbsSavedState.EMPTY_STATE, s.getSuperState());

        ClassLoader loader = AbsSavedState.class.getClassLoader();
        source = Parcel.obtain();
        source.writeParcelable(superState, 0);
        source.setDataPosition(0);
        s = new AbsSavedStateImpl(source, loader);
        assertTrue(s.getSuperState() instanceof AbsSavedState);

        source = Parcel.obtain();
        s = new AbsSavedStateImpl(source, loader);
        assertSame(AbsSavedState.EMPTY_STATE, s.getSuperState());
    }

    public void testCreator() {
        int size = 10;
        AbsSavedState[] array = AbsSavedState.CREATOR.newArray(size);
        assertNotNull(array);
        assertEquals(size, array.length);
        for (AbsSavedState state : array) {
            assertNull(state);
        }

        AbsSavedState state = new AbsSavedStateImpl(AbsSavedState.EMPTY_STATE);
        Parcel parcel = Parcel.obtain();
        state.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        AbsSavedState unparceled = AbsSavedState.CREATOR.createFromParcel(parcel);
        assertNotNull(unparceled);
        assertEquals(AbsSavedState.EMPTY_STATE, unparceled.getSuperState());

        AbsSavedState stateWithSuper = new AbsSavedStateImpl(state);
        parcel = Parcel.obtain();
        stateWithSuper.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        try {
            AbsSavedState.CREATOR.createFromParcel(parcel);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected.
        }
    }

    public void testWriteToParcel() {
        Parcelable superState = mock(Parcelable.class);
        AbsSavedState savedState = new AbsSavedStateImpl(superState);
        Parcel dest = Parcel.obtain();
        int flags = 2;
        savedState.writeToParcel(dest, flags);
        verify(superState).writeToParcel(eq(dest), eq(flags));
    }

    private static class AbsSavedStateImpl extends AbsSavedState {
        AbsSavedStateImpl(Parcelable superState) {
            super(superState);
        }

        AbsSavedStateImpl(Parcel source) {
            super(source);
        }

        AbsSavedStateImpl(Parcel source, ClassLoader loader) {
            super(source, loader);
        }

        public static final Creator<AbsSavedStateImpl> CREATOR = new Creator<AbsSavedStateImpl>() {
            @Override
            public AbsSavedStateImpl createFromParcel(Parcel source) {
                return new AbsSavedStateImpl(source);
            }

            @Override
            public AbsSavedStateImpl[] newArray(int size) {
                return new AbsSavedStateImpl[size];
            }
        };
    }
}
