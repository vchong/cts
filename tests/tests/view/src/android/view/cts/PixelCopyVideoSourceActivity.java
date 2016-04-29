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
 * limitations under the License.
 */

package android.view.cts;

import android.app.Activity;
import android.cts.util.MediaUtils;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import java.util.concurrent.CountDownLatch;

public class PixelCopyVideoSourceActivity extends Activity {
    private VideoView mVideoView;
    private CountDownLatch mVideoPlayingFence = new CountDownLatch(1);
    private boolean mCanPlayVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVideoView = new VideoView(this);
        mVideoView.setOnPreparedListener(mp -> {
            mp.setLooping(true);
            mVideoView.start();
            mVideoPlayingFence.countDown();
        });
        mCanPlayVideo = MediaUtils.hasCodecsForResource(this, R.raw.colorgrid_video);
        if (mCanPlayVideo) {
            Uri uri = Uri.parse("android.resource://android.view.cts/" + R.raw.colorgrid_video);
            mVideoView.setVideoURI(uri);
        }
        setContentView(mVideoView);
    }

    public boolean canPlayVideo() {
        return mCanPlayVideo;
    }

    public void waitForPlaying() throws InterruptedException {
        mVideoPlayingFence.await();
    }

    public VideoView getVideoView() {
        return mVideoView;
    }
}
