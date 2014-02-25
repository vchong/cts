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

public class TestRootn extends RSBaseCompute {

    private ScriptC_TestRootn script;
    private ScriptC_TestRootnRelaxed scriptRelaxed;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        script = new ScriptC_TestRootn(mRS);
        scriptRelaxed = new ScriptC_TestRootnRelaxed(mRS);
    }

    public class ArgumentsFloatIntFloat {
        public float inV;
        public int inN;
        public float out;

        public int ulf;
        public int ulfRelaxed;
    }

    private void checkRootnFloatIntFloat() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 1, 0x37d0d9514daae0ccl, false);
        Allocation inN = createRandomAllocation(mRS, Element.DataType.SIGNED_32, 1, 0x37d0d9514daae0c4l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            script.set_gAllocInN(inN);
            script.forEach_testRootnFloatIntFloat(inV, out);
            verifyResultsRootnFloatIntFloat(inV, inN, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testRootnFloatIntFloat: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            scriptRelaxed.set_gAllocInN(inN);
            scriptRelaxed.forEach_testRootnFloatIntFloat(inV, out);
            verifyResultsRootnFloatIntFloat(inV, inN, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testRootnFloatIntFloat: " + e.toString());
        }
    }

    private void verifyResultsRootnFloatIntFloat(Allocation inV, Allocation inN, Allocation out, boolean relaxed) {
        float[] arrayInV = new float[INPUTSIZE * 1];
        inV.copyTo(arrayInV);
        int[] arrayInN = new int[INPUTSIZE * 1];
        inN.copyTo(arrayInN);
        float[] arrayOut = new float[INPUTSIZE * 1];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 1 ; j++) {
                // Extract the inputs.
                ArgumentsFloatIntFloat args = new ArgumentsFloatIntFloat();
                args.inV = arrayInV[i];
                args.inN = arrayInN[i];
                // Figure out what the outputs should have been.
                CoreMathVerifier.computeRootn(args);
                int ulf = relaxed ? args.ulfRelaxed : args.ulf;
                // Figure out what the outputs should have been.
                boolean valid = true;
                int neededUlf = 0;
                neededUlf = (int) (Math.abs(args.out - arrayOut[i * 1 + j]) / Math.ulp(args.out) + 0.5);
                if (neededUlf > ulf) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append(String.format("Input inV: %14.8g %8x %15a",
                            args.inV, Float.floatToRawIntBits(args.inV), args.inV));
                    message.append("\n");
                    message.append(String.format("Input inN: %d",
                            args.inN));
                    message.append("\n");
                    message.append(String.format("Expected output out: %14.8g %8x %15a",
                            args.out, Float.floatToRawIntBits(args.out), args.out));
                    message.append("\n");
                    message.append(String.format("Actual   output out: %14.8g %8x %15a",
                            arrayOut[i * 1 + j], Float.floatToRawIntBits(arrayOut[i * 1 + j]), arrayOut[i * 1 + j]));
                    neededUlf = (int) (Math.abs(args.out - arrayOut[i * 1 + j]) / Math.ulp(args.out) + 0.5);
                    if (neededUlf > ulf) {
                        message.append(String.format(" FAILED, ulf needed %d, specified %d", neededUlf, ulf));
                    }
                    message.append("\n");
                    assertTrue("Incorrect output for checkRootnFloatIntFloat" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkRootnFloat2Int2Float2() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 2, 0x2a7a849dcb32d88el, false);
        Allocation inN = createRandomAllocation(mRS, Element.DataType.SIGNED_32, 2, 0x2a7a849dcb32d886l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            script.set_gAllocInN(inN);
            script.forEach_testRootnFloat2Int2Float2(inV, out);
            verifyResultsRootnFloat2Int2Float2(inV, inN, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testRootnFloat2Int2Float2: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            scriptRelaxed.set_gAllocInN(inN);
            scriptRelaxed.forEach_testRootnFloat2Int2Float2(inV, out);
            verifyResultsRootnFloat2Int2Float2(inV, inN, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testRootnFloat2Int2Float2: " + e.toString());
        }
    }

    private void verifyResultsRootnFloat2Int2Float2(Allocation inV, Allocation inN, Allocation out, boolean relaxed) {
        float[] arrayInV = new float[INPUTSIZE * 2];
        inV.copyTo(arrayInV);
        int[] arrayInN = new int[INPUTSIZE * 2];
        inN.copyTo(arrayInN);
        float[] arrayOut = new float[INPUTSIZE * 2];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 2 ; j++) {
                // Extract the inputs.
                ArgumentsFloatIntFloat args = new ArgumentsFloatIntFloat();
                args.inV = arrayInV[i * 2 + j];
                args.inN = arrayInN[i * 2 + j];
                // Figure out what the outputs should have been.
                CoreMathVerifier.computeRootn(args);
                int ulf = relaxed ? args.ulfRelaxed : args.ulf;
                // Figure out what the outputs should have been.
                boolean valid = true;
                int neededUlf = 0;
                neededUlf = (int) (Math.abs(args.out - arrayOut[i * 2 + j]) / Math.ulp(args.out) + 0.5);
                if (neededUlf > ulf) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append(String.format("Input inV: %14.8g %8x %15a",
                            args.inV, Float.floatToRawIntBits(args.inV), args.inV));
                    message.append("\n");
                    message.append(String.format("Input inN: %d",
                            args.inN));
                    message.append("\n");
                    message.append(String.format("Expected output out: %14.8g %8x %15a",
                            args.out, Float.floatToRawIntBits(args.out), args.out));
                    message.append("\n");
                    message.append(String.format("Actual   output out: %14.8g %8x %15a",
                            arrayOut[i * 2 + j], Float.floatToRawIntBits(arrayOut[i * 2 + j]), arrayOut[i * 2 + j]));
                    neededUlf = (int) (Math.abs(args.out - arrayOut[i * 2 + j]) / Math.ulp(args.out) + 0.5);
                    if (neededUlf > ulf) {
                        message.append(String.format(" FAILED, ulf needed %d, specified %d", neededUlf, ulf));
                    }
                    message.append("\n");
                    assertTrue("Incorrect output for checkRootnFloat2Int2Float2" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkRootnFloat3Int3Float3() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 3, 0x5030c300c0e54547l, false);
        Allocation inN = createRandomAllocation(mRS, Element.DataType.SIGNED_32, 3, 0x5030c300c0e5453fl, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            script.set_gAllocInN(inN);
            script.forEach_testRootnFloat3Int3Float3(inV, out);
            verifyResultsRootnFloat3Int3Float3(inV, inN, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testRootnFloat3Int3Float3: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            scriptRelaxed.set_gAllocInN(inN);
            scriptRelaxed.forEach_testRootnFloat3Int3Float3(inV, out);
            verifyResultsRootnFloat3Int3Float3(inV, inN, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testRootnFloat3Int3Float3: " + e.toString());
        }
    }

    private void verifyResultsRootnFloat3Int3Float3(Allocation inV, Allocation inN, Allocation out, boolean relaxed) {
        float[] arrayInV = new float[INPUTSIZE * 4];
        inV.copyTo(arrayInV);
        int[] arrayInN = new int[INPUTSIZE * 4];
        inN.copyTo(arrayInN);
        float[] arrayOut = new float[INPUTSIZE * 4];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 3 ; j++) {
                // Extract the inputs.
                ArgumentsFloatIntFloat args = new ArgumentsFloatIntFloat();
                args.inV = arrayInV[i * 4 + j];
                args.inN = arrayInN[i * 4 + j];
                // Figure out what the outputs should have been.
                CoreMathVerifier.computeRootn(args);
                int ulf = relaxed ? args.ulfRelaxed : args.ulf;
                // Figure out what the outputs should have been.
                boolean valid = true;
                int neededUlf = 0;
                neededUlf = (int) (Math.abs(args.out - arrayOut[i * 4 + j]) / Math.ulp(args.out) + 0.5);
                if (neededUlf > ulf) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append(String.format("Input inV: %14.8g %8x %15a",
                            args.inV, Float.floatToRawIntBits(args.inV), args.inV));
                    message.append("\n");
                    message.append(String.format("Input inN: %d",
                            args.inN));
                    message.append("\n");
                    message.append(String.format("Expected output out: %14.8g %8x %15a",
                            args.out, Float.floatToRawIntBits(args.out), args.out));
                    message.append("\n");
                    message.append(String.format("Actual   output out: %14.8g %8x %15a",
                            arrayOut[i * 4 + j], Float.floatToRawIntBits(arrayOut[i * 4 + j]), arrayOut[i * 4 + j]));
                    neededUlf = (int) (Math.abs(args.out - arrayOut[i * 4 + j]) / Math.ulp(args.out) + 0.5);
                    if (neededUlf > ulf) {
                        message.append(String.format(" FAILED, ulf needed %d, specified %d", neededUlf, ulf));
                    }
                    message.append("\n");
                    assertTrue("Incorrect output for checkRootnFloat3Int3Float3" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkRootnFloat4Int4Float4() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 4, 0x75e70163b697b200l, false);
        Allocation inN = createRandomAllocation(mRS, Element.DataType.SIGNED_32, 4, 0x75e70163b697b1f8l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            script.set_gAllocInN(inN);
            script.forEach_testRootnFloat4Int4Float4(inV, out);
            verifyResultsRootnFloat4Int4Float4(inV, inN, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testRootnFloat4Int4Float4: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            scriptRelaxed.set_gAllocInN(inN);
            scriptRelaxed.forEach_testRootnFloat4Int4Float4(inV, out);
            verifyResultsRootnFloat4Int4Float4(inV, inN, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testRootnFloat4Int4Float4: " + e.toString());
        }
    }

    private void verifyResultsRootnFloat4Int4Float4(Allocation inV, Allocation inN, Allocation out, boolean relaxed) {
        float[] arrayInV = new float[INPUTSIZE * 4];
        inV.copyTo(arrayInV);
        int[] arrayInN = new int[INPUTSIZE * 4];
        inN.copyTo(arrayInN);
        float[] arrayOut = new float[INPUTSIZE * 4];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 4 ; j++) {
                // Extract the inputs.
                ArgumentsFloatIntFloat args = new ArgumentsFloatIntFloat();
                args.inV = arrayInV[i * 4 + j];
                args.inN = arrayInN[i * 4 + j];
                // Figure out what the outputs should have been.
                CoreMathVerifier.computeRootn(args);
                int ulf = relaxed ? args.ulfRelaxed : args.ulf;
                // Figure out what the outputs should have been.
                boolean valid = true;
                int neededUlf = 0;
                neededUlf = (int) (Math.abs(args.out - arrayOut[i * 4 + j]) / Math.ulp(args.out) + 0.5);
                if (neededUlf > ulf) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append(String.format("Input inV: %14.8g %8x %15a",
                            args.inV, Float.floatToRawIntBits(args.inV), args.inV));
                    message.append("\n");
                    message.append(String.format("Input inN: %d",
                            args.inN));
                    message.append("\n");
                    message.append(String.format("Expected output out: %14.8g %8x %15a",
                            args.out, Float.floatToRawIntBits(args.out), args.out));
                    message.append("\n");
                    message.append(String.format("Actual   output out: %14.8g %8x %15a",
                            arrayOut[i * 4 + j], Float.floatToRawIntBits(arrayOut[i * 4 + j]), arrayOut[i * 4 + j]));
                    neededUlf = (int) (Math.abs(args.out - arrayOut[i * 4 + j]) / Math.ulp(args.out) + 0.5);
                    if (neededUlf > ulf) {
                        message.append(String.format(" FAILED, ulf needed %d, specified %d", neededUlf, ulf));
                    }
                    message.append("\n");
                    assertTrue("Incorrect output for checkRootnFloat4Int4Float4" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    public void testRootn() {
        checkRootnFloatIntFloat();
        checkRootnFloat2Int2Float2();
        checkRootnFloat3Int3Float3();
        checkRootnFloat4Int4Float4();
    }
}
