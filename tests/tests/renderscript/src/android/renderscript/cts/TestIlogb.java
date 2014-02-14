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

// Don't edit this file!  It is auto-generated by frameworks/rs/api/gen_runtime.

package android.renderscript.cts;

import android.renderscript.Allocation;
import android.renderscript.RSRuntimeException;
import android.renderscript.Element;

public class TestIlogb extends RSBaseCompute {

    private ScriptC_TestIlogb script;
    private ScriptC_TestIlogbRelaxed scriptRelaxed;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        script = new ScriptC_TestIlogb(mRS);
        scriptRelaxed = new ScriptC_TestIlogbRelaxed(mRS);
    }

    public class ArgumentsFloatInt {
        public float in;
        public int out;

        public int ulf;
        public int ulfRelaxed;
    }

    private void checkIlogbFloatInt() {
        Allocation in = CreateRandomAllocation(mRS, Element.DataType.FLOAT_32, 1, 0xb4601a22fc81377dL);
        try {
            Allocation out = Allocation.createSized(mRS, GetElement(mRS, Element.DataType.SIGNED_32, 1), INPUTSIZE);
            script.forEach_testIlogbFloatInt(in, out);
            verifyResultsIlogbFloatInt(in, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testIlogbFloatInt: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, GetElement(mRS, Element.DataType.SIGNED_32, 1), INPUTSIZE);
            scriptRelaxed.forEach_testIlogbFloatInt(in, out);
            verifyResultsIlogbFloatInt(in, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testIlogbFloatInt: " + e.toString());
        }
    }

    private void verifyResultsIlogbFloatInt(Allocation in, Allocation out, boolean relaxed) {
        float[] arrayIn = new float[INPUTSIZE * 1];
        in.copyTo(arrayIn);
        int[] arrayOut = new int[INPUTSIZE * 1];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 1 ; j++) {
                // Extract the inputs.
                ArgumentsFloatInt args = new ArgumentsFloatInt();
                args.in = arrayIn[i];
                // Figure out what the outputs should have been.
                CoreMathVerifier.computeIlogb(args);
                int ulf = relaxed ? args.ulfRelaxed : args.ulf;
                // Figure out what the outputs should have been.
                boolean valid = true;
                int neededUlf = 0;
                if (args.out != arrayOut[i * 1 + j]) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append(String.format("Input in: %x %.16f", Float.floatToRawIntBits(args.in), args.in));
                    message.append("\n");
                    message.append(String.format("Expected output out: %d", args.out));
                    message.append("\n");
                    message.append(String.format("Actual   output out: %d", arrayOut[i * 1 + j]));
                    if (args.out != arrayOut[i * 1 + j]) {
                        message.append(" FAIL");
                    }
                    message.append("\n");
                    assertTrue("Incorrect output for checkIlogbFloatInt" + (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkIlogbFloat2Int2() {
        Allocation in = CreateRandomAllocation(mRS, Element.DataType.FLOAT_32, 2, 0xc9f22a85624b6fb3L);
        try {
            Allocation out = Allocation.createSized(mRS, GetElement(mRS, Element.DataType.SIGNED_32, 2), INPUTSIZE);
            script.forEach_testIlogbFloat2Int2(in, out);
            verifyResultsIlogbFloat2Int2(in, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testIlogbFloat2Int2: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, GetElement(mRS, Element.DataType.SIGNED_32, 2), INPUTSIZE);
            scriptRelaxed.forEach_testIlogbFloat2Int2(in, out);
            verifyResultsIlogbFloat2Int2(in, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testIlogbFloat2Int2: " + e.toString());
        }
    }

    private void verifyResultsIlogbFloat2Int2(Allocation in, Allocation out, boolean relaxed) {
        float[] arrayIn = new float[INPUTSIZE * 2];
        in.copyTo(arrayIn);
        int[] arrayOut = new int[INPUTSIZE * 2];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 2 ; j++) {
                // Extract the inputs.
                ArgumentsFloatInt args = new ArgumentsFloatInt();
                args.in = arrayIn[i * 2 + j];
                // Figure out what the outputs should have been.
                CoreMathVerifier.computeIlogb(args);
                int ulf = relaxed ? args.ulfRelaxed : args.ulf;
                // Figure out what the outputs should have been.
                boolean valid = true;
                int neededUlf = 0;
                if (args.out != arrayOut[i * 2 + j]) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append(String.format("Input in: %x %.16f", Float.floatToRawIntBits(args.in), args.in));
                    message.append("\n");
                    message.append(String.format("Expected output out: %d", args.out));
                    message.append("\n");
                    message.append(String.format("Actual   output out: %d", arrayOut[i * 2 + j]));
                    if (args.out != arrayOut[i * 2 + j]) {
                        message.append(" FAIL");
                    }
                    message.append("\n");
                    assertTrue("Incorrect output for checkIlogbFloat2Int2" + (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkIlogbFloat3Int3() {
        Allocation in = CreateRandomAllocation(mRS, Element.DataType.FLOAT_32, 3, 0xc9f3f3a0612885b9L);
        try {
            Allocation out = Allocation.createSized(mRS, GetElement(mRS, Element.DataType.SIGNED_32, 3), INPUTSIZE);
            script.forEach_testIlogbFloat3Int3(in, out);
            verifyResultsIlogbFloat3Int3(in, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testIlogbFloat3Int3: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, GetElement(mRS, Element.DataType.SIGNED_32, 3), INPUTSIZE);
            scriptRelaxed.forEach_testIlogbFloat3Int3(in, out);
            verifyResultsIlogbFloat3Int3(in, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testIlogbFloat3Int3: " + e.toString());
        }
    }

    private void verifyResultsIlogbFloat3Int3(Allocation in, Allocation out, boolean relaxed) {
        float[] arrayIn = new float[INPUTSIZE * 4];
        in.copyTo(arrayIn);
        int[] arrayOut = new int[INPUTSIZE * 4];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 3 ; j++) {
                // Extract the inputs.
                ArgumentsFloatInt args = new ArgumentsFloatInt();
                args.in = arrayIn[i * 4 + j];
                // Figure out what the outputs should have been.
                CoreMathVerifier.computeIlogb(args);
                int ulf = relaxed ? args.ulfRelaxed : args.ulf;
                // Figure out what the outputs should have been.
                boolean valid = true;
                int neededUlf = 0;
                if (args.out != arrayOut[i * 4 + j]) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append(String.format("Input in: %x %.16f", Float.floatToRawIntBits(args.in), args.in));
                    message.append("\n");
                    message.append(String.format("Expected output out: %d", args.out));
                    message.append("\n");
                    message.append(String.format("Actual   output out: %d", arrayOut[i * 4 + j]));
                    if (args.out != arrayOut[i * 4 + j]) {
                        message.append(" FAIL");
                    }
                    message.append("\n");
                    assertTrue("Incorrect output for checkIlogbFloat3Int3" + (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkIlogbFloat4Int4() {
        Allocation in = CreateRandomAllocation(mRS, Element.DataType.FLOAT_32, 4, 0xc9f5bcbb60059bbfL);
        try {
            Allocation out = Allocation.createSized(mRS, GetElement(mRS, Element.DataType.SIGNED_32, 4), INPUTSIZE);
            script.forEach_testIlogbFloat4Int4(in, out);
            verifyResultsIlogbFloat4Int4(in, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testIlogbFloat4Int4: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, GetElement(mRS, Element.DataType.SIGNED_32, 4), INPUTSIZE);
            scriptRelaxed.forEach_testIlogbFloat4Int4(in, out);
            verifyResultsIlogbFloat4Int4(in, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testIlogbFloat4Int4: " + e.toString());
        }
    }

    private void verifyResultsIlogbFloat4Int4(Allocation in, Allocation out, boolean relaxed) {
        float[] arrayIn = new float[INPUTSIZE * 4];
        in.copyTo(arrayIn);
        int[] arrayOut = new int[INPUTSIZE * 4];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 4 ; j++) {
                // Extract the inputs.
                ArgumentsFloatInt args = new ArgumentsFloatInt();
                args.in = arrayIn[i * 4 + j];
                // Figure out what the outputs should have been.
                CoreMathVerifier.computeIlogb(args);
                int ulf = relaxed ? args.ulfRelaxed : args.ulf;
                // Figure out what the outputs should have been.
                boolean valid = true;
                int neededUlf = 0;
                if (args.out != arrayOut[i * 4 + j]) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append(String.format("Input in: %x %.16f", Float.floatToRawIntBits(args.in), args.in));
                    message.append("\n");
                    message.append(String.format("Expected output out: %d", args.out));
                    message.append("\n");
                    message.append(String.format("Actual   output out: %d", arrayOut[i * 4 + j]));
                    if (args.out != arrayOut[i * 4 + j]) {
                        message.append(" FAIL");
                    }
                    message.append("\n");
                    assertTrue("Incorrect output for checkIlogbFloat4Int4" + (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    public void testIlogb() {
        checkIlogbFloatInt();
        checkIlogbFloat2Int2();
        checkIlogbFloat3Int3();
        checkIlogbFloat4Int4();
    }
}
