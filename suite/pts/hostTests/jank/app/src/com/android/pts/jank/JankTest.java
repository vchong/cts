/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.android.pts.jank;

import com.android.pts.opengl.GLActivityIntentKeys;
import com.android.pts.opengl.primitive.BenchmarkName;
import com.android.pts.opengl.primitive.GLPrimitiveActivity;
import com.android.pts.util.PtsActivityInstrumentationTestCase2;

import android.content.Intent;

public class JankTest extends PtsActivityInstrumentationTestCase2<GLPrimitiveActivity> {

    public JankTest() {
        super(GLPrimitiveActivity.class);
    }

    /**
     * Runs the full OpenGL ES 2.0 pipeline test.
     */
    public void testFullPipeline() throws Exception {
        runBenchmark(BenchmarkName.FullPipeline);
    }

    /**
     * Runs the pixel output test.
     */
    public void testPixelOutput() throws Exception {
        runBenchmark(BenchmarkName.PixelOutput);
    }

    /**
     * Runs the shader performance test.
     */
    public void testShaderPerf() throws Exception {
        runBenchmark(BenchmarkName.ShaderPerf);
    }

    /**
     * Runs the context switch overhead test.
     */
    public void testContextSwitch() throws Exception {
        runBenchmark(BenchmarkName.ContextSwitch);
    }

    /**
     * Runs the benchhmark for jank test.
     */
    public void runBenchmark(BenchmarkName benchmark) throws Exception {
        Intent intent = new Intent();
        String benchmarkName = benchmark.toString();
        intent.putExtra(GLActivityIntentKeys.INTENT_EXTRA_BENCHMARK_NAME, benchmarkName);
        intent.putExtra(GLActivityIntentKeys.INTENT_EXTRA_OFFSCREEN, false);
        intent.putExtra(GLActivityIntentKeys.INTENT_EXTRA_NUM_FRAMES, 200);
        intent.putExtra(GLActivityIntentKeys.INTENT_EXTRA_NUM_ITERATIONS, 1);
        intent.putExtra(GLActivityIntentKeys.INTENT_EXTRA_TIMEOUT, 50000);
        GLPrimitiveActivity activity = null;
        setActivityIntent(intent);
        try {
            activity = getActivity();
            activity.waitForCompletion();
        } finally {
            if (activity != null) {
                activity.finish();
            }
        }
    }
}
