/*
     * Copyright (C) 2014 The Android Open Source Project
     *
     * Licensed under the Apache License, Version 2.0 (the "License");
     * you may not use this file except in compliance with the License.
     * You may obtain a copy of the License at
     *
     *      http://www.apache.org/licenses/LICENSE2.0
     *
     * Unless required by applicable law or agreed to in riting, software
     * distributed under the License is distributed on an "AS IS" BASIS,
     * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     * See the License for the specific language governing permissions and
     * limitations under the License.
     */
package android.uirendering.cts;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Modifies the canvas and paint objects when called.
 */
public abstract class DisplayModifier {
    private static final RectF gRect = new RectF(0, 0, 100, 100);
    private static final float[] gPts = new float[]{
            0, 100, 100, 0, 100, 200, 200, 100
    };
    private static final float[] gTriPts = new float[]{
            75, 0, 130, 130, 130, 130, 0, 130, 0, 130, 75, 0
    };
    private static final int NUM_PARALLEL_LINES = 24;
    private static final float[] gLinePts = new float[NUM_PARALLEL_LINES * 8 + gTriPts.length];

    static {
        int index;
        for (index = 0; index < gTriPts.length; index++) {
            gLinePts[index] = gTriPts[index];
        }
        float val = 0;
        for (int i = 0; i < NUM_PARALLEL_LINES; i++) {
            gLinePts[index + 0] = 150;
            gLinePts[index + 1] = val;
            gLinePts[index + 2] = 300;
            gLinePts[index + 3] = val;
            index += 4;
            val += 8 + (2.0f / NUM_PARALLEL_LINES);
        }
        val = 0;
        for (int i = 0; i < NUM_PARALLEL_LINES; i++) {
            gLinePts[index + 0] = val;
            gLinePts[index + 1] = 150;
            gLinePts[index + 2] = val;
            gLinePts[index + 3] = 300;
            index += 4;
            val += 8 + (2.0f / NUM_PARALLEL_LINES);
        }
    }

    // This linked hash map contains each of the different things that can be done to a canvas and
    // paint object, like anti-aliasing or drawing. Within those LinkedHashMaps are the various
    // options for that specific topic, which contains a displaymodifier which will affect the
    // given canvas and paint objects.
    public static final LinkedHashMap<String, LinkedHashMap<String, DisplayModifier>> sMaps =
            new LinkedHashMap<String, LinkedHashMap<String,DisplayModifier>>() {
                {
                    put("aa", new LinkedHashMap<String, DisplayModifier>() {
                        {
                            put("true", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    paint.setAntiAlias(true);
                                }
                            });
                            put("false", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    paint.setAntiAlias(false);
                                }
                            });
                        }
                    });
                    put("style", new LinkedHashMap<String, DisplayModifier>() {
                        {
                            put("fill", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    paint.setStyle(Paint.Style.FILL);
                                }
                            });
                            put("stroke", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    paint.setStyle(Paint.Style.STROKE);
                                }
                            });
                            put("fillAndStroke", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    paint.setStyle(Paint.Style.FILL_AND_STROKE);
                                }
                            });
                        }
                    });
                    put("strokeWidth", new LinkedHashMap<String, DisplayModifier>() {
                        {
                            put("hair", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    paint.setStrokeWidth(0);
                                }
                            });
                            put("0.3", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    paint.setStrokeWidth(0.3f);
                                }
                            });
                            put("1", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    paint.setStrokeWidth(1);
                                }
                            });
                            put("5", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    paint.setStrokeWidth(5);
                                }
                            });
                            put("30", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    paint.setStrokeWidth(30);
                                }
                            });
                        }
                    });
                    put("strokeCap", new LinkedHashMap<String, DisplayModifier>() {
                        {
                            put("butt", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    paint.setStrokeCap(Paint.Cap.BUTT);
                                }
                            });
                            put("round", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    paint.setStrokeCap(Paint.Cap.ROUND);
                                }
                            });
                            put("square", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    paint.setStrokeCap(Paint.Cap.SQUARE);
                                }
                            });
                        }
                    });
                    put("strokeJoin", new LinkedHashMap<String, DisplayModifier>() {
                        {
                            put("bevel", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    paint.setStrokeJoin(Paint.Join.BEVEL);
                                }
                            });
                            put("round", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    paint.setStrokeJoin(Paint.Join.ROUND);
                                }
                            });
                            put("miter", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    paint.setStrokeJoin(Paint.Join.MITER);
                                }
                            });
                            // TODO: add miter0, miter1 etc to test miter distances
                        }
                    });

                    put("transform", new LinkedHashMap<String, DisplayModifier>() {
                        {
                            put("noTransform", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                }
                            });
                            put("rotate5", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    canvas.rotate(5);
                                }
                            });
                            put("rotate45", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    canvas.rotate(45);
                                }
                            });
                            put("rotate90", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    canvas.rotate(90);
                                    canvas.translate(0, -200);
                                }
                            });
                            put("scale2x2", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    canvas.scale(2, 2);
                                }
                            });
                            put("rot20scl1x4", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    canvas.rotate(20);
                                    canvas.scale(1, 4);
                                }
                            });
                        }
                    });

                    put("shader", new LinkedHashMap<String, DisplayModifier>() {
                        {
                            put("noShader", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                }
                            });
                            put("repeatShader", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    paint.setShader(ResourceModifier.instance().repeatShader);
                                }
                            });
                            put("translatedShader", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    paint.setShader(ResourceModifier.instance().translatedShader);
                                }
                            });
                            put("scaledShader", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    paint.setShader(ResourceModifier.instance().scaledShader);
                                }
                            });
                            put("horGradient", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    paint.setShader(ResourceModifier.instance().horGradient);
                                }
                            });
                            put("diagGradient", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    paint.setShader(ResourceModifier.instance().diagGradient);
                                }
                            });
                            put("vertGradient", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    paint.setShader(ResourceModifier.instance().vertGradient);
                                }
                            });
                            put("radGradient", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    paint.setShader(ResourceModifier.instance().radGradient);
                                }
                            });
                            put("sweepGradient", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    paint.setShader(ResourceModifier.instance().sweepGradient);
                                }
                            });
                            put("composeShader", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    paint.setShader(ResourceModifier.instance().composeShader);
                                }
                            });
                            put("bad composeShader", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    paint.setShader(ResourceModifier.instance().nestedComposeShader);
                                }
                            });
                            put("bad composeShader 2", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    paint.setShader(
                                            ResourceModifier.instance().doubleGradientComposeShader);
                                }
                            });
                        }
                    });

                    put("xfermodes", new LinkedHashMap<String, DisplayModifier>() {
                        {
                            put("SRC", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
                                }
                            });
                            put("DST", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST));
                                }
                            });
                            put("SRC_OVER", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
                                }
                            });
                            put("DST_OVER", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
                                }
                            });
                            put("SRC_IN", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                                }
                            });
                            put("DST_IN", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
                                }
                            });
                            put("SRC_OUT", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
                                }
                            });
                            put("DST_OUT", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
                                }
                            });
                            put("SRC_ATOP", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
                                }
                            });
                            put("DST_ATOP", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
                                }
                            });
                            put("XOR", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
                                }
                            });
                            put("MULTIPLY", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
                                }
                            });
                            put("SCREEN", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SCREEN));
                                }
                            });
                        }
                    });

                    // FINAL MAP: DOES ACTUAL DRAWING
                    put("drawing", new LinkedHashMap<String, DisplayModifier>() {
                        {
                            put("roundRect", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    canvas.drawRoundRect(gRect, 20, 20, paint);
                                }
                            });
                            put("rect", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    canvas.drawRect(gRect, paint);
                                }
                            });
                            put("circle", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    canvas.drawCircle(100, 100, 75, paint);
                                }
                            });
                            put("oval", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    canvas.drawOval(gRect, paint);
                                }
                            });
                            put("lines", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    canvas.drawLines(gLinePts, paint);
                                }
                            });
                            put("plusPoints", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    canvas.drawPoints(gPts, paint);
                                }
                            });
                            put("text", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    paint.setTextSize(36);
                                    canvas.drawText("TEXTTEST", 0, 50, paint);
                                }
                            });
                            put("shadowtext", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    paint.setTextSize(36);
                                    paint.setShadowLayer(3.0f, 0.0f, 3.0f, 0xffff00ff);
                                    canvas.drawText("TEXTTEST", 0, 50, paint);
                                }
                            });
                            put("bitmapMesh", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    canvas.drawBitmapMesh(ResourceModifier.instance().bitmap, 3, 3,
                                            ResourceModifier.instance().bitmapVertices, 0, null, 0,
                                            null);
                                }
                            });
                            put("arc", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    canvas.drawArc(gRect, 260, 285, false, paint);
                                }
                            });
                            put("arcFromCenter", new DisplayModifier() {
                                @Override
                                public void modifyDrawing(Paint paint, Canvas canvas) {
                                    canvas.drawArc(gRect, 260, 285, true, paint);
                                }
                            });
                        }
                    });
                    // WARNING: DON'T PUT MORE MAPS BELOW THIS
                }
            };

    abstract public void modifyDrawing(Paint paint, Canvas canvas);

    public static class Accessor {
        public final static int AA_MASK =           0x1 << 0;
        public final static int STYLE_MASK =        0x1 << 1;
        public final static int STROKE_WIDTH_MASK = 0x1 << 2;
        public final static int STROKE_CAP_MASK =   0x1 << 3;
        public final static int STROKE_JOIN_MASK =  0x1 << 4;
        public final static int TRANSFORM_MASK =    0x1 << 5;
        public final static int SHADER_MASK =       0x1 << 6;
        public final static int XFERMODE_MASK =     0x1 << 7;
        public final static int SHAPES_MASK =       0x1 << 8;
        public final static int ALL_OPTIONS_MASK = 0x1FF;
        public final static int SHAPES_INDEX = 8;
        public final static int XFERMODE_INDEX = 7;
        private final int mMask;

        private int[] mIndices;
        private LinkedHashMap<String, LinkedHashMap<String, DisplayModifier>> mDisplayMap;

        public Accessor(int mask) {
            int totalModifiers = Integer.bitCount(mask);
            mIndices = new int[totalModifiers];
            mMask = mask;
            // Create a Display Map of the valid indices
            mDisplayMap = new LinkedHashMap<String, LinkedHashMap<String, DisplayModifier>>();
            int index = 0;
            for (String key : DisplayModifier.sMaps.keySet()) {
                if (validIndex(index)) {
                    mDisplayMap.put(key, DisplayModifier.sMaps.get(key));
                }
                index++;
            }
        }

        private LinkedHashMap<String, DisplayModifier> getMapAtIndex(int index) {
            int i = 0;
            for (LinkedHashMap<String, DisplayModifier> map : mDisplayMap.values()) {
                if (i == index) {
                    return map;
                }
                i++;
            }
            return null;
        }

        /**
         * This will create the next combination of drawing commands. If we have done every combination,
         * then we will return false.
         * @return true if there is more combinations to do
         */
        public boolean step() {
            int modifierMapIndex = mIndices.length - 1;
            // Start from the last map, and loop until it is at the front
            while (modifierMapIndex >= 0) {
                LinkedHashMap<String, DisplayModifier> map = getMapAtIndex(modifierMapIndex);
                mIndices[modifierMapIndex]++;

                // If we are still at a valid index, then we don't need to update any others
                if (mIndices[modifierMapIndex] < map.size()) {
                    break;
                }

                // If we updated and it was outside the boundary, and it was the last index then
                // we are done
                if (modifierMapIndex == 0) {
                    return false;
                }
                // If we ran off the end of the map, we need to update one more down the list
                mIndices[modifierMapIndex] = 0;

                modifierMapIndex--;
            }
            return true;
        }

        /**
         * Modifies the canvas and paint given for the particular combination currently
         */
        public void modifyDrawing(Canvas canvas, Paint paint) {
            final ArrayList<DisplayModifier> modifierArrayList = getModifierList();
            for (DisplayModifier modifier : modifierArrayList) {
                modifier.modifyDrawing(paint, canvas);
            }
        }

        /**
         * Gets a list of all the current modifications to be used.
         */
        private ArrayList<DisplayModifier> getModifierList() {
            ArrayList<DisplayModifier> modifierArrayList = new ArrayList<DisplayModifier>();
            int mapIndex = 0;

            // Through each possible category of modification
            for (LinkedHashMap<String, DisplayModifier> map : mDisplayMap.values()) {
                int displayModifierIndex = mIndices[mapIndex];
                // Loop until we find the modification we are going to use
                for (Map.Entry<String, DisplayModifier> modifierEntry : map.entrySet()) {
                    // Once we find the modification we want, then we will add it to the list,
                    // and the last applied modifications
                    if (displayModifierIndex == 0) {
                        modifierArrayList.add(modifierEntry.getValue());
                        break;
                    }
                    displayModifierIndex--;
                }
                mapIndex++;
            }
            return modifierArrayList;
        }

        /**
         * Using the given masks, it tells if the map at the given index should be used, or not.
         */
        private boolean validIndex(int index) {
            return (mMask & (0x1 << index)) != 0;
        }
    }
}
