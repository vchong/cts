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
package android.media.cts;

import com.android.cts.stub.R;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Environment;
import android.os.PowerManager;
import android.os.SystemClock;

import java.io.File;
import java.util.concurrent.CountDownLatch;

/**
 * Tests for the MediaPlayer API and local video/audio playback.
 *
 * The files in res/raw used by testLocalVideo* are (c) copyright 2008,
 * Blender Foundation / www.bigbuckbunny.org, and are licensed under the Creative Commons
 * Attribution 3.0 License at http://creativecommons.org/licenses/by/3.0/us/.
 */
public class MediaPlayerTest extends MediaPlayerTestBase {

    private static final String RECORDED_FILE =
                new File(Environment.getExternalStorageDirectory(),
                "record.out").getAbsolutePath();

    private static final int  RECORDED_VIDEO_WIDTH  = 176;
    private static final int  RECORDED_VIDEO_HEIGHT = 144;
    private static final long RECORDED_DURATION_MS  = 3000;

    public void testPlayNullSource() throws Exception {
        try {
            mMediaPlayer.setDataSource((String) null);
            fail("Null URI was accepted");
        } catch (RuntimeException e) {
            // expected
        }
    }

    public void testPlayAudio() throws Exception {
        final int mp3Duration = 34909;
        final int tolerance = 70;
        final int seekDuration = 100;
        final int resid = R.raw.testmp3_2;

        MediaPlayer mp = MediaPlayer.create(mContext, resid);
        try {
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.setWakeMode(mContext, PowerManager.PARTIAL_WAKE_LOCK);

            assertFalse(mp.isPlaying());
            mp.start();
            assertTrue(mp.isPlaying());

            assertFalse(mp.isLooping());
            mp.setLooping(true);
            assertTrue(mp.isLooping());

            assertEquals(mp3Duration, mp.getDuration(), tolerance);
            int pos = mp.getCurrentPosition();
            assertTrue(pos >= 0);
            assertTrue(pos < mp3Duration - seekDuration);

            mp.seekTo(pos + seekDuration);
            assertEquals(pos + seekDuration, mp.getCurrentPosition(), tolerance);

            // test pause and restart
            mp.pause();
            Thread.sleep(SLEEP_TIME);
            assertFalse(mp.isPlaying());
            mp.start();
            assertTrue(mp.isPlaying());

            // test stop and restart
            mp.stop();
            mp.reset();
            AssetFileDescriptor afd = mResources.openRawResourceFd(resid);
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            mp.prepare();
            assertFalse(mp.isPlaying());
            mp.start();
            assertTrue(mp.isPlaying());

            // waiting to complete
            while(mp.isPlaying()) {
                Thread.sleep(SLEEP_TIME);
            }
        } finally {
            mp.release();
        }
    }

    public void testPlayVideo() throws Exception {
        playVideoTest(R.raw.testvideo, 352, 288);
    }

    public void testSetNextMediaPlayer() throws Exception {
        AssetFileDescriptor afd = mResources.openRawResourceFd(R.raw.sine1320hz5sec);
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
                    afd.getLength());
            mMediaPlayer.prepare();
            mMediaPlayer2.reset();
            mMediaPlayer2.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
                    afd.getLength());
            mMediaPlayer2.prepare();
        } finally {
            afd.close();
        }

        mOnCompletionCalled.reset();
        mOnInfoCalled.reset();

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                assertEquals(mMediaPlayer, mp);
                mOnCompletionCalled.signal();
            }
        });
        mMediaPlayer2.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                assertEquals(mMediaPlayer2, mp);
                if (what == MediaPlayer.MEDIA_INFO_STARTED_AS_NEXT) {
                    mOnInfoCalled.signal();
                }
                return false;
            }
        });

        try {
            mMediaPlayer.setNextMediaPlayer(mMediaPlayer2);
            mMediaPlayer.start();
            assertTrue(mMediaPlayer.isPlaying());
            assertFalse(mOnCompletionCalled.isSignalled());
            assertFalse(mMediaPlayer2.isPlaying());
            assertFalse(mOnInfoCalled.isSignalled());
            while(mMediaPlayer.isPlaying()) {
                Thread.sleep(SLEEP_TIME);
            }
            assertTrue(mMediaPlayer2.isPlaying());
            assertTrue(mOnCompletionCalled.isSignalled());
            assertTrue(mOnInfoCalled.isSignalled());
        } finally {
            mMediaPlayer.reset();
            mMediaPlayer2.reset();
        }
    }

    // This test uses one mp3 that is silent but has a strong positive DC offset,
    // and a second mp3 that is also silent but has a strong negative DC offset.
    // If the two are played back overlapped, they will cancel each other out,
    // and result in zeroes being detected.
    public void testGapless() throws Exception {
        MediaPlayer mp1 = new MediaPlayer();
        mp1.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            AssetFileDescriptor afd = mContext.getResources().openRawResourceFd(R.raw.monodcneg);
            mp1.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            mp1.prepare();
        } catch (Exception e) {
            assertTrue(false);
        }
        int session = mp1.getAudioSessionId();

        MediaPlayer mp2 = new MediaPlayer();
        mp2.setAudioSessionId(session);
        mp2.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            AssetFileDescriptor afd = mContext.getResources().openRawResourceFd(R.raw.monodcneg);
            mp2.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            mp2.prepare();
        } catch (Exception e) {
            assertTrue(false);
        }

        int size = 256;
        int[] range = Visualizer.getCaptureSizeRange();
        if (size < range[0]) {
            size = range[0];
        }
        if (size > range[1]) {
            size = range[1];
        }
        byte [] vizdata = new byte[size];
        Visualizer vis  = null;
        vis = new Visualizer(session);
        assertTrue(vis.setCaptureSize(vizdata.length) == Visualizer.SUCCESS);
        assertTrue(vis.setEnabled(true) == Visualizer.SUCCESS);
        AudioManager am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        int oldRingerMode = am.getRingerMode();
        am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        int oldvolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        int maxvolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, maxvolume, 0);
        try {
            mp1.setNextMediaPlayer(mp2);
            mp1.start();
            assertTrue(mp1.isPlaying());
            assertFalse(mp2.isPlaying());
            // allow playback to get started
            Thread.sleep(SLEEP_TIME);
            long start = SystemClock.elapsedRealtime();
            // there should be no consecutive zeroes (-128) in the capture buffer
            // for the next 8 seconds. If silence is detected right away, then
            // the volume is probably turned all the way down (visualizer data
            // is captured after volume adjustment).
            boolean first = true;
            while((SystemClock.elapsedRealtime() - start) < 8000) {
                assertTrue(vis.getWaveForm(vizdata) == Visualizer.SUCCESS);
                for (int i = 0; i < vizdata.length - 1; i++) {
                    if (vizdata[i] == -128 && vizdata[i + 1] == -128) {
                        if (first) {
                            fail("silence detected, please increase volume and rerun test");
                        } else {
                            fail("gap or overlap detected");
                        }
                        break;
                    }
                }
                first = false;
            }
        } finally {
            mp1.release();
            mp2.release();
            vis.release();
            am.setRingerMode(oldRingerMode);
            am.setStreamVolume(AudioManager.STREAM_MUSIC, oldvolume, 0);
        }
    }

    /**
     * Test for reseting a surface during video playback
     * After reseting, the video should continue playing
     * from the time setDisplay() was called
     */
    public void testVideoSurfaceResetting() throws Exception {
        final int tolerance = 66 * 3 / 2; /* Test video is 15fps... 66 ms per frame */
        final int seekPos = 1500;

        final CountDownLatch seekDone = new CountDownLatch(1);

        mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                seekDone.countDown();
            }
        });

        loadResource(R.raw.testvideo);
        playLoadedVideo(352, 288, -1);

        Thread.sleep(SLEEP_TIME);

        int posBefore = mMediaPlayer.getCurrentPosition();
        mMediaPlayer.setDisplay(getActivity().getSurfaceHolder2());
        int posAfter = mMediaPlayer.getCurrentPosition();

        assertEquals(posAfter, posBefore, tolerance);
        assertTrue(mMediaPlayer.isPlaying());

        Thread.sleep(SLEEP_TIME);

        mMediaPlayer.seekTo(seekPos);
        seekDone.await();
        Thread.sleep(SLEEP_TIME / 2);

        posBefore = mMediaPlayer.getCurrentPosition();
        mMediaPlayer.setDisplay(null);
        posAfter = mMediaPlayer.getCurrentPosition();

        assertEquals(posAfter, posBefore, tolerance);
        assertEquals(seekPos + SLEEP_TIME / 2, posBefore, tolerance);
        assertTrue(mMediaPlayer.isPlaying());

        Thread.sleep(SLEEP_TIME);

        posBefore = mMediaPlayer.getCurrentPosition();
        mMediaPlayer.setDisplay(getActivity().getSurfaceHolder());
        posAfter = mMediaPlayer.getCurrentPosition();

        assertEquals(posAfter, posBefore, tolerance);
        assertTrue(mMediaPlayer.isPlaying());

        Thread.sleep(SLEEP_TIME);
    }

    public void testRecordedVideoPlayback0() throws Exception {
        testRecordedVideoPlaybackWithAngle(0);
    }

    public void testRecordedVideoPlayback90() throws Exception {
        testRecordedVideoPlaybackWithAngle(90);
    }

    public void testRecordedVideoPlayback180() throws Exception {
        testRecordedVideoPlaybackWithAngle(180);
    }

    public void testRecordedVideoPlayback270() throws Exception {
        testRecordedVideoPlaybackWithAngle(270);
    }

    private boolean hasCamera() {
        return getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    private void testRecordedVideoPlaybackWithAngle(int angle) throws Exception {
        final int width = RECORDED_VIDEO_WIDTH;
        final int height = RECORDED_VIDEO_HEIGHT;
        final String file = RECORDED_FILE;
        final long durationMs = RECORDED_DURATION_MS;

        if (!hasCamera()) {
            return;
        }
        checkOrientation(angle);
        recordVideo(width, height, angle, file, durationMs);
        checkDisplayedVideoSize(width, height, angle, file);
    }

    private void checkOrientation(int angle) throws Exception {
        assertTrue(angle >= 0);
        assertTrue(angle < 360);
        assertTrue((angle % 90) == 0);
    }

    private void recordVideo(
            int w, int h, int angle, String file, long durationMs) throws Exception {

        MediaRecorder recorder = new MediaRecorder();
        recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        recorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        recorder.setOutputFile(file);
        recorder.setOrientationHint(angle);
        recorder.setVideoSize(w, h);
        recorder.setPreviewDisplay(getActivity().getSurfaceHolder2().getSurface());
        recorder.prepare();
        recorder.start();
        Thread.sleep(durationMs);
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    private void checkDisplayedVideoSize(
            int w, int h, int angle, String file) throws Exception {

        int displayWidth  = w;
        int displayHeight = h;
        if ((angle % 180) != 0) {
            displayWidth  = h;
            displayHeight = w;
        }
        playVideoTest(file, displayWidth, displayHeight);
    }

    public void testLocalVideo_MP4_H264_480x360_500kbps_25fps_AAC_Stereo_128kbps_44110Hz()
            throws Exception {
        playVideoTest(
                R.raw.video_480x360_mp4_h264_500kbps_25fps_aac_stereo_128kbps_44100hz, 480, 360);
    }

    public void testLocalVideo_MP4_H264_480x360_500kbps_30fps_AAC_Stereo_128kbps_44110Hz()
            throws Exception {
        playVideoTest(
                R.raw.video_480x360_mp4_h264_500kbps_30fps_aac_stereo_128kbps_44100hz, 480, 360);
    }

    public void testLocalVideo_MP4_H264_480x360_1000kbps_25fps_AAC_Stereo_128kbps_44110Hz()
            throws Exception {
        playVideoTest(
                R.raw.video_480x360_mp4_h264_1000kbps_25fps_aac_stereo_128kbps_44100hz, 480, 360);
    }

    public void testLocalVideo_MP4_H264_480x360_1000kbps_30fps_AAC_Stereo_128kbps_44110Hz()
            throws Exception {
        playVideoTest(
                R.raw.video_480x360_mp4_h264_1000kbps_30fps_aac_stereo_128kbps_44100hz, 480, 360);
    }

    public void testLocalVideo_MP4_H264_480x360_1350kbps_25fps_AAC_Stereo_128kbps_44110Hz()
            throws Exception {
        playVideoTest(
                R.raw.video_480x360_mp4_h264_1350kbps_25fps_aac_stereo_128kbps_44100hz, 480, 360);
    }

    public void testLocalVideo_MP4_H264_480x360_1350kbps_30fps_AAC_Stereo_128kbps_44110Hz()
            throws Exception {
        playVideoTest(
                R.raw.video_480x360_mp4_h264_1350kbps_30fps_aac_stereo_128kbps_44100hz, 480, 360);
    }

    public void testLocalVideo_MP4_H264_480x360_1350kbps_30fps_AAC_Stereo_192kbps_44110Hz()
            throws Exception {
        playVideoTest(
                R.raw.video_480x360_mp4_h264_1350kbps_30fps_aac_stereo_192kbps_44100hz, 480, 360);
    }

    public void testLocalVideo_3gp_H263_176x144_56kbps_12fps_AAC_Mono_24kbps_11025Hz()
            throws Exception {
        playVideoTest(
                R.raw.video_176x144_3gp_h263_56kbps_12fps_aac_mono_24kbps_11025hz, 176, 144);
    }

    public void testLocalVideo_3gp_H263_176x144_56kbps_12fps_AAC_Mono_24kbps_22050Hz()
            throws Exception {
        playVideoTest(
                R.raw.video_176x144_3gp_h263_56kbps_12fps_aac_mono_24kbps_22050hz, 176, 144);
    }

    public void testLocalVideo_3gp_H263_176x144_56kbps_12fps_AAC_Stereo_24kbps_11025Hz()
            throws Exception {
        playVideoTest(
                R.raw.video_176x144_3gp_h263_56kbps_12fps_aac_stereo_24kbps_11025hz, 176, 144);
    }

    public void testLocalVideo_3gp_H263_176x144_56kbps_12fps_AAC_Stereo_24kbps_22050Hz()
            throws Exception {
        playVideoTest(
                R.raw.video_176x144_3gp_h263_56kbps_12fps_aac_stereo_24kbps_11025hz, 176, 144);
    }

    public void testLocalVideo_3gp_H263_176x144_56kbps_12fps_AAC_Stereo_128kbps_11025Hz()
            throws Exception {
        playVideoTest(
                R.raw.video_176x144_3gp_h263_56kbps_12fps_aac_stereo_128kbps_11025hz, 176, 144);
    }

    public void testLocalVideo_3gp_H263_176x144_56kbps_12fps_AAC_Stereo_128kbps_22050Hz()
            throws Exception {
        playVideoTest(
                R.raw.video_176x144_3gp_h263_56kbps_12fps_aac_stereo_128kbps_11025hz, 176, 144);
    }

    public void testLocalVideo_3gp_H263_176x144_56kbps_25fps_AAC_Mono_24kbps_11025Hz()
            throws Exception {
        playVideoTest(
                R.raw.video_176x144_3gp_h263_56kbps_25fps_aac_mono_24kbps_11025hz, 176, 144);
    }

    public void testLocalVideo_3gp_H263_176x144_56kbps_25fps_AAC_Mono_24kbps_22050Hz()
            throws Exception {
        playVideoTest(
                R.raw.video_176x144_3gp_h263_56kbps_25fps_aac_mono_24kbps_22050hz, 176, 144);
    }

    public void testLocalVideo_3gp_H263_176x144_56kbps_25fps_AAC_Stereo_24kbps_11025Hz()
            throws Exception {
        playVideoTest(
                R.raw.video_176x144_3gp_h263_56kbps_25fps_aac_stereo_24kbps_11025hz, 176, 144);
    }

    public void testLocalVideo_3gp_H263_176x144_56kbps_25fps_AAC_Stereo_24kbps_22050Hz()
            throws Exception {
        playVideoTest(
                R.raw.video_176x144_3gp_h263_56kbps_25fps_aac_stereo_24kbps_11025hz, 176, 144);
    }

    public void testLocalVideo_3gp_H263_176x144_56kbps_25fps_AAC_Stereo_128kbps_11025Hz()
            throws Exception {
        playVideoTest(
                R.raw.video_176x144_3gp_h263_56kbps_25fps_aac_stereo_128kbps_11025hz, 176, 144);
    }

    public void testLocalVideo_3gp_H263_176x144_56kbps_25fps_AAC_Stereo_128kbps_22050Hz()
            throws Exception {
        playVideoTest(
                R.raw.video_176x144_3gp_h263_56kbps_25fps_aac_stereo_128kbps_11025hz, 176, 144);
    }

    public void testLocalVideo_3gp_H263_176x144_300kbps_12fps_AAC_Mono_24kbps_11025Hz()
            throws Exception {
        playVideoTest(
                R.raw.video_176x144_3gp_h263_300kbps_12fps_aac_mono_24kbps_11025hz, 176, 144);
    }

    public void testLocalVideo_3gp_H263_176x144_300kbps_12fps_AAC_Mono_24kbps_22050Hz()
            throws Exception {
        playVideoTest(
                R.raw.video_176x144_3gp_h263_300kbps_12fps_aac_mono_24kbps_22050hz, 176, 144);
    }

    public void testLocalVideo_3gp_H263_176x144_300kbps_12fps_AAC_Stereo_24kbps_11025Hz()
            throws Exception {
        playVideoTest(
                R.raw.video_176x144_3gp_h263_300kbps_12fps_aac_stereo_24kbps_11025hz, 176, 144);
    }

    public void testLocalVideo_3gp_H263_176x144_300kbps_12fps_AAC_Stereo_24kbps_22050Hz()
            throws Exception {
        playVideoTest(
                R.raw.video_176x144_3gp_h263_300kbps_12fps_aac_stereo_24kbps_11025hz, 176, 144);
    }

    public void testLocalVideo_3gp_H263_176x144_300kbps_12fps_AAC_Stereo_128kbps_11025Hz()
            throws Exception {
        playVideoTest(
                R.raw.video_176x144_3gp_h263_300kbps_12fps_aac_stereo_128kbps_11025hz, 176, 144);
    }

    public void testLocalVideo_3gp_H263_176x144_300kbps_12fps_AAC_Stereo_128kbps_22050Hz()
            throws Exception {
        playVideoTest(
                R.raw.video_176x144_3gp_h263_300kbps_12fps_aac_stereo_128kbps_11025hz, 176, 144);
    }

    public void testLocalVideo_3gp_H263_176x144_300kbps_25fps_AAC_Mono_24kbps_11025Hz()
            throws Exception {
        playVideoTest(
                R.raw.video_176x144_3gp_h263_300kbps_25fps_aac_mono_24kbps_11025hz, 176, 144);
    }

    public void testLocalVideo_3gp_H263_176x144_300kbps_25fps_AAC_Mono_24kbps_22050Hz()
            throws Exception {
        playVideoTest(
                R.raw.video_176x144_3gp_h263_300kbps_25fps_aac_mono_24kbps_22050hz, 176, 144);
    }

    public void testLocalVideo_3gp_H263_176x144_300kbps_25fps_AAC_Stereo_24kbps_11025Hz()
            throws Exception {
        playVideoTest(
                R.raw.video_176x144_3gp_h263_300kbps_25fps_aac_stereo_24kbps_11025hz, 176, 144);
    }

    public void testLocalVideo_3gp_H263_176x144_300kbps_25fps_AAC_Stereo_24kbps_22050Hz()
            throws Exception {
        playVideoTest(
                R.raw.video_176x144_3gp_h263_300kbps_25fps_aac_stereo_24kbps_11025hz, 176, 144);
    }

    public void testLocalVideo_3gp_H263_176x144_300kbps_25fps_AAC_Stereo_128kbps_11025Hz()
            throws Exception {
        playVideoTest(
                R.raw.video_176x144_3gp_h263_300kbps_25fps_aac_stereo_128kbps_11025hz, 176, 144);
    }

    public void testLocalVideo_3gp_H263_176x144_300kbps_25fps_AAC_Stereo_128kbps_22050Hz()
            throws Exception {
        playVideoTest(
                R.raw.video_176x144_3gp_h263_300kbps_25fps_aac_stereo_128kbps_22050hz, 176, 144);
    }

    public void testCallback() throws Throwable {
        final int mp4Duration = 8484;

        loadResource(R.raw.testvideo);
        mMediaPlayer.setDisplay(getActivity().getSurfaceHolder());
        mMediaPlayer.setScreenOnWhilePlaying(true);

        mMediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                mOnVideoSizeChangedCalled.signal();
            }
        });

        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mOnPrepareCalled.signal();
            }
        });

        mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                mOnSeekCompleteCalled.signal();
            }
        });

        mOnCompletionCalled.reset();
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mOnCompletionCalled.signal();
            }
        });

        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mOnErrorCalled.signal();
                return false;
            }
        });

        mMediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                mOnInfoCalled.signal();
                return false;
            }
        });

        assertFalse(mOnPrepareCalled.isSignalled());
        assertFalse(mOnVideoSizeChangedCalled.isSignalled());
        mMediaPlayer.prepare();
        mOnPrepareCalled.waitForSignal();
        mOnVideoSizeChangedCalled.waitForSignal();
        mOnSeekCompleteCalled.reset();
        mMediaPlayer.seekTo(mp4Duration >> 1);
        mOnSeekCompleteCalled.waitForSignal();
        assertFalse(mOnCompletionCalled.isSignalled());
        mMediaPlayer.start();
        while(mMediaPlayer.isPlaying()) {
            Thread.sleep(SLEEP_TIME);
        }
        assertFalse(mMediaPlayer.isPlaying());
        mOnCompletionCalled.waitForSignal();
        assertFalse(mOnErrorCalled.isSignalled());
        mMediaPlayer.stop();
        mMediaPlayer.start();
        mOnErrorCalled.waitForSignal();
    }

    public void testRecordAndPlay() throws Exception {
        if (!hasMicrophone()) {
            return;
        }
        File outputFile = new File(Environment.getExternalStorageDirectory(),
                "record_and_play.3gp");
        String outputFileLocation = outputFile.getAbsolutePath();
        try {
            recordMedia(outputFileLocation);
            MediaPlayer mp = new MediaPlayer();
            try {
                mp.setDataSource(outputFileLocation);
                mp.prepareAsync();
                Thread.sleep(SLEEP_TIME);
                playAndStop(mp);
            } finally {
                mp.release();
            }

            Uri uri = Uri.parse(outputFileLocation);
            mp = new MediaPlayer();
            try {
                mp.setDataSource(mContext, uri);
                mp.prepareAsync();
                Thread.sleep(SLEEP_TIME);
                playAndStop(mp);
            } finally {
                mp.release();
            }

            try {
                mp = MediaPlayer.create(mContext, uri);
                playAndStop(mp);
            } finally {
                if (mp != null) {
                    mp.release();
                }
            }

            try {
                mp = MediaPlayer.create(mContext, uri, getActivity().getSurfaceHolder());
                playAndStop(mp);
            } finally {
                if (mp != null) {
                    mp.release();
                }
            }
        } finally {
            outputFile.delete();
        }
    }

    private void playAndStop(MediaPlayer mp) throws Exception {
        mp.start();
        Thread.sleep(SLEEP_TIME);
        mp.stop();
    }

    private void recordMedia(String outputFile) throws Exception {
        MediaRecorder mr = new MediaRecorder();
        try {
            mr.setAudioSource(MediaRecorder.AudioSource.MIC);
            mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mr.setOutputFile(outputFile);

            mr.prepare();
            mr.start();
            Thread.sleep(SLEEP_TIME);
            mr.stop();
        } finally {
            mr.release();
        }
    }

    private boolean hasMicrophone() {
        return getActivity().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_MICROPHONE);
    }
}
