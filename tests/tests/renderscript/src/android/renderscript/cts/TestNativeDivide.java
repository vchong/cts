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

public class TestNativeDivide extends RSBaseCompute {

    private ScriptC_TestNativeDivide script;
    private ScriptC_TestNativeDivideRelaxed scriptRelaxed;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        script = new ScriptC_TestNativeDivide(mRS);
        scriptRelaxed = new ScriptC_TestNativeDivideRelaxed(mRS);
    }

    public class ArgumentsFloatFloatFloat {
        public float inLhs;
        public float inRhs;
        public Target.Floaty out;
    }

    private void checkNativeDivideFloatFloatFloat() {
        Allocation inLhs = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 1, 0xe2845ef0c23d02del, false);
        Allocation inRhs = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 1, 0xe2845ef0c23d2e34l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            script.set_gAllocInRhs(inRhs);
            script.forEach_testNativeDivideFloatFloatFloat(inLhs, out);
            verifyResultsNativeDivideFloatFloatFloat(inLhs, inRhs, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeDivideFloatFloatFloat: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            scriptRelaxed.set_gAllocInRhs(inRhs);
            scriptRelaxed.forEach_testNativeDivideFloatFloatFloat(inLhs, out);
            verifyResultsNativeDivideFloatFloatFloat(inLhs, inRhs, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeDivideFloatFloatFloat: " + e.toString());
        }
    }

    private void verifyResultsNativeDivideFloatFloatFloat(Allocation inLhs, Allocation inRhs, Allocation out, boolean relaxed) {
        float[] arrayInLhs = new float[INPUTSIZE * 1];
        inLhs.copyTo(arrayInLhs);
        float[] arrayInRhs = new float[INPUTSIZE * 1];
        inRhs.copyTo(arrayInRhs);
        float[] arrayOut = new float[INPUTSIZE * 1];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 1 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloatFloat args = new ArgumentsFloatFloatFloat();
                args.inLhs = arrayInLhs[i];
                args.inRhs = arrayInRhs[i];
                // Figure out what the outputs should have been.
                Target target = new Target(relaxed);
                CoreMathVerifier.computeNativeDivide(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 1 + j])) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append("Input inLhs: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            args.inLhs, Float.floatToRawIntBits(args.inLhs), args.inLhs));
                    message.append("\n");
                    message.append("Input inRhs: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            args.inRhs, Float.floatToRawIntBits(args.inRhs), args.inRhs));
                    message.append("\n");
                    message.append("Expected output out: ");
                    message.append(args.out.toString());
                    message.append("\n");
                    message.append("Actual   output out: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            arrayOut[i * 1 + j], Float.floatToRawIntBits(arrayOut[i * 1 + j]), arrayOut[i * 1 + j]));
                    if (!args.out.couldBe(arrayOut[i * 1 + j])) {
                        message.append(" FAIL");
                    }
                    message.append("\n");
                    assertTrue("Incorrect output for checkNativeDivideFloatFloatFloat" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkNativeDivideFloat2Float2Float2() {
        Allocation inLhs = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 2, 0x51c6d6ecaeab1c48l, false);
        Allocation inRhs = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 2, 0x51c6d6ecaeab479el, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            script.set_gAllocInRhs(inRhs);
            script.forEach_testNativeDivideFloat2Float2Float2(inLhs, out);
            verifyResultsNativeDivideFloat2Float2Float2(inLhs, inRhs, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeDivideFloat2Float2Float2: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            scriptRelaxed.set_gAllocInRhs(inRhs);
            scriptRelaxed.forEach_testNativeDivideFloat2Float2Float2(inLhs, out);
            verifyResultsNativeDivideFloat2Float2Float2(inLhs, inRhs, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeDivideFloat2Float2Float2: " + e.toString());
        }
    }

    private void verifyResultsNativeDivideFloat2Float2Float2(Allocation inLhs, Allocation inRhs, Allocation out, boolean relaxed) {
        float[] arrayInLhs = new float[INPUTSIZE * 2];
        inLhs.copyTo(arrayInLhs);
        float[] arrayInRhs = new float[INPUTSIZE * 2];
        inRhs.copyTo(arrayInRhs);
        float[] arrayOut = new float[INPUTSIZE * 2];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 2 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloatFloat args = new ArgumentsFloatFloatFloat();
                args.inLhs = arrayInLhs[i * 2 + j];
                args.inRhs = arrayInRhs[i * 2 + j];
                // Figure out what the outputs should have been.
                Target target = new Target(relaxed);
                CoreMathVerifier.computeNativeDivide(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 2 + j])) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append("Input inLhs: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            args.inLhs, Float.floatToRawIntBits(args.inLhs), args.inLhs));
                    message.append("\n");
                    message.append("Input inRhs: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            args.inRhs, Float.floatToRawIntBits(args.inRhs), args.inRhs));
                    message.append("\n");
                    message.append("Expected output out: ");
                    message.append(args.out.toString());
                    message.append("\n");
                    message.append("Actual   output out: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            arrayOut[i * 2 + j], Float.floatToRawIntBits(arrayOut[i * 2 + j]), arrayOut[i * 2 + j]));
                    if (!args.out.couldBe(arrayOut[i * 2 + j])) {
                        message.append(" FAIL");
                    }
                    message.append("\n");
                    assertTrue("Incorrect output for checkNativeDivideFloat2Float2Float2" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkNativeDivideFloat3Float3Float3() {
        Allocation inLhs = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 3, 0xde4f2c1a2b24e021l, false);
        Allocation inRhs = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 3, 0xde4f2c1a2b250b77l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            script.set_gAllocInRhs(inRhs);
            script.forEach_testNativeDivideFloat3Float3Float3(inLhs, out);
            verifyResultsNativeDivideFloat3Float3Float3(inLhs, inRhs, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeDivideFloat3Float3Float3: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            scriptRelaxed.set_gAllocInRhs(inRhs);
            scriptRelaxed.forEach_testNativeDivideFloat3Float3Float3(inLhs, out);
            verifyResultsNativeDivideFloat3Float3Float3(inLhs, inRhs, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeDivideFloat3Float3Float3: " + e.toString());
        }
    }

    private void verifyResultsNativeDivideFloat3Float3Float3(Allocation inLhs, Allocation inRhs, Allocation out, boolean relaxed) {
        float[] arrayInLhs = new float[INPUTSIZE * 4];
        inLhs.copyTo(arrayInLhs);
        float[] arrayInRhs = new float[INPUTSIZE * 4];
        inRhs.copyTo(arrayInRhs);
        float[] arrayOut = new float[INPUTSIZE * 4];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 3 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloatFloat args = new ArgumentsFloatFloatFloat();
                args.inLhs = arrayInLhs[i * 4 + j];
                args.inRhs = arrayInRhs[i * 4 + j];
                // Figure out what the outputs should have been.
                Target target = new Target(relaxed);
                CoreMathVerifier.computeNativeDivide(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 4 + j])) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append("Input inLhs: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            args.inLhs, Float.floatToRawIntBits(args.inLhs), args.inLhs));
                    message.append("\n");
                    message.append("Input inRhs: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            args.inRhs, Float.floatToRawIntBits(args.inRhs), args.inRhs));
                    message.append("\n");
                    message.append("Expected output out: ");
                    message.append(args.out.toString());
                    message.append("\n");
                    message.append("Actual   output out: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            arrayOut[i * 4 + j], Float.floatToRawIntBits(arrayOut[i * 4 + j]), arrayOut[i * 4 + j]));
                    if (!args.out.couldBe(arrayOut[i * 4 + j])) {
                        message.append(" FAIL");
                    }
                    message.append("\n");
                    assertTrue("Incorrect output for checkNativeDivideFloat3Float3Float3" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkNativeDivideFloat4Float4Float4() {
        Allocation inLhs = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 4, 0x6ad78147a79ea3fal, false);
        Allocation inRhs = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 4, 0x6ad78147a79ecf50l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            script.set_gAllocInRhs(inRhs);
            script.forEach_testNativeDivideFloat4Float4Float4(inLhs, out);
            verifyResultsNativeDivideFloat4Float4Float4(inLhs, inRhs, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeDivideFloat4Float4Float4: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            scriptRelaxed.set_gAllocInRhs(inRhs);
            scriptRelaxed.forEach_testNativeDivideFloat4Float4Float4(inLhs, out);
            verifyResultsNativeDivideFloat4Float4Float4(inLhs, inRhs, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeDivideFloat4Float4Float4: " + e.toString());
        }
    }

    private void verifyResultsNativeDivideFloat4Float4Float4(Allocation inLhs, Allocation inRhs, Allocation out, boolean relaxed) {
        float[] arrayInLhs = new float[INPUTSIZE * 4];
        inLhs.copyTo(arrayInLhs);
        float[] arrayInRhs = new float[INPUTSIZE * 4];
        inRhs.copyTo(arrayInRhs);
        float[] arrayOut = new float[INPUTSIZE * 4];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 4 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloatFloat args = new ArgumentsFloatFloatFloat();
                args.inLhs = arrayInLhs[i * 4 + j];
                args.inRhs = arrayInRhs[i * 4 + j];
                // Figure out what the outputs should have been.
                Target target = new Target(relaxed);
                CoreMathVerifier.computeNativeDivide(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 4 + j])) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append("Input inLhs: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            args.inLhs, Float.floatToRawIntBits(args.inLhs), args.inLhs));
                    message.append("\n");
                    message.append("Input inRhs: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            args.inRhs, Float.floatToRawIntBits(args.inRhs), args.inRhs));
                    message.append("\n");
                    message.append("Expected output out: ");
                    message.append(args.out.toString());
                    message.append("\n");
                    message.append("Actual   output out: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            arrayOut[i * 4 + j], Float.floatToRawIntBits(arrayOut[i * 4 + j]), arrayOut[i * 4 + j]));
                    if (!args.out.couldBe(arrayOut[i * 4 + j])) {
                        message.append(" FAIL");
                    }
                    message.append("\n");
                    assertTrue("Incorrect output for checkNativeDivideFloat4Float4Float4" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    public void testNativeDivide() {
        checkNativeDivideFloatFloatFloat();
        checkNativeDivideFloat2Float2Float2();
        checkNativeDivideFloat3Float3Float3();
        checkNativeDivideFloat4Float4Float4();
    }
}
