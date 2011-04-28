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

package dot.junit.opcodes.or_long_2addr;

import dot.junit.DxTestCase;
import dot.junit.DxUtil;
import dot.junit.opcodes.or_long_2addr.d.T_or_long_2addr_1;
import dot.junit.opcodes.or_long_2addr.d.T_or_long_2addr_3;

public class Test_or_long_2addr extends DxTestCase {

    /**
     * @title Arguments = 123456789121l, 2l
     */
    public void testN1() {
        T_or_long_2addr_1 t = new T_or_long_2addr_1();
        assertEquals(123456789123l, t.run(123456789121l, 2l));
    }

    /**
     * @title Arguments = 0xffffffffffffff8l, 0xffffffffffffff1l
     */
    public void testN2() {
        T_or_long_2addr_1 t = new T_or_long_2addr_1();
        assertEquals(0xffffffffffffff9l, t.run(0xffffffffffffff8l,
                0xffffffffffffff1l));
    }

    /**
     * @title Arguments = 0xabcdefabcdef & -1
     */
    public void testN3() {
        T_or_long_2addr_1 t = new T_or_long_2addr_1();
        assertEquals(-1l, t.run(0xabcdefabcdefl, -1l));
    }
    
    /**
     * @title Types of arguments - double, long. Dalvik doens't distinguish 64-bits types internally,
     * so this operation of double and long makes no sense but shall not crash the VM.  
     */
    public void testN4() {
        T_or_long_2addr_3 t = new T_or_long_2addr_3();
        try {
            t.run(500000l, 1.05d);
        } catch (Throwable e) {
        }
    }

    /**
     * @title Arguments = 0 & -1
     */
    public void testB1() {
        T_or_long_2addr_1 t = new T_or_long_2addr_1();
        assertEquals(-1l, t.run(0l, -1l));
    }

    /**
     * @title Arguments = Long.MAX_VALUE & Long.MIN_VALUE
     */
    public void testB2() {
        T_or_long_2addr_1 t = new T_or_long_2addr_1();
        assertEquals(-1l, t.run(Long.MAX_VALUE, Long.MIN_VALUE));
    }

    /**
     * @constraint A24 
     * @title number of registers
     */
    public void testVFE1() {
        try {
            Class.forName("dot.junit.opcodes.or_long_2addr.d.T_or_long_2addr_2");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    

    /**
     * @constraint B1 
     * @title types of arguments - int & long
     */
    public void testVFE2() {
        try {
            Class.forName("dot.junit.opcodes.or_long_2addr.d.T_or_long_2addr_4");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint B1 
     * @title types of arguments - float & long
     */
    public void testVFE3() {
        try {
            Class.forName("dot.junit.opcodes.or_long_2addr.d.T_or_long_2addr_5");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint B1 
     * @title types of arguments - reference & long
     */
    public void testVFE4() {
        try {
            Class.forName("dot.junit.opcodes.or_long_2addr.d.T_or_long_2addr_6");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }
}
