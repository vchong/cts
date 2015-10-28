package com.android.cts.migration;

import com.android.tradefed.build.IBuildInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * A temporary helper to enable tests to work with both cts v1 and v2.
 */
public class MigrationHelper {

    private static final String COMPATIBILITY_BUILD_HELPER =
            "com.android.compatibility.common.tradefed.build.CompatibilityBuildHelper";
    private static final String CTS_BUILD_HELPER =
            "com.android.cts.tradefed.build.CtsBuildHelper";

    public static File getTestFile(IBuildInfo mBuild, String filename)
            throws FileNotFoundException {
        try {
            Class<?> cls = Class.forName(COMPATIBILITY_BUILD_HELPER);
            Constructor<?> cons = cls.getConstructor(IBuildInfo.class);
            Object instance = cons.newInstance(mBuild);
            Method method = cls.getMethod("getTestsDir");
            File dir = (File) method.invoke(instance);
            File file = new File(dir, filename);
            if (file.exists()) {
                return file;
            }
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException |
                IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            // Ignore and fall back to CtsBuildHelper
            e.printStackTrace();
        }
        try {
            Class<?> cls = Class.forName(CTS_BUILD_HELPER);
            Method builder = cls.getMethod("createBuildHelper", IBuildInfo.class);
            Object helper = builder.invoke(null, mBuild);
            Method method = cls.getMethod("getTestApp", String.class);
            File file = (File) method.invoke(helper, filename);
            if (file.exists()) {
                return file;
            }
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                IllegalArgumentException | InvocationTargetException e) {
            // Ignore
            e.printStackTrace();
        }
        throw new FileNotFoundException("Couldn't load file " + filename);
    }

}
