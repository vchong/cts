/*
 * Copyright (C) 2008 The Android Open Source Project
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Instrumentation;
import android.app.ActivityManager.ProcessErrorStateInfo;
import android.app.ActivityManager.RecentTaskInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Instrumentation.ActivityMonitor;
import android.app.Instrumentation.ActivityResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ConfigurationInfo;
import android.test.InstrumentationTestCase;

public class ActivityManagerTest extends InstrumentationTestCase {
    private static final String STUB_PACKAGE_NAME = "com.android.cts.app.stub";
    private static final int WAITFOR_MSEC = 5000;
    private static final String SERVICE_NAME = "android.app.cts.MockService";
    private static final int WAIT_TIME = 2000;
    // A secondary test activity from another APK.
    private static final String SIMPLE_PACKAGE_NAME = "com.android.cts.launcherapps.simpleapp";
    private static final String SIMPLE_ACTIVITY = ".SimpleActivity";
    // The action sent back by the SIMPLE_APP after a restart.
    private static final String ACTIVITY_LAUNCHED_ACTION =
            "com.android.cts.launchertests.LauncherAppsTests.LAUNCHED_ACTION";

    public static final int RESULT_PASS = 1;
    public static final int RESULT_FAIL = 2;
    public static final int RESULT_TIMEOUT = 3;

    private Context mContext;
    private ActivityManager mActivityManager;
    private Intent mIntent;
    private List<Activity> mStartedActivityList;
    private int mErrorProcessID;
    private Instrumentation mInstrumentation;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mInstrumentation = getInstrumentation();
        mContext = mInstrumentation.getContext();
        mActivityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        mStartedActivityList = new ArrayList<Activity>();
        mErrorProcessID = -1;
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        if (mIntent != null) {
            mInstrumentation.getContext().stopService(mIntent);
        }
        for (int i = 0; i < mStartedActivityList.size(); i++) {
            mStartedActivityList.get(i).finish();
        }
        if (mErrorProcessID != -1) {
            android.os.Process.killProcess(mErrorProcessID);
        }
    }

    public void testGetRecentTasks() throws Exception {
        int maxNum = 0;
        int flags = 0;

        List<RecentTaskInfo> recentTaskList;
        // Test parameter: maxNum is set to 0
        recentTaskList = mActivityManager.getRecentTasks(maxNum, flags);
        assertNotNull(recentTaskList);
        assertTrue(recentTaskList.size() == 0);
        // Test parameter: maxNum is set to 50
        maxNum = 50;
        recentTaskList = mActivityManager.getRecentTasks(maxNum, flags);
        assertNotNull(recentTaskList);
        // start recent1_activity.
        startSubActivity(ActivityManagerRecentOneActivity.class);
        Thread.sleep(WAIT_TIME);
        // start recent2_activity
        startSubActivity(ActivityManagerRecentTwoActivity.class);
        Thread.sleep(WAIT_TIME);
        /*
         * assert both recent1_activity and recent2_activity exist in the recent
         * tasks list. Moreover,the index of the recent2_activity is smaller
         * than the index of recent1_activity
         */
        recentTaskList = mActivityManager.getRecentTasks(maxNum, flags);
        int indexRecentOne = -1;
        int indexRecentTwo = -1;
        int i = 0;
        for (RecentTaskInfo rti : recentTaskList) {
            if (rti.baseIntent.getComponent().getClassName().equals(
                    ActivityManagerRecentOneActivity.class.getName())) {
                indexRecentOne = i;
            } else if (rti.baseIntent.getComponent().getClassName().equals(
                    ActivityManagerRecentTwoActivity.class.getName())) {
                indexRecentTwo = i;
            }
            i++;
        }
        assertTrue(indexRecentOne != -1 && indexRecentTwo != -1);
        assertTrue(indexRecentTwo < indexRecentOne);

        try {
            mActivityManager.getRecentTasks(-1, 0);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    public void testGetRecentTasksLimitedToCurrentAPK() throws Exception {
        int maxNum = 0;
        int flags = 0;

        // Check the number of tasks at this time.
        List<RecentTaskInfo>  recentTaskList;
        recentTaskList = mActivityManager.getRecentTasks(maxNum, flags);
        int numberOfEntriesFirstRun = recentTaskList.size();

        // Start another activity from another APK.
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClassName(SIMPLE_PACKAGE_NAME, SIMPLE_PACKAGE_NAME + SIMPLE_ACTIVITY);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ActivityLaunchedReceiver receiver = new ActivityLaunchedReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTIVITY_LAUNCHED_ACTION);
        mInstrumentation.getTargetContext().registerReceiver(receiver, filter);
        mContext.startActivity(intent);

        // Make sure the activity has really started.
        assertEquals(RESULT_PASS, receiver.waitForActivity());
        mInstrumentation.getTargetContext().unregisterReceiver(receiver);

        // There shouldn't be any more tasks in this list at this time.
        recentTaskList = mActivityManager.getRecentTasks(maxNum, flags);
        int numberOfEntriesSecondRun = recentTaskList.size();
        assertTrue(numberOfEntriesSecondRun == numberOfEntriesFirstRun);
    }

    private static class ActivityLaunchedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTIVITY_LAUNCHED_ACTION)) {
                synchronized(this) {
                   notifyAll();
                }
            }
        }

        public int waitForActivity() {
            synchronized(this) {
                try {
                    wait(5000);
                    return RESULT_PASS;
                } catch (InterruptedException e) {
                }
            }
            return RESULT_TIMEOUT;
        }
    }

    private final <T extends Activity> void startSubActivity(Class<T> activityClass) {
        final Instrumentation.ActivityResult result = new ActivityResult(0, new Intent());
        final ActivityMonitor monitor = new ActivityMonitor(activityClass.getName(), result, false);
        mInstrumentation.addMonitor(monitor);
        launchActivity(STUB_PACKAGE_NAME, activityClass, null);
        mStartedActivityList.add(monitor.waitForActivity());
    }

    public void testGetRunningTasks() {
        // Test illegal parameter
        List<RunningTaskInfo> runningTaskList;
        runningTaskList = mActivityManager.getRunningTasks(-1);
        assertTrue(runningTaskList.size() == 0);

        runningTaskList = mActivityManager.getRunningTasks(0);
        assertTrue(runningTaskList.size() == 0);

        runningTaskList = mActivityManager.getRunningTasks(20);
        int taskSize = runningTaskList.size();
        assertTrue(taskSize >= 0 && taskSize <= 20);

        // start recent1_activity.
        startSubActivity(ActivityManagerRecentOneActivity.class);
        // start recent2_activity
        startSubActivity(ActivityManagerRecentTwoActivity.class);

        /*
         * assert both recent1_activity and recent2_activity exist in the
         * running tasks list. Moreover,the index of the recent2_activity is
         * smaller than the index of recent1_activity
         */
        runningTaskList = mActivityManager.getRunningTasks(20);
        int indexRecentOne = -1;
        int indexRecentTwo = -1;
        int i = 0;
        for (RunningTaskInfo rti : runningTaskList) {
            if (rti.baseActivity.getClassName().equals(
                    ActivityManagerRecentOneActivity.class.getName())) {
                indexRecentOne = i;
            } else if (rti.baseActivity.getClassName().equals(
                    ActivityManagerRecentTwoActivity.class.getName())) {
                indexRecentTwo = i;
            }
            i++;
        }
        assertTrue(indexRecentOne != -1 && indexRecentTwo != -1);
        assertTrue(indexRecentTwo < indexRecentOne);
    }

    public void testGetRunningServices() throws Exception {
        // Test illegal parameter
        List<RunningServiceInfo> runningServiceInfo;
        runningServiceInfo = mActivityManager.getRunningServices(-1);
        assertTrue(runningServiceInfo.size() == 0);

        runningServiceInfo = mActivityManager.getRunningServices(0);
        assertTrue(runningServiceInfo.size() == 0);

        runningServiceInfo = mActivityManager.getRunningServices(5);
        assertTrue(runningServiceInfo.size() >= 0 && runningServiceInfo.size() <= 5);

        Intent intent = new Intent();
        intent.setClass(mInstrumentation.getTargetContext(), MockService.class);
        mInstrumentation.getTargetContext().startService(intent);
        Thread.sleep(WAIT_TIME);

        runningServiceInfo = mActivityManager.getRunningServices(Integer.MAX_VALUE);
        boolean foundService = false;
        for (RunningServiceInfo rs : runningServiceInfo) {
            if (rs.service.getClassName().equals(SERVICE_NAME)) {
                foundService = true;
                break;
            }
        }
        assertTrue(foundService);
        mContext.stopService(intent);
        Thread.sleep(WAIT_TIME);
    }

    public void testGetMemoryInfo() {
        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
        mActivityManager.getMemoryInfo(outInfo);
        assertTrue(outInfo.lowMemory == (outInfo.availMem <= outInfo.threshold));
    }

    public void testGetRunningAppProcesses() throws Exception {
        List<RunningAppProcessInfo> list = mActivityManager.getRunningAppProcesses();
        assertNotNull(list);
        final String SYSTEM_PROCESS = "system";
        boolean hasSystemProcess = false;
        // The package name is also the default name for the application process
        final String TEST_PROCESS = STUB_PACKAGE_NAME;
        boolean hasTestProcess = false;
        for (RunningAppProcessInfo ra : list) {
            if (ra.processName.equals(SYSTEM_PROCESS)) {
                hasSystemProcess = true;
            } else if (ra.processName.equals(TEST_PROCESS)) {
                hasTestProcess = true;
            }
        }
        // For security reasons the system process is not exposed.
        assertTrue(!hasSystemProcess && hasTestProcess);

        for (RunningAppProcessInfo ra : list) {
            if (ra.processName.equals("com.android.cts.app.stub:remote")) {
                fail("should be no process named com.android.cts.app.stub:remote");
            }
        }
        // start a new process
        mIntent = new Intent("android.app.REMOTESERVICE");
        mIntent.setPackage("com.android.cts.app.stub");
        mInstrumentation.getTargetContext().startService(mIntent);
        Thread.sleep(WAITFOR_MSEC);

        List<RunningAppProcessInfo> listNew = mActivityManager.getRunningAppProcesses();
        assertTrue(list.size() <= listNew.size());

        for (RunningAppProcessInfo ra : listNew) {
            if (ra.processName.equals("com.android.cts.app.stub:remote")) {
                return;
            }
        }
        fail("com.android.cts.app.stub:remote process should be available");
    }

    public void testGetProcessInErrorState() throws Exception {
        List<ActivityManager.ProcessErrorStateInfo> errList = null;
        errList = mActivityManager.getProcessesInErrorState();
    }

    public void testRestartPackage() {
    }

    public void testGetDeviceConfigurationInfo() {
        ConfigurationInfo conInf = mActivityManager.getDeviceConfigurationInfo();
        assertNotNull(conInf);
    }

    /**
     * Simple test for {@link ActivityManager.isUserAMonkey()} - verifies its false.
     *
     * TODO: test positive case
     */
    public void testIsUserAMonkey() {
        assertFalse(ActivityManager.isUserAMonkey());
    }

    /**
     * Verify that {@link ActivityManager.isRunningInTestHarness()} is false.
     */
    public void testIsRunningInTestHarness() {
        assertFalse("isRunningInTestHarness must be false in production builds",
                ActivityManager.isRunningInTestHarness());
    }
}
