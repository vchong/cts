/**
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.accessibilityservice.cts;

import android.accessibilityservice.GestureDescription;
import android.app.Instrumentation;
import android.os.Handler;

/**
 * A stub accessibility service to install for testing gesture dispatch
 */
public class StubGestureAccessibilityService extends InstrumentedAccessibilityService {

    public boolean doDispatchGesture(GestureDescription description, GestureResultCallback callback,
            Handler handler) {
        return dispatchGesture(description, callback, handler);
    }

    public static StubGestureAccessibilityService enableSelf(Instrumentation instrumentation) {
        return InstrumentedAccessibilityService.enableService(
                instrumentation, StubGestureAccessibilityService.class);
    }
}
