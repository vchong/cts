/*
 * Copyright (C) 2008 The Android Open Source Project
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

package android.graphics.drawable.cts;

import junit.framework.Assert;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Xml;

import java.io.IOException;

/**
 * The useful methods for graphics.drawable test.
 */
public class DrawableTestUtils {

    public static void skipCurrentTag(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        int outerDepth = parser.getDepth();
        int type;
        while ((type=parser.next()) != XmlPullParser.END_DOCUMENT
               && (type != XmlPullParser.END_TAG
                       || parser.getDepth() > outerDepth)) {
        }
    }

    /**
     * Retrieve an AttributeSet from a XML.
     *
     * @param parser the XmlPullParser to use for the xml parsing.
     * @param searchedNodeName the name of the target node.
     * @return the AttributeSet retrieved from specified node.
     * @throws IOException
     * @throws XmlPullParserException
     */
    public static AttributeSet getAttributeSet(XmlResourceParser parser, String searchedNodeName)
            throws XmlPullParserException, IOException {
        AttributeSet attrs = null;
        int type;
        while ((type = parser.next()) != XmlPullParser.END_DOCUMENT
                && type != XmlPullParser.START_TAG) {
        }
        String nodeName = parser.getName();
        if (!"alias".equals(nodeName)) {
            throw new RuntimeException();
        }
        int outerDepth = parser.getDepth();
        while ((type = parser.next()) != XmlPullParser.END_DOCUMENT
                && (type != XmlPullParser.END_TAG || parser.getDepth() > outerDepth)) {
            if (type == XmlPullParser.END_TAG || type == XmlPullParser.TEXT) {
                continue;
            }
            nodeName = parser.getName();
            if (searchedNodeName.equals(nodeName)) {
                outerDepth = parser.getDepth();
                while ((type = parser.next()) != XmlPullParser.END_DOCUMENT
                        && (type != XmlPullParser.END_TAG || parser.getDepth() > outerDepth)) {
                    if (type == XmlPullParser.END_TAG || type == XmlPullParser.TEXT) {
                        continue;
                    }
                    nodeName = parser.getName();
                    attrs = Xml.asAttributeSet(parser);
                    break;
                }
                break;
            } else {
                skipCurrentTag(parser);
            }
        }
        return attrs;
    }

    public static XmlResourceParser getResourceParser(Resources res, int resId)
            throws XmlPullParserException, IOException {
        final XmlResourceParser parser = res.getXml(resId);
        int type;
        while ((type = parser.next()) != XmlPullParser.START_TAG
                && type != XmlPullParser.END_DOCUMENT) {
            // Empty loop
        }
        return parser;
    }

    public static void setResourcesDensity(Resources res, int densityDpi) {
        final Configuration config = new Configuration();
        config.setTo(res.getConfiguration());
        config.densityDpi = densityDpi;
        res.updateConfiguration(config, null);
    }

    /**
     * Implements scaling as used by the Bitmap class. Resulting values are
     * rounded up (as distinct from resource scaling, which truncates or rounds
     * to the nearest pixel).
     *
     * @param size the pixel size to scale
     * @param sdensity the source density that corresponds to the size
     * @param tdensity the target density
     * @return the pixel size scaled for the target density
     */
    public static int scaleBitmapFromDensity(int size, int sdensity, int tdensity) {
        if (sdensity == 0 || tdensity == 0 || sdensity == tdensity) {
            return size;
        }

        // Scale by tdensity / sdensity, rounding up.
        return ((size * tdensity) + (sdensity >> 1)) / sdensity;
    }

    /**
     * Asserts that two images are similar within the given thresholds.
     *
     * @param message
     * @param expected
     * @param actual
     * @param pixelThreshold
     * @param pixelCountThreshold
     */
    public static void compareImages(String message, Bitmap expected, Bitmap actual,
            float pixelThreshold, float pixelCountThreshold) {
        int idealWidth = expected.getWidth();
        int idealHeight = expected.getHeight();

        Assert.assertTrue(idealWidth == actual.getWidth());
        Assert.assertTrue(idealHeight == actual.getHeight());

        int totalDiffPixelCount = 0;
        float totalPixelCount = idealWidth * idealHeight;
        for (int x = 0; x < idealWidth; x++) {
            for (int y = 0; y < idealHeight; y++) {
                int idealColor = expected.getPixel(x, y);
                int givenColor = actual.getPixel(x, y);
                if (idealColor == givenColor)
                    continue;

                float totalError = 0;
                totalError += Math.abs(Color.red(idealColor) - Color.red(givenColor));
                totalError += Math.abs(Color.green(idealColor) - Color.green(givenColor));
                totalError += Math.abs(Color.blue(idealColor) - Color.blue(givenColor));
                totalError += Math.abs(Color.alpha(idealColor) - Color.alpha(givenColor));

                if ((totalError / 1024.0f) >= pixelThreshold) {
                    Assert.fail((message + ": totalError is " + totalError));
                }

                totalDiffPixelCount++;
            }
        }
        if ((totalDiffPixelCount / totalPixelCount) >= pixelCountThreshold) {
            Assert.fail((message +": totalDiffPixelCount is " + totalDiffPixelCount));
        }
    }
}
