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
import android.renderscript.Element;
import android.renderscript.Type;
import android.renderscript.Sampler;
import android.renderscript.Script;
import android.renderscript.RSRuntimeException;
import com.android.cts.stub.R;

public class ClearObjectTest extends RSBaseCompute {
    int ObjectNum = 1;
    static final int TEST_ID_ELEMENT = 0;
    static final int TEST_ID_TYPE = 1;
    static final int TEST_ID_ALLOCATION = 2;
    static final int TEST_ID_SAMPLER = 3;
    static final int TEST_ID_SCRIPT = 4;
    private ScriptC_clear_object_element ms_element;
    private ScriptC_clear_object_type ms_type;
    private ScriptC_clear_object_allocation ms_allcation;
    private ScriptC_clear_object_sampler ms_sampler;
    private ScriptC_clear_object_script ms_script;

    @Override
    public void forEach(int testId, Allocation mIn, Allocation mOut) throws RSRuntimeException {
        switch (testId) {
        case TEST_ID_ELEMENT:
            ms_element.forEach_root(mOut);
            break;
        case TEST_ID_TYPE:
            ms_type.forEach_root(mOut);
            break;
        case TEST_ID_ALLOCATION:
            ms_allcation.forEach_root(mOut);
            break;
        case TEST_ID_SAMPLER:
            ms_sampler.forEach_root(mOut);
            break;
        case TEST_ID_SCRIPT:
            ms_script.forEach_root(mOut);
            break;
        }
    }

    public void testClearObjectElement() {
        ms_element = new ScriptC_clear_object_element(mRS, mRes, R.raw.clear_object_element);
        Element element = Element.BOOLEAN(mRS);
        Allocation mIn = Allocation.createSized(mRS, Element.I32(mRS), ObjectNum);
        Allocation mOut = Allocation.createSized(mRS, Element.I32(mRS), ObjectNum);
        ms_element.set_element(element);
        try {
            RSUtils.forEach(this, TEST_ID_ELEMENT, mIn, mOut);
        } catch (RSRuntimeException e) {
        }
        int[] tmpArray = new int[ObjectNum];
        mOut.copyTo(tmpArray);

        for(int i = 0; i < ObjectNum; i++)
            assertEquals(tmpArray[i], 1);
    }

    public void testclearObjectType() {
        ms_type = new ScriptC_clear_object_type(mRS, mRes, R.raw.clear_object_type);
        Type type= new Type.Builder(mRS, Element.I8(mRS)).setX(1).create();
        Allocation mOut = Allocation.createSized(mRS, Element.I32(mRS), ObjectNum);
        Allocation mIn = Allocation.createSized(mRS, Element.I32(mRS), ObjectNum);
        ms_type.set_type(type);

        try {
            RSUtils.forEach(this, TEST_ID_TYPE, mIn, mOut);
        } catch (RSRuntimeException e) {
        }
        int[] tmpArray = new int[ObjectNum];
        mOut.copyTo(tmpArray);

        for(int i = 0; i < ObjectNum; i++)
            assertEquals(tmpArray[i], 1);
    }

    public void testclearObjectAllocation() {
        ms_allcation = new ScriptC_clear_object_allocation(mRS, mRes,
                R.raw.clear_object_allocation);
        Allocation mOut = Allocation.createSized(mRS, Element.I32(mRS), ObjectNum);
        Allocation mIn = Allocation.createSized(mRS, Element.I32(mRS), ObjectNum);
        Allocation allocation = Allocation.createTyped(mRS, mIn.getType());
        ms_allcation.set_allocation(allocation);
        try {
            RSUtils.forEach(this, TEST_ID_ALLOCATION, mIn, mOut);
        } catch (RSRuntimeException e) {
        }
        int[] tmpArray = new int[ObjectNum];
        mOut.copyTo(tmpArray);

        for(int i = 0; i < ObjectNum; i++)
            assertEquals(tmpArray[i], 1);
    }

    public void testclearObjectSampler() {
        ms_sampler = new ScriptC_clear_object_sampler(mRS, mRes, R.raw.clear_object_sampler);
        Sampler sampler = new Sampler.Builder(mRS).create();
        Allocation mOut = Allocation.createSized(mRS, Element.I32(mRS), ObjectNum);
        Allocation mIn = Allocation.createSized(mRS, Element.I32(mRS), ObjectNum);
        ms_sampler.set_sampler(sampler);
        try {
            RSUtils.forEach(this, TEST_ID_SAMPLER, mIn, mOut);
        } catch (RSRuntimeException e) {
        }
        int[] tmpArray = new int[ObjectNum];
        mOut.copyTo(tmpArray);

        for(int i = 0; i < ObjectNum; i++)
            assertEquals(tmpArray[i], 1);
    }

    public void testclearObjectScript() {
        ms_script = new ScriptC_clear_object_script(mRS, mRes, R.raw.clear_object_script);
        Script script = new ScriptC_clear_object_type(mRS, mRes, R.raw.clear_object_element);
        Allocation mIn = Allocation.createSized(mRS, Element.I32(mRS), ObjectNum);
        Allocation mOut = Allocation.createSized(mRS, Element.I32(mRS), ObjectNum);
        ms_script.set_script(script);
        try {
            RSUtils.forEach(this, TEST_ID_SCRIPT, mIn, mOut);
        } catch (RSRuntimeException e) {
        }
        int[] tmpArray = new int[ObjectNum];
        mOut.copyTo(tmpArray);

        for(int i = 0; i < ObjectNum; i++)
            assertEquals(tmpArray[i], 1);
    }
}
