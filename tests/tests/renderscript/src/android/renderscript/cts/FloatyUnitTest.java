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

package android.renderscript.cts;

import android.renderscript.cts.Target;
import android.renderscript.cts.Float16Utils;
import android.renderscript.RSRuntimeException;
import android.util.Log;

public class FloatyUnitTest extends RSBaseCompute {
    static float subnormalFloat = 10000 * Float.MIN_VALUE;
    static float normalFloat1 = 1.7833920e+16f;
    static float normalFloat2 = -1.9905756e-16f;

    static double subnormalDouble = 10000 * Double.MIN_VALUE;
    static double normalDouble = 1.7833920e+16;

    // Some double values that are precisely representable in half-precision.
    static double[] preciseFloat16Values = {Double.NaN,
                                            Double.POSITIVE_INFINITY,
                                            Double.NEGATIVE_INFINITY,
                                            0., 1, 2, 74.0625, 3000, 65504,
                                            Float16Utils.MIN_VALUE,
                                            Float16Utils.MIN_VALUE * 100,
                                            -0., -1, -2, -74.0625, -3000, -65504,
                                            -Float16Utils.MIN_VALUE,
                                            -Float16Utils.MIN_VALUE * 100,
                                           };

    // Fail if Floaty f doesn't accept value
    private void shouldAccept(Target.Floaty f, double value) {
        if (!f.couldBe(value)) {
            StringBuilder message = new StringBuilder();
            message.append("Floaty: ");
            appendVariableToMessage(message, f);
            message.append("\n");
            message.append("Value: ");
            appendVariableToMessage(message, (float) value);
            message.append("\n");
            assertTrue("Floaty incorrectly doesn't accept value:\n" + message.toString(), false);
        }
    }

    // Fail if Floaty f accepts value
    private void shouldNotAccept(Target.Floaty f, double value) {
        if (f.couldBe(value)) {
            StringBuilder message = new StringBuilder();
            message.append("Floaty: ");
            appendVariableToMessage(message, f);
            message.append("\n");
            message.append("Value: ");
            appendVariableToMessage(message, (float) value);
            message.append("\n");
            assertTrue("Floaty incorrectly accepts value:\n" + message.toString(), false);
        }
    }

    // Test Target that accepts precise 1ulp error for floating values.
    public void testFloat1Ulp() {
        Target t = new Target(Target.FunctionType.NORMAL, Target.ReturnType.FLOAT, false);
        t.setPrecision(1, 1);

        Target.Floaty subnormalFloaty = t.new32(subnormalFloat);
        Target.Floaty normalFloaty = t.new32(normalFloat1);

        // for subnormal
        shouldAccept(subnormalFloaty, (double) subnormalFloat);
        shouldAccept(subnormalFloaty, (double) subnormalFloat + Math.ulp(subnormalFloat));
        shouldAccept(subnormalFloaty, (double) subnormalFloat - Math.ulp(subnormalFloat));
        shouldNotAccept(subnormalFloaty, (double) subnormalFloat + 2 * Math.ulp(subnormalFloat));
        shouldNotAccept(subnormalFloaty, (double) subnormalFloat - 2 * Math.ulp(subnormalFloat));
        shouldNotAccept(subnormalFloaty, (double) normalFloat1);

        // for normalFloaty
        shouldAccept(normalFloaty, (double) normalFloat1);
        shouldAccept(normalFloaty, (double) normalFloat1 + Math.ulp(normalFloat1));
        shouldAccept(normalFloaty, (double) normalFloat1 - Math.ulp(normalFloat1));
        shouldNotAccept(normalFloaty, (double) normalFloat1 + 2 * Math.ulp(normalFloat1));
        shouldNotAccept(normalFloaty, (double) normalFloat1 - 2 * Math.ulp(normalFloat1));
        shouldNotAccept(normalFloaty, (double) subnormalFloat);
    }

    // Test Target that accepts precise 8192ulp error for floating values.
    public void testFloat8192Ulp() {
        Target t = new Target(Target.FunctionType.NORMAL, Target.ReturnType.FLOAT, false);
        t.setPrecision(8192, 8192);

        Target.Floaty subnormalFloaty = t.new32(subnormalFloat);
        Target.Floaty normalFloaty = t.new32(normalFloat2);

        // for subnormalFloaty
        shouldAccept(subnormalFloaty, (double) subnormalFloat);
        shouldAccept(subnormalFloaty, (double) subnormalFloat + 8192 * Math.ulp(subnormalFloat));
        shouldAccept(subnormalFloaty, (double) subnormalFloat - 8192 * Math.ulp(subnormalFloat));
        shouldNotAccept(subnormalFloaty, (double) subnormalFloat + 8193 * Math.ulp(subnormalFloat));
        shouldNotAccept(subnormalFloaty, (double) subnormalFloat - 8193 * Math.ulp(subnormalFloat));
        shouldNotAccept(subnormalFloaty, (double) normalFloat1);

        // for normalFloaty
        shouldAccept(normalFloaty, (double) normalFloat2);
        shouldAccept(normalFloaty, (double) normalFloat2 + 8192 * Math.ulp(normalFloat2));
        shouldAccept(normalFloaty, (double) normalFloat2 - 8192 * Math.ulp(normalFloat2));
        shouldNotAccept(normalFloaty, (double) normalFloat2 + 8193 * Math.ulp(normalFloat2));
        shouldNotAccept(normalFloaty, (double) normalFloat2 - 8193 * Math.ulp(normalFloat2));
        shouldNotAccept(normalFloaty, (double) subnormalFloat);
    }

    // Test Target that accepts relaxed 1ulp error for floating values.
    public void testFloat1UlpRelaxed() {
        Target t = new Target(Target.FunctionType.NORMAL, Target.ReturnType.FLOAT, true);
        t.setPrecision(1, 1);

        Target.Floaty subnormalFloaty = t.new32(subnormalFloat);

        // for subnormal
        shouldAccept(subnormalFloaty, (double) subnormalFloat);
        // In relaxed mode, Floaty uses the smallest normal as the ULP if ULP is subnormal.
        shouldAccept(subnormalFloaty, (double) Float.MIN_NORMAL + Float.MIN_NORMAL);
        shouldAccept(subnormalFloaty, (double) 0.f - Float.MIN_NORMAL);
        shouldNotAccept(subnormalFloaty, (double) Float.MIN_NORMAL + 2 * Float.MIN_NORMAL);
        shouldNotAccept(subnormalFloaty, (double) 0.f - 2 * Float.MIN_NORMAL);
        shouldNotAccept(subnormalFloaty, (double) normalFloat1);
    }

    // Test Target that accepts relaxed 8192ulp error for floating values.
    public void testFloat8192UlpRelaxed() {
        Target t = new Target(Target.FunctionType.NORMAL, Target.ReturnType.FLOAT, true);
        t.setPrecision(8192, 8192);

        Target.Floaty subnormalFloaty = t.new32(subnormalFloat);

        // for subnormalFloaty
        shouldAccept(subnormalFloaty, (double) subnormalFloat);
        // In relaxed mode, Floaty uses the smallest normal as the ULP if ULP is subnormal.
        shouldAccept(subnormalFloaty, (double) Float.MIN_NORMAL + 8192 * Float.MIN_NORMAL);
        shouldAccept(subnormalFloaty, (double) 0.f - 8192 * Float.MIN_NORMAL);
        shouldNotAccept(subnormalFloaty, (double) Float.MIN_NORMAL + 8193 * Float.MIN_NORMAL);
        shouldNotAccept(subnormalFloaty, (double) 0.f - 8193 * Float.MIN_NORMAL);
        shouldNotAccept(subnormalFloaty, (double) normalFloat1);
    }

    // Test Target that accepts precise 1ulp error for double values.
    public void testDouble1Ulp() {
        Target t = new Target(Target.FunctionType.NORMAL, Target.ReturnType.DOUBLE, false);
        t.setPrecision(1, 1);

        Target.Floaty subnormalFloaty = t.new64(subnormalDouble);
        Target.Floaty normalFloaty = t.new64(normalDouble);

        // for subnormal
        shouldAccept(subnormalFloaty, subnormalDouble);
        shouldAccept(subnormalFloaty, subnormalDouble + Math.ulp(subnormalDouble));
        shouldAccept(subnormalFloaty, subnormalDouble - Math.ulp(subnormalDouble));
        shouldNotAccept(subnormalFloaty, subnormalDouble + 2 * Math.ulp(subnormalDouble));
        shouldNotAccept(subnormalFloaty, subnormalDouble - 2 * Math.ulp(subnormalDouble));
        shouldNotAccept(subnormalFloaty, normalDouble);

        // for normalFloaty
        shouldAccept(normalFloaty, normalDouble);
        shouldAccept(normalFloaty, normalDouble + Math.ulp(normalDouble));
        shouldAccept(normalFloaty, normalDouble - Math.ulp(normalDouble));
        shouldNotAccept(normalFloaty, subnormalDouble + 2 * Math.ulp(normalDouble));
        shouldNotAccept(normalFloaty, subnormalDouble - 2 * Math.ulp(normalDouble));
        shouldNotAccept(normalFloaty, subnormalDouble);
    }

    // Test Target that accepts precise 8192ulp error for double values.
    public void testDouble8192Ulp() {
        Target t = new Target(Target.FunctionType.NORMAL, Target.ReturnType.DOUBLE, false);
        t.setPrecision(8192, 8192);

        Target.Floaty subnormalFloaty = t.new64(subnormalDouble);
        Target.Floaty normalFloaty = t.new64(normalDouble);

        // for subnormal
        shouldAccept(subnormalFloaty, subnormalDouble);
        shouldAccept(subnormalFloaty, subnormalDouble + 8192 * Math.ulp(subnormalDouble));
        shouldAccept(subnormalFloaty, subnormalDouble - 8192 * Math.ulp(subnormalDouble));
        shouldNotAccept(subnormalFloaty, subnormalDouble + 8193 * Math.ulp(subnormalDouble));
        shouldNotAccept(subnormalFloaty, subnormalDouble - 8193 * Math.ulp(subnormalDouble));
        shouldNotAccept(subnormalFloaty, normalDouble);

        // for normalFloaty
        shouldAccept(normalFloaty, normalDouble);
        shouldAccept(normalFloaty, normalDouble + 8192 * Math.ulp(normalDouble));
        shouldAccept(normalFloaty, normalDouble - 8192 * Math.ulp(normalDouble));
        shouldNotAccept(normalFloaty, subnormalDouble + 8193 * Math.ulp(normalDouble));
        shouldNotAccept(normalFloaty, subnormalDouble - 8193 * Math.ulp(normalDouble));
        shouldNotAccept(normalFloaty, subnormalDouble);
    }

    // Test that range of allowed error is trimmed at the zero boundary.  This function tests both
    // float and double Targets.
    public void testRangeDoesNotAcrossZero() {
        Target t;
        Target.Floaty floaty;

        t = new Target(Target.FunctionType.NORMAL, Target.ReturnType.FLOAT, false);
        t.setPrecision(4, 4);

        floaty = t.new32(Float.MIN_VALUE);
        shouldAccept(floaty, (double) Float.MIN_VALUE);
        shouldAccept(floaty, (double) Float.MIN_VALUE + 4 * Float.MIN_VALUE);
        shouldAccept(floaty, (double) 0.f);
        shouldNotAccept(floaty, (double) 0.f - Float.MIN_VALUE);

        floaty = t.new32(-Float.MIN_VALUE);
        shouldAccept(floaty, (double) -Float.MIN_VALUE);
        shouldAccept(floaty, (double) -Float.MIN_VALUE - 4 * Float.MIN_VALUE);
        shouldAccept(floaty, (double) 0.f);
        shouldNotAccept(floaty, (double) 0.f + Float.MIN_VALUE);

        t = new Target(Target.FunctionType.NORMAL, Target.ReturnType.DOUBLE, false);
        t.setPrecision(4, 4);

        floaty = t.new64(Double.MIN_VALUE);
        shouldAccept(floaty, Double.MIN_VALUE);
        shouldAccept(floaty, Double.MIN_VALUE + 4 * Double.MIN_VALUE);
        shouldAccept(floaty, 0.f);
        shouldNotAccept(floaty, 0.f - Double.MIN_VALUE);

        floaty = t.new64(-Double.MIN_VALUE);
        shouldAccept(floaty, -Double.MIN_VALUE);
        shouldAccept(floaty, -Double.MIN_VALUE - 4 * Double.MIN_VALUE);
        shouldAccept(floaty, 0.f);
        shouldNotAccept(floaty, 0.f + Double.MIN_VALUE);
    }

    // Validate float16Ulp for a double value that is precisely representable in half-precision.
    private void validateFloat16Ulp(double value) {
        double absoluteValue = Math.abs(value);
        double ulp = Float16Utils.float16Ulp(value);

        if (absoluteValue == 0.) {
            assertEquals(Float16Utils.MIN_VALUE, ulp);
            return;
        }
        if (Double.isNaN(absoluteValue)) {
            assertTrue("Ulp for NaN is not NaN", Double.isNaN(ulp));
            return;
        }
        if (absoluteValue == Double.POSITIVE_INFINITY ||
            absoluteValue == Double.NEGATIVE_INFINITY) {
            assertEquals(Double.POSITIVE_INFINITY, ulp);
            return;
        }

        if (absoluteValue < Math.scalb(1., -24)) {
            assertTrue("Input double value smaller than MIN_VALUE for Float16", false);
        }

        if (absoluteValue < Math.scalb(1., -14)) {
            // Input is subnormal Float16.  Ulp should be 2^-24
            assertEquals(Math.scalb(1., -24), ulp);
            return;
        }

        boolean tested = false;
        int exponent = -13;
        double limit = Math.scalb(1., exponent);
        for (; exponent <= 16; exponent ++, limit *= 2) {
            if (absoluteValue < limit) {
                assertEquals(ulp, Math.scalb(1., exponent - 11));
                tested = true;
                break;
            }
        }
        assertTrue("Ulp not validated.  Absolute value " + Double.toString(absoluteValue), tested);
    }

    // Test float16Ulp for all valid inputs (i.e. all Float16 values represented as Double) and test
    // that assertions fire for Double values that are not representable in Float16.
    public void testFloat16Ulp() {
        // Test float16Ulp for all short values.
        for (short s = Short.MIN_VALUE; ; s ++) {
            validateFloat16Ulp(Float16Utils.convertFloat16ToDouble(s));
            // Detect loop termination here.  Doing so in the for statement creates an infinite
            // loop.
            if (s == Short.MAX_VALUE) {
                break;
            }
        }

        // Test float16Ulp for some known values
        for (double value: preciseFloat16Values) {
            validateFloat16Ulp(value);
        }

        // Expect an exception for values not representable in Float16.  The loop below tests this
        // for all values in this array and their negation.
        double valuesOutOfFloat16Range[] = {Math.scalb(1., -100),
                                            Double.valueOf("0x1.8p-24"),
                                            Double.valueOf("0x1.ffffffffp-20"),
                                            1024.55, 65520., 66000.
                                           };

        for (double value: valuesOutOfFloat16Range) {
            try {
                Float16Utils.float16Ulp(value);
                assertTrue("Expected exception not thrown for: " + Double.toString(value), false);
            } catch (RSRuntimeException e) {
                // Ignore the expected exception.
                // Log.w("testFloat16Ulp", "Received expected exception: " + e.getMessage());
            }

            try {
                Float16Utils.float16Ulp(-value);
                assertTrue("Expected exception not thrown for: " + Double.toString(value), false);
            } catch (RSRuntimeException e) {
                // Ignore the expected exception.
                // Log.w("testFloat16Ulp", "Received expected exception: " + e.getMessage());
            }
        }
    }

    private void validateRoundToFloat16(double argument, double low, double high) {
        double[] result = Float16Utils.roundToFloat16(argument);
        double[] expected = new double[]{low, high};

        for (int i = 0; i < 2; i ++) {
            if (result[i] == expected[i])
                continue;
            if (Double.isNaN(result[i]) && Double.isNaN(expected[i]))
                continue;

            StringBuilder message = new StringBuilder();
            message.append("For input ");
            appendVariableToMessage(message, argument);
            message.append("\n");
            message.append("For field " + Integer.toString(i) + ": Expected output: ");
            appendVariableToMessage(message, expected[i]);
            message.append("\n");
            message.append("Actual output: ");
            appendVariableToMessage(message, result[i]);
            message.append("\n");
            assertTrue("roundToFloat16 error:\n" + message.toString(), false);
        }
    }

    public void testRoundToFloat16() {
        // Validate values that are precisely representable in Float16.  The bounds have to be the
        // same as the input.
        for (double value: preciseFloat16Values) {
            validateRoundToFloat16(value, value, value);
        }

        // Validate for special cases:
        //   Double.MIN_VALUE is between Float16 0 and MIN_VALUE
        //   Double.MAX_VALUE is between Float16 MAX_VALUE and +Infinity
        //   65519 and 65520 are between Float16 MAX_VALUE and +Infinity (but their exponent is
        //       within Float16's range).
        //   3001.5 and 3000.5 are between Float16 3000 and 3002
        validateRoundToFloat16(Double.MIN_VALUE, 0.0, Float16Utils.MIN_VALUE);
        validateRoundToFloat16(Double.MAX_VALUE, Double.POSITIVE_INFINITY,
                               Double.POSITIVE_INFINITY);
        validateRoundToFloat16(65519., 65504., Double.POSITIVE_INFINITY);
        validateRoundToFloat16(65520., 65504., Double.POSITIVE_INFINITY);
        validateRoundToFloat16(3001.5, 3000., 3002.);
        validateRoundToFloat16(3000.5, 3000., 3002.);

        validateRoundToFloat16(-Double.MIN_VALUE, -Float16Utils.MIN_VALUE, -0.0);
        validateRoundToFloat16(-Double.MAX_VALUE, Double.NEGATIVE_INFINITY,
                               Double.NEGATIVE_INFINITY);
        validateRoundToFloat16(-65519., Double.NEGATIVE_INFINITY, -65504.);
        validateRoundToFloat16(-65520., Double.NEGATIVE_INFINITY, -65504.);
        validateRoundToFloat16(-3001.5, -3002., -3000.);
        validateRoundToFloat16(-3000.5, -3002., -3000.);

        // Validate that values precisely between two Float16 values get handled properly.
        double[] float16Sample = {0., 1., 100.,
                                  Float16Utils.MIN_VALUE,
                                  Float16Utils.MIN_VALUE * 100,
                                  Float16Utils.MIN_NORMAL * 100
                                 };

        for (double value: float16Sample) {
            double ulp = Float16Utils.float16Ulp(value);

            validateRoundToFloat16(value + 0.5 * ulp, value, value + ulp);
            validateRoundToFloat16(-value - 0.5 * ulp, -value - ulp, -value);
        }
    }
}
