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

public class TestSincos extends RSBaseCompute {

    private ScriptC_TestSincos script;
    private ScriptC_TestSincosRelaxed scriptRelaxed;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        script = new ScriptC_TestSincos(mRS);
        scriptRelaxed = new ScriptC_TestSincosRelaxed(mRS);
    }

    public class ArgumentsFloatFloatFloat {
        public float inV;
        public Target.Floaty outCos;
        public Target.Floaty out;
    }

    private void checkSincosFloatFloatFloat() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 1, 0xb8748e13e46c48d4l, false);
        try {
            Allocation outCos = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            script.set_gAllocOutCos(outCos);
            script.forEach_testSincosFloatFloatFloat(inV, out);
            verifyResultsSincosFloatFloatFloat(inV, outCos, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testSincosFloatFloatFloat: " + e.toString());
        }
        try {
            Allocation outCos = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            scriptRelaxed.set_gAllocOutCos(outCos);
            scriptRelaxed.forEach_testSincosFloatFloatFloat(inV, out);
            verifyResultsSincosFloatFloatFloat(inV, outCos, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testSincosFloatFloatFloat: " + e.toString());
        }
    }

    private void verifyResultsSincosFloatFloatFloat(Allocation inV, Allocation outCos, Allocation out, boolean relaxed) {
        float[] arrayInV = new float[INPUTSIZE * 1];
        inV.copyTo(arrayInV);
        float[] arrayOutCos = new float[INPUTSIZE * 1];
        outCos.copyTo(arrayOutCos);
        float[] arrayOut = new float[INPUTSIZE * 1];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 1 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloatFloat args = new ArgumentsFloatFloatFloat();
                args.inV = arrayInV[i];
                // Figure out what the outputs should have been.
                Target target = new Target(relaxed);
                CoreMathVerifier.computeSincos(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.outCos.couldBe(arrayOutCos[i * 1 + j])) {
                    valid = false;
                }
                if (!args.out.couldBe(arrayOut[i * 1 + j])) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append("Input inV: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            args.inV, Float.floatToRawIntBits(args.inV), args.inV));
                    message.append("\n");
                    message.append("Expected output outCos: ");
                    message.append(args.outCos.toString());
                    message.append("\n");
                    message.append("Actual   output outCos: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            arrayOutCos[i * 1 + j], Float.floatToRawIntBits(arrayOutCos[i * 1 + j]), arrayOutCos[i * 1 + j]));
                    if (!args.outCos.couldBe(arrayOutCos[i * 1 + j])) {
                        message.append(" FAIL");
                    }
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
                    assertTrue("Incorrect output for checkSincosFloatFloatFloat" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkSincosFloat2Float2Float2() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 2, 0xc85bab4e3e2fc77cl, false);
        try {
            Allocation outCos = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            script.set_gAllocOutCos(outCos);
            script.forEach_testSincosFloat2Float2Float2(inV, out);
            verifyResultsSincosFloat2Float2Float2(inV, outCos, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testSincosFloat2Float2Float2: " + e.toString());
        }
        try {
            Allocation outCos = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            scriptRelaxed.set_gAllocOutCos(outCos);
            scriptRelaxed.forEach_testSincosFloat2Float2Float2(inV, out);
            verifyResultsSincosFloat2Float2Float2(inV, outCos, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testSincosFloat2Float2Float2: " + e.toString());
        }
    }

    private void verifyResultsSincosFloat2Float2Float2(Allocation inV, Allocation outCos, Allocation out, boolean relaxed) {
        float[] arrayInV = new float[INPUTSIZE * 2];
        inV.copyTo(arrayInV);
        float[] arrayOutCos = new float[INPUTSIZE * 2];
        outCos.copyTo(arrayOutCos);
        float[] arrayOut = new float[INPUTSIZE * 2];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 2 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloatFloat args = new ArgumentsFloatFloatFloat();
                args.inV = arrayInV[i * 2 + j];
                // Figure out what the outputs should have been.
                Target target = new Target(relaxed);
                CoreMathVerifier.computeSincos(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.outCos.couldBe(arrayOutCos[i * 2 + j])) {
                    valid = false;
                }
                if (!args.out.couldBe(arrayOut[i * 2 + j])) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append("Input inV: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            args.inV, Float.floatToRawIntBits(args.inV), args.inV));
                    message.append("\n");
                    message.append("Expected output outCos: ");
                    message.append(args.outCos.toString());
                    message.append("\n");
                    message.append("Actual   output outCos: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            arrayOutCos[i * 2 + j], Float.floatToRawIntBits(arrayOutCos[i * 2 + j]), arrayOutCos[i * 2 + j]));
                    if (!args.outCos.couldBe(arrayOutCos[i * 2 + j])) {
                        message.append(" FAIL");
                    }
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
                    assertTrue("Incorrect output for checkSincosFloat2Float2Float2" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkSincosFloat3Float3Float3() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 3, 0x1cc0896e400dc91dl, false);
        try {
            Allocation outCos = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            script.set_gAllocOutCos(outCos);
            script.forEach_testSincosFloat3Float3Float3(inV, out);
            verifyResultsSincosFloat3Float3Float3(inV, outCos, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testSincosFloat3Float3Float3: " + e.toString());
        }
        try {
            Allocation outCos = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            scriptRelaxed.set_gAllocOutCos(outCos);
            scriptRelaxed.forEach_testSincosFloat3Float3Float3(inV, out);
            verifyResultsSincosFloat3Float3Float3(inV, outCos, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testSincosFloat3Float3Float3: " + e.toString());
        }
    }

    private void verifyResultsSincosFloat3Float3Float3(Allocation inV, Allocation outCos, Allocation out, boolean relaxed) {
        float[] arrayInV = new float[INPUTSIZE * 4];
        inV.copyTo(arrayInV);
        float[] arrayOutCos = new float[INPUTSIZE * 4];
        outCos.copyTo(arrayOutCos);
        float[] arrayOut = new float[INPUTSIZE * 4];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 3 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloatFloat args = new ArgumentsFloatFloatFloat();
                args.inV = arrayInV[i * 4 + j];
                // Figure out what the outputs should have been.
                Target target = new Target(relaxed);
                CoreMathVerifier.computeSincos(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.outCos.couldBe(arrayOutCos[i * 4 + j])) {
                    valid = false;
                }
                if (!args.out.couldBe(arrayOut[i * 4 + j])) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append("Input inV: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            args.inV, Float.floatToRawIntBits(args.inV), args.inV));
                    message.append("\n");
                    message.append("Expected output outCos: ");
                    message.append(args.outCos.toString());
                    message.append("\n");
                    message.append("Actual   output outCos: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            arrayOutCos[i * 4 + j], Float.floatToRawIntBits(arrayOutCos[i * 4 + j]), arrayOutCos[i * 4 + j]));
                    if (!args.outCos.couldBe(arrayOutCos[i * 4 + j])) {
                        message.append(" FAIL");
                    }
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
                    assertTrue("Incorrect output for checkSincosFloat3Float3Float3" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkSincosFloat4Float4Float4() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 4, 0x7125678e41ebcabel, false);
        try {
            Allocation outCos = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            script.set_gAllocOutCos(outCos);
            script.forEach_testSincosFloat4Float4Float4(inV, out);
            verifyResultsSincosFloat4Float4Float4(inV, outCos, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testSincosFloat4Float4Float4: " + e.toString());
        }
        try {
            Allocation outCos = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            scriptRelaxed.set_gAllocOutCos(outCos);
            scriptRelaxed.forEach_testSincosFloat4Float4Float4(inV, out);
            verifyResultsSincosFloat4Float4Float4(inV, outCos, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testSincosFloat4Float4Float4: " + e.toString());
        }
    }

    private void verifyResultsSincosFloat4Float4Float4(Allocation inV, Allocation outCos, Allocation out, boolean relaxed) {
        float[] arrayInV = new float[INPUTSIZE * 4];
        inV.copyTo(arrayInV);
        float[] arrayOutCos = new float[INPUTSIZE * 4];
        outCos.copyTo(arrayOutCos);
        float[] arrayOut = new float[INPUTSIZE * 4];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 4 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloatFloat args = new ArgumentsFloatFloatFloat();
                args.inV = arrayInV[i * 4 + j];
                // Figure out what the outputs should have been.
                Target target = new Target(relaxed);
                CoreMathVerifier.computeSincos(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.outCos.couldBe(arrayOutCos[i * 4 + j])) {
                    valid = false;
                }
                if (!args.out.couldBe(arrayOut[i * 4 + j])) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append("Input inV: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            args.inV, Float.floatToRawIntBits(args.inV), args.inV));
                    message.append("\n");
                    message.append("Expected output outCos: ");
                    message.append(args.outCos.toString());
                    message.append("\n");
                    message.append("Actual   output outCos: ");
                    message.append(String.format("%14.8g {%8x} %15a",
                            arrayOutCos[i * 4 + j], Float.floatToRawIntBits(arrayOutCos[i * 4 + j]), arrayOutCos[i * 4 + j]));
                    if (!args.outCos.couldBe(arrayOutCos[i * 4 + j])) {
                        message.append(" FAIL");
                    }
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
                    assertTrue("Incorrect output for checkSincosFloat4Float4Float4" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    public void testSincos() {
        checkSincosFloatFloatFloat();
        checkSincosFloat2Float2Float2();
        checkSincosFloat3Float3Float3();
        checkSincosFloat4Float4Float4();
    }
}
