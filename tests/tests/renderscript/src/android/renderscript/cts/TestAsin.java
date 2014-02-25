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

public class TestAsin extends RSBaseCompute {

    private ScriptC_TestAsin script;
    private ScriptC_TestAsinRelaxed scriptRelaxed;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        script = new ScriptC_TestAsin(mRS);
        scriptRelaxed = new ScriptC_TestAsinRelaxed(mRS);
    }

    public class ArgumentsFloatFloat {
        public float in;
        public float out;

        public int ulf;
        public int ulfRelaxed;
    }

    private void checkAsinFloatFloat() {
        Allocation in = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 1, 0xfd0a2c13b8687334l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            script.forEach_testAsinFloatFloat(in, out);
            verifyResultsAsinFloatFloat(in, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testAsinFloatFloat: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            scriptRelaxed.forEach_testAsinFloatFloat(in, out);
            verifyResultsAsinFloatFloat(in, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testAsinFloatFloat: " + e.toString());
        }
    }

    private void verifyResultsAsinFloatFloat(Allocation in, Allocation out, boolean relaxed) {
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
                CoreMathVerifier.computeAsin(args);
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
                    message.append(String.format("Input in: %14.8g %8x %15a",
                            args.in, Float.floatToRawIntBits(args.in), args.in));
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
                    assertTrue("Incorrect output for checkAsinFloatFloat" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkAsinFloat2Float2() {
        Allocation in = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 2, 0x9e777c6a9ba08db0l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            script.forEach_testAsinFloat2Float2(in, out);
            verifyResultsAsinFloat2Float2(in, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testAsinFloat2Float2: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            scriptRelaxed.forEach_testAsinFloat2Float2(in, out);
            verifyResultsAsinFloat2Float2(in, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testAsinFloat2Float2: " + e.toString());
        }
    }

    private void verifyResultsAsinFloat2Float2(Allocation in, Allocation out, boolean relaxed) {
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
                CoreMathVerifier.computeAsin(args);
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
                    message.append(String.format("Input in: %14.8g %8x %15a",
                            args.in, Float.floatToRawIntBits(args.in), args.in));
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
                    assertTrue("Incorrect output for checkAsinFloat2Float2" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkAsinFloat3Float3() {
        Allocation in = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 3, 0x9e77870bfaa7234al, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            script.forEach_testAsinFloat3Float3(in, out);
            verifyResultsAsinFloat3Float3(in, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testAsinFloat3Float3: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            scriptRelaxed.forEach_testAsinFloat3Float3(in, out);
            verifyResultsAsinFloat3Float3(in, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testAsinFloat3Float3: " + e.toString());
        }
    }

    private void verifyResultsAsinFloat3Float3(Allocation in, Allocation out, boolean relaxed) {
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
                CoreMathVerifier.computeAsin(args);
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
                    message.append(String.format("Input in: %14.8g %8x %15a",
                            args.in, Float.floatToRawIntBits(args.in), args.in));
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
                    assertTrue("Incorrect output for checkAsinFloat3Float3" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkAsinFloat4Float4() {
        Allocation in = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 4, 0x9e7791ad59adb8e4l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            script.forEach_testAsinFloat4Float4(in, out);
            verifyResultsAsinFloat4Float4(in, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testAsinFloat4Float4: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            scriptRelaxed.forEach_testAsinFloat4Float4(in, out);
            verifyResultsAsinFloat4Float4(in, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testAsinFloat4Float4: " + e.toString());
        }
    }

    private void verifyResultsAsinFloat4Float4(Allocation in, Allocation out, boolean relaxed) {
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
                CoreMathVerifier.computeAsin(args);
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
                    message.append(String.format("Input in: %14.8g %8x %15a",
                            args.in, Float.floatToRawIntBits(args.in), args.in));
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
                    assertTrue("Incorrect output for checkAsinFloat4Float4" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    public void testAsin() {
        checkAsinFloatFloat();
        checkAsinFloat2Float2();
        checkAsinFloat3Float3();
        checkAsinFloat4Float4();
    }
}
