/*
 * Copyright (C) 2014 The Android Open Source Project
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

package com.android.cts.holo_capture;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.CountDownLatch;

public class CaptureActivity extends Activity {
    private static final String TAG = "CaptureActivity";
    private CountDownLatch mLatch = new CountDownLatch(1);

    public void waitForCompletion() throws InterruptedException {
        mLatch.await();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent();
        intent.setClassName("com.android.cts.holo", "android.holo.cts.ThemeTestActivity");
        intent.putExtra("task", 2);
        intent.putExtra("layoutAdapterMode", 1);
        int requestCode = 0;
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult req:" + requestCode + " res:" + resultCode);
        mLatch.countDown();
    }
}
