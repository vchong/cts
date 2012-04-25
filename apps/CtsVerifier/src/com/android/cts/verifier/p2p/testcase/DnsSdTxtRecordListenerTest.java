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

package com.android.cts.verifier.p2p.testcase;

import java.util.ArrayList;
import java.util.List;

import android.net.nsd.DnsSdTxtRecord;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager.DnsSdTxtRecordListener;
import android.util.Log;

/**
 * The utility class for
 * testing android.net.wifi.p2p.WifiP2pManager.DnsSdTxtRecordListener callback function.
 */
public class DnsSdTxtRecordListenerTest extends ListenerTest
    implements DnsSdTxtRecordListener {

    private static final String TAG = "DnsSdTxtRecordListenerTest";

    public static final List<ListenerArgument> NO_DNS_TXT
            = new ArrayList<ListenerArgument>();

    public static final List<ListenerArgument> ALL_DNS_TXT
            = new ArrayList<ListenerArgument>();

    public static final List<ListenerArgument> IPP_DNS_TXT
            = new ArrayList<ListenerArgument>();

    public static final List<ListenerArgument> AFP_DNS_TXT
            = new ArrayList<ListenerArgument>();

    static {
        initialize();
    }

    /**
     * The target device address.
     */
    private String mTargetAddr;

    public DnsSdTxtRecordListenerTest(String targetAddr) {
        mTargetAddr = targetAddr;
    }

    @Override
    public void onDnsSdTxtRecordAvailable(String fullDomainName,
            DnsSdTxtRecord record, WifiP2pDevice srcDevice) {
        Log.d(TAG, fullDomainName + " " + record + " received from "
                + srcDevice.deviceAddress);

        /*
         * Check only the response from the target device.
         * The response from other devices are ignored.
         */
        if (srcDevice.deviceAddress.equalsIgnoreCase(mTargetAddr)) {
            receiveCallback(new Argument(fullDomainName, record));
        }
    }

    private static void initialize() {
        String ippDomainName = "myprinter._ipp._tcp.local.";
        String afpDomainName = "example._afpovertcp._tcp.local.";

        DnsSdTxtRecord IppTxtRecord = new DnsSdTxtRecord();
        DnsSdTxtRecord afpTxtRecord = new DnsSdTxtRecord();
        IppTxtRecord.set("txtvers", "1");
        IppTxtRecord.set("pdl", "application/postscript");

        IPP_DNS_TXT.add(new Argument(ippDomainName, IppTxtRecord));
        AFP_DNS_TXT.add(new Argument(afpDomainName, afpTxtRecord));
        ALL_DNS_TXT.add(new Argument(ippDomainName, IppTxtRecord));
        ALL_DNS_TXT.add(new Argument(afpDomainName, afpTxtRecord));
    }

    /**
     * The container of the argument of {@link #onDnsSdTxtRecordAvailable}.
     */
    static class Argument extends ListenerArgument {

        private String mFullDomainName;
        private DnsSdTxtRecord mRecord;

        /**
         * Set the argument of {@link #onDnsSdTxtRecordAvailable}.
         * @param fullDomainName full domain name.
         * @param record txt record.
         */
        Argument(String fullDomainName, DnsSdTxtRecord record) {
            mFullDomainName = fullDomainName;
            mRecord = record;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof Argument)) {
                return false;
            }
            Argument arg = (Argument)obj;
            return equals(mFullDomainName, arg.mFullDomainName) &&
                    equals(mRecord, arg.mRecord);
        }

        private boolean equals(String s1, String s2) {
            if (s1 == null && s2 == null) {
                return true;
            }
            if (s1 == null || s2 == null) {
                return false;
            }
            return s1.equals(s2);
        }

        private boolean equals(DnsSdTxtRecord s1, DnsSdTxtRecord s2) {
            if (s1 == null && s2 == null) {
                return true;
            }
            if (s1 == null || s2 == null) {
                return false;
            }
            return s1.equals(s2);
        }

        @Override
        public String toString() {
            return "domainName=" + mFullDomainName + " record='" + mRecord + "'";
        }
    }
}
