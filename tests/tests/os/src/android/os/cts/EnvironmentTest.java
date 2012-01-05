/*
 * Copyright (C) 2009 The Android Open Source Project
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
package android.os.cts;

import junit.framework.TestCase;
import android.os.Environment;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;

@TestTargetClass(Environment.class)
public class EnvironmentTest extends TestCase {
    @TestTargets({
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            notes = "Test constructor(s) of {@link Environment}",
            method = "Environment",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            notes = "Test method: getExternalStorageState",
            method = "getExternalStorageState",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            notes = "Test method: getExternalStorageDirectory",
            method = "getExternalStorageDirectory",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            notes = "Test method: getRootDirectory",
            method = "getRootDirectory",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            notes = "Test method: getDownloadCacheDirectory",
            method = "getDownloadCacheDirectory",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            notes = "Test method: getDataDirectory",
            method = "getDataDirectory",
            args = {}
        )
    })
    public void testEnvironment() {
        new Environment();
        assertNotNull(Environment.getExternalStorageState());
        assertTrue(Environment.getExternalStorageDirectory().isDirectory());
        assertTrue(Environment.getRootDirectory().isDirectory());
        assertTrue(Environment.getDownloadCacheDirectory().isDirectory());
        assertTrue(Environment.getDataDirectory().isDirectory());
    }

    /**
     * TMPDIR being set prevents apps from asking to have temporary files
     * placed in their own storage, instead forcing their location to
     * something OS-defined. If TMPDIR points to a global shared directory,
     * this could compromise the security of the files.
     */
    public void testNoTmpDir() {
        assertNull("environment variable TMPDIR should not be set",
                System.getenv("TMPDIR"));
    }
}
