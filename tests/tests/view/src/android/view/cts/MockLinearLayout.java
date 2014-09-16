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

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ViewParent;
import android.widget.LinearLayout;

public class MockLinearLayout extends LinearLayout {

    public boolean mIsInvalidateChildInParentCalled;

    public MockLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MockLinearLayout(Context context) {
        super(context);
    }

    @Override
    public ViewParent invalidateChildInParent(int[] location, Rect dirty) {
        mIsInvalidateChildInParentCalled = true;
        return super.invalidateChildInParent(location, dirty);
    }

}

