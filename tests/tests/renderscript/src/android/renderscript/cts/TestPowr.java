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

public class TestPowr extends RSBaseCompute {

    private ScriptC_TestPowr script;
    private ScriptC_TestPowrRelaxed scriptRelaxed;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        script = new ScriptC_TestPowr(mRS);
        scriptRelaxed = new ScriptC_TestPowrRelaxed(mRS);
    }

    public class ArgumentsFloatFloatFloat {
        public float inX;
        public float inY;
        public Floaty out;
    }

    private void checkPowrFloatFloatFloat() {
        Allocation inX = createRandomFloatAllocation(mRS, Element.DataType.FLOAT_32, 1, 0x616e17ec158f6a8dl, 0, 3000);
        Allocation inY = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 1, 0x616e17ec158f6a8el, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            script.set_gAllocInY(inY);
            script.forEach_testPowrFloatFloatFloat(inX, out);
            verifyResultsPowrFloatFloatFloat(inX, inY, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testPowrFloatFloatFloat: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            scriptRelaxed.set_gAllocInY(inY);
            scriptRelaxed.forEach_testPowrFloatFloatFloat(inX, out);
            verifyResultsPowrFloatFloatFloat(inX, inY, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testPowrFloatFloatFloat: " + e.toString());
        }
    }

    private void verifyResultsPowrFloatFloatFloat(Allocation inX, Allocation inY, Allocation out, boolean relaxed) {
        float[] arrayInX = new float[INPUTSIZE * 1];
        inX.copyTo(arrayInX);
        float[] arrayInY = new float[INPUTSIZE * 1];
        inY.copyTo(arrayInY);
        float[] arrayOut = new float[INPUTSIZE * 1];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 1 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloatFloat args = new ArgumentsFloatFloatFloat();
                args.inX = arrayInX[i];
                args.inY = arrayInY[i];
                // Figure out what the outputs should have been.
                Floaty.setRelaxed(relaxed);
                CoreMathVerifier.computePowr(args);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 1 + j])) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append("Input inX: ");
                    message.append(String.format("%14.8g %8x %15a",
                            args.inX, Float.floatToRawIntBits(args.inX), args.inX));
                    message.append("\n");
                    message.append("Input inY: ");
                    message.append(String.format("%14.8g %8x %15a",
                            args.inY, Float.floatToRawIntBits(args.inY), args.inY));
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
                    assertTrue("Incorrect output for checkPowrFloatFloatFloat" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkPowrFloat2Float2Float2() {
        Allocation inX = createRandomFloatAllocation(mRS, Element.DataType.FLOAT_32, 2, 0xfc919df3002fbd93l, 0, 3000);
        Allocation inY = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 2, 0xfc919df3002fbd94l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            script.set_gAllocInY(inY);
            script.forEach_testPowrFloat2Float2Float2(inX, out);
            verifyResultsPowrFloat2Float2Float2(inX, inY, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testPowrFloat2Float2Float2: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 2), INPUTSIZE);
            scriptRelaxed.set_gAllocInY(inY);
            scriptRelaxed.forEach_testPowrFloat2Float2Float2(inX, out);
            verifyResultsPowrFloat2Float2Float2(inX, inY, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testPowrFloat2Float2Float2: " + e.toString());
        }
    }

    private void verifyResultsPowrFloat2Float2Float2(Allocation inX, Allocation inY, Allocation out, boolean relaxed) {
        float[] arrayInX = new float[INPUTSIZE * 2];
        inX.copyTo(arrayInX);
        float[] arrayInY = new float[INPUTSIZE * 2];
        inY.copyTo(arrayInY);
        float[] arrayOut = new float[INPUTSIZE * 2];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 2 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloatFloat args = new ArgumentsFloatFloatFloat();
                args.inX = arrayInX[i * 2 + j];
                args.inY = arrayInY[i * 2 + j];
                // Figure out what the outputs should have been.
                Floaty.setRelaxed(relaxed);
                CoreMathVerifier.computePowr(args);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 2 + j])) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append("Input inX: ");
                    message.append(String.format("%14.8g %8x %15a",
                            args.inX, Float.floatToRawIntBits(args.inX), args.inX));
                    message.append("\n");
                    message.append("Input inY: ");
                    message.append(String.format("%14.8g %8x %15a",
                            args.inY, Float.floatToRawIntBits(args.inY), args.inY));
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
                    assertTrue("Incorrect output for checkPowrFloat2Float2Float2" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkPowrFloat3Float3Float3() {
        Allocation inX = createRandomFloatAllocation(mRS, Element.DataType.FLOAT_32, 3, 0x50f67c13020dbf34l, 0, 3000);
        Allocation inY = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 3, 0x50f67c13020dbf35l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            script.set_gAllocInY(inY);
            script.forEach_testPowrFloat3Float3Float3(inX, out);
            verifyResultsPowrFloat3Float3Float3(inX, inY, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testPowrFloat3Float3Float3: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 3), INPUTSIZE);
            scriptRelaxed.set_gAllocInY(inY);
            scriptRelaxed.forEach_testPowrFloat3Float3Float3(inX, out);
            verifyResultsPowrFloat3Float3Float3(inX, inY, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testPowrFloat3Float3Float3: " + e.toString());
        }
    }

    private void verifyResultsPowrFloat3Float3Float3(Allocation inX, Allocation inY, Allocation out, boolean relaxed) {
        float[] arrayInX = new float[INPUTSIZE * 4];
        inX.copyTo(arrayInX);
        float[] arrayInY = new float[INPUTSIZE * 4];
        inY.copyTo(arrayInY);
        float[] arrayOut = new float[INPUTSIZE * 4];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 3 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloatFloat args = new ArgumentsFloatFloatFloat();
                args.inX = arrayInX[i * 4 + j];
                args.inY = arrayInY[i * 4 + j];
                // Figure out what the outputs should have been.
                Floaty.setRelaxed(relaxed);
                CoreMathVerifier.computePowr(args);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 4 + j])) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append("Input inX: ");
                    message.append(String.format("%14.8g %8x %15a",
                            args.inX, Float.floatToRawIntBits(args.inX), args.inX));
                    message.append("\n");
                    message.append("Input inY: ");
                    message.append(String.format("%14.8g %8x %15a",
                            args.inY, Float.floatToRawIntBits(args.inY), args.inY));
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
                    assertTrue("Incorrect output for checkPowrFloat3Float3Float3" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    private void checkPowrFloat4Float4Float4() {
        Allocation inX = createRandomFloatAllocation(mRS, Element.DataType.FLOAT_32, 4, 0xa55b5a3303ebc0d5l, 0, 3000);
        Allocation inY = createRandomAllocation(mRS, Element.DataType.FLOAT_32, 4, 0xa55b5a3303ebc0d6l, false);
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            script.set_gAllocInY(inY);
            script.forEach_testPowrFloat4Float4Float4(inX, out);
            verifyResultsPowrFloat4Float4Float4(inX, inY, out, false);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testPowrFloat4Float4Float4: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, getElement(mRS, Element.DataType.FLOAT_32, 4), INPUTSIZE);
            scriptRelaxed.set_gAllocInY(inY);
            scriptRelaxed.forEach_testPowrFloat4Float4Float4(inX, out);
            verifyResultsPowrFloat4Float4Float4(inX, inY, out, true);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testPowrFloat4Float4Float4: " + e.toString());
        }
    }

    private void verifyResultsPowrFloat4Float4Float4(Allocation inX, Allocation inY, Allocation out, boolean relaxed) {
        float[] arrayInX = new float[INPUTSIZE * 4];
        inX.copyTo(arrayInX);
        float[] arrayInY = new float[INPUTSIZE * 4];
        inY.copyTo(arrayInY);
        float[] arrayOut = new float[INPUTSIZE * 4];
        out.copyTo(arrayOut);
        for (int i = 0; i < INPUTSIZE; i++) {
            for (int j = 0; j < 4 ; j++) {
                // Extract the inputs.
                ArgumentsFloatFloatFloat args = new ArgumentsFloatFloatFloat();
                args.inX = arrayInX[i * 4 + j];
                args.inY = arrayInY[i * 4 + j];
                // Figure out what the outputs should have been.
                Floaty.setRelaxed(relaxed);
                CoreMathVerifier.computePowr(args);
                // Validate the outputs.
                boolean valid = true;
                if (!args.out.couldBe(arrayOut[i * 4 + j])) {
                    valid = false;
                }
                if (!valid) {
                    StringBuilder message = new StringBuilder();
                    message.append("Input inX: ");
                    message.append(String.format("%14.8g %8x %15a",
                            args.inX, Float.floatToRawIntBits(args.inX), args.inX));
                    message.append("\n");
                    message.append("Input inY: ");
                    message.append(String.format("%14.8g %8x %15a",
                            args.inY, Float.floatToRawIntBits(args.inY), args.inY));
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
                    assertTrue("Incorrect output for checkPowrFloat4Float4Float4" +
                            (relaxed ? "_relaxed" : "") + ":\n" + message.toString(), valid);
                }
            }
        }
    }

    public void testPowr() {
        checkPowrFloatFloatFloat();
        checkPowrFloat2Float2Float2();
        checkPowrFloat3Float3Float3();
        checkPowrFloat4Float4Float4();
    }
}
