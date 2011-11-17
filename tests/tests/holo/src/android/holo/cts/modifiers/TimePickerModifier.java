/*
 * Copyright (C) 2011 The Android Open Source Project
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

package android.holo.cts.modifiers;

import android.holo.cts.LayoutModifier;
import android.view.View;
import android.widget.TimePicker;

public class TimePickerModifier implements LayoutModifier {

    @Override
    public View modifyView(View view) {
        TimePicker timePicker = (TimePicker) view;
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(13);
        timePicker.setCurrentMinute(37);
        return view;
    }
}
