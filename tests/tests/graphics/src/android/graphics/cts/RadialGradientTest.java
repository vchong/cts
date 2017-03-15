/*
 * Copyright (C) 2017 The Android Open Source Project
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

package android.graphics.cts;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader.TileMode;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import com.android.compatibility.common.util.ColorUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

@SmallTest
@RunWith(AndroidJUnit4.class)
public class RadialGradientTest {
    @Test
    public void testSet() {
        // only using 1 pixel radius gradient since we don't care about interpolation here.
        // Note: we align start/end to be center pixel so colors at those pixels are exact.
        RadialGradient gradient = new RadialGradient(0.5f, 0.5f, 1,
                Color.RED, Color.BLUE, TileMode.CLAMP);

        Bitmap bitmap = Bitmap.createBitmap(3, 1, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setShader(gradient);
        canvas.drawPaint(paint);


        // red, blue, clamped to blue
        ColorUtils.verifyColor(Color.RED, bitmap.getPixel(0, 0), 5);
        ColorUtils.verifyColor(Color.BLUE, bitmap.getPixel(1, 0), 5);
        ColorUtils.verifyColor(Color.BLUE, bitmap.getPixel(2, 0), 5);

        gradient.set(0.5f, 0.5f, 1, Color.GREEN, Color.WHITE, TileMode.MIRROR);
        canvas.drawPaint(paint);

        // green, white, mirrored to green
        ColorUtils.verifyColor(Color.GREEN, bitmap.getPixel(0, 0), 5);
        ColorUtils.verifyColor(Color.WHITE, bitmap.getPixel(1, 0), 5);
        ColorUtils.verifyColor(Color.GREEN, bitmap.getPixel(2, 0), 5);
    }
}
