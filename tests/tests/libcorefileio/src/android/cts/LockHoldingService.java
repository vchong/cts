/*
 * Copyright (C) 2016 The Android Open Source Project
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
 * limitations under the License
 */

package android.cts;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLock;

public class LockHoldingService extends Service {

    final String TAG = "CtsLibcoreFileIOTestCases";

    File file = null;

    FileLock fileLock = null;

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID) {
        try {
            this.file = FileChannelTryLockTest.createFileInDir(FileChannelTryLockTest.dirName,
                    FileChannelTryLockTest.sharedFileName);
            try {
                this.fileLock = new FileOutputStream(file).getChannel().tryLock();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        sendBroadcast(new Intent().setAction("android.cts.CtsLibcoreFileIOTestCases"));
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        try {
            fileLock.close();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        FileChannelTryLockTest.deleteDir();
    }
}
