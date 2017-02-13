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
package android.uirendering.cts.testinfrastructure;

import android.annotation.Nullable;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.uirendering.cts.R;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.ViewTreeObserver;

/**
 * A generic activity that uses a view specified by the user.
 */
public class DrawActivity extends Activity {
    private final static long TIME_OUT_MS = 10000;
    private final Point mLock = new Point();

    private Handler mHandler;
    private View mView;
    private View mViewWrapper;
    private boolean mOnTv;

    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);
        mHandler = new RenderSpecHandler();
        int uiMode = getResources().getConfiguration().uiMode;
        mOnTv = (uiMode & Configuration.UI_MODE_TYPE_MASK) == Configuration.UI_MODE_TYPE_TELEVISION;
    }

    public boolean getOnTv() {
        return mOnTv;
    }

    public Point enqueueRenderSpecAndWait(int layoutId, CanvasClient canvasClient,
            @Nullable ViewInitializer viewInitializer, boolean useHardware, boolean usePicture) {
        ((RenderSpecHandler) mHandler).setViewInitializer(viewInitializer);
        int arg2 = (useHardware ? View.LAYER_TYPE_NONE : View.LAYER_TYPE_SOFTWARE);
        if (canvasClient != null) {
            mHandler.obtainMessage(RenderSpecHandler.CANVAS_MSG, usePicture ? 1 : 0,
                    arg2, canvasClient).sendToTarget();
        } else {
            mHandler.obtainMessage(RenderSpecHandler.LAYOUT_MSG, layoutId, arg2).sendToTarget();
        }

        Point point = new Point();
        synchronized (mLock) {
            try {
                mLock.wait(TIME_OUT_MS);
                point.set(mLock.x, mLock.y);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return point;
    }

    public void reset() {
        mHandler.sendEmptyMessage(RenderSpecHandler.RESET_MSG);
        synchronized (mLock) {
            try {
                mLock.wait(TIME_OUT_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private ViewInitializer mViewInitializer;

    private void notifyOnDrawCompleted() {
        DrawCounterListener onDrawListener = new DrawCounterListener();
        mView.getViewTreeObserver().addOnDrawListener(onDrawListener);
        mView.invalidate();
    }

    private class RenderSpecHandler extends Handler {
        public static final int RESET_MSG = 0;
        public static final int LAYOUT_MSG = 1;
        public static final int CANVAS_MSG = 2;


        public void setViewInitializer(ViewInitializer viewInitializer) {
            mViewInitializer = viewInitializer;
        }

        public void handleMessage(Message message) {
            if (message.what == RESET_MSG) {
                ((ViewGroup)findViewById(android.R.id.content)).removeAllViews();
                synchronized (mLock) {
                    mLock.set(-1, -1);
                    mLock.notify();
                }
                return;
            }
            setContentView(R.layout.test_container);
            ViewStub stub = (ViewStub) findViewById(R.id.test_content_stub);
            mViewWrapper = findViewById(R.id.test_content_wrapper);
            switch (message.what) {
                case LAYOUT_MSG: {
                    stub.setLayoutResource(message.arg1);
                    mView = stub.inflate();

                    // temporary hack to accomodate webview that may be contained in layout
                    drawCountDelay = 10;
                } break;

                case CANVAS_MSG: {
                    stub.setLayoutResource(R.layout.test_content_canvasclientview);
                    mView = stub.inflate();
                    ((CanvasClientView) mView).setCanvasClient((CanvasClient) (message.obj));
                    if (message.arg1 != 0) {
                        ((CanvasClientView) mView).setUsePicture(true);
                    }
                } break;
            }

            if (mView == null) {
                throw new IllegalStateException("failed to inflate test content");
            }

            if (mViewInitializer != null) {
                mViewInitializer.initializeView(mView);
            }

            // set layer on wrapper parent of view, so view initializer
            // can control layer type of View under test.
            mViewWrapper.setLayerType(message.arg2, null);

            notifyOnDrawCompleted();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mViewInitializer != null) {
            mViewInitializer.teardownView();
        }
    }

    @Override
    public void finish() {
        // Ignore
    }

    /** Call this when all the tests that use this activity have completed.
     * This will then clean up any internal state and finish the activity. */
    public void allTestsFinished() {
        super.finish();
    }

    private class DrawCounterListener implements ViewTreeObserver.OnDrawListener {
        @Override
        public void onDraw() {
            mView.post(() -> {
                mView.getViewTreeObserver().removeOnDrawListener(this);
                synchronized (mLock) {
                    final int[] locationOnScreen = mViewWrapper.getLocationOnScreen();
                    mLock.set(locationOnScreen[0], locationOnScreen[1]);
                    mLock.notify();
                }
            });
        }
    }
}
