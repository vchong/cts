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
package android.app.cts;

import android.app.FragmentManager;
import android.app.stubs.FragmentTestActivity;
import android.app.stubs.FragmentTestActivity.OnTransitionListener;
import android.app.stubs.FragmentTestActivity.TestFragment;
import android.app.stubs.R;
import android.os.Debug;
import android.os.SystemClock;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

public class FragmentTransitionTest extends
        ActivityInstrumentationTestCase2<FragmentTestActivity> {
    private TestFragment mStartFragment;
    private TestFragment mMidFragment;
    private TestFragment mEndFragment;
    private FragmentTestActivity mActivity;

    public FragmentTransitionTest() {
        super(FragmentTestActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mStartFragment = null;
        mMidFragment = null;
        mEndFragment = null;
        mActivity = getActivity();
    }

    public void testFragmentTransition() throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStartFragment = new TestFragment(R.layout.fragment_start);
                mActivity.getFragmentManager().beginTransaction()
                        .replace(R.id.content, mStartFragment)
                        .commit();
                mActivity.getFragmentManager().executePendingTransactions();
            }
        });
        waitForEnd(mStartFragment, TestFragment.ENTER);
        assertTrue(mStartFragment.wasEndCalled(TestFragment.ENTER));
        mStartFragment.clearNotifications();
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                final View sharedElement = mActivity.findViewById(R.id.hello);
                assertEquals("source", sharedElement.getTransitionName());

                mEndFragment = new TestFragment(R.layout.fragment_end);
                mActivity.getFragmentManager().beginTransaction()
                        .replace(R.id.content, mEndFragment)
                        .addSharedElement(sharedElement, "destination")
                        .addToBackStack(null)
                        .commit();
                mActivity.getFragmentManager().executePendingTransactions();
            }
        });
        waitForEnd(mEndFragment, TestFragment.ENTER);
        assertTrue(mEndFragment.wasEndCalled(TestFragment.ENTER));
        assertTrue(mStartFragment.wasEndCalled(TestFragment.EXIT));
        assertTrue(mEndFragment.wasEndCalled(TestFragment.SHARED_ELEMENT_ENTER));
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                final View textView = mActivity.findViewById(R.id.hello);
                assertEquals("destination", textView.getTransitionName());
                mActivity.getFragmentManager().popBackStack();
                mActivity.getFragmentManager().executePendingTransactions();
            }
        });
        waitForEnd(mStartFragment, TestFragment.REENTER);
        assertTrue(mStartFragment.wasEndCalled(TestFragment.REENTER));
        assertTrue(mEndFragment.wasEndCalled(TestFragment.RETURN));
    }

    public void testFirstOutLastInTransition() throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStartFragment = new TestFragment(R.layout.fragment_start);
                mActivity.getFragmentManager().beginTransaction()
                        .replace(R.id.content, mStartFragment)
                        .commit();
                mActivity.getFragmentManager().executePendingTransactions();
            }
        });
        waitForEnd(mStartFragment, TestFragment.ENTER);
        assertTrue(mStartFragment.wasEndCalled(TestFragment.ENTER));
        mStartFragment.clearNotifications();

        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMidFragment = new TestFragment(R.layout.checkbox_layout);
                mEndFragment = new TestFragment(R.layout.fragment_end);
                mActivity.getFragmentManager().beginTransaction()
                        .replace(R.id.content, mMidFragment)
                        .replace(R.id.content, mEndFragment)
                        .addToBackStack(null)
                        .commit();
                mActivity.getFragmentManager().executePendingTransactions();
            }
        });
        waitForEnd(mEndFragment, TestFragment.ENTER);
        assertTrue(mEndFragment.wasEndCalled(TestFragment.ENTER));
        assertFalse(mEndFragment.wasEndCalled(TestFragment.EXIT));
        assertFalse(mEndFragment.wasEndCalled(TestFragment.RETURN));
        assertFalse(mEndFragment.wasEndCalled(TestFragment.REENTER));

        assertTrue(mStartFragment.wasEndCalled(TestFragment.EXIT));
        assertFalse(mStartFragment.wasEndCalled(TestFragment.ENTER));
        assertFalse(mStartFragment.wasEndCalled(TestFragment.RETURN));
        assertFalse(mStartFragment.wasEndCalled(TestFragment.REENTER));

        assertFalse(mMidFragment.wasStartCalled(TestFragment.ENTER));
        assertFalse(mMidFragment.wasStartCalled(TestFragment.EXIT));
        assertFalse(mMidFragment.wasStartCalled(TestFragment.REENTER));
        assertFalse(mMidFragment.wasStartCalled(TestFragment.RETURN));

        mStartFragment.clearNotifications();
        mEndFragment.clearNotifications();

        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                mActivity.getFragmentManager().popBackStack();
                mActivity.getFragmentManager().executePendingTransactions();
            }
        });
        waitForEnd(mEndFragment, TestFragment.RETURN);
        assertTrue(mEndFragment.wasEndCalled(TestFragment.RETURN));
        assertFalse(mMidFragment.wasStartCalled(TestFragment.ENTER));
        assertFalse(mMidFragment.wasStartCalled(TestFragment.EXIT));
        assertFalse(mMidFragment.wasStartCalled(TestFragment.REENTER));
        assertFalse(mMidFragment.wasStartCalled(TestFragment.RETURN));

        assertTrue(mStartFragment.wasStartCalled(TestFragment.REENTER));
        assertFalse(mStartFragment.wasStartCalled(TestFragment.ENTER));
        assertFalse(mStartFragment.wasStartCalled(TestFragment.EXIT));
        assertFalse(mStartFragment.wasStartCalled(TestFragment.RETURN));
    }

    public void testPopTwo() throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStartFragment = new TestFragment(R.layout.fragment_start);
                mActivity.getFragmentManager().beginTransaction()
                        .replace(R.id.content, mStartFragment)
                        .commit();
                mActivity.getFragmentManager().executePendingTransactions();
            }
        });
        waitForEnd(mStartFragment, TestFragment.ENTER);
        assertTrue(mStartFragment.wasEndCalled(TestFragment.ENTER));
        mStartFragment.clearNotifications();

        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMidFragment = new TestFragment(R.layout.checkbox_layout);
                mActivity.getFragmentManager().beginTransaction()
                        .replace(R.id.content, mMidFragment)
                        .addToBackStack(null)
                        .commit();
                mActivity.getFragmentManager().executePendingTransactions();
            }
        });
        waitForEnd(mMidFragment, TestFragment.ENTER);
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                mEndFragment = new TestFragment(R.layout.fragment_end);
                mActivity.getFragmentManager().beginTransaction()
                        .replace(R.id.content, mEndFragment)
                        .addToBackStack(null)
                        .commit();
                mActivity.getFragmentManager().executePendingTransactions();
            }
        });
        waitForEnd(mEndFragment, TestFragment.ENTER);
        assertTrue(mEndFragment.wasEndCalled(TestFragment.ENTER));
        assertFalse(mEndFragment.wasEndCalled(TestFragment.EXIT));
        assertFalse(mEndFragment.wasEndCalled(TestFragment.RETURN));
        assertFalse(mEndFragment.wasEndCalled(TestFragment.REENTER));

        assertTrue(mStartFragment.wasEndCalled(TestFragment.EXIT));
        assertFalse(mStartFragment.wasEndCalled(TestFragment.ENTER));
        assertFalse(mStartFragment.wasEndCalled(TestFragment.RETURN));
        assertFalse(mStartFragment.wasEndCalled(TestFragment.REENTER));

        assertTrue(mMidFragment.wasStartCalled(TestFragment.ENTER));
        assertTrue(mMidFragment.wasStartCalled(TestFragment.EXIT));
        assertFalse(mMidFragment.wasStartCalled(TestFragment.REENTER));
        assertFalse(mMidFragment.wasStartCalled(TestFragment.RETURN));

        mStartFragment.clearNotifications();
        mMidFragment.clearNotifications();
        mEndFragment.clearNotifications();

        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                FragmentManager fm = mActivity.getFragmentManager();
                int id = fm.getBackStackEntryAt(0).getId();
                fm.popBackStack(id, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fm.executePendingTransactions();
            }
        });
        waitForEnd(mEndFragment, TestFragment.RETURN);
        assertTrue(mEndFragment.wasEndCalled(TestFragment.RETURN));

        assertFalse(mMidFragment.wasStartCalled(TestFragment.ENTER));
        assertFalse(mMidFragment.wasStartCalled(TestFragment.EXIT));
        assertFalse(mMidFragment.wasStartCalled(TestFragment.REENTER));
        assertFalse(mMidFragment.wasStartCalled(TestFragment.RETURN));

        assertTrue(mStartFragment.wasStartCalled(TestFragment.REENTER));
        assertFalse(mStartFragment.wasStartCalled(TestFragment.ENTER));
        assertFalse(mStartFragment.wasStartCalled(TestFragment.EXIT));
        assertFalse(mStartFragment.wasStartCalled(TestFragment.RETURN));
    }

    public void testNullTransition() throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStartFragment = new TestFragment(R.layout.fragment_start);
                mStartFragment.clearTransitions();
                mActivity.getFragmentManager().beginTransaction()
                        .replace(R.id.content, mStartFragment)
                        .commit();
                mActivity.getFragmentManager().executePendingTransactions();
            }
        });
        waitForStart(mStartFragment, TestFragment.ENTER);
        // No transitions
        assertFalse(mStartFragment.wasStartCalled(TestFragment.ENTER));

        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMidFragment = new TestFragment(R.layout.checkbox_layout);
                mEndFragment = new TestFragment(R.layout.fragment_end);
                mEndFragment.clearTransitions();
                mActivity.getFragmentManager().beginTransaction()
                        .replace(R.id.content, mMidFragment)
                        .replace(R.id.content, mEndFragment)
                        .addToBackStack(null)
                        .commit();
                mActivity.getFragmentManager().executePendingTransactions();
            }
        });
        waitForStart(mEndFragment, TestFragment.ENTER);
        assertFalse(mEndFragment.wasEndCalled(TestFragment.ENTER));
        assertFalse(mEndFragment.wasEndCalled(TestFragment.EXIT));
        assertFalse(mEndFragment.wasEndCalled(TestFragment.RETURN));
        assertFalse(mEndFragment.wasEndCalled(TestFragment.REENTER));

        assertFalse(mStartFragment.wasEndCalled(TestFragment.EXIT));
        assertFalse(mStartFragment.wasEndCalled(TestFragment.ENTER));
        assertFalse(mStartFragment.wasEndCalled(TestFragment.RETURN));
        assertFalse(mStartFragment.wasEndCalled(TestFragment.REENTER));

        assertFalse(mMidFragment.wasStartCalled(TestFragment.ENTER));
        assertFalse(mMidFragment.wasStartCalled(TestFragment.EXIT));
        assertFalse(mMidFragment.wasStartCalled(TestFragment.REENTER));
        assertFalse(mMidFragment.wasStartCalled(TestFragment.RETURN));

        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                mActivity.getFragmentManager().popBackStack();
                mActivity.getFragmentManager().executePendingTransactions();
            }
        });
        waitForStart(mEndFragment, TestFragment.RETURN);
        assertFalse(mEndFragment.wasEndCalled(TestFragment.RETURN));
        assertFalse(mMidFragment.wasStartCalled(TestFragment.ENTER));
        assertFalse(mMidFragment.wasStartCalled(TestFragment.EXIT));
        assertFalse(mMidFragment.wasStartCalled(TestFragment.REENTER));
        assertFalse(mMidFragment.wasStartCalled(TestFragment.RETURN));

        assertFalse(mStartFragment.wasStartCalled(TestFragment.REENTER));
        assertFalse(mStartFragment.wasStartCalled(TestFragment.ENTER));
        assertFalse(mStartFragment.wasStartCalled(TestFragment.EXIT));
        assertFalse(mStartFragment.wasStartCalled(TestFragment.RETURN));
    }

    public void testRemoveAdded() throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStartFragment = new TestFragment(R.layout.fragment_start);
                mActivity.getFragmentManager().beginTransaction()
                        .replace(R.id.content, mStartFragment)
                        .commit();
                mActivity.getFragmentManager().executePendingTransactions();
            }
        });
        waitForEnd(mStartFragment, TestFragment.ENTER);
        assertTrue(mStartFragment.wasEndCalled(TestFragment.ENTER));
        mStartFragment.clearNotifications();
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                mEndFragment = new TestFragment(R.layout.fragment_end);
                mActivity.getFragmentManager().beginTransaction()
                        .replace(R.id.content, mEndFragment)
                        .replace(R.id.content, mStartFragment)
                        .replace(R.id.content, mEndFragment)
                        .addToBackStack(null)
                        .commit();
                mActivity.getFragmentManager().executePendingTransactions();
            }
        });
        waitForEnd(mEndFragment, TestFragment.ENTER);
        assertTrue(mEndFragment.wasEndCalled(TestFragment.ENTER));
        assertTrue(mStartFragment.wasEndCalled(TestFragment.EXIT));
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                mActivity.getFragmentManager().popBackStack();
                mActivity.getFragmentManager().executePendingTransactions();
            }
        });
        waitForEnd(mStartFragment, TestFragment.REENTER);
        assertTrue(mStartFragment.wasEndCalled(TestFragment.REENTER));
        assertTrue(mEndFragment.wasEndCalled(TestFragment.RETURN));
    }

    public void testAddRemoved() throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStartFragment = new TestFragment(R.layout.fragment_start);
                mActivity.getFragmentManager().beginTransaction()
                        .replace(R.id.content, mStartFragment)
                        .commit();
                mActivity.getFragmentManager().executePendingTransactions();
            }
        });
        waitForEnd(mStartFragment, TestFragment.ENTER);
        assertTrue(mStartFragment.wasEndCalled(TestFragment.ENTER));
        mStartFragment.clearNotifications();
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                mEndFragment = new TestFragment(R.layout.fragment_end);
                mActivity.getFragmentManager().beginTransaction()
                        .replace(R.id.content, mEndFragment)
                        .replace(R.id.content, mStartFragment)
                        .addToBackStack(null)
                        .commit();
                mActivity.getFragmentManager().executePendingTransactions();
            }
        });
        waitForStart(mStartFragment, TestFragment.ENTER);
        assertFalse(mStartFragment.wasStartCalled(TestFragment.ENTER));
        assertFalse(mStartFragment.wasStartCalled(TestFragment.EXIT));
        assertFalse(mEndFragment.wasStartCalled(TestFragment.ENTER));
        assertFalse(mEndFragment.wasStartCalled(TestFragment.EXIT));
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                mActivity.getFragmentManager().popBackStack();
                mActivity.getFragmentManager().executePendingTransactions();
            }
        });
        waitForStart(mStartFragment, TestFragment.REENTER);
        assertFalse(mStartFragment.wasStartCalled(TestFragment.REENTER));
        assertFalse(mStartFragment.wasStartCalled(TestFragment.RETURN));
        assertFalse(mEndFragment.wasStartCalled(TestFragment.REENTER));
        assertFalse(mEndFragment.wasStartCalled(TestFragment.RETURN));
    }

    private boolean waitForStart(TestFragment fragment, int key) throws InterruptedException {
        final boolean started;
        WaitForTransition listener = new WaitForTransition(key, true);
        fragment.setOnTransitionListener(listener);
        final long endTime = SystemClock.uptimeMillis() + 100;
        synchronized (listener) {
            long waitTime;
            while ((waitTime = endTime - SystemClock.uptimeMillis()) > 0 &&
                    !listener.isDone()) {
                listener.wait(waitTime);
            }
            started = listener.isDone();
        }
        fragment.setOnTransitionListener(null);
        getInstrumentation().waitForIdleSync();
        return started;
    }

    private boolean waitForEnd(TestFragment fragment, int key) throws InterruptedException {
        if (!waitForStart(fragment, key)) {
            return false;
        }
        final boolean ended;
        WaitForTransition listener = new WaitForTransition(key, false);
        fragment.setOnTransitionListener(listener);
        final long endTime = SystemClock.uptimeMillis() + 400;
        synchronized (listener) {
            long waitTime;
            while ((waitTime = endTime - SystemClock.uptimeMillis()) > 0 &&
                    !listener.isDone()) {
                listener.wait(waitTime);
            }
            ended = listener.isDone();
        }
        fragment.setOnTransitionListener(null);
        getInstrumentation().waitForIdleSync();
        return ended;
    }

    private static class WaitForTransition implements OnTransitionListener {
        final int key;
        final boolean isStart;
        boolean isDone;

        public WaitForTransition(int key, boolean isStart) {
            this.key = key;
            this.isStart = isStart;
        }

        protected boolean isComplete(TestFragment fragment) {
            if (isStart) {
                return fragment.wasStartCalled(key);
            } else {
                return fragment.wasEndCalled(key);
            }
        }

        public synchronized boolean isDone() {
            return isDone;
        }

        @Override
        public synchronized void onTransition(TestFragment fragment) {
            isDone = isComplete(fragment);
            if (isDone) {
                notifyAll();
            }
        }
    }

}
