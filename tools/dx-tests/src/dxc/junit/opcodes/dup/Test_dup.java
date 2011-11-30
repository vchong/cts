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

package dxc.junit.opcodes.dup;

import dxc.junit.DxTestCase;
import dxc.junit.DxUtil;
import dxc.junit.opcodes.dup.jm.T_dup_1;
import dxc.junit.opcodes.dup.jm.T_dup_6;
import dxc.junit.opcodes.dup.jm.T_dup_7;


public class Test_dup extends DxTestCase {

    /**
     * @title  type of argument - int
     */
    public void testN1() {
        T_dup_1 t = new T_dup_1();
        assertTrue(t.run());
    }
    
    /**
     * @title  type of argument - float
     */
    public void testN2() {
        T_dup_6 t = new T_dup_6();
        assertTrue(t.run());
    }
    
    /**
     * @title  type of argument - reference
     */
    public void testN3() {
        T_dup_7 t = new T_dup_7();
        assertTrue(t.run());
    }
    
    /**
     * @constraint 4.8.2.1
     * @title number of arguments 
     */
    public void testVFE1() {
        try
        {
            Class.forName("dxc.junit.opcodes.dup.jm.T_dup_2");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }
    
    /**
     * @constraint 4.8.2.1
     * @title type of argument - double 
     */
    public void testVFE2() {
        try
        {
            Class.forName("dxc.junit.opcodes.dup.jm.T_dup_3");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }
    
    /**
     * @constraint 4.8.2.1
     * @title type of argument - long 
     */
    public void testVFE3() {
        try
        {
            Class.forName("dxc.junit.opcodes.dup.jm.T_dup_4");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint 4.8.2.5
     * @title stack size
     */
    public void testVFE4() {
        try
        {
            Class.forName("dxc.junit.opcodes.dup.jm.T_dup_5");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

}
