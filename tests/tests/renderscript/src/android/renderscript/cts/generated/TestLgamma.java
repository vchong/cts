/*
 * Copyright (C) 2015 The Android Open Source Project
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

// Don't edit this file!  It is auto-generated by frameworks/rs/api/generate.sh.

package android.renderscript.cts;

import android.renderscript.Allocation;
import android.renderscript.RSRuntimeException;
import android.renderscript.Element;

public class TestLgamma extends RSBaseCompute {

    private ScriptC_TestLgamma script;
    private ScriptC_TestLgammaRelaxed scriptRelaxed;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        script = new ScriptC_TestLgamma(mRS);
        scriptRelaxed = new ScriptC_TestLgammaRelaxed(mRS);
    }

    public class ArgumentsFloatFloat {
        public float inV;
        public Target.Floaty out;
    }

    private void checkLgammaFloatFloat() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 1, 0x50bc4bel, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            script.forEach_testLgammaFloatFloat(inV, out);
            verifyResultsLgammaFloatFloat(inV, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLgammaFloatFloat: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            scriptRelaxed.forEach_testLgammaFloatFloat(inV, out);
            verifyResultsLgammaFloatFloat(inV, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLgammaFloatFloat: " + e.toString());
        }
    }

    private void verifyResultsLgammaFloatFloat(Allocation inV, Allocation out, boolean relaxed) {
        float[] arrayInV = new float[INPUTSIZE * 1];
        inV.copyTo(arrayInV);
        float[] arrayOut = new float[INPUTSIZE * 1];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 1 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloat args = new ArgumentsFloatFloat();
                args.inV = arrayInV[i];
                // Figure out what the outputs should have been.
                Target target = new Target(relaxed);
                CoreMathVerifier.computeLgamma(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 1 + j])) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append("Input inV: ");
                    appendVariableToMessage(message, args.inV);
                    message.append("\n");
                    message.append("Expected output out: ");
                    appendVariableToMessage(message, args.out);
                    message.append("\n");
                    message.append("Actual   output out: ");
                    appendVariableToMessage(message, arrayOut[i * 1 + j]);
                    if (!args.out.couldBe(arrayOut[i * 1 + j])) {
                        message.append(" FAIL");
                    }
                    message.append("\n");
                    assertTrue("Incorrect output for checkLgammaFloatFloat" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkLgammaFloat2Float2() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 2, 0x367a4132l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            script.forEach_testLgammaFloat2Float2(inV, out);
            verifyResultsLgammaFloat2Float2(inV, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLgammaFloat2Float2: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            scriptRelaxed.forEach_testLgammaFloat2Float2(inV, out);
            verifyResultsLgammaFloat2Float2(inV, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLgammaFloat2Float2: " + e.toString());
        }
    }

    private void verifyResultsLgammaFloat2Float2(Allocation inV, Allocation out, boolean relaxed) {
        float[] arrayInV = new float[INPUTSIZE * 2];
        inV.copyTo(arrayInV);
        float[] arrayOut = new float[INPUTSIZE * 2];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 2 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloat args = new ArgumentsFloatFloat();
                args.inV = arrayInV[i * 2 + j];
                // Figure out what the outputs should have been.
                Target target = new Target(relaxed);
                CoreMathVerifier.computeLgamma(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 2 + j])) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append("Input inV: ");
                    appendVariableToMessage(message, args.inV);
                    message.append("\n");
                    message.append("Expected output out: ");
                    appendVariableToMessage(message, args.out);
                    message.append("\n");
                    message.append("Actual   output out: ");
                    appendVariableToMessage(message, arrayOut[i * 2 + j]);
                    if (!args.out.couldBe(arrayOut[i * 2 + j])) {
                        message.append(" FAIL");
                    }
                    message.append("\n");
                    assertTrue("Incorrect output for checkLgammaFloat2Float2" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkLgammaFloat3Float3() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 3, 0x2c956210l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            script.forEach_testLgammaFloat3Float3(inV, out);
            verifyResultsLgammaFloat3Float3(inV, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLgammaFloat3Float3: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            scriptRelaxed.forEach_testLgammaFloat3Float3(inV, out);
            verifyResultsLgammaFloat3Float3(inV, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLgammaFloat3Float3: " + e.toString());
        }
    }

    private void verifyResultsLgammaFloat3Float3(Allocation inV, Allocation out, boolean relaxed) {
        float[] arrayInV = new float[INPUTSIZE * 4];
        inV.copyTo(arrayInV);
        float[] arrayOut = new float[INPUTSIZE * 4];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 3 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloat args = new ArgumentsFloatFloat();
                args.inV = arrayInV[i * 4 + j];
                // Figure out what the outputs should have been.
                Target target = new Target(relaxed);
                CoreMathVerifier.computeLgamma(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 4 + j])) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append("Input inV: ");
                    appendVariableToMessage(message, args.inV);
                    message.append("\n");
                    message.append("Expected output out: ");
                    appendVariableToMessage(message, args.out);
                    message.append("\n");
                    message.append("Actual   output out: ");
                    appendVariableToMessage(message, arrayOut[i * 4 + j]);
                    if (!args.out.couldBe(arrayOut[i * 4 + j])) {
                        message.append(" FAIL");
                    }
                    message.append("\n");
                    assertTrue("Incorrect output for checkLgammaFloat3Float3" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkLgammaFloat4Float4() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 4, 0x22b082eel, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            script.forEach_testLgammaFloat4Float4(inV, out);
            verifyResultsLgammaFloat4Float4(inV, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLgammaFloat4Float4: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            scriptRelaxed.forEach_testLgammaFloat4Float4(inV, out);
            verifyResultsLgammaFloat4Float4(inV, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLgammaFloat4Float4: " + e.toString());
        }
    }

    private void verifyResultsLgammaFloat4Float4(Allocation inV, Allocation out, boolean relaxed) {
        float[] arrayInV = new float[INPUTSIZE * 4];
        inV.copyTo(arrayInV);
        float[] arrayOut = new float[INPUTSIZE * 4];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 4 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloat args = new ArgumentsFloatFloat();
                args.inV = arrayInV[i * 4 + j];
                // Figure out what the outputs should have been.
                Target target = new Target(relaxed);
                CoreMathVerifier.computeLgamma(args, target);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 4 + j])) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append("Input inV: ");
                    appendVariableToMessage(message, args.inV);
                    message.append("\n");
                    message.append("Expected output out: ");
                    appendVariableToMessage(message, args.out);
                    message.append("\n");
                    message.append("Actual   output out: ");
                    appendVariableToMessage(message, arrayOut[i * 4 + j]);
                    if (!args.out.couldBe(arrayOut[i * 4 + j])) {
                        message.append(" FAIL");
                    }
                    message.append("\n");
                    assertTrue("Incorrect output for checkLgammaFloat4Float4" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    public class ArgumentsFloatIntFloat {
        public float inV;
        public int outSignOfGamma;
        public float out;
    }

    private void checkLgammaFloatIntFloat() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 1, 0x979c4bb7l, false);
        try {
            Allocation outSignOfGamma = Allocation.createSized(mRS, getElement(mRS, Element.DataType.SIGNED_32, 1), INPUTSIZE);
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            script.set_gAllocOutSignOfGamma(outSignOfGamma);
            script.forEach_testLgammaFloatIntFloat(inV, out);
            verifyResultsLgammaFloatIntFloat(inV, outSignOfGamma, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLgammaFloatIntFloat: " + e.toString());
        }
        try {
            Allocation outSignOfGamma = Allocation.createSized(mRS, getElement(mRS, Element.DataType.SIGNED_32, 1), INPUTSIZE);
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            scriptRelaxed.set_gAllocOutSignOfGamma(outSignOfGamma);
            scriptRelaxed.forEach_testLgammaFloatIntFloat(inV, out);
            verifyResultsLgammaFloatIntFloat(inV, outSignOfGamma, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLgammaFloatIntFloat: " + e.toString());
        }
    }

    private void verifyResultsLgammaFloatIntFloat(Allocation inV, Allocation outSignOfGamma, Allocation out, boolean relaxed) {
        float[] arrayInV = new float[INPUTSIZE * 1];
        inV.copyTo(arrayInV);
        int[] arrayOutSignOfGamma = new int[INPUTSIZE * 1];
        outSignOfGamma.copyTo(arrayOutSignOfGamma);
        float[] arrayOut = new float[INPUTSIZE * 1];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 1 ; j++) {
                // Extract the inputs.
                ArgumentsFloatIntFloat args = new ArgumentsFloatIntFloat();
                args.inV = arrayInV[i];
                // Extract the outputs.
                args.outSignOfGamma = arrayOutSignOfGamma[i * 1 + j];
                args.out = arrayOut[i * 1 + j];
                // Ask the CoreMathVerifier to validate.
                Target target = new Target(relaxed);
                String errorMessage = CoreMathVerifier.verifyLgamma(args, target);
                boolean valid = errorMessage == null;
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append("Input inV: ");
                    appendVariableToMessage(message, args.inV);
                    message.append("\n");
                    message.append("Output outSignOfGamma: ");
                    appendVariableToMessage(message, args.outSignOfGamma);
                    message.append("\n");
                    message.append("Output out: ");
                    appendVariableToMessage(message, args.out);
                    message.append("\n");
                    message.append(errorMessage);
                    assertTrue("Incorrect output for checkLgammaFloatIntFloat" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkLgammaFloat2Int2Float2() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 2, 0x984bcf7fl, false);
        try {
            Allocation outSignOfGamma = Allocation.createSized(mRS, getElement(mRS, Element.DataType.SIGNED_32, 2), INPUTSIZE);
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            script.set_gAllocOutSignOfGamma(outSignOfGamma);
            script.forEach_testLgammaFloat2Int2Float2(inV, out);
            verifyResultsLgammaFloat2Int2Float2(inV, outSignOfGamma, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLgammaFloat2Int2Float2: " + e.toString());
        }
        try {
            Allocation outSignOfGamma = Allocation.createSized(mRS, getElement(mRS, Element.DataType.SIGNED_32, 2), INPUTSIZE);
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            scriptRelaxed.set_gAllocOutSignOfGamma(outSignOfGamma);
            scriptRelaxed.forEach_testLgammaFloat2Int2Float2(inV, out);
            verifyResultsLgammaFloat2Int2Float2(inV, outSignOfGamma, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLgammaFloat2Int2Float2: " + e.toString());
        }
    }

    private void verifyResultsLgammaFloat2Int2Float2(Allocation inV, Allocation outSignOfGamma, Allocation out, boolean relaxed) {
        float[] arrayInV = new float[INPUTSIZE * 2];
        inV.copyTo(arrayInV);
        int[] arrayOutSignOfGamma = new int[INPUTSIZE * 2];
        outSignOfGamma.copyTo(arrayOutSignOfGamma);
        float[] arrayOut = new float[INPUTSIZE * 2];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 2 ; j++) {
                // Extract the inputs.
                ArgumentsFloatIntFloat args = new ArgumentsFloatIntFloat();
                args.inV = arrayInV[i * 2 + j];
                // Extract the outputs.
                args.outSignOfGamma = arrayOutSignOfGamma[i * 2 + j];
                args.out = arrayOut[i * 2 + j];
                // Ask the CoreMathVerifier to validate.
                Target target = new Target(relaxed);
                String errorMessage = CoreMathVerifier.verifyLgamma(args, target);
                boolean valid = errorMessage == null;
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append("Input inV: ");
                    appendVariableToMessage(message, args.inV);
                    message.append("\n");
                    message.append("Output outSignOfGamma: ");
                    appendVariableToMessage(message, args.outSignOfGamma);
                    message.append("\n");
                    message.append("Output out: ");
                    appendVariableToMessage(message, args.out);
                    message.append("\n");
                    message.append(errorMessage);
                    assertTrue("Incorrect output for checkLgammaFloat2Int2Float2" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkLgammaFloat3Int3Float3() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 3, 0x8dfe3c38l, false);
        try {
            Allocation outSignOfGamma = Allocation.createSized(mRS, getElement(mRS, Element.DataType.SIGNED_32, 3), INPUTSIZE);
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            script.set_gAllocOutSignOfGamma(outSignOfGamma);
            script.forEach_testLgammaFloat3Int3Float3(inV, out);
            verifyResultsLgammaFloat3Int3Float3(inV, outSignOfGamma, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLgammaFloat3Int3Float3: " + e.toString());
        }
        try {
            Allocation outSignOfGamma = Allocation.createSized(mRS, getElement(mRS, Element.DataType.SIGNED_32, 3), INPUTSIZE);
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            scriptRelaxed.set_gAllocOutSignOfGamma(outSignOfGamma);
            scriptRelaxed.forEach_testLgammaFloat3Int3Float3(inV, out);
            verifyResultsLgammaFloat3Int3Float3(inV, outSignOfGamma, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLgammaFloat3Int3Float3: " + e.toString());
        }
    }

    private void verifyResultsLgammaFloat3Int3Float3(Allocation inV, Allocation outSignOfGamma, Allocation out, boolean relaxed) {
        float[] arrayInV = new float[INPUTSIZE * 4];
        inV.copyTo(arrayInV);
        int[] arrayOutSignOfGamma = new int[INPUTSIZE * 4];
        outSignOfGamma.copyTo(arrayOutSignOfGamma);
        float[] arrayOut = new float[INPUTSIZE * 4];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 3 ; j++) {
                // Extract the inputs.
                ArgumentsFloatIntFloat args = new ArgumentsFloatIntFloat();
                args.inV = arrayInV[i * 4 + j];
                // Extract the outputs.
                args.outSignOfGamma = arrayOutSignOfGamma[i * 4 + j];
                args.out = arrayOut[i * 4 + j];
                // Ask the CoreMathVerifier to validate.
                Target target = new Target(relaxed);
                String errorMessage = CoreMathVerifier.verifyLgamma(args, target);
                boolean valid = errorMessage == null;
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append("Input inV: ");
                    appendVariableToMessage(message, args.inV);
                    message.append("\n");
                    message.append("Output outSignOfGamma: ");
                    appendVariableToMessage(message, args.outSignOfGamma);
                    message.append("\n");
                    message.append("Output out: ");
                    appendVariableToMessage(message, args.out);
                    message.append("\n");
                    message.append(errorMessage);
                    assertTrue("Incorrect output for checkLgammaFloat3Int3Float3" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkLgammaFloat4Int4Float4() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 4, 0x83b0a8f1l, false);
        try {
            Allocation outSignOfGamma = Allocation.createSized(mRS, getElement(mRS, Element.DataType.SIGNED_32, 4), INPUTSIZE);
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            script.set_gAllocOutSignOfGamma(outSignOfGamma);
            script.forEach_testLgammaFloat4Int4Float4(inV, out);
            verifyResultsLgammaFloat4Int4Float4(inV, outSignOfGamma, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLgammaFloat4Int4Float4: " + e.toString());
        }
        try {
            Allocation outSignOfGamma = Allocation.createSized(mRS, getElement(mRS, Element.DataType.SIGNED_32, 4), INPUTSIZE);
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            scriptRelaxed.set_gAllocOutSignOfGamma(outSignOfGamma);
            scriptRelaxed.forEach_testLgammaFloat4Int4Float4(inV, out);
            verifyResultsLgammaFloat4Int4Float4(inV, outSignOfGamma, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testLgammaFloat4Int4Float4: " + e.toString());
        }
    }

    private void verifyResultsLgammaFloat4Int4Float4(Allocation inV, Allocation outSignOfGamma, Allocation out, boolean relaxed) {
        float[] arrayInV = new float[INPUTSIZE * 4];
        inV.copyTo(arrayInV);
        int[] arrayOutSignOfGamma = new int[INPUTSIZE * 4];
        outSignOfGamma.copyTo(arrayOutSignOfGamma);
        float[] arrayOut = new float[INPUTSIZE * 4];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 4 ; j++) {
                // Extract the inputs.
                ArgumentsFloatIntFloat args = new ArgumentsFloatIntFloat();
                args.inV = arrayInV[i * 4 + j];
                // Extract the outputs.
                args.outSignOfGamma = arrayOutSignOfGamma[i * 4 + j];
                args.out = arrayOut[i * 4 + j];
                // Ask the CoreMathVerifier to validate.
                Target target = new Target(relaxed);
                String errorMessage = CoreMathVerifier.verifyLgamma(args, target);
                boolean valid = errorMessage == null;
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append("Input inV: ");
                    appendVariableToMessage(message, args.inV);
                    message.append("\n");
                    message.append("Output outSignOfGamma: ");
                    appendVariableToMessage(message, args.outSignOfGamma);
                    message.append("\n");
                    message.append("Output out: ");
                    appendVariableToMessage(message, args.out);
                    message.append("\n");
                    message.append(errorMessage);
                    assertTrue("Incorrect output for checkLgammaFloat4Int4Float4" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    public void testLgamma() {
        checkLgammaFloatFloat();
        checkLgammaFloat2Float2();
        checkLgammaFloat3Float3();
        checkLgammaFloat4Float4();
        checkLgammaFloatIntFloat();
        checkLgammaFloat2Int2Float2();
        checkLgammaFloat3Int3Float3();
        checkLgammaFloat4Int4Float4();
    }
}
