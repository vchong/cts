/*
 * Copyright (C) 2013 The Android Open Source Project
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

package android.security.cts;

import junit.framework.TestCase;

public class NativeCodeTest extends TestCase {

    static {
        System.loadLibrary("ctssecurity_jni");
    }

    public void testVroot() throws Exception {
        assertTrue(doVrootTest());
    }

    public void testPerfEvent() throws Exception {
        assertFalse("Device is vulnerable to CVE-2013-2094. Please apply security patch "
                    + "at http://git.kernel.org/cgit/linux/kernel/git/torvalds/linux.git/"
                    + "commit/?id=8176cced706b5e5d15887584150764894e94e02f",
                    doPerfEventTest());
    }

    public void testSockDiag() throws Exception {
        int result = doSockDiagTest();
        assertFalse("Encountered unexpected error: " + result + ".", (result == -1));
        assertEquals(0, result);
    }

    /**
     * Returns true iff this device is vulnerable to CVE-2013-2094.
     * A patch for CVE-2013-2094 can be found at
     * http://git.kernel.org/cgit/linux/kernel/git/torvalds/linux.git/commit/?id=8176cced706b5e5d15887584150764894e94e02f
     */
    private static native boolean doPerfEventTest();

    /**
     * Hangs if device is vulnerable to CVE-2013-1763, returns -1 if
     * unexpected error occurs, 0 otherwise.
     */
    private static native int doSockDiagTest();

    /**
     * ANDROID-11234878
     *
     * Returns true if the device is patched against the vroot
     * vulnerability. Returns false if there was some problem running
     * the test (for example, out of memory), or the test fails but wasn't
     * able to crash the device. Most of the time, however, the device will
     * crash if the vulnerability is present.
     *
     * The following patch addresses this bug:
     * https://git.kernel.org/cgit/linux/kernel/git/torvalds/linux.git/commit/arch/arm/include/asm/uaccess.h?id=8404663f81d212918ff85f493649a7991209fa04
     */
    private static native boolean doVrootTest();
}
