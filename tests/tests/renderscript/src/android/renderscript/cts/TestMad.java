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

public class TestMad extends RSBaseCompute {

    private ScriptC_TestMad script;
    private ScriptC_TestMadRelaxed scriptRelaxed;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        script = new ScriptC_TestMad(mRS);
        scriptRelaxed = new ScriptC_TestMadRelaxed(mRS);
    }

    public class ArgumentsFloatFloatFloatFloat {
        public float inA;
        public float inB;
        public float inC;
        public float out;

        public int ulf;
        public int ulfRelaxed;
    }

    private void checkMadFloatFloatFloatFloat() {
        Allocation inA = CreateRandomAllocation(mRS, Element.DataType.FLOAT_32, 1, 0xb3b9b8429c39984L);
        Allocation inB = CreateRandomAllocation(mRS, Element.DataType.FLOAT_32, 1, 0xb3b9b8429c39984L);
        Allocation inC = CreateRandomAllocation(mRS, Element.DataType.FLOAT_32, 1, 0xb3b9b8429c39984L);
        try {
            Allocation out = Allocation.createSized(mRS, GetElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            script.set_gAllocInB(inB);
            script.set_gAllocInC(inC);
            script.forEach_testMadFloatFloatFloatFloat(inA, out);
            verifyResultsMadFloatFloatFloatFloat(inA, inB, inC, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testMadFloatFloatFloatFloat: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, GetElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            scriptRelaxed.set_gAllocInB(inB);
            scriptRelaxed.set_gAllocInC(inC);
            scriptRelaxed.forEach_testMadFloatFloatFloatFloat(inA, out);
            verifyResultsMadFloatFloatFloatFloat(inA, inB, inC, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testMadFloatFloatFloatFloat: " + e.toString());
        }
    }

    private void verifyResultsMadFloatFloatFloatFloat(Allocation inA, Allocation inB, Allocation inC, Allocation out, boolean relaxed) {
        float[] arrayInA = new float[INPUTSIZE * 1];
        inA.copyTo(arrayInA);
        float[] arrayInB = new float[INPUTSIZE * 1];
        inB.copyTo(arrayInB);
        float[] arrayInC = new float[INPUTSIZE * 1];
        inC.copyTo(arrayInC);
        float[] arrayOut = new float[INPUTSIZE * 1];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 1 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloatFloatFloat args = new ArgumentsFloatFloatFloatFloat();
                args.inA = arrayInA[i];
                args.inB = arrayInB[i];
                args.inC = arrayInC[i];
                // Figure out what the outputs should have been.
                CoreMathVerifier.computeMad(args);
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
                    message.append(String.format("Input inA: %x %.16f", Float.floatToRawIntBits(args.inA), args.inA));
                    message.append("\n");
                    message.append(String.format("Input inB: %x %.16f", Float.floatToRawIntBits(args.inB), args.inB));
                    message.append("\n");
                    message.append(String.format("Input inC: %x %.16f", Float.floatToRawIntBits(args.inC), args.inC));
                    message.append("\n");
                    message.append(String.format("Expected output out: %x %.16f", Float.floatToRawIntBits(args.out), args.out));
                    message.append("\n");
                    message.append(String.format("Actual   output out: %x %.16f", Float.floatToRawIntBits(arrayOut[i * 1 + j]), arrayOut[i * 1 + j]));
                    neededUlf = (int) (Math.abs(args.out - arrayOut[i * 1 + j]) / Math.ulp(args.out) + 0.5);
                    if (neededUlf > ulf) {
                        message.append(String.format(" FAILED, ulf needed %d, specified %d", neededUlf, ulf));
                    }
                    message.append("\n");
                    assertTrue("Incorrect output for checkMadFloatFloatFloatFloat" + (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkMadFloat2Float2Float2Float2() {
        Allocation inA = CreateRandomAllocation(mRS, Element.DataType.FLOAT_32, 2, 0x44d6ec3d0f204b7cL);
        Allocation inB = CreateRandomAllocation(mRS, Element.DataType.FLOAT_32, 2, 0x44d6ec3d0f204b7cL);
        Allocation inC = CreateRandomAllocation(mRS, Element.DataType.FLOAT_32, 2, 0x44d6ec3d0f204b7cL);
        try {
            Allocation out = Allocation.createSized(mRS, GetElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            script.set_gAllocInB(inB);
            script.set_gAllocInC(inC);
            script.forEach_testMadFloat2Float2Float2Float2(inA, out);
            verifyResultsMadFloat2Float2Float2Float2(inA, inB, inC, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testMadFloat2Float2Float2Float2: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, GetElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            scriptRelaxed.set_gAllocInB(inB);
            scriptRelaxed.set_gAllocInC(inC);
            scriptRelaxed.forEach_testMadFloat2Float2Float2Float2(inA, out);
            verifyResultsMadFloat2Float2Float2Float2(inA, inB, inC, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testMadFloat2Float2Float2Float2: " + e.toString());
        }
    }

    private void verifyResultsMadFloat2Float2Float2Float2(Allocation inA, Allocation inB, Allocation inC, Allocation out, boolean relaxed) {
        float[] arrayInA = new float[INPUTSIZE * 2];
        inA.copyTo(arrayInA);
        float[] arrayInB = new float[INPUTSIZE * 2];
        inB.copyTo(arrayInB);
        float[] arrayInC = new float[INPUTSIZE * 2];
        inC.copyTo(arrayInC);
        float[] arrayOut = new float[INPUTSIZE * 2];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 2 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloatFloatFloat args = new ArgumentsFloatFloatFloatFloat();
                args.inA = arrayInA[i * 2 + j];
                args.inB = arrayInB[i * 2 + j];
                args.inC = arrayInC[i * 2 + j];
                // Figure out what the outputs should have been.
                CoreMathVerifier.computeMad(args);
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
                    message.append(String.format("Input inA: %x %.16f", Float.floatToRawIntBits(args.inA), args.inA));
                    message.append("\n");
                    message.append(String.format("Input inB: %x %.16f", Float.floatToRawIntBits(args.inB), args.inB));
                    message.append("\n");
                    message.append(String.format("Input inC: %x %.16f", Float.floatToRawIntBits(args.inC), args.inC));
                    message.append("\n");
                    message.append(String.format("Expected output out: %x %.16f", Float.floatToRawIntBits(args.out), args.out));
                    message.append("\n");
                    message.append(String.format("Actual   output out: %x %.16f", Float.floatToRawIntBits(arrayOut[i * 2 + j]), arrayOut[i * 2 + j]));
                    neededUlf = (int) (Math.abs(args.out - arrayOut[i * 2 + j]) / Math.ulp(args.out) + 0.5);
                    if (neededUlf > ulf) {
                        message.append(String.format(" FAILED, ulf needed %d, specified %d", neededUlf, ulf));
                    }
                    message.append("\n");
                    assertTrue("Incorrect output for checkMadFloat2Float2Float2Float2" + (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkMadFloat3Float3Float3Float3() {
        Allocation inA = CreateRandomAllocation(mRS, Element.DataType.FLOAT_32, 3, 0x1a508fd7e1878518L);
        Allocation inB = CreateRandomAllocation(mRS, Element.DataType.FLOAT_32, 3, 0x1a508fd7e1878518L);
        Allocation inC = CreateRandomAllocation(mRS, Element.DataType.FLOAT_32, 3, 0x1a508fd7e1878518L);
        try {
            Allocation out = Allocation.createSized(mRS, GetElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            script.set_gAllocInB(inB);
            script.set_gAllocInC(inC);
            script.forEach_testMadFloat3Float3Float3Float3(inA, out);
            verifyResultsMadFloat3Float3Float3Float3(inA, inB, inC, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testMadFloat3Float3Float3Float3: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, GetElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            scriptRelaxed.set_gAllocInB(inB);
            scriptRelaxed.set_gAllocInC(inC);
            scriptRelaxed.forEach_testMadFloat3Float3Float3Float3(inA, out);
            verifyResultsMadFloat3Float3Float3Float3(inA, inB, inC, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testMadFloat3Float3Float3Float3: " + e.toString());
        }
    }

    private void verifyResultsMadFloat3Float3Float3Float3(Allocation inA, Allocation inB, Allocation inC, Allocation out, boolean relaxed) {
        float[] arrayInA = new float[INPUTSIZE * 4];
        inA.copyTo(arrayInA);
        float[] arrayInB = new float[INPUTSIZE * 4];
        inB.copyTo(arrayInB);
        float[] arrayInC = new float[INPUTSIZE * 4];
        inC.copyTo(arrayInC);
        float[] arrayOut = new float[INPUTSIZE * 4];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 3 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloatFloatFloat args = new ArgumentsFloatFloatFloatFloat();
                args.inA = arrayInA[i * 4 + j];
                args.inB = arrayInB[i * 4 + j];
                args.inC = arrayInC[i * 4 + j];
                // Figure out what the outputs should have been.
                CoreMathVerifier.computeMad(args);
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
                    message.append(String.format("Input inA: %x %.16f", Float.floatToRawIntBits(args.inA), args.inA));
                    message.append("\n");
                    message.append(String.format("Input inB: %x %.16f", Float.floatToRawIntBits(args.inB), args.inB));
                    message.append("\n");
                    message.append(String.format("Input inC: %x %.16f", Float.floatToRawIntBits(args.inC), args.inC));
                    message.append("\n");
                    message.append(String.format("Expected output out: %x %.16f", Float.floatToRawIntBits(args.out), args.out));
                    message.append("\n");
                    message.append(String.format("Actual   output out: %x %.16f", Float.floatToRawIntBits(arrayOut[i * 4 + j]), arrayOut[i * 4 + j]));
                    neededUlf = (int) (Math.abs(args.out - arrayOut[i * 4 + j]) / Math.ulp(args.out) + 0.5);
                    if (neededUlf > ulf) {
                        message.append(String.format(" FAILED, ulf needed %d, specified %d", neededUlf, ulf));
                    }
                    message.append("\n");
                    assertTrue("Incorrect output for checkMadFloat3Float3Float3Float3" + (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkMadFloat4Float4Float4Float4() {
        Allocation inA = CreateRandomAllocation(mRS, Element.DataType.FLOAT_32, 4, 0xefca3372b3eebeb4L);
        Allocation inB = CreateRandomAllocation(mRS, Element.DataType.FLOAT_32, 4, 0xefca3372b3eebeb4L);
        Allocation inC = CreateRandomAllocation(mRS, Element.DataType.FLOAT_32, 4, 0xefca3372b3eebeb4L);
        try {
            Allocation out = Allocation.createSized(mRS, GetElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            script.set_gAllocInB(inB);
            script.set_gAllocInC(inC);
            script.forEach_testMadFloat4Float4Float4Float4(inA, out);
            verifyResultsMadFloat4Float4Float4Float4(inA, inB, inC, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testMadFloat4Float4Float4Float4: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, GetElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            scriptRelaxed.set_gAllocInB(inB);
            scriptRelaxed.set_gAllocInC(inC);
            scriptRelaxed.forEach_testMadFloat4Float4Float4Float4(inA, out);
            verifyResultsMadFloat4Float4Float4Float4(inA, inB, inC, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testMadFloat4Float4Float4Float4: " + e.toString());
        }
    }

    private void verifyResultsMadFloat4Float4Float4Float4(Allocation inA, Allocation inB, Allocation inC, Allocation out, boolean relaxed) {
        float[] arrayInA = new float[INPUTSIZE * 4];
        inA.copyTo(arrayInA);
        float[] arrayInB = new float[INPUTSIZE * 4];
        inB.copyTo(arrayInB);
        float[] arrayInC = new float[INPUTSIZE * 4];
        inC.copyTo(arrayInC);
        float[] arrayOut = new float[INPUTSIZE * 4];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 4 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloatFloatFloat args = new ArgumentsFloatFloatFloatFloat();
                args.inA = arrayInA[i * 4 + j];
                args.inB = arrayInB[i * 4 + j];
                args.inC = arrayInC[i * 4 + j];
                // Figure out what the outputs should have been.
                CoreMathVerifier.computeMad(args);
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
                    message.append(String.format("Input inA: %x %.16f", Float.floatToRawIntBits(args.inA), args.inA));
                    message.append("\n");
                    message.append(String.format("Input inB: %x %.16f", Float.floatToRawIntBits(args.inB), args.inB));
                    message.append("\n");
                    message.append(String.format("Input inC: %x %.16f", Float.floatToRawIntBits(args.inC), args.inC));
                    message.append("\n");
                    message.append(String.format("Expected output out: %x %.16f", Float.floatToRawIntBits(args.out), args.out));
                    message.append("\n");
                    message.append(String.format("Actual   output out: %x %.16f", Float.floatToRawIntBits(arrayOut[i * 4 + j]), arrayOut[i * 4 + j]));
                    neededUlf = (int) (Math.abs(args.out - arrayOut[i * 4 + j]) / Math.ulp(args.out) + 0.5);
                    if (neededUlf > ulf) {
                        message.append(String.format(" FAILED, ulf needed %d, specified %d", neededUlf, ulf));
                    }
                    message.append("\n");
                    assertTrue("Incorrect output for checkMadFloat4Float4Float4Float4" + (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    public void testMad() {
        checkMadFloatFloatFloatFloat();
        checkMadFloat2Float2Float2Float2();
        checkMadFloat3Float3Float3Float3();
        checkMadFloat4Float4Float4Float4();
    }
}
