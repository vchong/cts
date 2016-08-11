/*
 * Copyright (C) 2015 The Android Open Source Project
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

import android.app.Activity;
import android.os.Bundle;
import android.view.SearchEvent;

public class SearchEventActivity extends Activity {
    private SearchEvent mSearchEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.windowstub_layout);
    }

    @Override
    public boolean onSearchRequested() {
        mSearchEvent = getSearchEvent();
        return true;
    }

    public SearchEvent getTestSearchEvent() {
        return mSearchEvent;
    }
}
