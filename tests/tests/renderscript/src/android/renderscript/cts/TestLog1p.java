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

public class TestLog1p extends RSBaseCompute {

    private ScriptC_TestLog1p script;
    private ScriptC_TestLog1pRelaxed scriptRelaxed;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        script = new ScriptC_TestLog1p(mRS);
        scriptRelaxed = new ScriptC_TestLog1pRelaxed(mRS);
    }

    public class ArgumentsFloatFloat {
        public float in;
        public Floaty out;
    }

    private void checkLog1pFloatFloat() {
        Allocation in = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 1, 0x83e3423b7d907be0l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            script.forEach_testLog1pFloatFloat(in, out);
            verifyResultsLog1pFloatFloat(in, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLog1pFloatFloat: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            scriptRelaxed.forEach_testLog1pFloatFloat(in, out);
            verifyResultsLog1pFloatFloat(in, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLog1pFloatFloat: " + e.toString());
        }
    }

    private void verifyResultsLog1pFloatFloat(Allocation in, Allocation out, boolean relaxed) {
        float[] arrayIn = new float[INPUTSIZE * 1];
        in.copyTo(arrayIn);
        float[] arrayOut = new float[INPUTSIZE * 1];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 1 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloat args = new ArgumentsFloatFloat();
                args.in = arrayIn[i];
                // Figure out what the outputs should have been.
                Floaty.setRelaxed(relaxed);
                CoreMathVerifier.computeLog1p(args);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 1 + j])) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append("Input in: ");
                    message.append(String.format("%14.8g %8x %15a",
                            args.in, Float.floatToRawIntBits(args.in), args.in));
                    message.append("\n");
                    message.append("Expected output out: ");
                    message.append(args.out.toString());
                    message.append("\n");
                    message.append("Actual   output out: ");
                    message.append(String.format("%14.8g %8x %15a",
                            arrayOut[i * 1 + j], Float.floatToRawIntBits(arrayOut[i * 1 + j]), arrayOut[i * 1 + j]));
                    if (!args.out.couldBe(arrayOut[i * 1 + j])) {
                        message.append(" FAIL");
                    }
                    message.append("\n");
                    assertTrue("Incorrect output for checkLog1pFloatFloat" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkLog1pFloat2Float2() {
        Allocation in = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 2, 0x946881a999c72ffcl, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            script.forEach_testLog1pFloat2Float2(in, out);
            verifyResultsLog1pFloat2Float2(in, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLog1pFloat2Float2: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            scriptRelaxed.forEach_testLog1pFloat2Float2(in, out);
            verifyResultsLog1pFloat2Float2(in, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLog1pFloat2Float2: " + e.toString());
        }
    }

    private void verifyResultsLog1pFloat2Float2(Allocation in, Allocation out, boolean relaxed) {
        float[] arrayIn = new float[INPUTSIZE * 2];
        in.copyTo(arrayIn);
        float[] arrayOut = new float[INPUTSIZE * 2];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 2 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloat args = new ArgumentsFloatFloat();
                args.in = arrayIn[i * 2 + j];
                // Figure out what the outputs should have been.
                Floaty.setRelaxed(relaxed);
                CoreMathVerifier.computeLog1p(args);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 2 + j])) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append("Input in: ");
                    message.append(String.format("%14.8g %8x %15a",
                            args.in, Float.floatToRawIntBits(args.in), args.in));
                    message.append("\n");
                    message.append("Expected output out: ");
                    message.append(args.out.toString());
                    message.append("\n");
                    message.append("Actual   output out: ");
                    message.append(String.format("%14.8g %8x %15a",
                            arrayOut[i * 2 + j], Float.floatToRawIntBits(arrayOut[i * 2 + j]), arrayOut[i * 2 + j]));
                    if (!args.out.couldBe(arrayOut[i * 2 + j])) {
                        message.append(" FAIL");
                    }
                    message.append("\n");
                    assertTrue("Incorrect output for checkLog1pFloat2Float2" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkLog1pFloat3Float3() {
        Allocation in = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 3, 0x94688c4af8cdc596l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            script.forEach_testLog1pFloat3Float3(in, out);
            verifyResultsLog1pFloat3Float3(in, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLog1pFloat3Float3: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            scriptRelaxed.forEach_testLog1pFloat3Float3(in, out);
            verifyResultsLog1pFloat3Float3(in, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLog1pFloat3Float3: " + e.toString());
        }
    }

    private void verifyResultsLog1pFloat3Float3(Allocation in, Allocation out, boolean relaxed) {
        float[] arrayIn = new float[INPUTSIZE * 4];
        in.copyTo(arrayIn);
        float[] arrayOut = new float[INPUTSIZE * 4];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 3 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloat args = new ArgumentsFloatFloat();
                args.in = arrayIn[i * 4 + j];
                // Figure out what the outputs should have been.
                Floaty.setRelaxed(relaxed);
                CoreMathVerifier.computeLog1p(args);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 4 + j])) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append("Input in: ");
                    message.append(String.format("%14.8g %8x %15a",
                            args.in, Float.floatToRawIntBits(args.in), args.in));
                    message.append("\n");
                    message.append("Expected output out: ");
                    message.append(args.out.toString());
                    message.append("\n");
                    message.append("Actual   output out: ");
                    message.append(String.format("%14.8g %8x %15a",
                            arrayOut[i * 4 + j], Float.floatToRawIntBits(arrayOut[i * 4 + j]), arrayOut[i * 4 + j]));
                    if (!args.out.couldBe(arrayOut[i * 4 + j])) {
                        message.append(" FAIL");
                    }
                    message.append("\n");
                    assertTrue("Incorrect output for checkLog1pFloat3Float3" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkLog1pFloat4Float4() {
        Allocation in = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 4, 0x946896ec57d45b30l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            script.forEach_testLog1pFloat4Float4(in, out);
            verifyResultsLog1pFloat4Float4(in, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLog1pFloat4Float4: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            scriptRelaxed.forEach_testLog1pFloat4Float4(in, out);
            verifyResultsLog1pFloat4Float4(in, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLog1pFloat4Float4: " + e.toString());
        }
    }

    private void verifyResultsLog1pFloat4Float4(Allocation in, Allocation out, boolean relaxed) {
        float[] arrayIn = new float[INPUTSIZE * 4];
        in.copyTo(arrayIn);
        float[] arrayOut = new float[INPUTSIZE * 4];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 4 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloat args = new ArgumentsFloatFloat();
                args.in = arrayIn[i * 4 + j];
                // Figure out what the outputs should have been.
                Floaty.setRelaxed(relaxed);
                CoreMathVerifier.computeLog1p(args);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 4 + j])) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append("Input in: ");
                    message.append(String.format("%14.8g %8x %15a",
                            args.in, Float.floatToRawIntBits(args.in), args.in));
                    message.append("\n");
                    message.append("Expected output out: ");
                    message.append(args.out.toString());
                    message.append("\n");
                    message.append("Actual   output out: ");
                    message.append(String.format("%14.8g %8x %15a",
                            arrayOut[i * 4 + j], Float.floatToRawIntBits(arrayOut[i * 4 + j]), arrayOut[i * 4 + j]));
                    if (!args.out.couldBe(arrayOut[i * 4 + j])) {
                        message.append(" FAIL");
                    }
                    message.append("\n");
                    assertTrue("Incorrect output for checkLog1pFloat4Float4" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    public void testLog1p() {
        checkLog1pFloatFloat();
        checkLog1pFloat2Float2();
        checkLog1pFloat3Float3();
        checkLog1pFloat4Float4();
    }
}
