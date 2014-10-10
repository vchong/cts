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
package com.android.cts.managedprofile.crossprofilecontent;

import static com.android.cts.managedprofile.BaseManagedProfileTest.ADMIN_RECEIVER_COMPONENT;

import android.app.admin.DevicePolicyManager;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

public class CrossProfileContentTest extends
        ActivityInstrumentationTestCase2<IntentSenderActivity> {

    private static final String MESSAGE = "Sample Message";

    private static final String ACTION_READ_FROM_URI = "com.android.cts.action.READ_FROM_URI";

    private static final String ACTION_WRITE_TO_URI = "com.android.cts.action.WRITE_TO_URI";

    private static final String ACTION_TAKE_PERSISTABLE_URI_PERMISSION =
            "com.android.cts.action.TAKE_PERSISTABLE_URI_PERMISSION";

    private static final String TAG = "CrossProfileContentTest";

    private static final String BASIC_CONTENT_PROVIDER_AUTHORITY =
            "com.android.cts.managedprofile.basiccontentProvider";


    private DevicePolicyManager mDpm;

    private Context mContext;

    public CrossProfileContentTest() {
        super(IntentSenderActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mContext = getInstrumentation().getTargetContext();
        mDpm = (DevicePolicyManager) mContext.getSystemService(Context.DEVICE_POLICY_SERVICE);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_READ_FROM_URI);
        intentFilter.addAction(ACTION_WRITE_TO_URI);
        intentFilter.addAction(ACTION_TAKE_PERSISTABLE_URI_PERMISSION);
        mDpm.addCrossProfileIntentFilter(ADMIN_RECEIVER_COMPONENT, intentFilter,
                DevicePolicyManager.FLAG_PARENT_CAN_ACCESS_MANAGED);
    }

    @Override
    protected void tearDown() throws Exception {
        mDpm.clearCrossProfileIntentFilters(ADMIN_RECEIVER_COMPONENT);
        super.tearDown();
    }

    /**
     * This method will send an intent to a receiver in another profile.
     * This intent will have, in the ClipData, a uri whose associated file stores a message.
     * The receiver will read the message from the uri, and put it inside the result intent.
     */
    public void testReceiverCanRead() {
        Uri uri = getUriWithTextInFile("reading_test", MESSAGE);
        assertTrue(uri != null);
        Intent intent = new Intent(ACTION_READ_FROM_URI);
        intent.setClipData(ClipData.newRawUri("", uri));
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent result = getActivity().getResultForIntent(intent);
        assertTrue(result != null);
        assertEquals(MESSAGE, result.getStringExtra("extra_response"));
    }

    /**
     * This method will send an intent to a receiver in another profile.
     * This intent will have a message in an extra, and a uri specified by the ClipData.
     * The receiver will read the message from the extra, and write it to the uri in
     * the ClipData.
     */
    public void testReceiverCanWrite() {
        // It's the receiver of the intent that should write to the uri, not us. So, for now, we
        // write an empty string.
        Uri uri = getUriWithTextInFile("writing_test", "");
        assertTrue(uri != null);
        Intent intent = new Intent(ACTION_WRITE_TO_URI);
        intent.setClipData(ClipData.newRawUri("", uri));
        intent.putExtra("extra_message", MESSAGE);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        getActivity().getResultForIntent(intent);
        assertEquals(MESSAGE, getFirstLineFromUri(uri));
    }

    public void testPersistablePermission() {
        Uri uri = getUriWithTextInFile("persistable_test", MESSAGE);
        grantPersistableReadPermission(uri);

        // Now checking if the receiver can read this uri, without re-granting the read permission.
        Intent intent = new Intent(ACTION_READ_FROM_URI);
        intent.setClipData(ClipData.newRawUri("", uri));
        Intent result = getActivity().getResultForIntent(intent);
        assertTrue(result != null);
        assertEquals(MESSAGE, result.getStringExtra("extra_response"));
    }

    /**
     * The intent receiver will try to read uriNotGranted.
     * Inside the same user, this uri can be read if the receiver has the
     * com.android.cts.managedprofile.permission.SAMPLE permission. But since we cross
     * user-boundaries, it should not be able to (only uri grants work accross users for apps
     * without special permission).
     * We also grant uriGranted to the receiver (this uri belongs to the same content provider as
     * uriNotGranted), to enforce that even if an app has permission to one uri of a
     * ContentProvider, it still cannot access a uri it does not have access to.
     */
    public void testAppPermissionsDontWorkAcrossProfiles() {
        // The FileProvider does not allow to use app permissions. So we need to use another
        // ContentProvider.
        Uri uriGranted = getBasicContentProviderUri("uri_granted");
        Uri uriNotGranted = getBasicContentProviderUri("uri_not_granted");

        // Granting uriGranted to the receiver
        // Using a persistable permission so that it is kept even after we restart the receiver
        // activity with another intent.
        grantPersistableReadPermission(uriGranted);

        Intent notGrant = new Intent(ACTION_READ_FROM_URI);
        notGrant.setClipData(ClipData.newRawUri("", uriNotGranted));

        Intent result = getActivity().getResultForIntent(notGrant);
        assertTrue(result != null);
        // The receiver did not have permission to read the uri. So it should have caught a security
        // exception.
        assertTrue(result.getBooleanExtra("extra_caught_security_exception", false));
    }

    private void grantPersistableReadPermission(Uri uri) {
        Intent grantPersistable = new Intent(ACTION_TAKE_PERSISTABLE_URI_PERMISSION);
        grantPersistable.setClipData(ClipData.newRawUri("", uri));
        grantPersistable.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        getActivity().getResultForIntent(grantPersistable);
    }

    private Uri getBasicContentProviderUri(String path) {
        // The uris created here are not associated with any data. But this does not prevent us from
        // granting these uris to other apps, or these apps from trying to access these uris.
        return new Uri.Builder()
                .scheme(ContentResolver.SCHEME_CONTENT)
                .authority(BASIC_CONTENT_PROVIDER_AUTHORITY)
                .path(path)
                .build();
    }

    private Uri getUriWithTextInFile(String name, String text) {
        String filename = mContext.getFilesDir() + File.separator + "texts" + File.separator
                + name + ".txt";
        Log.i(TAG, "Creating file " + filename + " with text \"" + text + "\"");
        final File file = new File(filename);
        file.getParentFile().mkdirs(); // If the folder doesn't exists it is created
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(text);
            writer.close();
        } catch(IOException e) {
            Log.e(TAG, "Could not create file " + filename + " with text " + text);
            return null;
        }
        return FileProvider.getUriForFile(mContext, "com.android.cts.managedprofile.fileprovider",
                file);
    }

    /**
     * Returns the first line of the file associated with uri.
     */
    private String getFirstLineFromUri(Uri uri) {
        try {
            InputStream is = mContext.getContentResolver().openInputStream(uri);
            BufferedReader r = new BufferedReader(new InputStreamReader(is));
            return r.readLine();
        } catch (IOException e) {
            Log.e(TAG, "could not read the uri " + uri);
            return null;
        }
    }
}
