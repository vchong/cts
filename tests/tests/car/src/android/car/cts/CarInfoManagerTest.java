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
package android.car.cts;

import android.car.Car;
import android.car.CarInfoManager;
import android.os.Bundle;

public class CarInfoManagerTest extends CarApiTestBase {

    private CarInfoManager mCarInfoManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mCarInfoManager = (CarInfoManager) getCar().getCarManager(Car.INFO_SERVICE);
    }

    public void testManufacturer() throws Exception {
        final String NO_SUCH_NAME = "no-such-information-available";
        Bundle info = mCarInfoManager.getBasicInfo();
        info.getString(CarInfoManager.BASIC_INFO_KEY_MANUFACTURER);
        info.getString(CarInfoManager.BASIC_INFO_KEY_MODEL);
        info.getString(CarInfoManager.BASIC_INFO_KEY_MODEL_YEAR);
        assertNotNull(info.getString(CarInfoManager.BASIC_INFO_KEY_VEHICLE_ID));
    }

    public void testNoSuchInfo() throws Exception {
        final String NO_SUCH_NAME = "no-such-information-available";
        Bundle info = mCarInfoManager.getBasicInfo();
        assertNotNull(info);
        assertNull(info.getCharSequence(NO_SUCH_NAME));
    }
}
