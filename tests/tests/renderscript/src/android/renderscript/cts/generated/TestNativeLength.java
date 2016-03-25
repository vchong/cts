/*
 * Copyright (C) 2016 The Android Open Source Project
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
import android.renderscript.cts.Target;

import java.util.Arrays;

public class TestNativeLength extends RSBaseCompute {

    private ScriptC_TestNativeLength script;
    private ScriptC_TestNativeLengthRelaxed scriptRelaxed;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        script = new ScriptC_TestNativeLength(mRS);
        scriptRelaxed = new ScriptC_TestNativeLengthRelaxed(mRS);
    }

    public class ArgumentsFloatFloat {
        public float inV;
        public Target.Floaty out;
    }

    private void checkNativeLengthFloatFloat() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 1, 0xd1df20ecl, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            script.forEach_testNativeLengthFloatFloat(inV, out);
            verifyResultsNativeLengthFloatFloat(inV, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeLengthFloatFloat: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            scriptRelaxed.forEach_testNativeLengthFloatFloat(inV, out);
            verifyResultsNativeLengthFloatFloat(inV, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeLengthFloatFloat: " + e.toString());
        }
    }

    private void verifyResultsNativeLengthFloatFloat(Allocation inV, Allocation out, boolean relaxed) {
        float[] arrayInV = new float[INPUTSIZE * 1];
        Arrays.fill(arrayInV, (float) 42);
        inV.copyTo(arrayInV);
        float[] arrayOut = new float[INPUTSIZE * 1];
        Arrays.fill(arrayOut, (float) 42);
        out.copyTo(arrayOut);
        StringBuilder message = new StringBuilder();
        boolean errorFound = false;
        for (int i = 0; i < INPUTSIZE; i++) {
            ArgumentsFloatFloat args = new ArgumentsFloatFloat();
            // Create the appropriate sized arrays in args
            // Fill args with the input values
            args.inV = arrayInV[i];
            Target target = new Target(Target.FunctionType.NATIVE, Target.ReturnType.FLOAT, relaxed);
            CoreMathVerifier.computeNativeLength(args, target);

            // Compare the expected outputs to the actual values returned by RS.
            boolean valid = true;
            if (!args.out.couldBe(arrayOut[i])) {
                valid = false;
            }
            if (!valid) {
                if (!errorFound) {
                    errorFound = true;
                    message.append("Input inV: ");
                    appendVariableToMessage(message, arrayInV[i]);
                    message.append("\n");
                    message.append("Expected output out: ");
                    appendVariableToMessage(message, args.out);
                    message.append("\n");
                    message.append("Actual   output out: ");
                    appendVariableToMessage(message, arrayOut[i]);
                    if (!args.out.couldBe(arrayOut[i])) {
                        message.append(" FAIL");
                    }
                    message.append("\n");
                    message.append("Errors at");
                }
                message.append(" [");
                message.append(Integer.toString(i));
                message.append("]");
            }
        }
        assertFalse("Incorrect output for checkNativeLengthFloatFloat" +
                (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), errorFound);
    }

    public class ArgumentsFloatNFloat {
        public float[] inV;
        public Target.Floaty out;
    }

    private void checkNativeLengthFloat2Float() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 2, 0xf1b4824cl, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            script.forEach_testNativeLengthFloat2Float(inV, out);
            verifyResultsNativeLengthFloat2Float(inV, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeLengthFloat2Float: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            scriptRelaxed.forEach_testNativeLengthFloat2Float(inV, out);
            verifyResultsNativeLengthFloat2Float(inV, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeLengthFloat2Float: " + e.toString());
        }
    }

    private void verifyResultsNativeLengthFloat2Float(Allocation inV, Allocation out, boolean relaxed) {
        float[] arrayInV = new float[INPUTSIZE * 2];
        Arrays.fill(arrayInV, (float) 42);
        inV.copyTo(arrayInV);
        float[] arrayOut = new float[INPUTSIZE * 1];
        Arrays.fill(arrayOut, (float) 42);
        out.copyTo(arrayOut);
        StringBuilder message = new StringBuilder();
        boolean errorFound = false;
        for (int i = 0; i < INPUTSIZE; i++) {
            ArgumentsFloatNFloat args = new ArgumentsFloatNFloat();
            // Create the appropriate sized arrays in args
            args.inV = new float[2];
            // Fill args with the input values
            for (int j = 0; j < 2 ; j++) {
                args.inV[j] = arrayInV[i * 2 + j];
            }
            Target target = new Target(Target.FunctionType.NATIVE, Target.ReturnType.FLOAT, relaxed);
            CoreMathVerifier.computeNativeLength(args, target);

            // Compare the expected outputs to the actual values returned by RS.
            boolean valid = true;
            if (!args.out.couldBe(arrayOut[i])) {
                valid = false;
            }
            if (!valid) {
                if (!errorFound) {
                    errorFound = true;
                    for (int j = 0; j < 2 ; j++) {
                        message.append("Input inV: ");
                        appendVariableToMessage(message, arrayInV[i * 2 + j]);
                        message.append("\n");
                    }
                    message.append("Expected output out: ");
                    appendVariableToMessage(message, args.out);
                    message.append("\n");
                    message.append("Actual   output out: ");
                    appendVariableToMessage(message, arrayOut[i]);
                    if (!args.out.couldBe(arrayOut[i])) {
                        message.append(" FAIL");
                    }
                    message.append("\n");
                    message.append("Errors at");
                }
                message.append(" [");
                message.append(Integer.toString(i));
                message.append("]");
            }
        }
        assertFalse("Incorrect output for checkNativeLengthFloat2Float" +
                (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), errorFound);
    }

    private void checkNativeLengthFloat3Float() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 3, 0x50bb10adl, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            script.forEach_testNativeLengthFloat3Float(inV, out);
            verifyResultsNativeLengthFloat3Float(inV, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeLengthFloat3Float: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            scriptRelaxed.forEach_testNativeLengthFloat3Float(inV, out);
            verifyResultsNativeLengthFloat3Float(inV, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeLengthFloat3Float: " + e.toString());
        }
    }

    private void verifyResultsNativeLengthFloat3Float(Allocation inV, Allocation out, boolean relaxed) {
        float[] arrayInV = new float[INPUTSIZE * 4];
        Arrays.fill(arrayInV, (float) 42);
        inV.copyTo(arrayInV);
        float[] arrayOut = new float[INPUTSIZE * 1];
        Arrays.fill(arrayOut, (float) 42);
        out.copyTo(arrayOut);
        StringBuilder message = new StringBuilder();
        boolean errorFound = false;
        for (int i = 0; i < INPUTSIZE; i++) {
            ArgumentsFloatNFloat args = new ArgumentsFloatNFloat();
            // Create the appropriate sized arrays in args
            args.inV = new float[3];
            // Fill args with the input values
            for (int j = 0; j < 3 ; j++) {
                args.inV[j] = arrayInV[i * 4 + j];
            }
            Target target = new Target(Target.FunctionType.NATIVE, Target.ReturnType.FLOAT, relaxed);
            CoreMathVerifier.computeNativeLength(args, target);

            // Compare the expected outputs to the actual values returned by RS.
            boolean valid = true;
            if (!args.out.couldBe(arrayOut[i])) {
                valid = false;
            }
            if (!valid) {
                if (!errorFound) {
                    errorFound = true;
                    for (int j = 0; j < 3 ; j++) {
                        message.append("Input inV: ");
                        appendVariableToMessage(message, arrayInV[i * 4 + j]);
                        message.append("\n");
                    }
                    message.append("Expected output out: ");
                    appendVariableToMessage(message, args.out);
                    message.append("\n");
                    message.append("Actual   output out: ");
                    appendVariableToMessage(message, arrayOut[i]);
                    if (!args.out.couldBe(arrayOut[i])) {
                        message.append(" FAIL");
                    }
                    message.append("\n");
                    message.append("Errors at");
                }
                message.append(" [");
                message.append(Integer.toString(i));
                message.append("]");
            }
        }
        assertFalse("Incorrect output for checkNativeLengthFloat3Float" +
                (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), errorFound);
    }

    private void checkNativeLengthFloat4Float() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 4, 0xafc19f0el, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            script.forEach_testNativeLengthFloat4Float(inV, out);
            verifyResultsNativeLengthFloat4Float(inV, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeLengthFloat4Float: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            scriptRelaxed.forEach_testNativeLengthFloat4Float(inV, out);
            verifyResultsNativeLengthFloat4Float(inV, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeLengthFloat4Float: " + e.toString());
        }
    }

    private void verifyResultsNativeLengthFloat4Float(Allocation inV, Allocation out, boolean relaxed) {
        float[] arrayInV = new float[INPUTSIZE * 4];
        Arrays.fill(arrayInV, (float) 42);
        inV.copyTo(arrayInV);
        float[] arrayOut = new float[INPUTSIZE * 1];
        Arrays.fill(arrayOut, (float) 42);
        out.copyTo(arrayOut);
        StringBuilder message = new StringBuilder();
        boolean errorFound = false;
        for (int i = 0; i < INPUTSIZE; i++) {
            ArgumentsFloatNFloat args = new ArgumentsFloatNFloat();
            // Create the appropriate sized arrays in args
            args.inV = new float[4];
            // Fill args with the input values
            for (int j = 0; j < 4 ; j++) {
                args.inV[j] = arrayInV[i * 4 + j];
            }
            Target target = new Target(Target.FunctionType.NATIVE, Target.ReturnType.FLOAT, relaxed);
            CoreMathVerifier.computeNativeLength(args, target);

            // Compare the expected outputs to the actual values returned by RS.
            boolean valid = true;
            if (!args.out.couldBe(arrayOut[i])) {
                valid = false;
            }
            if (!valid) {
                if (!errorFound) {
                    errorFound = true;
                    for (int j = 0; j < 4 ; j++) {
                        message.append("Input inV: ");
                        appendVariableToMessage(message, arrayInV[i * 4 + j]);
                        message.append("\n");
                    }
                    message.append("Expected output out: ");
                    appendVariableToMessage(message, args.out);
                    message.append("\n");
                    message.append("Actual   output out: ");
                    appendVariableToMessage(message, arrayOut[i]);
                    if (!args.out.couldBe(arrayOut[i])) {
                        message.append(" FAIL");
                    }
                    message.append("\n");
                    message.append("Errors at");
                }
                message.append(" [");
                message.append(Integer.toString(i));
                message.append("]");
            }
        }
        assertFalse("Incorrect output for checkNativeLengthFloat4Float" +
                (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), errorFound);
    }

    public class ArgumentsHalfHalf {
        public short inV;
        public double inVDouble;
        public Target.Floaty out;
    }

    private void checkNativeLengthHalfHalf() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_16, 1, 0x1777436l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_16, 1), INPUTSIZE);
            script.forEach_testNativeLengthHalfHalf(inV, out);
            verifyResultsNativeLengthHalfHalf(inV, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeLengthHalfHalf: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_16, 1), INPUTSIZE);
            scriptRelaxed.forEach_testNativeLengthHalfHalf(inV, out);
            verifyResultsNativeLengthHalfHalf(inV, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeLengthHalfHalf: " + e.toString());
        }
    }

    private void verifyResultsNativeLengthHalfHalf(Allocation inV, Allocation out, boolean relaxed) {
        short[] arrayInV = new short[INPUTSIZE * 1];
        Arrays.fill(arrayInV, (short) 42);
        inV.copyTo(arrayInV);
        short[] arrayOut = new short[INPUTSIZE * 1];
        Arrays.fill(arrayOut, (short) 42);
        out.copyTo(arrayOut);
        StringBuilder message = new StringBuilder();
        boolean errorFound = false;
        for (int i = 0; i < INPUTSIZE; i++) {
            ArgumentsHalfHalf args = new ArgumentsHalfHalf();
            // Create the appropriate sized arrays in args
            // Fill args with the input values
            args.inV = arrayInV[i];
            args.inVDouble = Float16Utils.convertFloat16ToDouble(args.inV);
            Target target = new Target(Target.FunctionType.NATIVE, Target.ReturnType.HALF, relaxed);
            CoreMathVerifier.computeNativeLength(args, target);

            // Compare the expected outputs to the actual values returned by RS.
            boolean valid = true;
            if (!args.out.couldBe(Float16Utils.convertFloat16ToDouble(arrayOut[i]))) {
                valid = false;
            }
            if (!valid) {
                if (!errorFound) {
                    errorFound = true;
                    message.append("Input inV: ");
                    appendVariableToMessage(message, arrayInV[i]);
                    message.append("\n");
                    message.append("Expected output out: ");
                    appendVariableToMessage(message, args.out);
                    message.append("\n");
                    message.append("Actual   output out: ");
                    appendVariableToMessage(message, arrayOut[i]);
                    message.append("\n");
                    message.append("Actual   output out (in double): ");
                    appendVariableToMessage(message, Float16Utils.convertFloat16ToDouble(arrayOut[i]));
                    if (!args.out.couldBe(Float16Utils.convertFloat16ToDouble(arrayOut[i]))) {
                        message.append(" FAIL");
                    }
                    message.append("\n");
                    message.append("Errors at");
                }
                message.append(" [");
                message.append(Integer.toString(i));
                message.append("]");
            }
        }
        assertFalse("Incorrect output for checkNativeLengthHalfHalf" +
                (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), errorFound);
    }

    public class ArgumentsHalfNHalf {
        public short[] inV;
        public double[] inVDouble;
        public Target.Floaty out;
    }

    private void checkNativeLengthHalf2Half() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_16, 2, 0xbffb8a74l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_16, 1), INPUTSIZE);
            script.forEach_testNativeLengthHalf2Half(inV, out);
            verifyResultsNativeLengthHalf2Half(inV, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeLengthHalf2Half: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_16, 1), INPUTSIZE);
            scriptRelaxed.forEach_testNativeLengthHalf2Half(inV, out);
            verifyResultsNativeLengthHalf2Half(inV, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeLengthHalf2Half: " + e.toString());
        }
    }

    private void verifyResultsNativeLengthHalf2Half(Allocation inV, Allocation out, boolean relaxed) {
        short[] arrayInV = new short[INPUTSIZE * 2];
        Arrays.fill(arrayInV, (short) 42);
        inV.copyTo(arrayInV);
        short[] arrayOut = new short[INPUTSIZE * 1];
        Arrays.fill(arrayOut, (short) 42);
        out.copyTo(arrayOut);
        StringBuilder message = new StringBuilder();
        boolean errorFound = false;
        for (int i = 0; i < INPUTSIZE; i++) {
            ArgumentsHalfNHalf args = new ArgumentsHalfNHalf();
            // Create the appropriate sized arrays in args
            args.inV = new short[2];
            args.inVDouble = new double[2];
            // Fill args with the input values
            for (int j = 0; j < 2 ; j++) {
                args.inV[j] = arrayInV[i * 2 + j];
                args.inVDouble[j] = Float16Utils.convertFloat16ToDouble(args.inV[j]);
            }
            Target target = new Target(Target.FunctionType.NATIVE, Target.ReturnType.HALF, relaxed);
            CoreMathVerifier.computeNativeLength(args, target);

            // Compare the expected outputs to the actual values returned by RS.
            boolean valid = true;
            if (!args.out.couldBe(Float16Utils.convertFloat16ToDouble(arrayOut[i]))) {
                valid = false;
            }
            if (!valid) {
                if (!errorFound) {
                    errorFound = true;
                    for (int j = 0; j < 2 ; j++) {
                        message.append("Input inV: ");
                        appendVariableToMessage(message, arrayInV[i * 2 + j]);
                        message.append("\n");
                    }
                    message.append("Expected output out: ");
                    appendVariableToMessage(message, args.out);
                    message.append("\n");
                    message.append("Actual   output out: ");
                    appendVariableToMessage(message, arrayOut[i]);
                    message.append("\n");
                    message.append("Actual   output out (in double): ");
                    appendVariableToMessage(message, Float16Utils.convertFloat16ToDouble(arrayOut[i]));
                    if (!args.out.couldBe(Float16Utils.convertFloat16ToDouble(arrayOut[i]))) {
                        message.append(" FAIL");
                    }
                    message.append("\n");
                    message.append("Errors at");
                }
                message.append(" [");
                message.append(Integer.toString(i));
                message.append("]");
            }
        }
        assertFalse("Incorrect output for checkNativeLengthHalf2Half" +
                (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), errorFound);
    }

    private void checkNativeLengthHalf3Half() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_16, 3, 0x9a26417l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_16, 1), INPUTSIZE);
            script.forEach_testNativeLengthHalf3Half(inV, out);
            verifyResultsNativeLengthHalf3Half(inV, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeLengthHalf3Half: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_16, 1), INPUTSIZE);
            scriptRelaxed.forEach_testNativeLengthHalf3Half(inV, out);
            verifyResultsNativeLengthHalf3Half(inV, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeLengthHalf3Half: " + e.toString());
        }
    }

    private void verifyResultsNativeLengthHalf3Half(Allocation inV, Allocation out, boolean relaxed) {
        short[] arrayInV = new short[INPUTSIZE * 4];
        Arrays.fill(arrayInV, (short) 42);
        inV.copyTo(arrayInV);
        short[] arrayOut = new short[INPUTSIZE * 1];
        Arrays.fill(arrayOut, (short) 42);
        out.copyTo(arrayOut);
        StringBuilder message = new StringBuilder();
        boolean errorFound = false;
        for (int i = 0; i < INPUTSIZE; i++) {
            ArgumentsHalfNHalf args = new ArgumentsHalfNHalf();
            // Create the appropriate sized arrays in args
            args.inV = new short[3];
            args.inVDouble = new double[3];
            // Fill args with the input values
            for (int j = 0; j < 3 ; j++) {
                args.inV[j] = arrayInV[i * 4 + j];
                args.inVDouble[j] = Float16Utils.convertFloat16ToDouble(args.inV[j]);
            }
            Target target = new Target(Target.FunctionType.NATIVE, Target.ReturnType.HALF, relaxed);
            CoreMathVerifier.computeNativeLength(args, target);

            // Compare the expected outputs to the actual values returned by RS.
            boolean valid = true;
            if (!args.out.couldBe(Float16Utils.convertFloat16ToDouble(arrayOut[i]))) {
                valid = false;
            }
            if (!valid) {
                if (!errorFound) {
                    errorFound = true;
                    for (int j = 0; j < 3 ; j++) {
                        message.append("Input inV: ");
                        appendVariableToMessage(message, arrayInV[i * 4 + j]);
                        message.append("\n");
                    }
                    message.append("Expected output out: ");
                    appendVariableToMessage(message, args.out);
                    message.append("\n");
                    message.append("Actual   output out: ");
                    appendVariableToMessage(message, arrayOut[i]);
                    message.append("\n");
                    message.append("Actual   output out (in double): ");
                    appendVariableToMessage(message, Float16Utils.convertFloat16ToDouble(arrayOut[i]));
                    if (!args.out.couldBe(Float16Utils.convertFloat16ToDouble(arrayOut[i]))) {
                        message.append(" FAIL");
                    }
                    message.append("\n");
                    message.append("Errors at");
                }
                message.append(" [");
                message.append(Integer.toString(i));
                message.append("]");
            }
        }
        assertFalse("Incorrect output for checkNativeLengthHalf3Half" +
                (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), errorFound);
    }

    private void checkNativeLengthHalf4Half() {
        Allocation inV = createRandomAllocation(mRS, Element.DataType.FLOAT_16, 4, 0x53493dbal, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_16, 1), INPUTSIZE);
            script.forEach_testNativeLengthHalf4Half(inV, out);
            verifyResultsNativeLengthHalf4Half(inV, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeLengthHalf4Half: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_16, 1), INPUTSIZE);
            scriptRelaxed.forEach_testNativeLengthHalf4Half(inV, out);
            verifyResultsNativeLengthHalf4Half(inV, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testNativeLengthHalf4Half: " + e.toString());
        }
    }

    private void verifyResultsNativeLengthHalf4Half(Allocation inV, Allocation out, boolean relaxed) {
        short[] arrayInV = new short[INPUTSIZE * 4];
        Arrays.fill(arrayInV, (short) 42);
        inV.copyTo(arrayInV);
        short[] arrayOut = new short[INPUTSIZE * 1];
        Arrays.fill(arrayOut, (short) 42);
        out.copyTo(arrayOut);
        StringBuilder message = new StringBuilder();
        boolean errorFound = false;
        for (int i = 0; i < INPUTSIZE; i++) {
            ArgumentsHalfNHalf args = new ArgumentsHalfNHalf();
            // Create the appropriate sized arrays in args
            args.inV = new short[4];
            args.inVDouble = new double[4];
            // Fill args with the input values
            for (int j = 0; j < 4 ; j++) {
                args.inV[j] = arrayInV[i * 4 + j];
                args.inVDouble[j] = Float16Utils.convertFloat16ToDouble(args.inV[j]);
            }
            Target target = new Target(Target.FunctionType.NATIVE, Target.ReturnType.HALF, relaxed);
            CoreMathVerifier.computeNativeLength(args, target);

            // Compare the expected outputs to the actual values returned by RS.
            boolean valid = true;
            if (!args.out.couldBe(Float16Utils.convertFloat16ToDouble(arrayOut[i]))) {
                valid = false;
            }
            if (!valid) {
                if (!errorFound) {
                    errorFound = true;
                    for (int j = 0; j < 4 ; j++) {
                        message.append("Input inV: ");
                        appendVariableToMessage(message, arrayInV[i * 4 + j]);
                        message.append("\n");
                    }
                    message.append("Expected output out: ");
                    appendVariableToMessage(message, args.out);
                    message.append("\n");
                    message.append("Actual   output out: ");
                    appendVariableToMessage(message, arrayOut[i]);
                    message.append("\n");
                    message.append("Actual   output out (in double): ");
                    appendVariableToMessage(message, Float16Utils.convertFloat16ToDouble(arrayOut[i]));
                    if (!args.out.couldBe(Float16Utils.convertFloat16ToDouble(arrayOut[i]))) {
                        message.append(" FAIL");
                    }
                    message.append("\n");
                    message.append("Errors at");
                }
                message.append(" [");
                message.append(Integer.toString(i));
                message.append("]");
            }
        }
        assertFalse("Incorrect output for checkNativeLengthHalf4Half" +
                (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), errorFound);
    }

    public void testNativeLength() {
        checkNativeLengthFloatFloat();
        checkNativeLengthFloat2Float();
        checkNativeLengthFloat3Float();
        checkNativeLengthFloat4Float();
        checkNativeLengthHalfHalf();
        checkNativeLengthHalf2Half();
        checkNativeLengthHalf3Half();
        checkNativeLengthHalf4Half();
    }
}
