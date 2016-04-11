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
        assertTrue("Device is vulnerable to CVE-2013-6282. Please apply security patch at "
                   + "https://git.kernel.org/cgit/linux/kernel/git/torvalds/linux.git/"
                   + "commit/arch/arm/include/asm/uaccess.h?id="
                   + "8404663f81d212918ff85f493649a7991209fa04", doVrootTest());
    }

    public void testPerfEvent() throws Exception {
        assertFalse("Device is vulnerable to CVE-2013-2094. Please apply security patch "
                    + "at http://git.kernel.org/cgit/linux/kernel/git/torvalds/linux.git/"
                    + "commit/?id=8176cced706b5e5d15887584150764894e94e02f",
                    doPerfEventTest());
    }

    public void testPerfEvent2() throws Exception {
        assertTrue(doPerfEventTest2());
    }

    public void testSockDiag() throws Exception {
        int result = doSockDiagTest();
        assertFalse("Encountered unexpected error: " + result + ".", (result == -1));
        assertEquals(0, result);
    }

    public void testFutex() throws Exception {
        assertTrue("Device is vulnerable to CVE-2014-3153, a vulnerability in the futex() system "
                   + "call. Please apply the security patch at "
                   + "https://git.kernel.org/cgit/linux/kernel/git/torvalds/linux.git/commit/"
                   + "?id=e9c243a5a6de0be8e584c604d353412584b592f8",
                   doFutexTest());
    }

    public void testPipeReadV() throws Exception {
        assertTrue("Device is vulnerable to CVE-2015-1805 and/or CVE-2016-0774,"
                   + " a vulnerability in the pipe_read() function."
                   + " Please apply the following patches:\n"
                   + "https://git.kernel.org/cgit/linux/kernel/git/stable/linux-stable.git/commit/?id=75cf667b7fac08a7b21694adca7dff07361be68a\n"
                   + "https://git.kernel.org/cgit/linux/kernel/git/stable/linux-stable.git/commit/?id=feae3ca2e5e1a8f44aa6290255d3d9709985d0b2\n",
                   doPipeReadVTest());
    }

    /**
     * Returns true iff this device is vulnerable to CVE-2013-2094.
     * A patch for CVE-2013-2094 can be found at
     * http://git.kernel.org/cgit/linux/kernel/git/torvalds/linux.git/commit/?id=8176cced706b5e5d15887584150764894e94e02f
     */
    private static native boolean doPerfEventTest();

    /**
     * CVE-2013-4254
     *
     * Verifies that
     * http://git.kernel.org/cgit/linux/kernel/git/torvalds/linux.git/commit/?id=c95eb3184ea1a3a2551df57190c81da695e2144b
     * is applied to the system. Returns true if the patch is applied,
     * and crashes the system otherwise.
     *
     * While you're at it, please also apply the following patch:
     * http://git.kernel.org/cgit/linux/kernel/git/torvalds/linux.git/commit/?id=b88a2595b6d8aedbd275c07dfa784657b4f757eb
     *
     * Credit: https://github.com/deater/perf_event_tests/blob/master/exploits/arm_perf_exploit.c
     */
    private static native boolean doPerfEventTest2();

    /**
     * ANDROID-11234878 / CVE-2013-6282
     *
     * Returns true if the device is patched against the vroot vulnerability, false otherwise.
     *
     * The following patch addresses this bug:
     * https://git.kernel.org/cgit/linux/kernel/git/torvalds/linux.git/commit/arch/arm/include/asm/uaccess.h?id=8404663f81d212918ff85f493649a7991209fa04
     */
    private static native boolean doVrootTest();

    public void testCVE20141710() throws Exception {
        assertTrue("Device is vulnerable to CVE-2014-1710", doCVE20141710Test());
    }

    /**
     * CVE-2015-1805 and CVE-2016-0774
     *
     * Returns true if the patches are applied, crashes the system otherwise.
     *
     * Detects if the following patches are present.
     * https://git.kernel.org/cgit/linux/kernel/git/stable/linux-stable.git/commit/?id=75cf667b7fac08a7b21694adca7dff07361be68a
     * https://git.kernel.org/cgit/linux/kernel/git/stable/linux-stable.git/commit/?id=feae3ca2e5e1a8f44aa6290255d3d9709985d0b2
     *
     * b/27275324 and b/27721803
     */
    private static native boolean doPipeReadVTest();

    /**
     * ANDROID-15455425 / CVE-2014-3153
     *
     * Returns true if the device is patched against the futex() system call vulnerability.
     *
     * More information on this vulnerability is at http://seclists.org/oss-sec/2014/q2/467 and
     * the patch is at:
     * https://git.kernel.org/cgit/linux/kernel/git/torvalds/linux.git/commit/?id=e9c243a5a6de0be8e584c604d353412584b592f8
     */
    private static native boolean doFutexTest();

    /**
     * Returns true if the device is immune to CVE-2014-1710,
     * false if the device is vulnerable.
     */
    private static native boolean doCVE20141710Test();

    /**
     * Hangs if device is vulnerable to CVE-2013-1763, returns -1 if
     * unexpected error occurs, 0 otherwise.
     */
    private static native int doSockDiagTest();
}
