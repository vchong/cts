package com.android.compatibility.common.tradefed.result;

import com.android.compatibility.common.tradefed.build.CompatibilityBuildInfo;
import com.android.compatibility.common.tradefed.testtype.CompatibilityTest;
import com.android.compatibility.common.util.AbiUtils;
import com.android.compatibility.common.util.ICaseResult;
import com.android.compatibility.common.util.IInvocationResult;
import com.android.compatibility.common.util.IModuleResult;
import com.android.compatibility.common.util.ITestResult;
import com.android.compatibility.common.util.InvocationResult;
import com.android.compatibility.common.util.MetricsStore;
import com.android.compatibility.common.util.ReportLog;
import com.android.compatibility.common.util.TestStatus;
import com.android.compatibility.common.util.XmlResultHandler;
import com.android.ddmlib.Log;
import com.android.ddmlib.Log.LogLevel;
import com.android.ddmlib.testrunner.TestIdentifier;
import com.android.tradefed.build.IBuildInfo;
import com.android.tradefed.config.Option;
import com.android.tradefed.config.Option.Importance;
import com.android.tradefed.config.OptionClass;
import com.android.tradefed.result.ITestInvocationListener;
import com.android.tradefed.result.InputStreamSource;
import com.android.tradefed.result.LogDataType;
import com.android.tradefed.result.LogFileSaver;
import com.android.tradefed.result.TestSummary;
import com.android.tradefed.util.FileUtil;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Reporter for Compatibility test results.
 */
@OptionClass(alias="result-reporter")
public class ResultReporter implements ITestInvocationListener {

    private static final String RESULT_KEY = "COMPATIBILITY_TEST_RESULT";
    private static final String DEVICE_INFO_COLLECTOR = "com.android.compatibility.deviceinfo";
    private static final String[] RESULT_RESOURCES = {
        "compatibility-result.css",
        "compatibility-result.xsd",
        "compatibility-result.xsl",
        "logo.png",
        "newrule-green.png"};

    @Option(name = CompatibilityTest.RETRY_OPTION,
            shortName = 'r',
            description = "retry a previous session.",
            importance = Importance.IF_UNSET)
    private Integer mRetrySessionId = null;

    private String mDeviceSerial;

    private boolean mInitialized;
    private IInvocationResult mResult;
    private File mResultDir = null;
    private File mLogDir = null;
    private long mStartTime;
    private boolean mIsDeviceInfoRun;
    private IModuleResult mCurrentModuleResult;
    private ICaseResult mCurrentCaseResult;
    private ITestResult mCurrentResult;
    private CompatibilityBuildInfo mBuild;

    /**
     * {@inheritDoc}
     */
    @Override
    public void invocationStarted(IBuildInfo buildInfo) {
        mInitialized = false;
        mBuild = (CompatibilityBuildInfo) buildInfo;
        mDeviceSerial = buildInfo.getDeviceSerial();
        if (mDeviceSerial == null) {
            mDeviceSerial = "unknown_device";
        }
        long time = System.currentTimeMillis();
        String dirSuffix = getDirSuffix(time);
        if (mRetrySessionId != null) {
            Log.d(mDeviceSerial, String.format("Retrying session %d", mRetrySessionId));
            List<IInvocationResult> results = null;
            try {
                results = XmlResultHandler.getResults(
                        mBuild.getResultsDir());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (results != null && mRetrySessionId >= 0 && mRetrySessionId < results.size()) {
                mResult = results.get(mRetrySessionId);
            } else {
                throw new IllegalArgumentException(
                        String.format("Could not find session %d",mRetrySessionId));
            }
            mStartTime = mResult.getStartTime();
            mResultDir = mResult.getResultDir();
        } else {
            mStartTime = time;
            try {
                mResultDir = new File(mBuild.getResultsDir(), dirSuffix);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (mResultDir != null && mResultDir.mkdirs()) {
                Log.logAndDisplay(LogLevel.INFO, mDeviceSerial,
                        String.format("Created result dir %s", mResultDir.getAbsolutePath()));
            } else {
                throw new IllegalArgumentException(String.format("Could not create result dir %s",
                        mResultDir.getAbsolutePath()));
            }
            mResult = new InvocationResult(mStartTime, mResultDir);
        }
        try {
            mLogDir = new File(mBuild.getLogsDir(), dirSuffix);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (mLogDir != null && mLogDir.mkdirs()) {
            Log.logAndDisplay(LogLevel.INFO, mDeviceSerial,
                    String.format("Created log dir %s", mLogDir.getAbsolutePath()));
        } else {
            throw new IllegalArgumentException(String.format("Could not create log dir %s",
                    mLogDir.getAbsolutePath()));
        }
        mInitialized = true;
    }

    /**
     * @return a {@link String} to use for directory suffixes created from the given time.
     */
    private String getDirSuffix(long time) {
        return new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss").format(new Date(time));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void testRunStarted(String id, int numTests) {
        Log.logAndDisplay(LogLevel.INFO, mDeviceSerial,
                String.format("Starting %s with %d tests", id, numTests));
        mIsDeviceInfoRun = AbiUtils.parseTestName(id).equals(DEVICE_INFO_COLLECTOR);
        if (!mIsDeviceInfoRun) {
            mCurrentModuleResult = mResult.getOrCreateModule(id);
            mCurrentModuleResult.setDeviceSerial(mDeviceSerial);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void testStarted(TestIdentifier test) {
        if (!mIsDeviceInfoRun) {
            mCurrentCaseResult = mCurrentModuleResult.getOrCreateResult(test.getClassName());
            mCurrentResult = mCurrentCaseResult.getOrCreateResult(test.getTestName());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void testEnded(TestIdentifier test, Map<String, String> metrics) {
        if (!mIsDeviceInfoRun) {
            // device test can have performance results in test metrics
            String perfResult = metrics.get(RESULT_KEY);
            ReportLog report = null;
            if (perfResult != null) {
                try {
                    report = ReportLog.parse(perfResult);
                } catch (XmlPullParserException | IOException e) {
                    e.printStackTrace();
                }
            } else {
                // host test should be checked into MetricsStore.
                report = MetricsStore.removeResult(
                        mDeviceSerial, mCurrentModuleResult.getAbi(), test.toString());
            }
            mCurrentResult.passed(report);
            Log.logAndDisplay(LogLevel.INFO, mDeviceSerial, String.format("%s passed", test));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void testIgnored(TestIdentifier test) {
        Log.logAndDisplay(LogLevel.INFO, mDeviceSerial, String.format("%s ignored", test));
        // ignore
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void testFailed(TestIdentifier test, String trace) {
        if (!mIsDeviceInfoRun) {
            mCurrentResult.failed(trace);
            Log.logAndDisplay(LogLevel.INFO, mDeviceSerial,
                    String.format("%s failed: %s", test, trace));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void testAssumptionFailure(TestIdentifier test, String trace) {
        if (!mIsDeviceInfoRun) {
            mCurrentResult.failed(trace);
            Log.logAndDisplay(LogLevel.INFO, mDeviceSerial,
                    String.format("%s failed assumption: %s", test, trace));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void testRunStopped(long elapsedTime) {
        Log.logAndDisplay(LogLevel.INFO, mDeviceSerial,
                String.format("%s stopped after %dms", mCurrentModuleResult.getId(), elapsedTime));
        // ignore
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void testRunEnded(long elapsedTime, Map<String, String> metrics) {
        if (mIsDeviceInfoRun) {
            mResult.populateDeviceInfoMetrics(metrics);
        } else {
            Log.logAndDisplay(LogLevel.INFO, mDeviceSerial,
                    String.format("%s completed in %dms. %d passed, %d failed, %d non executed",
                            mCurrentModuleResult.getId(),
                            elapsedTime,
                            mCurrentModuleResult.countResults(TestStatus.PASS),
                            mCurrentModuleResult.countResults(TestStatus.FAIL),
                            mCurrentModuleResult.countResults(TestStatus.NOT_EXECUTED)));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void testRunFailed(String id) {
        Log.logAndDisplay(LogLevel.INFO, mDeviceSerial, String.format("%s failed to run", id));
        // ignore
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TestSummary getSummary() {
        // ignore
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void invocationEnded(long elapsedTime) {
        Log.d(mDeviceSerial, String.format("ResultReporter.invocationEnded(%d)", elapsedTime));
        if (mInitialized) {
            Log.logAndDisplay(LogLevel.INFO, mDeviceSerial, String.format(
                    "Invocation completed in %dms. %d passed, %d failed, %d non executed",
                    elapsedTime,
                    mResult.countResults(TestStatus.PASS),
                    mResult.countResults(TestStatus.FAIL),
                    mResult.countResults(TestStatus.NOT_EXECUTED)));
            try {
                XmlResultHandler.writeResults(mBuild.getSuiteName(),
                        mBuild.getSuiteVersion(), mBuild.getSuitePlan(), mResult,
                        mResultDir, mStartTime, elapsedTime + mStartTime);
                copyFormattingFiles(mResultDir);
                zipResults(mResultDir);
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void invocationFailed(Throwable cause) {
        Log.d(mDeviceSerial, String.format("ResultReporter.invocationFailed(%s)", cause));
        mInitialized = false;
        // Clean up
        mResultDir.delete();
        mResultDir = null;
        mLogDir.delete();
        mLogDir = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void testLog(String name, LogDataType type, InputStreamSource stream) {
        Log.d(mDeviceSerial, String.format("ResultReporter.testLog(%s, %s, %s)", name, type,
                stream));
        try {
            LogFileSaver saver = new LogFileSaver(mLogDir);
            File logFile = saver.saveAndZipLogData(name, type, stream.createInputStream());
            Log.i(mDeviceSerial, String.format("Saved logs for %s in %s", name,
                    logFile.getAbsolutePath()));
        } catch (IOException e) {
            Log.e(mDeviceSerial, String.format("Failed to write log for %s", name));
            e.printStackTrace();
        }
    }

    /**
     * Copy the xml formatting files stored in this jar to the results directory
     *
     * @param resultsDir
     */
    static void copyFormattingFiles(File resultsDir) {
        for (String resultFileName : RESULT_RESOURCES) {
            InputStream configStream = XmlResultHandler.class.getResourceAsStream(
                    String.format("/report/%s", resultFileName));
            if (configStream != null) {
                File resultFile = new File(resultsDir, resultFileName);
                try {
                    FileUtil.writeToFile(configStream, resultFile);
                } catch (IOException e) {
                    Log.w(ResultReporter.class.getSimpleName(),
                            String.format("Failed to write %s to file", resultFileName));
                }
            } else {
                Log.w(ResultReporter.class.getSimpleName(),
                        String.format("Failed to load %s from jar", resultFileName));
            }
        }
    }

    /**
     * Zip the contents of the given results directory.
     *
     * @param resultsDir
     */
    @SuppressWarnings("deprecation")
    private static void zipResults(File resultsDir) {
        try {
            // create a file in parent directory, with same name as resultsDir
            File zipResultFile = new File(resultsDir.getParent(), String.format("%s.zip",
                    resultsDir.getName()));
            FileUtil.createZip(resultsDir, zipResultFile);
        } catch (IOException e) {
            Log.w(ResultReporter.class.getSimpleName(),
                    String.format("Failed to create zip for %s", resultsDir.getName()));
        }
    }

    /**
     * @return the mResult
     */
    public IInvocationResult getResult() {
        return mResult;
    }

}
