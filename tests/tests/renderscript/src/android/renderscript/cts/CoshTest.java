/*
 * Copyright (C) 2012 The Android Open Source Project
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

package android.renderscript.cts;

import android.renderscript.Allocation;
import com.android.cts.stub.R;
import android.renderscript.RSRuntimeException;

public class CoshTest extends RSBaseCompute {

    private ScriptC_cosh_f32 ms_f32;
    private ScriptC_cosh_f32_2 ms_f32_2;
    private ScriptC_cosh_f32_3 ms_f32_3;
    private ScriptC_cosh_f32_4 ms_f32_4;

    @Override
    public void forEach(int testId, Allocation mIn, Allocation mOut) throws RSRuntimeException {
        switch (testId) {
        case TEST_F32:
            ms_f32.forEach_root(mIn, mOut);
            break;
        case TEST_F32_2:
            ms_f32_2.forEach_root(mIn, mOut);
            break;
        case TEST_F32_3:
            ms_f32_3.forEach_root(mIn, mOut);
            break;
        case TEST_F32_4:
            ms_f32_4.forEach_root(mIn, mOut);
            break;
        }
    }

    @Override
    protected float[] getRefArray(float[] in, int input_size, int stride, int skip) {
        float[] ref = new float[input_size * stride];
        for (int i = 0; i < input_size; i++) {
            for (int j = 0; j < stride - skip; j++) {
                int idx= i * stride + j;
                int idxRef = i * (stride - skip) + j;
                ref[idxRef] = (float)(Math.cosh((double)in[idx]));
            }
        }
        return ref;
    }

    public void testCoshF32() {
        ms_f32 = new ScriptC_cosh_f32(mRS, mRes, R.raw.cosh_f32);
        doF32(0xfe, 4);
    }

    public void testCoshF32_2() {
        ms_f32_2 = new ScriptC_cosh_f32_2(mRS, mRes, R.raw.cosh_f32_2);
        doF32_2(0x71, 4);
    }

    public void testCoshF32_3() {
        ms_f32_3 = new ScriptC_cosh_f32_3(mRS, mRes, R.raw.cosh_f32_3);
        doF32_3(0xa, 4);
    }

    public void testCoshF32_4() {
        ms_f32_4 = new ScriptC_cosh_f32_4(mRS, mRes, R.raw.cosh_f32_4);
        doF32_4(0xabe, 4);
    }
}
