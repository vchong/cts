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

package android.os.storage.cts;

import android.os.cts.R;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.os.Environment;
import android.os.Parcel;
import android.os.storage.OnObbStateChangeListener;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.test.AndroidTestCase;
import android.test.ComparisonFailure;
import android.util.Log;

import com.android.compatibility.common.util.FileUtils;

import libcore.io.Streams;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StorageManagerTest extends AndroidTestCase {

    private static final String TAG = StorageManager.class.getSimpleName();

    private static final long MAX_WAIT_TIME = 25*1000;
    private static final long WAIT_TIME_INCR = 5*1000;

    private static final String OBB_MOUNT_PREFIX = "/mnt/obb/";
    private static final String TEST1_NEW_CONTENTS = "1\n";

    private StorageManager mStorageManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
    }

    public void testMountAndUnmountObbNormal() throws IOException {
        for (File target : getTargetFiles()) {
            target = new File(target, "test1_new.obb");
            Log.d(TAG, "Testing path " + target);
            doMountAndUnmountObbNormal(target);
        }
    }

    private void doMountAndUnmountObbNormal(File outFile) throws IOException {
        final String canonPath = mountObb(R.raw.test1_new, outFile, OnObbStateChangeListener.MOUNTED);

        mountObb(R.raw.test1_new, outFile, OnObbStateChangeListener.ERROR_ALREADY_MOUNTED);

        try {
            final String mountPath = checkMountedPath(canonPath);
            final File mountDir = new File(mountPath);
            final File testFile = new File(mountDir, "test1.txt");

            assertTrue("OBB mounted path should be a directory", mountDir.isDirectory());
            assertTrue("test1.txt does not exist in OBB dir", testFile.exists());
            assertFileContains(testFile, TEST1_NEW_CONTENTS);
        } finally {
            unmountObb(outFile, OnObbStateChangeListener.UNMOUNTED);
        }
    }

    public void testAttemptMountNonObb() {
        for (File target : getTargetFiles()) {
            target = new File(target, "test1_nosig.obb");
            Log.d(TAG, "Testing path " + target);
            doAttemptMountNonObb(target);
        }
    }

    private void doAttemptMountNonObb(File outFile) {
        mountObb(R.raw.test1_nosig, outFile, OnObbStateChangeListener.ERROR_INTERNAL);

        assertFalse("OBB should not be mounted",
                mStorageManager.isObbMounted(outFile.getPath()));

        assertNull("OBB's mounted path should be null",
                mStorageManager.getMountedObbPath(outFile.getPath()));
    }

    public void testAttemptMountObbWrongPackage() {
        for (File target : getTargetFiles()) {
            target = new File(target, "test1_wrongpackage.obb");
            Log.d(TAG, "Testing path " + target);
            doAttemptMountObbWrongPackage(target);
        }
    }

    private void doAttemptMountObbWrongPackage(File outFile) {
        mountObb(R.raw.test1_wrongpackage, outFile,
                OnObbStateChangeListener.ERROR_PERMISSION_DENIED);

        assertFalse("OBB should not be mounted",
                mStorageManager.isObbMounted(outFile.getPath()));

        assertNull("OBB's mounted path should be null",
                mStorageManager.getMountedObbPath(outFile.getPath()));
    }

    public void testMountAndUnmountTwoObbs() throws IOException {
        for (File target : getTargetFiles()) {
            Log.d(TAG, "Testing target " + target);
            final File test1 = new File(target, "test1.obb");
            final File test2 = new File(target, "test2.obb");
            doMountAndUnmountTwoObbs(test1, test2);
        }
    }

    private void doMountAndUnmountTwoObbs(File file1, File file2) throws IOException {
        ObbObserver oo1 = mountObbWithoutWait(R.raw.test1_new, file1);
        ObbObserver oo2 = mountObbWithoutWait(R.raw.test1_new, file2);

        Log.d(TAG, "Waiting for OBB #1 to complete mount");
        waitForObbActionCompletion(file1, oo1, OnObbStateChangeListener.MOUNTED);
        Log.d(TAG, "Waiting for OBB #2 to complete mount");
        waitForObbActionCompletion(file2, oo2, OnObbStateChangeListener.MOUNTED);

        try {
            final String mountPath1 = checkMountedPath(oo1.getPath());
            final File mountDir1 = new File(mountPath1);
            final File testFile1 = new File(mountDir1, "test1.txt");
            assertTrue("OBB mounted path should be a directory", mountDir1.isDirectory());
            assertTrue("test1.txt does not exist in OBB dir", testFile1.exists());
            assertFileContains(testFile1, TEST1_NEW_CONTENTS);

            final String mountPath2 = checkMountedPath(oo2.getPath());
            final File mountDir2 = new File(mountPath2);
            final File testFile2 = new File(mountDir2, "test1.txt");
            assertTrue("OBB mounted path should be a directory", mountDir2.isDirectory());
            assertTrue("test1.txt does not exist in OBB dir", testFile2.exists());
            assertFileContains(testFile2, TEST1_NEW_CONTENTS);
        } finally {
            unmountObb(file1, OnObbStateChangeListener.UNMOUNTED);
            unmountObb(file2, OnObbStateChangeListener.UNMOUNTED);
        }
    }

    public void testGetPrimaryVolume() throws Exception {
        final StorageVolume volume = mStorageManager.getPrimaryStorageVolume();
        assertNotNull("Did not get primary storage", volume);

        // Tests some basic Scoped Directory Access requests.
        assertNull("Should not grant access for root directory", volume.createAccessIntent(null));
        assertNull("Should not grant access for invalid directory",
                volume.createAccessIntent("/system"));
        assertNotNull("Should grant access for valid directory " + Environment.DIRECTORY_DOCUMENTS,
                volume.createAccessIntent(Environment.DIRECTORY_DOCUMENTS));

        // Tests basic properties.
        assertNotNull("Should have description", volume.getDescription(mContext));
        assertTrue("Should be primary", volume.isPrimary());
        assertEquals("Wrong state", Environment.MEDIA_MOUNTED, volume.getState());

        // Tests properties that depend on storage type (emulated or physical)
        final String uuid = volume.getUuid();
        final boolean removable = volume.isRemovable();
        final boolean emulated = volume.isEmulated();
        if (emulated) {
            assertFalse("Should not be removable", removable);
            assertNull("Should not have uuid", uuid);
        } else {
            assertTrue("Should be removable", removable);
            assertNotNull("Should have uuid", uuid);
        }

        // Tests path - although it's not a public API, sm.getPrimaryStorageVolume()
        // explicitly states it should match Environment#getExternalStorageDirectory
        final String path = volume.getPath();
        assertEquals("Path does not match Environment's",
                Environment.getExternalStorageDirectory().getAbsolutePath(), path);

        // Tests Parcelable contract.
        assertEquals("Wrong describeContents", 0, volume.describeContents());
        final Parcel parcel = Parcel.obtain();
        try {
            volume.writeToParcel(parcel, 0);
            parcel.setDataPosition(0);
            final StorageVolume clone = StorageVolume.CREATOR.createFromParcel(parcel);
            assertStorageVolumesEquals(volume, clone);
        } finally {
            parcel.recycle();
        }
    }

    public void testGetStorageVolumes() throws Exception {
        final List<StorageVolume> volumes = mStorageManager.getStorageVolumes();
        assertFalse("No volume return", volumes.isEmpty());
        StorageVolume primary = null;
        for (StorageVolume volume : volumes) {
            if (volume.isPrimary()) {
                assertNull("There can be only one primary volume: " + volumes, primary);
                primary = volume;
            }
        }
        assertNotNull("No primary volume on  " + volumes, primary);
        assertStorageVolumesEquals(primary, mStorageManager.getPrimaryVolume());
    }

    public void testGetStorageVolume() throws Exception {
        assertNull("Should not get volume for null path", mStorageManager.getStorageVolume(null));
        assertNull("Should not get volume for invalid path",
                mStorageManager.getStorageVolume(new File("/system")));
        assertNull("Should not get volume for storage directory",
                mStorageManager.getStorageVolume(Environment.getStorageDirectory()));

        final File root = Environment.getExternalStorageDirectory();
        final StorageVolume primary = mStorageManager.getPrimaryStorageVolume();
        final StorageVolume rootVolume = mStorageManager.getStorageVolume(root);
        assertNotNull("No volume for root (" + root + ")", rootVolume);
        assertStorageVolumesEquals(primary, rootVolume);

        final File child = new File(root, "child");
        StorageVolume childVolume = mStorageManager.getStorageVolume(child);
        assertNotNull("No volume for child (" + child + ")", childVolume);
        assertStorageVolumesEquals(primary, childVolume);
    }

    private void assertStorageVolumesEquals(StorageVolume volume, StorageVolume clone)
            throws Exception {
        // Asserts equals() method.
        assertEquals("StorageVolume.equals() mismatch", volume, clone);
        // Make sure all fields match.
        for (Field field : StorageVolume.class.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) continue;
            field.setAccessible(true);
            final Object originalValue = field.get(volume);
            final Object clonedValue = field.get(clone);
            assertEquals("Mismatch for field " + field.getName(), originalValue, clonedValue);
        }
    }

    private static void assertStartsWith(String message, String prefix, String actual) {
        if (!actual.startsWith(prefix)) {
            throw new ComparisonFailure(message, prefix, actual);
        }
    }

    private static void assertFileContains(File file, String contents) throws IOException {
        byte[] actual = Streams.readFully(new FileInputStream(file));
        byte[] expected = contents.getBytes("UTF-8");
        assertEquals("unexpected size", expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals("unexpected value at offset " + i, expected[i], actual[i]);
        }
    }

    private static class ObbObserver extends OnObbStateChangeListener {
        private String path;

        public int state = -1;
        boolean done = false;

        @Override
        public void onObbStateChange(String path, int state) {
            Log.d(TAG, "Received message.  path=" + path + ", state=" + state);
            synchronized (this) {
                this.path = path;
                this.state = state;
                done = true;
                notifyAll();
            }
        }

        public String getPath() {
            assertTrue("Expected ObbObserver to have received a state change.", done);
            return path;
        }

        public int getState() {
            assertTrue("Expected ObbObserver to have received a state change.", done);
            return state;
        }

        public boolean isDone() {
            return done;
        }

        public boolean waitForCompletion() {
            long waitTime = 0;
            synchronized (this) {
                while (!isDone() && waitTime < MAX_WAIT_TIME) {
                    try {
                        wait(WAIT_TIME_INCR);
                        waitTime += WAIT_TIME_INCR;
                    } catch (InterruptedException e) {
                        Log.i(TAG, "Interrupted during sleep", e);
                    }
                }
            }

            return isDone();
        }
    }

    private List<File> getTargetFiles() {
        final List<File> targets = new ArrayList<File>();
        targets.add(mContext.getFilesDir());
        for (File dir : mContext.getObbDirs()) {
            assertNotNull("Valid media must be inserted during CTS", dir);
            assertEquals("Valid media must be inserted during CTS", Environment.MEDIA_MOUNTED,
                    Environment.getStorageState(dir));
            targets.add(dir);
        }
        return targets;
    }

    private void copyRawToFile(int rawResId, File outFile) {
        Resources res = mContext.getResources();
        InputStream is = null;
        try {
            is = res.openRawResource(rawResId);
        } catch (NotFoundException e) {
            fail("Failed to load resource with id: " + rawResId);
        }
        assertTrue(FileUtils.copyToFile(is, outFile));
        exposeFile(outFile);
    }

    private File exposeFile(File file) {
        file.setReadable(true, false);
        file.setReadable(true, true);

        File dir = file.getParentFile();
        do {
            dir.setExecutable(true, false);
            dir.setExecutable(true, true);
            dir = dir.getParentFile();
        } while (dir != null);

        return file;
    }

    private String mountObb(final int resource, final File file, int expectedState) {
        copyRawToFile(resource, file);

        final ObbObserver observer = new ObbObserver();
        assertTrue("mountObb call on " + file.getPath() + " should succeed",
                mStorageManager.mountObb(file.getPath(), null, observer));

        assertTrue("Mount should have completed",
                observer.waitForCompletion());

        if (expectedState == OnObbStateChangeListener.MOUNTED) {
            assertTrue("OBB should be mounted", mStorageManager.isObbMounted(observer.getPath()));
        }

        assertEquals(expectedState, observer.getState());

        return observer.getPath();
    }

    private ObbObserver mountObbWithoutWait(final int resource, final File file) {
        copyRawToFile(resource, file);

        final ObbObserver observer = new ObbObserver();
        assertTrue("mountObb call on " + file.getPath() + " should succeed",
                mStorageManager.mountObb(file.getPath(), null, observer));

        return observer;
    }

    private void waitForObbActionCompletion(final File file, final ObbObserver observer,
            int expectedState) {
        assertTrue("Mount should have completed", observer.waitForCompletion());

        assertTrue("OBB should be mounted", mStorageManager.isObbMounted(observer.getPath()));

        assertEquals(expectedState, observer.getState());
    }

    private String checkMountedPath(final String path) {
        final String mountPath = mStorageManager.getMountedObbPath(path);
        assertStartsWith("Path should be in " + OBB_MOUNT_PREFIX,
                OBB_MOUNT_PREFIX,
                mountPath);
        return mountPath;
    }

    private void unmountObb(final File file, int expectedState) {
        final ObbObserver observer = new ObbObserver();

        assertTrue("unmountObb call on test1_new.obb should succeed",
                mStorageManager.unmountObb(file.getPath(), false, observer));

        assertTrue("Unmount should have completed",
                observer.waitForCompletion());

        assertEquals(expectedState, observer.getState());

        if (expectedState == OnObbStateChangeListener.UNMOUNTED) {
            assertFalse("OBB should not be mounted", mStorageManager.isObbMounted(file.getPath()));
        }
    }
}
