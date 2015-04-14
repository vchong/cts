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

public class TestNativeRootn extends RSBaseCompute {

    private ScriptC_TestNativeRootn script;
    private ScriptC_TestNativeRootnRelaxed scriptRelaxed;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        script = new ScriptC_TestNativeRootn(mRS);
        scriptRelaxed = new ScriptC_TestNativeRootnRelaxed(mRS);
    }

    public class ArgumentsFloatIntFloat {
        public float inV;
        public int inN;
        public Target.Floaty out;
    }

    private void checkNativeRootnFloatIntFloat() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 1, 0x6693fe5c237ac0f1l, false);
        Allocation inN = createRandomAllocation(mRS, Element.DataType.SIGNED_32, 1, 0x6693fe5c237ac0e9l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            script.set_gAllocInN(inN);
            script.forEach_testNativeRootnFloatIntFloat(inV, out);
            verifyResultsNativeRootnFloatIntFloat(inV, inN, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeRootnFloatIntFloat: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            scriptRelaxed.set_gAllocInN(inN);
            scriptRelaxed.forEach_testNativeRootnFloatIntFloat(inV, out);
            verifyResultsNativeRootnFloatIntFloat(inV, inN, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeRootnFloatIntFloat: " + e.toString());
        }
    }

    private void verifyResultsNativeRootnFloatIntFloat(Allocation inV, Allocation inN, Allocation out, boolean relaxed) {
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
                Target target = new Target(relaxed);
                CoreMathVerifier.computeNativeRootn(args, target);
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
                    message.append("Input inN: ");
                    appendVariableToMessage(message, args.inN);
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
                    assertTrue("Incorrect output for checkNativeRootnFloatIntFloat" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkNativeRootnFloat2Int2Float2() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 2, 0x5363e8c04afd5bcdl, false);
        Allocation inN = createRandomAllocation(mRS, Element.DataType.SIGNED_32, 2, 0x5363e8c04afd5bc5l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            script.set_gAllocInN(inN);
            script.forEach_testNativeRootnFloat2Int2Float2(inV, out);
            verifyResultsNativeRootnFloat2Int2Float2(inV, inN, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeRootnFloat2Int2Float2: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            scriptRelaxed.set_gAllocInN(inN);
            scriptRelaxed.forEach_testNativeRootnFloat2Int2Float2(inV, out);
            verifyResultsNativeRootnFloat2Int2Float2(inV, inN, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeRootnFloat2Int2Float2: " + e.toString());
        }
    }

    private void verifyResultsNativeRootnFloat2Int2Float2(Allocation inV, Allocation inN, Allocation out, boolean relaxed) {
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
                Target target = new Target(relaxed);
                CoreMathVerifier.computeNativeRootn(args, target);
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
                    message.append("Input inN: ");
                    appendVariableToMessage(message, args.inN);
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
                    assertTrue("Incorrect output for checkNativeRootnFloat2Int2Float2" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkNativeRootnFloat3Int3Float3() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 3, 0x791a272340afc886l, false);
        Allocation inN = createRandomAllocation(mRS, Element.DataType.SIGNED_32, 3, 0x791a272340afc87el, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            script.set_gAllocInN(inN);
            script.forEach_testNativeRootnFloat3Int3Float3(inV, out);
            verifyResultsNativeRootnFloat3Int3Float3(inV, inN, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeRootnFloat3Int3Float3: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            scriptRelaxed.set_gAllocInN(inN);
            scriptRelaxed.forEach_testNativeRootnFloat3Int3Float3(inV, out);
            verifyResultsNativeRootnFloat3Int3Float3(inV, inN, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeRootnFloat3Int3Float3: " + e.toString());
        }
    }

    private void verifyResultsNativeRootnFloat3Int3Float3(Allocation inV, Allocation inN, Allocation out, boolean relaxed) {
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
                Target target = new Target(relaxed);
                CoreMathVerifier.computeNativeRootn(args, target);
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
                    message.append("Input inN: ");
                    appendVariableToMessage(message, args.inN);
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
                    assertTrue("Incorrect output for checkNativeRootnFloat3Int3Float3" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkNativeRootnFloat4Int4Float4() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 4, 0x9ed065863662353fl, false);
        Allocation inN = createRandomAllocation(mRS, Element.DataType.SIGNED_32, 4, 0x9ed0658636623537l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            script.set_gAllocInN(inN);
            script.forEach_testNativeRootnFloat4Int4Float4(inV, out);
            verifyResultsNativeRootnFloat4Int4Float4(inV, inN, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeRootnFloat4Int4Float4: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            scriptRelaxed.set_gAllocInN(inN);
            scriptRelaxed.forEach_testNativeRootnFloat4Int4Float4(inV, out);
            verifyResultsNativeRootnFloat4Int4Float4(inV, inN, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeRootnFloat4Int4Float4: " + e.toString());
        }
    }

    private void verifyResultsNativeRootnFloat4Int4Float4(Allocation inV, Allocation inN, Allocation out, boolean relaxed) {
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
                Target target = new Target(relaxed);
                CoreMathVerifier.computeNativeRootn(args, target);
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
                    message.append("Input inN: ");
                    appendVariableToMessage(message, args.inN);
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
                    assertTrue("Incorrect output for checkNativeRootnFloat4Int4Float4" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    public void testNativeRootn() {
        checkNativeRootnFloatIntFloat();
        checkNativeRootnFloat2Int2Float2();
        checkNativeRootnFloat3Int3Float3();
        checkNativeRootnFloat4Int4Float4();
    }
}
