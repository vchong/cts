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

package android.view.inputmethod.cts;

import android.app.Instrumentation;
import android.content.Context;
import android.cts.util.PollingCheck;
import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.cts.R;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class BaseInputConnectionTest extends
        ActivityInstrumentationTestCase2<InputMethodCtsActivity> {

    private InputMethodCtsActivity mActivity;
    private Window mWindow;
    private EditText mView;
    private BaseInputConnection mConnection;
    private Instrumentation mInstrumentation;

    public BaseInputConnectionTest() {
        super("android.view.cts", InputMethodCtsActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mInstrumentation = getInstrumentation();
        mActivity = getActivity();
        new PollingCheck() {
            @Override
                protected boolean check() {
                return mActivity.hasWindowFocus();
            }
        }.run();
        mWindow = mActivity.getWindow();
        mView = (EditText) mWindow.findViewById(R.id.entry);
        mConnection = new BaseInputConnection(mView, true);
    }

    public void testDefaultMethods() {
        // These methods are default to return fixed result.

        assertFalse(mConnection.beginBatchEdit());
        assertFalse(mConnection.endBatchEdit());

        // only fit for test default implementation of commitCompletion.
        int completionId = 1;
        String completionString = "commitCompletion test";
        assertFalse(mConnection.commitCompletion(new CompletionInfo(completionId,
                0, completionString)));

        assertNull(mConnection.getExtractedText(new ExtractedTextRequest(), 0));

        // only fit for test default implementation of performEditorAction.
        int actionCode = 1;
        int actionId = 2;
        String action = "android.intent.action.MAIN";
        assertTrue(mConnection.performEditorAction(actionCode));
        assertFalse(mConnection.performContextMenuAction(actionId));
        assertFalse(mConnection.performPrivateCommand(action, new Bundle()));
    }

    public void testOpComposingSpans() {
        Spannable text = new SpannableString("Test ComposingSpans");
        BaseInputConnection.setComposingSpans(text);
        assertTrue(BaseInputConnection.getComposingSpanStart(text) > -1);
        assertTrue(BaseInputConnection.getComposingSpanEnd(text) > -1);
        BaseInputConnection.removeComposingSpans(text);
        assertTrue(BaseInputConnection.getComposingSpanStart(text) == -1);
        assertTrue(BaseInputConnection.getComposingSpanEnd(text) == -1);
    }

    /**
     * getEditable: Return the target of edit operations. The default implementation
     *              returns its own fake editable that is just used for composing text.
     * clearMetaKeyStates: Default implementation uses
     *              MetaKeyKeyListener#clearMetaKeyState(long, int) to clear the state.
     *              BugId:1738511
     * commitText: 1. Default implementation replaces any existing composing text with the given
     *                text.
     *             2. In addition, only if dummy mode, a key event is sent for the new text and the
     *                current editable buffer cleared.
     * deleteSurroundingText: The default implementation performs the deletion around the current
     *              selection position of the editable text.
     * getCursorCapsMode: 1. The default implementation uses TextUtils.getCapsMode to get the
     *                  cursor caps mode for the current selection position in the editable text.
     *                  TextUtils.getCapsMode is tested fully in TextUtilsTest#testGetCapsMode.
     *                    2. In dummy mode in which case 0 is always returned.
     * getTextBeforeCursor, getTextAfterCursor: The default implementation performs the deletion
     *                          around the current selection position of the editable text.
     * setSelection: changes the selection position in the current editable text.
     */
    public void testOpTextMethods() throws Throwable {
        // return is an default Editable instance with empty source
        final Editable text = mConnection.getEditable();
        assertNotNull(text);
        assertEquals(0, text.length());

        // Test commitText, not dummy mode
        CharSequence str = "TestCommit ";
        Editable inputText = Editable.Factory.getInstance().newEditable(str);
        mConnection.commitText(inputText, inputText.length());
        final Editable text2 = mConnection.getEditable();
        int strLength = str.length();
        assertEquals(strLength, text2.length());
        assertEquals(str.toString(), text2.toString());
        assertEquals(TextUtils.CAP_MODE_WORDS,
                mConnection.getCursorCapsMode(TextUtils.CAP_MODE_WORDS));
        int offLength = 3;
        CharSequence expected = str.subSequence(strLength - offLength, strLength);
        assertEquals(expected.toString(), mConnection.getTextBeforeCursor(offLength,
                BaseInputConnection.GET_TEXT_WITH_STYLES).toString());
        mConnection.setSelection(0, 0);
        expected = str.subSequence(0, offLength);
        assertEquals(expected.toString(), mConnection.getTextAfterCursor(offLength,
                BaseInputConnection.GET_TEXT_WITH_STYLES).toString());

        runTestOnUiThread(new Runnable() {
            public void run() {
                assertTrue(mView.requestFocus());
                assertTrue(mView.isFocused());
            }
        });

        // dummy mode
        BaseInputConnection dummyConnection = new BaseInputConnection(mView, false);
        dummyConnection.commitText(inputText, inputText.length());
        new PollingCheck() {
            @Override
            protected boolean check() {
                return text2.toString().equals(mView.getText().toString());
            }
        }.run();
        assertEquals(0, dummyConnection.getCursorCapsMode(TextUtils.CAP_MODE_WORDS));

        // Test deleteSurroundingText
        int end = text2.length();
        mConnection.setSelection(end, end);
        // Delete the ending space
        assertTrue(mConnection.deleteSurroundingText(1, 2));
        Editable text3 = mConnection.getEditable();
        assertEquals(strLength - 1, text3.length());
        String expectedDelString = "TestCommit";
        assertEquals(expectedDelString, text3.toString());
    }

    /**
     * finishComposingText: 1. The default implementation removes the composing state from the
     *                         current editable text.
     *                      2. In addition, only if dummy mode, a key event is sent for the new
     *                         text and the current editable buffer cleared.
     * setComposingText: The default implementation places the given text into the editable,
     *                  replacing any existing composing text
     */
    public void testFinishComposingText() throws Throwable {
        CharSequence str = "TestFinish";
        Editable inputText = Editable.Factory.getInstance().newEditable(str);
        mConnection.commitText(inputText, inputText.length());
        final Editable text = mConnection.getEditable();
        // Test finishComposingText, not dummy mode
        BaseInputConnection.setComposingSpans(text);
        assertTrue(BaseInputConnection.getComposingSpanStart(text) > -1);
        assertTrue(BaseInputConnection.getComposingSpanEnd(text) > -1);
        mConnection.finishComposingText();
        assertTrue(BaseInputConnection.getComposingSpanStart(text) == -1);
        assertTrue(BaseInputConnection.getComposingSpanEnd(text) == -1);

        runTestOnUiThread(new Runnable() {
            public void run() {
                assertTrue(mView.requestFocus());
                assertTrue(mView.isFocused());
            }
        });

        // dummy mode
        BaseInputConnection dummyConnection = new BaseInputConnection(mView, false);
        dummyConnection.setComposingText(str, str.length());
        dummyConnection.finishComposingText();
        new PollingCheck() {
            @Override
            protected boolean check() {
                return text.toString().equals(mView.getText().toString());
            }
        }.run();
    }

    /**
     * Provides standard implementation for sending a key event to the window
     * attached to the input connection's view
     */
    public void testSendKeyEvent() throws Throwable {
        runTestOnUiThread(new Runnable() {
            public void run() {
                assertTrue(mView.requestFocus());
                assertTrue(mView.isFocused());
            }
        });

        // 12-key support
        KeyCharacterMap keymap = KeyCharacterMap.load(KeyCharacterMap.VIRTUAL_KEYBOARD);
        if (keymap.getKeyboardType() == KeyCharacterMap.NUMERIC) {
            // 'Q' in case of 12-key(NUMERIC) keyboard
            mConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_7));
            mConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_7));
        }
        else {
            mInstrumentation.sendStringSync("q");
            mInstrumentation.waitForIdleSync();
        }
        new PollingCheck() {
            @Override
            protected boolean check() {
                return "q".equals(mView.getText().toString());
            }
        }.run();
    }

    /**
     * Updates InputMethodManager with the current fullscreen mode.
     */
    public void testReportFullscreenMode() {
        InputMethodManager imManager = (InputMethodManager) mInstrumentation.getTargetContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        mConnection.reportFullscreenMode(false);
        assertFalse(imManager.isFullscreenMode());
        mConnection.reportFullscreenMode(true);
        assertTrue(imManager.isFullscreenMode());
    }

    /**
     * An utility method to create an instance of {@link BaseInputConnection} in dummy mode with
     * an initial text and selection range.
     * @param view the {@link View} to be associated with the {@link BaseInputConnection}.
     * @param source the initial text.
     * @param selectionStart the initial selection start index.
     * @param selectionEnd the initial selection end index.
     * @return {@link BaseInputConnection} instantiated in dummy mode with {@code source} and
     * selection range from {@code selectionStart} to {@code selectionEnd}
     */
    private static BaseInputConnection createDummyConnectionWithSelection(
            final View view, final CharSequence source, final int selectionStart,
            final int selectionEnd) {
        final Editable editable = Editable.Factory.getInstance().newEditable(source);
        Selection.setSelection(editable, selectionStart, selectionEnd);
        return new BaseInputConnection(view, false) {
            @Override
            public Editable getEditable() {
                return editable;
            }
        };
    }

    /**
     * Tests {@link BaseInputConnection#deleteSurroundingText(int, int)} comprehensively.
     */
    public void testDeleteSurroundingText() throws Throwable {
        // For text "012[]3456789", calling deleteSurroundingText(0, 0) must produce "012[]3456789",
        // where '[' and ']' indicate the text selection range.
        {
            final BaseInputConnection dummyConnection = createDummyConnectionWithSelection(
                    mView, "0123456789", 3, 3);

            dummyConnection.deleteSurroundingText(0, 0);
            assertEquals("012", dummyConnection.getTextBeforeCursor(10, 0).toString());
            assertTrue(TextUtils.isEmpty(dummyConnection.getSelectedText(0)));  // null is allowed.
            assertEquals("3456789", dummyConnection.getTextAfterCursor(10, 0).toString());
            assertEquals("0123456789", dummyConnection.getEditable().toString());
        }

        // For text "012[]3456789", calling deleteSurroundingText(-1, -1) must produce
        // "012[]3456789", where '[' and ']' indicate the text selection range.
        {
            final BaseInputConnection dummyConnection = createDummyConnectionWithSelection(
                    mView, "0123456789", 3, 3);

            dummyConnection.deleteSurroundingText(-1, -1);
            assertEquals("012", dummyConnection.getTextBeforeCursor(10, 0).toString());
            assertTrue(TextUtils.isEmpty(dummyConnection.getSelectedText(0)));  // null is allowed.
            assertEquals("3456789", dummyConnection.getTextAfterCursor(10, 0).toString());
            assertEquals("0123456789", dummyConnection.getEditable().toString());
        }

        // For text "012[]3456789", calling deleteSurroundingText(1, 2) must produce
        // "01[]56789", where '[' and ']' indicate the text selection range.
        {
            final BaseInputConnection dummyConnection = createDummyConnectionWithSelection(
                    mView, "0123456789", 3, 3);

            dummyConnection.deleteSurroundingText(1, 2);
            assertEquals("01", dummyConnection.getTextBeforeCursor(10, 0).toString());
            assertTrue(TextUtils.isEmpty(dummyConnection.getSelectedText(0)));  // null is allowed.
            assertEquals("56789", dummyConnection.getTextAfterCursor(10, 0).toString());
            assertEquals("0156789", dummyConnection.getEditable().toString());
        }

        // For text "012[]3456789", calling deleteSurroundingText(10, 1) must produce "[]456789",
        // where '[' and ']' indicate the text selection range.
        {
            final BaseInputConnection dummyConnection = createDummyConnectionWithSelection(
                    mView, "0123456789", 3, 3);

            assertTrue(dummyConnection.deleteSurroundingText(10, 1));
            assertEquals("", dummyConnection.getTextBeforeCursor(10, 0).toString());
            assertTrue(TextUtils.isEmpty(dummyConnection.getSelectedText(0)));  // null is allowed.
            assertEquals("456789", dummyConnection.getTextAfterCursor(10, 0).toString());
            assertEquals("456789", dummyConnection.getEditable().toString());
        }

        // For text "012[]3456789", calling deleteSurroundingText(1, 10) must produce "01[]",
        // where '[' and ']' indicate the text selection range.
        {
            final BaseInputConnection dummyConnection = createDummyConnectionWithSelection(
                    mView, "0123456789", 3, 3);

            assertTrue(dummyConnection.deleteSurroundingText(1, 10));
            assertEquals("01", dummyConnection.getTextBeforeCursor(10, 0).toString());
            assertTrue(TextUtils.isEmpty(dummyConnection.getSelectedText(0)));  // null is allowed.
            assertEquals("", dummyConnection.getTextAfterCursor(10, 0).toString());
            assertEquals("01", dummyConnection.getEditable().toString());
        }

        // For text "[]0123456789", calling deleteSurroundingText(3, 3) must produce "[]3456789",
        // where '[' and ']' indicate the text selection range.
        {
            final BaseInputConnection dummyConnection = createDummyConnectionWithSelection(
                    mView, "0123456789", 0, 0);

            assertTrue(dummyConnection.deleteSurroundingText(3, 3));
            assertEquals("", dummyConnection.getTextBeforeCursor(10, 0).toString());
            assertTrue(TextUtils.isEmpty(dummyConnection.getSelectedText(0)));  // null is allowed.
            assertEquals("3456789", dummyConnection.getTextAfterCursor(10, 0).toString());
            assertEquals("3456789", dummyConnection.getEditable().toString());
        }

        // For text "0123456789[]", calling deleteSurroundingText(3, 3) must produce "0123456[]",
        // where '[' and ']' indicate the text selection range.
        {
            final BaseInputConnection dummyConnection = createDummyConnectionWithSelection(
                    mView, "0123456789", 10, 10);

            assertTrue(dummyConnection.deleteSurroundingText(3, 3));
            assertEquals("0123456", dummyConnection.getTextBeforeCursor(10, 0).toString());
            assertTrue(TextUtils.isEmpty(dummyConnection.getSelectedText(0)));  // null is allowed.
            assertEquals("", dummyConnection.getTextAfterCursor(10, 0).toString());
            assertEquals("0123456", dummyConnection.getEditable().toString());
        }

        // For text "012[345]6789", calling deleteSurroundingText(0, 0) must produce "012[345]6789",
        // where '[' and ']' indicate the text selection range.
        {
            final BaseInputConnection dummyConnection = createDummyConnectionWithSelection(
                    mView, "0123456789", 3, 6);

            dummyConnection.deleteSurroundingText(0, 0);
            assertEquals("012", dummyConnection.getTextBeforeCursor(10, 0).toString());
            assertEquals("345", dummyConnection.getSelectedText(0).toString());
            assertEquals("6789", dummyConnection.getTextAfterCursor(10, 0).toString());
            assertEquals("0123456789", dummyConnection.getEditable().toString());
        }

        // For text "012[345]6789", calling deleteSurroundingText(-1, -1) must produce
        // "012[345]6789", where '[' and ']' indicate the text selection range.
        {
            final BaseInputConnection dummyConnection = createDummyConnectionWithSelection(
                    mView, "0123456789", 3, 6);

            dummyConnection.deleteSurroundingText(-1, -1);
            assertEquals("012", dummyConnection.getTextBeforeCursor(10, 0).toString());
            assertEquals("345", dummyConnection.getSelectedText(0).toString());
            assertEquals("6789", dummyConnection.getTextAfterCursor(10, 0).toString());
            assertEquals("0123456789", dummyConnection.getEditable().toString());
        }

        // For text "012[345]6789", calling deleteSurroundingText(1, 2) must produce
        // "01[345]89", where '[' and ']' indicate the text selection range.
        {
            final BaseInputConnection dummyConnection = createDummyConnectionWithSelection(
                    mView, "0123456789", 3, 6);

            dummyConnection.deleteSurroundingText(1, 2);
            assertEquals("01", dummyConnection.getTextBeforeCursor(10, 0).toString());
            assertEquals("345", dummyConnection.getSelectedText(0).toString());
            assertEquals("89", dummyConnection.getTextAfterCursor(10, 0).toString());
            assertEquals("0134589", dummyConnection.getEditable().toString());
        }

        // For text "012[345]6789", calling deleteSurroundingText(10, 1) must produce
        // "[345]789", where '[' and ']' indicate the text selection range.
        {
            final BaseInputConnection dummyConnection = createDummyConnectionWithSelection(
                    mView, "0123456789", 3, 6);

            dummyConnection.deleteSurroundingText(10, 1);
            assertEquals("", dummyConnection.getTextBeforeCursor(10, 0).toString());
            assertEquals("345", dummyConnection.getSelectedText(0).toString());
            assertEquals("789", dummyConnection.getTextAfterCursor(10, 0).toString());
            assertEquals("345789", dummyConnection.getEditable().toString());
        }

        // For text "012[345]6789", calling deleteSurroundingText(1, 10) must produce
        // "[345]789", where '[' and ']' indicate the text selection range.
        {
            final BaseInputConnection dummyConnection = createDummyConnectionWithSelection(
                    mView, "0123456789", 3, 6);

            dummyConnection.deleteSurroundingText(1, 10);
            assertEquals("01", dummyConnection.getTextBeforeCursor(10, 0).toString());
            assertEquals("345", dummyConnection.getSelectedText(0).toString());
            assertEquals("", dummyConnection.getTextAfterCursor(10, 0).toString());
            assertEquals("01345", dummyConnection.getEditable().toString());
        }

        // For text "[012]3456789", calling deleteSurroundingText(3, 3) must produce "[012]6789",
        // where '[' and ']' indicate the text selection range.
        {
            final BaseInputConnection dummyConnection = createDummyConnectionWithSelection(
                    mView, "0123456789", 0, 3);

            assertTrue(dummyConnection.deleteSurroundingText(3, 3));
            assertEquals("", dummyConnection.getTextBeforeCursor(10, 0).toString());
            assertEquals("012", dummyConnection.getSelectedText(0).toString());
            assertEquals("6789", dummyConnection.getTextAfterCursor(10, 0).toString());
            assertEquals("0126789", dummyConnection.getEditable().toString());
        }

        // For text "0123456[789]", calling deleteSurroundingText(3, 3) must produce "0123[789]",
        // where '[' and ']' indicate the text selection range.
        {
            final BaseInputConnection dummyConnection = createDummyConnectionWithSelection(
                    mView, "0123456789", 7, 10);

            assertTrue(dummyConnection.deleteSurroundingText(3, 3));
            assertEquals("0123", dummyConnection.getTextBeforeCursor(10, 0).toString());
            assertEquals("789", dummyConnection.getSelectedText(0).toString());
            assertEquals("", dummyConnection.getTextAfterCursor(10, 0).toString());
            assertEquals("0123789", dummyConnection.getEditable().toString());
        }

        // For text "[0123456789]", calling deleteSurroundingText(0, 0) must produce "[0123456789]",
        // where '[' and ']' indicate the text selection range.
        {
            final BaseInputConnection dummyConnection = createDummyConnectionWithSelection(
                    mView, "0123456789", 0, 10);

            assertTrue(dummyConnection.deleteSurroundingText(0, 0));
            assertEquals("", dummyConnection.getTextBeforeCursor(10, 0).toString());
            assertEquals("0123456789", dummyConnection.getSelectedText(0).toString());
            assertEquals("", dummyConnection.getTextAfterCursor(10, 0).toString());
            assertEquals("0123456789", dummyConnection.getEditable().toString());
        }

        // For text "[0123456789]", calling deleteSurroundingText(1, 1) must produce "[0123456789]",
        // where '[' and ']' indicate the text selection range.
        {
            final BaseInputConnection dummyConnection = createDummyConnectionWithSelection(
                    mView, "0123456789", 0, 10);

            assertTrue(dummyConnection.deleteSurroundingText(1, 1));
            assertEquals("", dummyConnection.getTextBeforeCursor(10, 0).toString());
            assertEquals("0123456789", dummyConnection.getSelectedText(0).toString());
            assertEquals("", dummyConnection.getTextAfterCursor(10, 0).toString());
            assertEquals("0123456789", dummyConnection.getEditable().toString());
        }
    }
}
