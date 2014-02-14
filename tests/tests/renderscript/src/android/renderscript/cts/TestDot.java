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

public class TestDot extends RSBaseCompute {

    private ScriptC_TestDot script;
    private ScriptC_TestDotRelaxed scriptRelaxed;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        script = new ScriptC_TestDot(mRS);
        scriptRelaxed = new ScriptC_TestDotRelaxed(mRS);
    }

    private void checkDotFloatFloatFloat() {
        Allocation inLhs = CreateRandomAllocation(mRS, Element.DataType.FLOAT_32, 1, 0x84945ebed029514eL);
        Allocation inRhs = CreateRandomAllocation(mRS, Element.DataType.FLOAT_32, 1, 0x84945ebed029514eL);
        try {
            Allocation out = Allocation.createSized(mRS, GetElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            script.set_gAllocInRhs(inRhs);
            script.forEach_testDotFloatFloatFloat(inLhs, out);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testDotFloatFloatFloat: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, GetElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            scriptRelaxed.set_gAllocInRhs(inRhs);
            scriptRelaxed.forEach_testDotFloatFloatFloat(inLhs, out);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testDotFloatFloatFloat: " + e.toString());
        }
    }

    private void checkDotFloat2Float2Float() {
        Allocation inLhs = CreateRandomAllocation(mRS, Element.DataType.FLOAT_32, 2, 0x801f9d8e635785faL);
        Allocation inRhs = CreateRandomAllocation(mRS, Element.DataType.FLOAT_32, 2, 0x801f9d8e635785faL);
        try {
            Allocation out = Allocation.createSized(mRS, GetElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            script.set_gAllocInRhs(inRhs);
            script.forEach_testDotFloat2Float2Float(inLhs, out);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testDotFloat2Float2Float: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, GetElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            scriptRelaxed.set_gAllocInRhs(inRhs);
            scriptRelaxed.forEach_testDotFloat2Float2Float(inLhs, out);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testDotFloat2Float2Float: " + e.toString());
        }
    }

    private void checkDotFloat3Float3Float() {
        Allocation inLhs = CreateRandomAllocation(mRS, Element.DataType.FLOAT_32, 3, 0xd56f5b47b0c7d224L);
        Allocation inRhs = CreateRandomAllocation(mRS, Element.DataType.FLOAT_32, 3, 0xd56f5b47b0c7d224L);
        try {
            Allocation out = Allocation.createSized(mRS, GetElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            script.set_gAllocInRhs(inRhs);
            script.forEach_testDotFloat3Float3Float(inLhs, out);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testDotFloat3Float3Float: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, GetElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            scriptRelaxed.set_gAllocInRhs(inRhs);
            scriptRelaxed.forEach_testDotFloat3Float3Float(inLhs, out);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testDotFloat3Float3Float: " + e.toString());
        }
    }

    private void checkDotFloat4Float4Float() {
        Allocation inLhs = CreateRandomAllocation(mRS, Element.DataType.FLOAT_32, 4, 0x2abf1900fe381e4eL);
        Allocation inRhs = CreateRandomAllocation(mRS, Element.DataType.FLOAT_32, 4, 0x2abf1900fe381e4eL);
        try {
            Allocation out = Allocation.createSized(mRS, GetElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            script.set_gAllocInRhs(inRhs);
            script.forEach_testDotFloat4Float4Float(inLhs, out);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testDotFloat4Float4Float: " + e.toString());
        }
        try {
            Allocation out = Allocation.createSized(mRS, GetElement(mRS, Element.DataType.FLOAT_32, 1), INPUTSIZE);
            scriptRelaxed.set_gAllocInRhs(inRhs);
            scriptRelaxed.forEach_testDotFloat4Float4Float(inLhs, out);
        } catch (Exception e) {
            throw new RSRuntimeException("RenderScript. Can't invoke forEach_testDotFloat4Float4Float: " + e.toString());
        }
    }

    public void testDot() {
        checkDotFloatFloatFloat();
        checkDotFloat2Float2Float();
        checkDotFloat3Float3Float();
        checkDotFloat4Float4Float();
    }
}
