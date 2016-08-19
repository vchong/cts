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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import android.cts.util.CtsKeyEventUtil;
import android.support.test.filters.MediumTest;
import android.support.test.runner.AndroidJUnit4;
import android.text.InputType;
import android.text.method.DateTimeKeyListener;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test {@link android.text.method.DateTimeKeyListener}.
 */
@MediumTest
@RunWith(AndroidJUnit4.class)
public class DateTimeKeyListenerTest extends KeyListenerTestCase {
    @Test
    public void testConstructor() {
        new DateTimeKeyListener();
    }

    @Test
    public void testGetInstance() {
        DateTimeKeyListener listener1 = DateTimeKeyListener.getInstance();
        DateTimeKeyListener listener2 = DateTimeKeyListener.getInstance();

        assertNotNull(listener1);
        assertNotNull(listener2);
        assertSame(listener1, listener2);
    }

    @Test
    public void testGetAcceptedChars() {
        MockDateTimeKeyListener mockDateTimeKeyListener = new MockDateTimeKeyListener();

        TextMethodUtils.assertEquals(DateTimeKeyListener.CHARACTERS,
                mockDateTimeKeyListener.getAcceptedChars());
    }

    @Test
    public void testGetInputType() {
        DateTimeKeyListener listener = DateTimeKeyListener.getInstance();

        int expected = InputType.TYPE_CLASS_DATETIME
                | InputType.TYPE_DATETIME_VARIATION_NORMAL;
        assertEquals(expected, listener.getInputType());
    }

    /*
     * Scenario description:
     * 1. Press '1' key and check if the content of TextView becomes "1"
     * 2. Press '2' key and check if the content of TextView becomes "12"
     * 3. Press 'a' key if it is producible
     * 4. Press 'p' key if it is producible
     * 5. Press 'm' key if it is producible
     * 6. Press an unaccepted key if it exists. and this key will not be accepted.
     * 7. Remove DateKeyListener and Press '1' key, this key will not be accepted
     */
    @Test
    public void testDateTimeKeyListener() {
        final DateTimeKeyListener dateTimeKeyListener = DateTimeKeyListener.getInstance();
        setKeyListenerSync(dateTimeKeyListener);
        String expectedText = "";
        assertEquals(expectedText, mTextView.getText().toString());

        // press '1' key.
        CtsKeyEventUtil.sendString(mInstrumentation, mTextView, "1");
        expectedText += "1";
        assertEquals(expectedText, mTextView.getText().toString());

        // press '2' key.
        CtsKeyEventUtil.sendString(mInstrumentation, mTextView, "2");
        expectedText += "2";
        assertEquals(expectedText, mTextView.getText().toString());

        // press 'a' key if producible
        KeyCharacterMap kcm = KeyCharacterMap.load(KeyCharacterMap.VIRTUAL_KEYBOARD);
        if ('a' == kcm.getMatch(KeyEvent.KEYCODE_A, DateTimeKeyListener.CHARACTERS)) {
            expectedText += "a";
            CtsKeyEventUtil.sendKeyDownUp(mInstrumentation, mTextView, KeyEvent.KEYCODE_A);
            assertEquals(expectedText, mTextView.getText().toString());
        }

        // press 'p' key if producible
        if ('p' == kcm.getMatch(KeyEvent.KEYCODE_P, DateTimeKeyListener.CHARACTERS)) {
            expectedText += "p";
            CtsKeyEventUtil.sendKeyDownUp(mInstrumentation, mTextView, KeyEvent.KEYCODE_P);
            assertEquals(expectedText, mTextView.getText().toString());
        }

        // press 'm' key if producible
        if ('m' == kcm.getMatch(KeyEvent.KEYCODE_M, DateTimeKeyListener.CHARACTERS)) {
            expectedText += "m";
            CtsKeyEventUtil.sendKeyDownUp(mInstrumentation, mTextView, KeyEvent.KEYCODE_M);
            assertEquals(expectedText, mTextView.getText().toString());
        }

        // press an unaccepted key if it exists.
        int keyCode = TextMethodUtils.getUnacceptedKeyCode(DateTimeKeyListener.CHARACTERS);
        if (-1 != keyCode) {
            CtsKeyEventUtil.sendKeys(mInstrumentation, mTextView, keyCode);
            assertEquals(expectedText, mTextView.getText().toString());
        }

        // remove DateTimeKeyListener
        setKeyListenerSync(null);
        assertEquals(expectedText, mTextView.getText().toString());

        CtsKeyEventUtil.sendString(mInstrumentation, mTextView, "1");
        assertEquals(expectedText, mTextView.getText().toString());
    }


    /**
     * A mocked {@link android.text.method.DateTimeKeyListener} for testing purposes.
     *
     * Allows {@link DateTimeKeyListenerTest} to call
     * {@link android.text.method.DateTimeKeyListener#getAcceptedChars()}.
     */
    private class MockDateTimeKeyListener extends DateTimeKeyListener {
        @Override
        protected char[] getAcceptedChars() {
            return super.getAcceptedChars();
        }
    }
}
