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

package android.media.tv.cts;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.tv.TvContentRating;
import android.media.tv.TvContract;
import android.net.Uri;
import android.os.RemoteException;
import android.test.AndroidTestCase;

import com.android.cts.tv.R;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * Test for {@link android.media.tv.TvContract}.
 */
public class TvContractTest extends AndroidTestCase {
    private static final String[] CHANNELS_PROJECTION = {
        TvContract.Channels._ID,
        TvContract.Channels.COLUMN_INPUT_ID,
        TvContract.Channels.COLUMN_TYPE,
        TvContract.Channels.COLUMN_SERVICE_TYPE,
        TvContract.Channels.COLUMN_ORIGINAL_NETWORK_ID,
        TvContract.Channels.COLUMN_TRANSPORT_STREAM_ID,
        TvContract.Channels.COLUMN_SERVICE_ID,
        TvContract.Channels.COLUMN_DISPLAY_NUMBER,
        TvContract.Channels.COLUMN_DISPLAY_NAME,
        TvContract.Channels.COLUMN_NETWORK_AFFILIATION,
        TvContract.Channels.COLUMN_DESCRIPTION,
        TvContract.Channels.COLUMN_VIDEO_FORMAT,
        TvContract.Channels.COLUMN_INTERNAL_PROVIDER_DATA,
        TvContract.Channels.COLUMN_VERSION_NUMBER,
    };

    private static final String[] PROGRAMS_PROJECTION = {
        TvContract.Programs._ID,
        TvContract.Programs.COLUMN_CHANNEL_ID,
        TvContract.Programs.COLUMN_TITLE,
        TvContract.Programs.COLUMN_SEASON_NUMBER,
        TvContract.Programs.COLUMN_EPISODE_NUMBER,
        TvContract.Programs.COLUMN_EPISODE_TITLE,
        TvContract.Programs.COLUMN_START_TIME_UTC_MILLIS,
        TvContract.Programs.COLUMN_END_TIME_UTC_MILLIS,
        TvContract.Programs.COLUMN_BROADCAST_GENRE,
        TvContract.Programs.COLUMN_CANONICAL_GENRE,
        TvContract.Programs.COLUMN_SHORT_DESCRIPTION,
        TvContract.Programs.COLUMN_LONG_DESCRIPTION,
        TvContract.Programs.COLUMN_VIDEO_WIDTH,
        TvContract.Programs.COLUMN_VIDEO_HEIGHT,
        TvContract.Programs.COLUMN_AUDIO_LANGUAGE,
        TvContract.Programs.COLUMN_CONTENT_RATING,
        TvContract.Programs.COLUMN_POSTER_ART_URI,
        TvContract.Programs.COLUMN_THUMBNAIL_URI,
        TvContract.Programs.COLUMN_INTERNAL_PROVIDER_DATA,
        TvContract.Programs.COLUMN_VERSION_NUMBER,
    };

    private static long OPERATION_TIME = 1000l;

    private String mInputId;
    private ContentResolver mContentResolver;
    private Uri mChannelsUri;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mInputId = TvContract.buildInputId(
                new ComponentName(getContext(), StubTunerTvInputService.class));
        mContentResolver = getContext().getContentResolver();
        mChannelsUri = TvContract.buildChannelsUriForInput(mInputId);
    }

    @Override
    protected void tearDown() throws Exception {
        // Clean up, just in case we failed to delete the entry when a test failed.
        // The cotentUris are specific to this package, so this will delete only the
        // entries inserted by this package.
        String[] projection = { TvContract.Channels._ID };
        try (Cursor cursor = mContentResolver.query(mChannelsUri, projection, null, null, null)) {
            while (cursor != null && cursor.moveToNext()) {
                long channelId = cursor.getLong(0);
                mContentResolver.delete(
                        TvContract.buildProgramsUriForChannel(channelId), null, null);
            }
        }
        mContentResolver.delete(mChannelsUri, null, null);
        super.tearDown();
    }

    private static ContentValues createDummyChannelValues(String inputId) {
        ContentValues values = new ContentValues();
        values.put(TvContract.Channels.COLUMN_INPUT_ID, inputId);
        values.put(TvContract.Channels.COLUMN_TYPE, TvContract.Channels.TYPE_OTHER);
        values.put(TvContract.Channels.COLUMN_SERVICE_TYPE,
                TvContract.Channels.SERVICE_TYPE_AUDIO_VIDEO);
        values.put(TvContract.Channels.COLUMN_DISPLAY_NUMBER, "1");
        values.put(TvContract.Channels.COLUMN_VIDEO_FORMAT, TvContract.Channels.VIDEO_FORMAT_480P);

        return values;
    }

    private static ContentValues createDummyProgramValues(long channelId) {
        ContentValues values = new ContentValues();
        values.put(TvContract.Programs.COLUMN_CHANNEL_ID, channelId);
        values.put(TvContract.Programs.COLUMN_EPISODE_TITLE, "Title");
        values.put(TvContract.Programs.COLUMN_CANONICAL_GENRE, TvContract.Programs.Genres.encode(
                TvContract.Programs.Genres.MOVIES, TvContract.Programs.Genres.DRAMA));
        TvContentRating rating = TvContentRating.createRating("android.media.tv", "US_TVPG",
                "US_TVPG_TV_MA", "US_TVPG_S", "US_TVPG_V");
        values.put(TvContract.Programs.COLUMN_CONTENT_RATING, rating.flattenToString());

        return values;
    }

    private static void verifyStringColumn(Cursor cursor, ContentValues expectedValues,
            String columnName) {
        if (expectedValues.containsKey(columnName)) {
            assertEquals(expectedValues.getAsString(columnName),
                    cursor.getString(cursor.getColumnIndex(columnName)));
        }
    }

    private static void verifyIntegerColumn(Cursor cursor, ContentValues expectedValues,
            String columnName) {
        if (expectedValues.containsKey(columnName)) {
            assertEquals(expectedValues.getAsInteger(columnName).intValue(),
                    cursor.getInt(cursor.getColumnIndex(columnName)));
        }
    }

    private static void verifyLongColumn(Cursor cursor, ContentValues expectedValues,
            String columnName) {
        if (expectedValues.containsKey(columnName)) {
            assertEquals(expectedValues.getAsLong(columnName).longValue(),
                    cursor.getLong(cursor.getColumnIndex(columnName)));
        }
    }

    private static void verifyBlobColumn(Cursor cursor, ContentValues expectedValues,
            String columnName) {
        if (expectedValues.containsKey(columnName)) {
            byte[] expected = expectedValues.getAsByteArray(columnName);
            byte[] actual = cursor.getBlob(cursor.getColumnIndex(columnName));
            assertEquals(expected.length, actual.length);
            for (int i = 0; i < expected.length; ++i) {
                assertEquals(expected[i], actual[i]);
            }
        }
    }

    private void verifyChannel(Uri channelUri, ContentValues expectedValues, long channelId) {
        try (Cursor cursor = mContentResolver.query(
                channelUri, CHANNELS_PROJECTION, null, null, null)) {
            assertNotNull(cursor);
            assertEquals(cursor.getCount(), 1);
            assertTrue(cursor.moveToNext());
            assertEquals(channelId, cursor.getLong(cursor.getColumnIndex(TvContract.Channels._ID)));
            verifyStringColumn(cursor, expectedValues, TvContract.Channels.COLUMN_INPUT_ID);
            verifyStringColumn(cursor, expectedValues, TvContract.Channels.COLUMN_TYPE);
            verifyStringColumn(cursor, expectedValues, TvContract.Channels.COLUMN_SERVICE_TYPE);
            verifyIntegerColumn(cursor, expectedValues,
                    TvContract.Channels.COLUMN_ORIGINAL_NETWORK_ID);
            verifyIntegerColumn(cursor, expectedValues,
                    TvContract.Channels.COLUMN_TRANSPORT_STREAM_ID);
            verifyIntegerColumn(cursor, expectedValues,
                    TvContract.Channels.COLUMN_SERVICE_ID);
            verifyStringColumn(cursor, expectedValues, TvContract.Channels.COLUMN_DISPLAY_NUMBER);
            verifyStringColumn(cursor, expectedValues, TvContract.Channels.COLUMN_DISPLAY_NAME);
            verifyStringColumn(cursor, expectedValues,
                    TvContract.Channels.COLUMN_NETWORK_AFFILIATION);
            verifyStringColumn(cursor, expectedValues, TvContract.Channels.COLUMN_DESCRIPTION);
            verifyStringColumn(cursor, expectedValues, TvContract.Channels.COLUMN_VIDEO_FORMAT);
            verifyBlobColumn(cursor, expectedValues,
                    TvContract.Channels.COLUMN_INTERNAL_PROVIDER_DATA);
            verifyIntegerColumn(cursor, expectedValues, TvContract.Channels.COLUMN_VERSION_NUMBER);
        }
    }

    public void testChannelsTable() throws Exception {
        // Test: insert
        ContentValues values = createDummyChannelValues(mInputId);

        Uri rowUri = mContentResolver.insert(mChannelsUri, values);
        long channelId = ContentUris.parseId(rowUri);
        Uri channelUri = TvContract.buildChannelUri(channelId);
        verifyChannel(channelUri, values, channelId);

        // Test: update
        values.put(TvContract.Channels.COLUMN_DISPLAY_NUMBER, "1-1");
        values.put(TvContract.Channels.COLUMN_DISPLAY_NAME, "One dash one");
        values.put(TvContract.Channels.COLUMN_INTERNAL_PROVIDER_DATA, "Coffee".getBytes());

        mContentResolver.update(channelUri, values, null, null);
        verifyChannel(channelUri, values, channelId);

        // Test: delete
        mContentResolver.delete(mChannelsUri, null, null);
        try (Cursor cursor = mContentResolver.query(
                mChannelsUri, CHANNELS_PROJECTION, null, null, null)) {
            assertEquals(0, cursor.getCount());
        }
    }

    private void verifyProgram(Uri programUri, ContentValues expectedValues, long programId) {
        try (Cursor cursor = mContentResolver.query(
                programUri, PROGRAMS_PROJECTION, null, null, null)) {
            assertNotNull(cursor);
            assertEquals(cursor.getCount(), 1);
            assertTrue(cursor.moveToNext());
            assertEquals(programId, cursor.getLong(cursor.getColumnIndex(TvContract.Programs._ID)));
            verifyLongColumn(cursor, expectedValues, TvContract.Programs.COLUMN_CHANNEL_ID);
            verifyStringColumn(cursor, expectedValues, TvContract.Programs.COLUMN_TITLE);
            verifyIntegerColumn(cursor, expectedValues, TvContract.Programs.COLUMN_SEASON_NUMBER);
            verifyIntegerColumn(cursor, expectedValues, TvContract.Programs.COLUMN_EPISODE_NUMBER);
            verifyStringColumn(cursor, expectedValues, TvContract.Programs.COLUMN_EPISODE_TITLE);
            verifyLongColumn(cursor, expectedValues,
                    TvContract.Programs.COLUMN_START_TIME_UTC_MILLIS);
            verifyLongColumn(cursor, expectedValues,
                    TvContract.Programs.COLUMN_END_TIME_UTC_MILLIS);
            verifyStringColumn(cursor, expectedValues, TvContract.Programs.COLUMN_BROADCAST_GENRE);
            verifyStringColumn(cursor, expectedValues, TvContract.Programs.COLUMN_CANONICAL_GENRE);
            verifyStringColumn(cursor, expectedValues,
                    TvContract.Programs.COLUMN_SHORT_DESCRIPTION);
            verifyStringColumn(cursor, expectedValues, TvContract.Programs.COLUMN_LONG_DESCRIPTION);
            verifyIntegerColumn(cursor, expectedValues, TvContract.Programs.COLUMN_VIDEO_WIDTH);
            verifyIntegerColumn(cursor, expectedValues, TvContract.Programs.COLUMN_VIDEO_HEIGHT);
            verifyStringColumn(cursor, expectedValues, TvContract.Programs.COLUMN_AUDIO_LANGUAGE);
            verifyStringColumn(cursor, expectedValues, TvContract.Programs.COLUMN_CONTENT_RATING);
            verifyStringColumn(cursor, expectedValues, TvContract.Programs.COLUMN_POSTER_ART_URI);
            verifyStringColumn(cursor, expectedValues, TvContract.Programs.COLUMN_THUMBNAIL_URI);
            verifyBlobColumn(cursor, expectedValues,
                    TvContract.Programs.COLUMN_INTERNAL_PROVIDER_DATA);
            verifyIntegerColumn(cursor, expectedValues, TvContract.Programs.COLUMN_VERSION_NUMBER);
        }
    }

    public void testChannelLogo() throws Exception {
        // Set-up: add a channel.
        ContentValues values = createDummyChannelValues(mInputId);
        Uri logoUri = TvContract.buildChannelLogoUri(mContentResolver.insert(mChannelsUri, values));
        Bitmap logo = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.robot);

        // Write
        try (AssetFileDescriptor fd = mContentResolver.openAssetFileDescriptor(logoUri, "w")) {
            try (OutputStream os = fd.createOutputStream()) {
                logo.compress(Bitmap.CompressFormat.PNG, 100, os);
            }
        }

        // Give some time for TvProvider to process the logo.
        Thread.sleep(OPERATION_TIME);

        // Read and verify
        try (AssetFileDescriptor fd = mContentResolver.openAssetFileDescriptor(logoUri, "r")) {
            try (InputStream is = fd.createInputStream()) {
                // Assure that the stream is decodable as a Bitmap.
                BitmapFactory.decodeStream(is);
            }
        }
    }

    public void testProgramsTable() throws Exception {
        // Set-up: add a channel.
        ContentValues values = createDummyChannelValues(mInputId);
        long channelId = ContentUris.parseId(mContentResolver.insert(mChannelsUri, values));
        Uri programsUri = TvContract.buildProgramsUriForChannel(channelId);

        // Test: insert
        values = createDummyProgramValues(channelId);

        Uri rowUri = mContentResolver.insert(programsUri, values);
        long programId = ContentUris.parseId(rowUri);
        Uri programUri = TvContract.buildProgramUri(programId);
        verifyProgram(programUri, values, programId);

        // Test: update
        values.put(TvContract.Programs.COLUMN_EPISODE_TITLE, "Sample title");
        values.put(TvContract.Programs.COLUMN_SHORT_DESCRIPTION, "Short description");
        values.put(TvContract.Programs.COLUMN_INTERNAL_PROVIDER_DATA, "Coffee".getBytes());

        mContentResolver.update(programUri, values, null, null);
        verifyProgram(programUri, values, programId);

        // Test: delete
        mContentResolver.delete(programsUri, null, null);
        try (Cursor cursor = mContentResolver.query(
                programsUri, PROGRAMS_PROJECTION, null, null, null)) {
            assertEquals(0, cursor.getCount());
        }
    }

    public void testProgramsScheduleOverlap() throws Exception {
        final long startMillis = 1403712000000l;  // Jun 25 2014 16:00 UTC
        final long endMillis = 1403719200000l;  // Jun 25 2014 18:00 UTC
        final long hour = 3600000l;

        // Set-up: add a channel and program.
        ContentValues values = createDummyChannelValues(mInputId);
        long channelId = ContentUris.parseId(mContentResolver.insert(mChannelsUri, values));
        Uri programsUri = TvContract.buildProgramsUriForChannel(channelId);
        values = createDummyProgramValues(channelId);
        values.put(TvContract.Programs.COLUMN_START_TIME_UTC_MILLIS, startMillis);
        values.put(TvContract.Programs.COLUMN_END_TIME_UTC_MILLIS, endMillis);
        mContentResolver.insert(programsUri, values);

        // Overlap 1: starts early, ends early.
        try (Cursor cursor = mContentResolver.query(TvContract.buildProgramsUriForChannel(channelId,
                startMillis - hour, endMillis - hour), PROGRAMS_PROJECTION, null, null, null)) {
            assertEquals(1, cursor.getCount());
        }

        // Overlap 2: starts early, ends late.
        try (Cursor cursor = mContentResolver.query(TvContract.buildProgramsUriForChannel(channelId,
                startMillis - hour, endMillis + hour), PROGRAMS_PROJECTION, null, null, null)) {
            assertEquals(1, cursor.getCount());
        }

        // Overlap 3: starts early, ends late.
        try (Cursor cursor = mContentResolver.query(TvContract.buildProgramsUriForChannel(channelId,
                startMillis + hour / 2, endMillis - hour / 2), PROGRAMS_PROJECTION,
                null, null, null)) {
            assertEquals(1, cursor.getCount());
        }

        // Overlap 4: starts late, ends late.
        try (Cursor cursor = mContentResolver.query(TvContract.buildProgramsUriForChannel(channelId,
                startMillis + hour, endMillis + hour), PROGRAMS_PROJECTION, null, null, null)) {
            assertEquals(1, cursor.getCount());
        }

        // Non-overlap 1: ends too early.
        try (Cursor cursor = mContentResolver.query(TvContract.buildProgramsUriForChannel(channelId,
                startMillis - hour, startMillis - hour / 2), PROGRAMS_PROJECTION,
                null, null, null)) {
            assertEquals(0, cursor.getCount());
        }

        // Non-overlap 2: starts too late
        try (Cursor cursor = mContentResolver.query(TvContract.buildProgramsUriForChannel(channelId,
                endMillis + hour, endMillis + hour * 2), PROGRAMS_PROJECTION, null, null, null)) {
            assertEquals(0, cursor.getCount());
        }
    }
}
