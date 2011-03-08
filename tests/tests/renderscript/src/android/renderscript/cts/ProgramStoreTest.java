/*
 * Copyright (C) 2011 The Android Open Source Project
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

import com.android.cts.stub.R;

import android.renderscript.ProgramStore;
import android.renderscript.ProgramStore.Builder;
import android.renderscript.ProgramStore.DepthFunc;
import android.renderscript.ProgramStore.BlendSrcFunc;
import android.renderscript.ProgramStore.BlendDstFunc;

public class ProgramStoreTest extends RSBaseGraphics {

    void varyBuilderColorAndDither(ProgramStore.Builder pb) {
        for (int r = 0; r <= 1; r++) {
            boolean isR = (r == 1);
            for (int g = 0; g <= 1; g++) {
                boolean isG = (g == 1);
                for (int b = 0; b <= 1; b++) {
                    boolean isB = (b == 1);
                    for (int a = 0; a <= 1; a++) {
                        boolean isA = (a == 1);
                        for (int dither = 0; dither <= 1; dither++) {
                            boolean isDither = (dither == 1);
                            pb.setDepthMaskEnabled(isDither);
                            pb.setColorMaskEnabled(isR, isG, isB, isA);
                            pb.create();
                        }
                    }
                }
            }
        }
    }

    public void testProgramStoreBuilder() {
        for (int depth = 0; depth <= 1; depth++) {
            boolean depthMask = (depth == 1);
            for (DepthFunc df : DepthFunc.values()) {
                for (BlendSrcFunc bsf : BlendSrcFunc.values()) {
                    for (BlendDstFunc bdf : BlendDstFunc.values()) {
                        ProgramStore.Builder b = new ProgramStore.Builder(mRS);
                        b.setDepthFunc(df);
                        b.setDepthMaskEnabled(depthMask);
                        b.setBlendFunc(bsf, bdf);
                        varyBuilderColorAndDither(b);
                    }
                }
            }
        }
    }

    public void testPrebuiltProgramStore() {
        assertTrue(ProgramStore.BLEND_ALPHA_DEPTH_NONE(mRS) != null);
        assertTrue(ProgramStore.BLEND_ALPHA_DEPTH_TEST(mRS) != null);
        assertTrue(ProgramStore.BLEND_NONE_DEPTH_NONE(mRS) != null);
        assertTrue(ProgramStore.BLEND_NONE_DEPTH_TEST(mRS) != null);
    }
}


