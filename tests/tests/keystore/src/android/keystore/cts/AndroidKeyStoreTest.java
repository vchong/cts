/*
 * Copyright 2013 The Android Open Source Project
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

package android.keystore.cts;

import android.security.KeyPairGeneratorSpec;
import android.security.KeyStoreParameter;
import android.test.AndroidTestCase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStore.Entry;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStore.TrustedCertificateEntry;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.x500.X500Principal;

public class AndroidKeyStoreTest extends AndroidTestCase {
    private KeyStore mKeyStore;

    private static final String TEST_ALIAS_1 = "test1";

    private static final String TEST_ALIAS_2 = "test2";

    private static final String TEST_ALIAS_3 = "test3";

    /*
     * The keys and certificates below are generated with:
     *
     * openssl req -new -x509 -days 3650 -extensions v3_ca -keyout cakey.pem -out cacert.pem
     * openssl req -newkey rsa:1024 -keyout userkey.pem -nodes -days 3650 -out userkey.req
     * mkdir -p demoCA/newcerts
     * touch demoCA/index.txt
     * echo "01" > demoCA/serial
     * openssl ca -out usercert.pem -in userkey.req -cert cacert.pem -keyfile cakey.pem -days 3650
     */

    /**
     * Generated from above and converted with:
     *
     * openssl x509 -outform d -in cacert.pem | xxd -i | sed 's/0x/(byte) 0x/g'
     */
    private static final byte[] FAKE_RSA_CA_1 = {
            (byte) 0x30, (byte) 0x82, (byte) 0x02, (byte) 0xce, (byte) 0x30, (byte) 0x82,
            (byte) 0x02, (byte) 0x37, (byte) 0xa0, (byte) 0x03, (byte) 0x02, (byte) 0x01,
            (byte) 0x02, (byte) 0x02, (byte) 0x09, (byte) 0x00, (byte) 0xe1, (byte) 0x6a,
            (byte) 0xa2, (byte) 0xf4, (byte) 0x2e, (byte) 0x55, (byte) 0x48, (byte) 0x0a,
            (byte) 0x30, (byte) 0x0d, (byte) 0x06, (byte) 0x09, (byte) 0x2a, (byte) 0x86,
            (byte) 0x48, (byte) 0x86, (byte) 0xf7, (byte) 0x0d, (byte) 0x01, (byte) 0x01,
            (byte) 0x05, (byte) 0x05, (byte) 0x00, (byte) 0x30, (byte) 0x4f, (byte) 0x31,
            (byte) 0x0b, (byte) 0x30, (byte) 0x09, (byte) 0x06, (byte) 0x03, (byte) 0x55,
            (byte) 0x04, (byte) 0x06, (byte) 0x13, (byte) 0x02, (byte) 0x55, (byte) 0x53,
            (byte) 0x31, (byte) 0x0b, (byte) 0x30, (byte) 0x09, (byte) 0x06, (byte) 0x03,
            (byte) 0x55, (byte) 0x04, (byte) 0x08, (byte) 0x13, (byte) 0x02, (byte) 0x43,
            (byte) 0x41, (byte) 0x31, (byte) 0x16, (byte) 0x30, (byte) 0x14, (byte) 0x06,
            (byte) 0x03, (byte) 0x55, (byte) 0x04, (byte) 0x07, (byte) 0x13, (byte) 0x0d,
            (byte) 0x4d, (byte) 0x6f, (byte) 0x75, (byte) 0x6e, (byte) 0x74, (byte) 0x61,
            (byte) 0x69, (byte) 0x6e, (byte) 0x20, (byte) 0x56, (byte) 0x69, (byte) 0x65,
            (byte) 0x77, (byte) 0x31, (byte) 0x1b, (byte) 0x30, (byte) 0x19, (byte) 0x06,
            (byte) 0x03, (byte) 0x55, (byte) 0x04, (byte) 0x0a, (byte) 0x13, (byte) 0x12,
            (byte) 0x41, (byte) 0x6e, (byte) 0x64, (byte) 0x72, (byte) 0x6f, (byte) 0x69,
            (byte) 0x64, (byte) 0x20, (byte) 0x54, (byte) 0x65, (byte) 0x73, (byte) 0x74,
            (byte) 0x20, (byte) 0x43, (byte) 0x61, (byte) 0x73, (byte) 0x65, (byte) 0x73,
            (byte) 0x30, (byte) 0x1e, (byte) 0x17, (byte) 0x0d, (byte) 0x31, (byte) 0x32,
            (byte) 0x30, (byte) 0x38, (byte) 0x31, (byte) 0x34, (byte) 0x31, (byte) 0x36,
            (byte) 0x35, (byte) 0x35, (byte) 0x34, (byte) 0x34, (byte) 0x5a, (byte) 0x17,
            (byte) 0x0d, (byte) 0x32, (byte) 0x32, (byte) 0x30, (byte) 0x38, (byte) 0x31,
            (byte) 0x32, (byte) 0x31, (byte) 0x36, (byte) 0x35, (byte) 0x35, (byte) 0x34,
            (byte) 0x34, (byte) 0x5a, (byte) 0x30, (byte) 0x4f, (byte) 0x31, (byte) 0x0b,
            (byte) 0x30, (byte) 0x09, (byte) 0x06, (byte) 0x03, (byte) 0x55, (byte) 0x04,
            (byte) 0x06, (byte) 0x13, (byte) 0x02, (byte) 0x55, (byte) 0x53, (byte) 0x31,
            (byte) 0x0b, (byte) 0x30, (byte) 0x09, (byte) 0x06, (byte) 0x03, (byte) 0x55,
            (byte) 0x04, (byte) 0x08, (byte) 0x13, (byte) 0x02, (byte) 0x43, (byte) 0x41,
            (byte) 0x31, (byte) 0x16, (byte) 0x30, (byte) 0x14, (byte) 0x06, (byte) 0x03,
            (byte) 0x55, (byte) 0x04, (byte) 0x07, (byte) 0x13, (byte) 0x0d, (byte) 0x4d,
            (byte) 0x6f, (byte) 0x75, (byte) 0x6e, (byte) 0x74, (byte) 0x61, (byte) 0x69,
            (byte) 0x6e, (byte) 0x20, (byte) 0x56, (byte) 0x69, (byte) 0x65, (byte) 0x77,
            (byte) 0x31, (byte) 0x1b, (byte) 0x30, (byte) 0x19, (byte) 0x06, (byte) 0x03,
            (byte) 0x55, (byte) 0x04, (byte) 0x0a, (byte) 0x13, (byte) 0x12, (byte) 0x41,
            (byte) 0x6e, (byte) 0x64, (byte) 0x72, (byte) 0x6f, (byte) 0x69, (byte) 0x64,
            (byte) 0x20, (byte) 0x54, (byte) 0x65, (byte) 0x73, (byte) 0x74, (byte) 0x20,
            (byte) 0x43, (byte) 0x61, (byte) 0x73, (byte) 0x65, (byte) 0x73, (byte) 0x30,
            (byte) 0x81, (byte) 0x9f, (byte) 0x30, (byte) 0x0d, (byte) 0x06, (byte) 0x09,
            (byte) 0x2a, (byte) 0x86, (byte) 0x48, (byte) 0x86, (byte) 0xf7, (byte) 0x0d,
            (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x05, (byte) 0x00, (byte) 0x03,
            (byte) 0x81, (byte) 0x8d, (byte) 0x00, (byte) 0x30, (byte) 0x81, (byte) 0x89,
            (byte) 0x02, (byte) 0x81, (byte) 0x81, (byte) 0x00, (byte) 0xa3, (byte) 0x72,
            (byte) 0xab, (byte) 0xd0, (byte) 0xe4, (byte) 0xad, (byte) 0x2f, (byte) 0xe7,
            (byte) 0xe2, (byte) 0x79, (byte) 0x07, (byte) 0x36, (byte) 0x3d, (byte) 0x0c,
            (byte) 0x8d, (byte) 0x42, (byte) 0x9a, (byte) 0x0a, (byte) 0x33, (byte) 0x64,
            (byte) 0xb3, (byte) 0xcd, (byte) 0xb2, (byte) 0xd7, (byte) 0x3a, (byte) 0x42,
            (byte) 0x06, (byte) 0x77, (byte) 0x45, (byte) 0x29, (byte) 0xe9, (byte) 0xcb,
            (byte) 0xb7, (byte) 0x4a, (byte) 0xd6, (byte) 0xee, (byte) 0xad, (byte) 0x01,
            (byte) 0x91, (byte) 0x9b, (byte) 0x0c, (byte) 0x59, (byte) 0xa1, (byte) 0x03,
            (byte) 0xfa, (byte) 0xf0, (byte) 0x5a, (byte) 0x7c, (byte) 0x4f, (byte) 0xf7,
            (byte) 0x8d, (byte) 0x36, (byte) 0x0f, (byte) 0x1f, (byte) 0x45, (byte) 0x7d,
            (byte) 0x1b, (byte) 0x31, (byte) 0xa1, (byte) 0x35, (byte) 0x0b, (byte) 0x00,
            (byte) 0xed, (byte) 0x7a, (byte) 0xb6, (byte) 0xc8, (byte) 0x4e, (byte) 0xa9,
            (byte) 0x86, (byte) 0x4c, (byte) 0x7b, (byte) 0x99, (byte) 0x57, (byte) 0x41,
            (byte) 0x12, (byte) 0xef, (byte) 0x6b, (byte) 0xbc, (byte) 0x3d, (byte) 0x60,
            (byte) 0xf2, (byte) 0x99, (byte) 0x1a, (byte) 0xcd, (byte) 0xed, (byte) 0x56,
            (byte) 0xa4, (byte) 0xe5, (byte) 0x36, (byte) 0x9f, (byte) 0x24, (byte) 0x1f,
            (byte) 0xdc, (byte) 0x89, (byte) 0x40, (byte) 0xc8, (byte) 0x99, (byte) 0x92,
            (byte) 0xab, (byte) 0x4a, (byte) 0xb5, (byte) 0x61, (byte) 0x45, (byte) 0x62,
            (byte) 0xff, (byte) 0xa3, (byte) 0x45, (byte) 0x65, (byte) 0xaf, (byte) 0xf6,
            (byte) 0x27, (byte) 0x30, (byte) 0x51, (byte) 0x0e, (byte) 0x0e, (byte) 0xeb,
            (byte) 0x79, (byte) 0x0c, (byte) 0xbe, (byte) 0xb3, (byte) 0x0a, (byte) 0x6f,
            (byte) 0x29, (byte) 0x06, (byte) 0xdc, (byte) 0x2f, (byte) 0x6b, (byte) 0x51,
            (byte) 0x02, (byte) 0x03, (byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0xa3,
            (byte) 0x81, (byte) 0xb1, (byte) 0x30, (byte) 0x81, (byte) 0xae, (byte) 0x30,
            (byte) 0x1d, (byte) 0x06, (byte) 0x03, (byte) 0x55, (byte) 0x1d, (byte) 0x0e,
            (byte) 0x04, (byte) 0x16, (byte) 0x04, (byte) 0x14, (byte) 0x33, (byte) 0x05,
            (byte) 0xee, (byte) 0xfe, (byte) 0x6f, (byte) 0x60, (byte) 0xc7, (byte) 0xf9,
            (byte) 0xa9, (byte) 0xd2, (byte) 0x73, (byte) 0x5c, (byte) 0x8f, (byte) 0x6d,
            (byte) 0xa2, (byte) 0x2f, (byte) 0x97, (byte) 0x8e, (byte) 0x5d, (byte) 0x51,
            (byte) 0x30, (byte) 0x7f, (byte) 0x06, (byte) 0x03, (byte) 0x55, (byte) 0x1d,
            (byte) 0x23, (byte) 0x04, (byte) 0x78, (byte) 0x30, (byte) 0x76, (byte) 0x80,
            (byte) 0x14, (byte) 0x33, (byte) 0x05, (byte) 0xee, (byte) 0xfe, (byte) 0x6f,
            (byte) 0x60, (byte) 0xc7, (byte) 0xf9, (byte) 0xa9, (byte) 0xd2, (byte) 0x73,
            (byte) 0x5c, (byte) 0x8f, (byte) 0x6d, (byte) 0xa2, (byte) 0x2f, (byte) 0x97,
            (byte) 0x8e, (byte) 0x5d, (byte) 0x51, (byte) 0xa1, (byte) 0x53, (byte) 0xa4,
            (byte) 0x51, (byte) 0x30, (byte) 0x4f, (byte) 0x31, (byte) 0x0b, (byte) 0x30,
            (byte) 0x09, (byte) 0x06, (byte) 0x03, (byte) 0x55, (byte) 0x04, (byte) 0x06,
            (byte) 0x13, (byte) 0x02, (byte) 0x55, (byte) 0x53, (byte) 0x31, (byte) 0x0b,
            (byte) 0x30, (byte) 0x09, (byte) 0x06, (byte) 0x03, (byte) 0x55, (byte) 0x04,
            (byte) 0x08, (byte) 0x13, (byte) 0x02, (byte) 0x43, (byte) 0x41, (byte) 0x31,
            (byte) 0x16, (byte) 0x30, (byte) 0x14, (byte) 0x06, (byte) 0x03, (byte) 0x55,
            (byte) 0x04, (byte) 0x07, (byte) 0x13, (byte) 0x0d, (byte) 0x4d, (byte) 0x6f,
            (byte) 0x75, (byte) 0x6e, (byte) 0x74, (byte) 0x61, (byte) 0x69, (byte) 0x6e,
            (byte) 0x20, (byte) 0x56, (byte) 0x69, (byte) 0x65, (byte) 0x77, (byte) 0x31,
            (byte) 0x1b, (byte) 0x30, (byte) 0x19, (byte) 0x06, (byte) 0x03, (byte) 0x55,
            (byte) 0x04, (byte) 0x0a, (byte) 0x13, (byte) 0x12, (byte) 0x41, (byte) 0x6e,
            (byte) 0x64, (byte) 0x72, (byte) 0x6f, (byte) 0x69, (byte) 0x64, (byte) 0x20,
            (byte) 0x54, (byte) 0x65, (byte) 0x73, (byte) 0x74, (byte) 0x20, (byte) 0x43,
            (byte) 0x61, (byte) 0x73, (byte) 0x65, (byte) 0x73, (byte) 0x82, (byte) 0x09,
            (byte) 0x00, (byte) 0xe1, (byte) 0x6a, (byte) 0xa2, (byte) 0xf4, (byte) 0x2e,
            (byte) 0x55, (byte) 0x48, (byte) 0x0a, (byte) 0x30, (byte) 0x0c, (byte) 0x06,
            (byte) 0x03, (byte) 0x55, (byte) 0x1d, (byte) 0x13, (byte) 0x04, (byte) 0x05,
            (byte) 0x30, (byte) 0x03, (byte) 0x01, (byte) 0x01, (byte) 0xff, (byte) 0x30,
            (byte) 0x0d, (byte) 0x06, (byte) 0x09, (byte) 0x2a, (byte) 0x86, (byte) 0x48,
            (byte) 0x86, (byte) 0xf7, (byte) 0x0d, (byte) 0x01, (byte) 0x01, (byte) 0x05,
            (byte) 0x05, (byte) 0x00, (byte) 0x03, (byte) 0x81, (byte) 0x81, (byte) 0x00,
            (byte) 0x8c, (byte) 0x30, (byte) 0x42, (byte) 0xfa, (byte) 0xeb, (byte) 0x1a,
            (byte) 0x26, (byte) 0xeb, (byte) 0xda, (byte) 0x56, (byte) 0x32, (byte) 0xf2,
            (byte) 0x9d, (byte) 0xa5, (byte) 0x24, (byte) 0xd8, (byte) 0x3a, (byte) 0xda,
            (byte) 0x30, (byte) 0xa6, (byte) 0x8b, (byte) 0x46, (byte) 0xfe, (byte) 0xfe,
            (byte) 0xdb, (byte) 0xf1, (byte) 0xe6, (byte) 0xe1, (byte) 0x7c, (byte) 0x1b,
            (byte) 0xe7, (byte) 0x77, (byte) 0x00, (byte) 0xa1, (byte) 0x1c, (byte) 0x19,
            (byte) 0x17, (byte) 0x73, (byte) 0xb0, (byte) 0xf0, (byte) 0x9d, (byte) 0xf3,
            (byte) 0x4f, (byte) 0xb6, (byte) 0xbc, (byte) 0xc7, (byte) 0x47, (byte) 0x85,
            (byte) 0x2a, (byte) 0x4a, (byte) 0xa1, (byte) 0xa5, (byte) 0x58, (byte) 0xf5,
            (byte) 0xc5, (byte) 0x1a, (byte) 0x51, (byte) 0xb1, (byte) 0x04, (byte) 0x80,
            (byte) 0xee, (byte) 0x3a, (byte) 0xec, (byte) 0x2f, (byte) 0xe1, (byte) 0xfd,
            (byte) 0x58, (byte) 0xeb, (byte) 0xed, (byte) 0x82, (byte) 0x9e, (byte) 0x38,
            (byte) 0xa3, (byte) 0x24, (byte) 0x75, (byte) 0xf7, (byte) 0x3e, (byte) 0xc2,
            (byte) 0xc5, (byte) 0x27, (byte) 0xeb, (byte) 0x6f, (byte) 0x7b, (byte) 0x50,
            (byte) 0xda, (byte) 0x43, (byte) 0xdc, (byte) 0x3b, (byte) 0x0b, (byte) 0x6f,
            (byte) 0x78, (byte) 0x8f, (byte) 0xb0, (byte) 0x66, (byte) 0xe1, (byte) 0x12,
            (byte) 0x87, (byte) 0x5f, (byte) 0x97, (byte) 0x7b, (byte) 0xca, (byte) 0x14,
            (byte) 0x79, (byte) 0xf7, (byte) 0xe8, (byte) 0x6c, (byte) 0x72, (byte) 0xdb,
            (byte) 0x91, (byte) 0x65, (byte) 0x17, (byte) 0x54, (byte) 0xe0, (byte) 0x74,
            (byte) 0x1d, (byte) 0xac, (byte) 0x47, (byte) 0x04, (byte) 0x12, (byte) 0xe0,
            (byte) 0xc3, (byte) 0x66, (byte) 0x19, (byte) 0x05, (byte) 0x2e, (byte) 0x7e,
            (byte) 0xf1, (byte) 0x61
    };

    /**
     * Generated from above and converted with:
     *
     * openssl pkcs8 -topk8 -outform d -in userkey.pem -nocrypt | xxd -i | sed 's/0x/(byte) 0x/g'
     */
    private static final byte[] FAKE_RSA_KEY_1 = new byte[] {
            (byte) 0x30, (byte) 0x82, (byte) 0x02, (byte) 0x78, (byte) 0x02, (byte) 0x01,
            (byte) 0x00, (byte) 0x30, (byte) 0x0d, (byte) 0x06, (byte) 0x09, (byte) 0x2a,
            (byte) 0x86, (byte) 0x48, (byte) 0x86, (byte) 0xf7, (byte) 0x0d, (byte) 0x01,
            (byte) 0x01, (byte) 0x01, (byte) 0x05, (byte) 0x00, (byte) 0x04, (byte) 0x82,
            (byte) 0x02, (byte) 0x62, (byte) 0x30, (byte) 0x82, (byte) 0x02, (byte) 0x5e,
            (byte) 0x02, (byte) 0x01, (byte) 0x00, (byte) 0x02, (byte) 0x81, (byte) 0x81,
            (byte) 0x00, (byte) 0xce, (byte) 0x29, (byte) 0xeb, (byte) 0xf6, (byte) 0x5b,
            (byte) 0x25, (byte) 0xdc, (byte) 0xa1, (byte) 0xa6, (byte) 0x2c, (byte) 0x66,
            (byte) 0xcb, (byte) 0x20, (byte) 0x90, (byte) 0x27, (byte) 0x86, (byte) 0x8a,
            (byte) 0x44, (byte) 0x71, (byte) 0x50, (byte) 0xda, (byte) 0xd3, (byte) 0x02,
            (byte) 0x77, (byte) 0x55, (byte) 0xe9, (byte) 0xe8, (byte) 0x08, (byte) 0xf3,
            (byte) 0x36, (byte) 0x9a, (byte) 0xae, (byte) 0xab, (byte) 0x04, (byte) 0x6d,
            (byte) 0x00, (byte) 0x99, (byte) 0xbf, (byte) 0x7d, (byte) 0x0f, (byte) 0x67,
            (byte) 0x8b, (byte) 0x1d, (byte) 0xd4, (byte) 0x2b, (byte) 0x7c, (byte) 0xcb,
            (byte) 0xcd, (byte) 0x33, (byte) 0xc7, (byte) 0x84, (byte) 0x30, (byte) 0xe2,
            (byte) 0x45, (byte) 0x21, (byte) 0xb3, (byte) 0x75, (byte) 0xf5, (byte) 0x79,
            (byte) 0x02, (byte) 0xda, (byte) 0x50, (byte) 0xa3, (byte) 0x8b, (byte) 0xce,
            (byte) 0xc3, (byte) 0x8e, (byte) 0x0f, (byte) 0x25, (byte) 0xeb, (byte) 0x08,
            (byte) 0x2c, (byte) 0xdd, (byte) 0x1c, (byte) 0xcf, (byte) 0xff, (byte) 0x3b,
            (byte) 0xde, (byte) 0xb6, (byte) 0xaa, (byte) 0x2a, (byte) 0xa9, (byte) 0xc4,
            (byte) 0x8a, (byte) 0x24, (byte) 0x24, (byte) 0xe6, (byte) 0x29, (byte) 0x0d,
            (byte) 0x98, (byte) 0x4c, (byte) 0x32, (byte) 0xa1, (byte) 0x7b, (byte) 0x23,
            (byte) 0x2b, (byte) 0x42, (byte) 0x30, (byte) 0xee, (byte) 0x78, (byte) 0x08,
            (byte) 0x47, (byte) 0xad, (byte) 0xf2, (byte) 0x96, (byte) 0xd5, (byte) 0xf1,
            (byte) 0x62, (byte) 0x42, (byte) 0x2d, (byte) 0x35, (byte) 0x19, (byte) 0xb4,
            (byte) 0x3c, (byte) 0xc9, (byte) 0xc3, (byte) 0x5f, (byte) 0x03, (byte) 0x16,
            (byte) 0x3a, (byte) 0x23, (byte) 0xac, (byte) 0xcb, (byte) 0xce, (byte) 0x9e,
            (byte) 0x51, (byte) 0x2e, (byte) 0x6d, (byte) 0x02, (byte) 0x03, (byte) 0x01,
            (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x81, (byte) 0x80, (byte) 0x16,
            (byte) 0x59, (byte) 0xc3, (byte) 0x24, (byte) 0x1d, (byte) 0x33, (byte) 0x98,
            (byte) 0x9c, (byte) 0xc9, (byte) 0xc8, (byte) 0x2c, (byte) 0x88, (byte) 0xbf,
            (byte) 0x0a, (byte) 0x01, (byte) 0xce, (byte) 0xfb, (byte) 0x34, (byte) 0x7a,
            (byte) 0x58, (byte) 0x7a, (byte) 0xb0, (byte) 0xbf, (byte) 0xa6, (byte) 0xb2,
            (byte) 0x60, (byte) 0xbe, (byte) 0x70, (byte) 0x21, (byte) 0xf5, (byte) 0xfc,
            (byte) 0x85, (byte) 0x0d, (byte) 0x33, (byte) 0x58, (byte) 0xa1, (byte) 0xe5,
            (byte) 0x09, (byte) 0x36, (byte) 0x84, (byte) 0xb2, (byte) 0x04, (byte) 0x0a,
            (byte) 0x02, (byte) 0xd3, (byte) 0x88, (byte) 0x1f, (byte) 0x0c, (byte) 0x2b,
            (byte) 0x1d, (byte) 0xe9, (byte) 0x3d, (byte) 0xe7, (byte) 0x79, (byte) 0xf9,
            (byte) 0x32, (byte) 0x5c, (byte) 0x8a, (byte) 0x75, (byte) 0x49, (byte) 0x12,
            (byte) 0xe4, (byte) 0x05, (byte) 0x26, (byte) 0xd4, (byte) 0x2e, (byte) 0x9e,
            (byte) 0x1f, (byte) 0xcc, (byte) 0x54, (byte) 0xad, (byte) 0x33, (byte) 0x8d,
            (byte) 0x99, (byte) 0x00, (byte) 0xdc, (byte) 0xf5, (byte) 0xb4, (byte) 0xa2,
            (byte) 0x2f, (byte) 0xba, (byte) 0xe5, (byte) 0x62, (byte) 0x30, (byte) 0x6d,
            (byte) 0xe6, (byte) 0x3d, (byte) 0xeb, (byte) 0x24, (byte) 0xc2, (byte) 0xdc,
            (byte) 0x5f, (byte) 0xb7, (byte) 0x16, (byte) 0x35, (byte) 0xa3, (byte) 0x98,
            (byte) 0x98, (byte) 0xa8, (byte) 0xef, (byte) 0xe8, (byte) 0xc4, (byte) 0x96,
            (byte) 0x6d, (byte) 0x38, (byte) 0xab, (byte) 0x26, (byte) 0x6d, (byte) 0x30,
            (byte) 0xc2, (byte) 0xa0, (byte) 0x44, (byte) 0xe4, (byte) 0xff, (byte) 0x7e,
            (byte) 0xbe, (byte) 0x7c, (byte) 0x33, (byte) 0xa5, (byte) 0x10, (byte) 0xad,
            (byte) 0xd7, (byte) 0x1e, (byte) 0x13, (byte) 0x20, (byte) 0xb3, (byte) 0x1f,
            (byte) 0x41, (byte) 0x02, (byte) 0x41, (byte) 0x00, (byte) 0xf1, (byte) 0x89,
            (byte) 0x07, (byte) 0x0f, (byte) 0xe8, (byte) 0xcf, (byte) 0xab, (byte) 0x13,
            (byte) 0x2a, (byte) 0x8f, (byte) 0x88, (byte) 0x80, (byte) 0x11, (byte) 0x9a,
            (byte) 0x79, (byte) 0xb6, (byte) 0x59, (byte) 0x3a, (byte) 0x50, (byte) 0x6e,
            (byte) 0x57, (byte) 0x37, (byte) 0xab, (byte) 0x2a, (byte) 0xd2, (byte) 0xaa,
            (byte) 0xd9, (byte) 0x72, (byte) 0x73, (byte) 0xff, (byte) 0x8b, (byte) 0x47,
            (byte) 0x76, (byte) 0xdd, (byte) 0xdc, (byte) 0xf5, (byte) 0x97, (byte) 0x44,
            (byte) 0x3a, (byte) 0x78, (byte) 0xbe, (byte) 0x17, (byte) 0xb4, (byte) 0x22,
            (byte) 0x6f, (byte) 0xe5, (byte) 0x23, (byte) 0x70, (byte) 0x1d, (byte) 0x10,
            (byte) 0x5d, (byte) 0xba, (byte) 0x16, (byte) 0x81, (byte) 0xf1, (byte) 0x45,
            (byte) 0xce, (byte) 0x30, (byte) 0xb4, (byte) 0xab, (byte) 0x80, (byte) 0xe4,
            (byte) 0x98, (byte) 0x31, (byte) 0x02, (byte) 0x41, (byte) 0x00, (byte) 0xda,
            (byte) 0x82, (byte) 0x9d, (byte) 0x3f, (byte) 0xca, (byte) 0x2f, (byte) 0xe1,
            (byte) 0xd4, (byte) 0x86, (byte) 0x77, (byte) 0x48, (byte) 0xa6, (byte) 0xab,
            (byte) 0xab, (byte) 0x1c, (byte) 0x42, (byte) 0x5c, (byte) 0xd5, (byte) 0xc7,
            (byte) 0x46, (byte) 0x59, (byte) 0x91, (byte) 0x3f, (byte) 0xfc, (byte) 0xcc,
            (byte) 0xec, (byte) 0xc2, (byte) 0x40, (byte) 0x12, (byte) 0x2c, (byte) 0x8d,
            (byte) 0x1f, (byte) 0xa2, (byte) 0x18, (byte) 0x88, (byte) 0xee, (byte) 0x82,
            (byte) 0x4a, (byte) 0x5a, (byte) 0x5e, (byte) 0x88, (byte) 0x20, (byte) 0xe3,
            (byte) 0x7b, (byte) 0xe0, (byte) 0xd8, (byte) 0x3a, (byte) 0x52, (byte) 0x9a,
            (byte) 0x26, (byte) 0x6a, (byte) 0x04, (byte) 0xec, (byte) 0xe8, (byte) 0xb9,
            (byte) 0x48, (byte) 0x40, (byte) 0xe1, (byte) 0xe1, (byte) 0x83, (byte) 0xa6,
            (byte) 0x67, (byte) 0xa6, (byte) 0xfd, (byte) 0x02, (byte) 0x41, (byte) 0x00,
            (byte) 0x89, (byte) 0x72, (byte) 0x3e, (byte) 0xb0, (byte) 0x90, (byte) 0xfd,
            (byte) 0x4c, (byte) 0x0e, (byte) 0xd6, (byte) 0x13, (byte) 0x63, (byte) 0xcb,
            (byte) 0xed, (byte) 0x38, (byte) 0x88, (byte) 0xb6, (byte) 0x79, (byte) 0xc4,
            (byte) 0x33, (byte) 0x6c, (byte) 0xf6, (byte) 0xf8, (byte) 0xd8, (byte) 0xd0,
            (byte) 0xbf, (byte) 0x9d, (byte) 0x35, (byte) 0xac, (byte) 0x69, (byte) 0xd2,
            (byte) 0x2b, (byte) 0xc1, (byte) 0xf9, (byte) 0x24, (byte) 0x7b, (byte) 0xce,
            (byte) 0xcd, (byte) 0xcb, (byte) 0xa7, (byte) 0xb2, (byte) 0x7a, (byte) 0x0a,
            (byte) 0x27, (byte) 0x19, (byte) 0xc9, (byte) 0xaf, (byte) 0x0d, (byte) 0x21,
            (byte) 0x89, (byte) 0x88, (byte) 0x7c, (byte) 0xad, (byte) 0x9e, (byte) 0x8d,
            (byte) 0x47, (byte) 0x6d, (byte) 0x3f, (byte) 0xce, (byte) 0x7b, (byte) 0xa1,
            (byte) 0x74, (byte) 0xf1, (byte) 0xa0, (byte) 0xa1, (byte) 0x02, (byte) 0x41,
            (byte) 0x00, (byte) 0xd9, (byte) 0xa8, (byte) 0xf5, (byte) 0xfe, (byte) 0xce,
            (byte) 0xe6, (byte) 0x77, (byte) 0x6b, (byte) 0xfe, (byte) 0x2d, (byte) 0xe0,
            (byte) 0x1e, (byte) 0xb6, (byte) 0x2e, (byte) 0x12, (byte) 0x4e, (byte) 0x40,
            (byte) 0xaf, (byte) 0x6a, (byte) 0x7b, (byte) 0x37, (byte) 0x49, (byte) 0x2a,
            (byte) 0x96, (byte) 0x25, (byte) 0x83, (byte) 0x49, (byte) 0xd4, (byte) 0x0c,
            (byte) 0xc6, (byte) 0x78, (byte) 0x25, (byte) 0x24, (byte) 0x90, (byte) 0x90,
            (byte) 0x06, (byte) 0x15, (byte) 0x9e, (byte) 0xfe, (byte) 0xf9, (byte) 0xdf,
            (byte) 0x5b, (byte) 0xf3, (byte) 0x7e, (byte) 0x38, (byte) 0x70, (byte) 0xeb,
            (byte) 0x57, (byte) 0xd0, (byte) 0xd9, (byte) 0xa7, (byte) 0x0e, (byte) 0x14,
            (byte) 0xf7, (byte) 0x95, (byte) 0x68, (byte) 0xd5, (byte) 0xc8, (byte) 0xab,
            (byte) 0x9d, (byte) 0x3a, (byte) 0x2b, (byte) 0x51, (byte) 0xf9, (byte) 0x02,
            (byte) 0x41, (byte) 0x00, (byte) 0x96, (byte) 0xdf, (byte) 0xe9, (byte) 0x67,
            (byte) 0x6c, (byte) 0xdc, (byte) 0x90, (byte) 0x14, (byte) 0xb4, (byte) 0x1d,
            (byte) 0x22, (byte) 0x33, (byte) 0x4a, (byte) 0x31, (byte) 0xc1, (byte) 0x9d,
            (byte) 0x2e, (byte) 0xff, (byte) 0x9a, (byte) 0x2a, (byte) 0x95, (byte) 0x4b,
            (byte) 0x27, (byte) 0x74, (byte) 0xcb, (byte) 0x21, (byte) 0xc3, (byte) 0xd2,
            (byte) 0x0b, (byte) 0xb2, (byte) 0x46, (byte) 0x87, (byte) 0xf8, (byte) 0x28,
            (byte) 0x01, (byte) 0x8b, (byte) 0xd8, (byte) 0xb9, (byte) 0x4b, (byte) 0xcd,
            (byte) 0x9a, (byte) 0x96, (byte) 0x41, (byte) 0x0e, (byte) 0x36, (byte) 0x6d,
            (byte) 0x40, (byte) 0x42, (byte) 0xbc, (byte) 0xd9, (byte) 0xd3, (byte) 0x7b,
            (byte) 0xbc, (byte) 0xa7, (byte) 0x92, (byte) 0x90, (byte) 0xdd, (byte) 0xa1,
            (byte) 0x9c, (byte) 0xce, (byte) 0xa1, (byte) 0x87, (byte) 0x11, (byte) 0x51
    };

    /**
     * Generated from above and converted with:
     *
     * openssl x509 -outform d -in usercert.pem | xxd -i | sed 's/0x/(byte) 0x/g'
     */
    private static final byte[] FAKE_RSA_USER_1 = new byte[] {
            (byte) 0x30, (byte) 0x82, (byte) 0x02, (byte) 0x95, (byte) 0x30, (byte) 0x82,
            (byte) 0x01, (byte) 0xfe, (byte) 0xa0, (byte) 0x03, (byte) 0x02, (byte) 0x01,
            (byte) 0x02, (byte) 0x02, (byte) 0x01, (byte) 0x01, (byte) 0x30, (byte) 0x0d,
            (byte) 0x06, (byte) 0x09, (byte) 0x2a, (byte) 0x86, (byte) 0x48, (byte) 0x86,
            (byte) 0xf7, (byte) 0x0d, (byte) 0x01, (byte) 0x01, (byte) 0x05, (byte) 0x05,
            (byte) 0x00, (byte) 0x30, (byte) 0x4f, (byte) 0x31, (byte) 0x0b, (byte) 0x30,
            (byte) 0x09, (byte) 0x06, (byte) 0x03, (byte) 0x55, (byte) 0x04, (byte) 0x06,
            (byte) 0x13, (byte) 0x02, (byte) 0x55, (byte) 0x53, (byte) 0x31, (byte) 0x0b,
            (byte) 0x30, (byte) 0x09, (byte) 0x06, (byte) 0x03, (byte) 0x55, (byte) 0x04,
            (byte) 0x08, (byte) 0x13, (byte) 0x02, (byte) 0x43, (byte) 0x41, (byte) 0x31,
            (byte) 0x16, (byte) 0x30, (byte) 0x14, (byte) 0x06, (byte) 0x03, (byte) 0x55,
            (byte) 0x04, (byte) 0x07, (byte) 0x13, (byte) 0x0d, (byte) 0x4d, (byte) 0x6f,
            (byte) 0x75, (byte) 0x6e, (byte) 0x74, (byte) 0x61, (byte) 0x69, (byte) 0x6e,
            (byte) 0x20, (byte) 0x56, (byte) 0x69, (byte) 0x65, (byte) 0x77, (byte) 0x31,
            (byte) 0x1b, (byte) 0x30, (byte) 0x19, (byte) 0x06, (byte) 0x03, (byte) 0x55,
            (byte) 0x04, (byte) 0x0a, (byte) 0x13, (byte) 0x12, (byte) 0x41, (byte) 0x6e,
            (byte) 0x64, (byte) 0x72, (byte) 0x6f, (byte) 0x69, (byte) 0x64, (byte) 0x20,
            (byte) 0x54, (byte) 0x65, (byte) 0x73, (byte) 0x74, (byte) 0x20, (byte) 0x43,
            (byte) 0x61, (byte) 0x73, (byte) 0x65, (byte) 0x73, (byte) 0x30, (byte) 0x1e,
            (byte) 0x17, (byte) 0x0d, (byte) 0x31, (byte) 0x32, (byte) 0x30, (byte) 0x38,
            (byte) 0x31, (byte) 0x34, (byte) 0x32, (byte) 0x33, (byte) 0x32, (byte) 0x35,
            (byte) 0x34, (byte) 0x38, (byte) 0x5a, (byte) 0x17, (byte) 0x0d, (byte) 0x32,
            (byte) 0x32, (byte) 0x30, (byte) 0x38, (byte) 0x31, (byte) 0x32, (byte) 0x32,
            (byte) 0x33, (byte) 0x32, (byte) 0x35, (byte) 0x34, (byte) 0x38, (byte) 0x5a,
            (byte) 0x30, (byte) 0x55, (byte) 0x31, (byte) 0x0b, (byte) 0x30, (byte) 0x09,
            (byte) 0x06, (byte) 0x03, (byte) 0x55, (byte) 0x04, (byte) 0x06, (byte) 0x13,
            (byte) 0x02, (byte) 0x55, (byte) 0x53, (byte) 0x31, (byte) 0x0b, (byte) 0x30,
            (byte) 0x09, (byte) 0x06, (byte) 0x03, (byte) 0x55, (byte) 0x04, (byte) 0x08,
            (byte) 0x13, (byte) 0x02, (byte) 0x43, (byte) 0x41, (byte) 0x31, (byte) 0x1b,
            (byte) 0x30, (byte) 0x19, (byte) 0x06, (byte) 0x03, (byte) 0x55, (byte) 0x04,
            (byte) 0x0a, (byte) 0x13, (byte) 0x12, (byte) 0x41, (byte) 0x6e, (byte) 0x64,
            (byte) 0x72, (byte) 0x6f, (byte) 0x69, (byte) 0x64, (byte) 0x20, (byte) 0x54,
            (byte) 0x65, (byte) 0x73, (byte) 0x74, (byte) 0x20, (byte) 0x43, (byte) 0x61,
            (byte) 0x73, (byte) 0x65, (byte) 0x73, (byte) 0x31, (byte) 0x1c, (byte) 0x30,
            (byte) 0x1a, (byte) 0x06, (byte) 0x03, (byte) 0x55, (byte) 0x04, (byte) 0x03,
            (byte) 0x13, (byte) 0x13, (byte) 0x73, (byte) 0x65, (byte) 0x72, (byte) 0x76,
            (byte) 0x65, (byte) 0x72, (byte) 0x31, (byte) 0x2e, (byte) 0x65, (byte) 0x78,
            (byte) 0x61, (byte) 0x6d, (byte) 0x70, (byte) 0x6c, (byte) 0x65, (byte) 0x2e,
            (byte) 0x63, (byte) 0x6f, (byte) 0x6d, (byte) 0x30, (byte) 0x81, (byte) 0x9f,
            (byte) 0x30, (byte) 0x0d, (byte) 0x06, (byte) 0x09, (byte) 0x2a, (byte) 0x86,
            (byte) 0x48, (byte) 0x86, (byte) 0xf7, (byte) 0x0d, (byte) 0x01, (byte) 0x01,
            (byte) 0x01, (byte) 0x05, (byte) 0x00, (byte) 0x03, (byte) 0x81, (byte) 0x8d,
            (byte) 0x00, (byte) 0x30, (byte) 0x81, (byte) 0x89, (byte) 0x02, (byte) 0x81,
            (byte) 0x81, (byte) 0x00, (byte) 0xce, (byte) 0x29, (byte) 0xeb, (byte) 0xf6,
            (byte) 0x5b, (byte) 0x25, (byte) 0xdc, (byte) 0xa1, (byte) 0xa6, (byte) 0x2c,
            (byte) 0x66, (byte) 0xcb, (byte) 0x20, (byte) 0x90, (byte) 0x27, (byte) 0x86,
            (byte) 0x8a, (byte) 0x44, (byte) 0x71, (byte) 0x50, (byte) 0xda, (byte) 0xd3,
            (byte) 0x02, (byte) 0x77, (byte) 0x55, (byte) 0xe9, (byte) 0xe8, (byte) 0x08,
            (byte) 0xf3, (byte) 0x36, (byte) 0x9a, (byte) 0xae, (byte) 0xab, (byte) 0x04,
            (byte) 0x6d, (byte) 0x00, (byte) 0x99, (byte) 0xbf, (byte) 0x7d, (byte) 0x0f,
            (byte) 0x67, (byte) 0x8b, (byte) 0x1d, (byte) 0xd4, (byte) 0x2b, (byte) 0x7c,
            (byte) 0xcb, (byte) 0xcd, (byte) 0x33, (byte) 0xc7, (byte) 0x84, (byte) 0x30,
            (byte) 0xe2, (byte) 0x45, (byte) 0x21, (byte) 0xb3, (byte) 0x75, (byte) 0xf5,
            (byte) 0x79, (byte) 0x02, (byte) 0xda, (byte) 0x50, (byte) 0xa3, (byte) 0x8b,
            (byte) 0xce, (byte) 0xc3, (byte) 0x8e, (byte) 0x0f, (byte) 0x25, (byte) 0xeb,
            (byte) 0x08, (byte) 0x2c, (byte) 0xdd, (byte) 0x1c, (byte) 0xcf, (byte) 0xff,
            (byte) 0x3b, (byte) 0xde, (byte) 0xb6, (byte) 0xaa, (byte) 0x2a, (byte) 0xa9,
            (byte) 0xc4, (byte) 0x8a, (byte) 0x24, (byte) 0x24, (byte) 0xe6, (byte) 0x29,
            (byte) 0x0d, (byte) 0x98, (byte) 0x4c, (byte) 0x32, (byte) 0xa1, (byte) 0x7b,
            (byte) 0x23, (byte) 0x2b, (byte) 0x42, (byte) 0x30, (byte) 0xee, (byte) 0x78,
            (byte) 0x08, (byte) 0x47, (byte) 0xad, (byte) 0xf2, (byte) 0x96, (byte) 0xd5,
            (byte) 0xf1, (byte) 0x62, (byte) 0x42, (byte) 0x2d, (byte) 0x35, (byte) 0x19,
            (byte) 0xb4, (byte) 0x3c, (byte) 0xc9, (byte) 0xc3, (byte) 0x5f, (byte) 0x03,
            (byte) 0x16, (byte) 0x3a, (byte) 0x23, (byte) 0xac, (byte) 0xcb, (byte) 0xce,
            (byte) 0x9e, (byte) 0x51, (byte) 0x2e, (byte) 0x6d, (byte) 0x02, (byte) 0x03,
            (byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0xa3, (byte) 0x7b, (byte) 0x30,
            (byte) 0x79, (byte) 0x30, (byte) 0x09, (byte) 0x06, (byte) 0x03, (byte) 0x55,
            (byte) 0x1d, (byte) 0x13, (byte) 0x04, (byte) 0x02, (byte) 0x30, (byte) 0x00,
            (byte) 0x30, (byte) 0x2c, (byte) 0x06, (byte) 0x09, (byte) 0x60, (byte) 0x86,
            (byte) 0x48, (byte) 0x01, (byte) 0x86, (byte) 0xf8, (byte) 0x42, (byte) 0x01,
            (byte) 0x0d, (byte) 0x04, (byte) 0x1f, (byte) 0x16, (byte) 0x1d, (byte) 0x4f,
            (byte) 0x70, (byte) 0x65, (byte) 0x6e, (byte) 0x53, (byte) 0x53, (byte) 0x4c,
            (byte) 0x20, (byte) 0x47, (byte) 0x65, (byte) 0x6e, (byte) 0x65, (byte) 0x72,
            (byte) 0x61, (byte) 0x74, (byte) 0x65, (byte) 0x64, (byte) 0x20, (byte) 0x43,
            (byte) 0x65, (byte) 0x72, (byte) 0x74, (byte) 0x69, (byte) 0x66, (byte) 0x69,
            (byte) 0x63, (byte) 0x61, (byte) 0x74, (byte) 0x65, (byte) 0x30, (byte) 0x1d,
            (byte) 0x06, (byte) 0x03, (byte) 0x55, (byte) 0x1d, (byte) 0x0e, (byte) 0x04,
            (byte) 0x16, (byte) 0x04, (byte) 0x14, (byte) 0x32, (byte) 0xa1, (byte) 0x1e,
            (byte) 0x6b, (byte) 0x69, (byte) 0x04, (byte) 0xfe, (byte) 0xb3, (byte) 0xcd,
            (byte) 0xf8, (byte) 0xbb, (byte) 0x14, (byte) 0xcd, (byte) 0xff, (byte) 0xd4,
            (byte) 0x16, (byte) 0xc3, (byte) 0xab, (byte) 0x44, (byte) 0x2f, (byte) 0x30,
            (byte) 0x1f, (byte) 0x06, (byte) 0x03, (byte) 0x55, (byte) 0x1d, (byte) 0x23,
            (byte) 0x04, (byte) 0x18, (byte) 0x30, (byte) 0x16, (byte) 0x80, (byte) 0x14,
            (byte) 0x33, (byte) 0x05, (byte) 0xee, (byte) 0xfe, (byte) 0x6f, (byte) 0x60,
            (byte) 0xc7, (byte) 0xf9, (byte) 0xa9, (byte) 0xd2, (byte) 0x73, (byte) 0x5c,
            (byte) 0x8f, (byte) 0x6d, (byte) 0xa2, (byte) 0x2f, (byte) 0x97, (byte) 0x8e,
            (byte) 0x5d, (byte) 0x51, (byte) 0x30, (byte) 0x0d, (byte) 0x06, (byte) 0x09,
            (byte) 0x2a, (byte) 0x86, (byte) 0x48, (byte) 0x86, (byte) 0xf7, (byte) 0x0d,
            (byte) 0x01, (byte) 0x01, (byte) 0x05, (byte) 0x05, (byte) 0x00, (byte) 0x03,
            (byte) 0x81, (byte) 0x81, (byte) 0x00, (byte) 0x46, (byte) 0x42, (byte) 0xef,
            (byte) 0x56, (byte) 0x89, (byte) 0x78, (byte) 0x90, (byte) 0x38, (byte) 0x24,
            (byte) 0x9f, (byte) 0x8c, (byte) 0x7a, (byte) 0xce, (byte) 0x7a, (byte) 0xa5,
            (byte) 0xb5, (byte) 0x1e, (byte) 0x74, (byte) 0x96, (byte) 0x34, (byte) 0x49,
            (byte) 0x8b, (byte) 0xed, (byte) 0x44, (byte) 0xb3, (byte) 0xc9, (byte) 0x05,
            (byte) 0xd7, (byte) 0x48, (byte) 0x55, (byte) 0x52, (byte) 0x59, (byte) 0x15,
            (byte) 0x0b, (byte) 0xaa, (byte) 0x16, (byte) 0x86, (byte) 0xd2, (byte) 0x8e,
            (byte) 0x16, (byte) 0x99, (byte) 0xe8, (byte) 0x5f, (byte) 0x11, (byte) 0x71,
            (byte) 0x42, (byte) 0x55, (byte) 0xd1, (byte) 0xc4, (byte) 0x6f, (byte) 0x2e,
            (byte) 0xa9, (byte) 0x64, (byte) 0x6f, (byte) 0xd8, (byte) 0xfd, (byte) 0x43,
            (byte) 0x13, (byte) 0x24, (byte) 0xaa, (byte) 0x67, (byte) 0xe6, (byte) 0xf5,
            (byte) 0xca, (byte) 0x80, (byte) 0x5e, (byte) 0x3a, (byte) 0x3e, (byte) 0xcc,
            (byte) 0x4f, (byte) 0xba, (byte) 0x87, (byte) 0xe6, (byte) 0xae, (byte) 0xbf,
            (byte) 0x8f, (byte) 0xd5, (byte) 0x28, (byte) 0x38, (byte) 0x58, (byte) 0x30,
            (byte) 0x24, (byte) 0xf6, (byte) 0x53, (byte) 0x5b, (byte) 0x41, (byte) 0x53,
            (byte) 0xe6, (byte) 0x45, (byte) 0xbc, (byte) 0xbe, (byte) 0xe6, (byte) 0xbb,
            (byte) 0x5d, (byte) 0xd8, (byte) 0xa7, (byte) 0xf9, (byte) 0x64, (byte) 0x99,
            (byte) 0x04, (byte) 0x43, (byte) 0x75, (byte) 0xd7, (byte) 0x2d, (byte) 0x32,
            (byte) 0x0a, (byte) 0x94, (byte) 0xaf, (byte) 0x06, (byte) 0x34, (byte) 0xae,
            (byte) 0x46, (byte) 0xbd, (byte) 0xda, (byte) 0x00, (byte) 0x0e, (byte) 0x25,
            (byte) 0xc2, (byte) 0xf7, (byte) 0xc9, (byte) 0xc3, (byte) 0x65, (byte) 0xd2,
            (byte) 0x08, (byte) 0x41, (byte) 0x0a, (byte) 0xf3, (byte) 0x72
    };

    /*
     * The keys and certificates below are generated with:
     *
     * openssl req -new -x509 -days 3650 -extensions v3_ca -keyout cakey.pem -out cacert.pem
     * openssl ecparam -name prime256v1 -out ecparam.pem
     * openssl req -newkey ec:ecparam.pem -keyout userkey.pem -nodes -days 3650 -out userkey.req
     * mkdir -p demoCA/newcerts
     * touch demoCA/index.txt
     * echo "01" > demoCA/serial
     * openssl ca -out usercert.pem -in userkey.req -cert cacert.pem -keyfile cakey.pem -days 3650
     */

    /**
     * Generated from above and converted with:
     *
     * openssl x509 -outform d -in cacert.pem | xxd -i | sed 's/0x/(byte) 0x/g'
     */
    private static final byte[] FAKE_EC_CA_1 = {
            (byte) 0x30, (byte) 0x82, (byte) 0x02, (byte) 0x58, (byte) 0x30, (byte) 0x82,
            (byte) 0x01, (byte) 0xc1, (byte) 0xa0, (byte) 0x03, (byte) 0x02, (byte) 0x01,
            (byte) 0x02, (byte) 0x02, (byte) 0x09, (byte) 0x00, (byte) 0xe1, (byte) 0xb2,
            (byte) 0x8c, (byte) 0x04, (byte) 0x95, (byte) 0xeb, (byte) 0x10, (byte) 0xcb,
            (byte) 0x30, (byte) 0x0d, (byte) 0x06, (byte) 0x09, (byte) 0x2a, (byte) 0x86,
            (byte) 0x48, (byte) 0x86, (byte) 0xf7, (byte) 0x0d, (byte) 0x01, (byte) 0x01,
            (byte) 0x05, (byte) 0x05, (byte) 0x00, (byte) 0x30, (byte) 0x45, (byte) 0x31,
            (byte) 0x0b, (byte) 0x30, (byte) 0x09, (byte) 0x06, (byte) 0x03, (byte) 0x55,
            (byte) 0x04, (byte) 0x06, (byte) 0x13, (byte) 0x02, (byte) 0x41, (byte) 0x55,
            (byte) 0x31, (byte) 0x13, (byte) 0x30, (byte) 0x11, (byte) 0x06, (byte) 0x03,
            (byte) 0x55, (byte) 0x04, (byte) 0x08, (byte) 0x0c, (byte) 0x0a, (byte) 0x53,
            (byte) 0x6f, (byte) 0x6d, (byte) 0x65, (byte) 0x2d, (byte) 0x53, (byte) 0x74,
            (byte) 0x61, (byte) 0x74, (byte) 0x65, (byte) 0x31, (byte) 0x21, (byte) 0x30,
            (byte) 0x1f, (byte) 0x06, (byte) 0x03, (byte) 0x55, (byte) 0x04, (byte) 0x0a,
            (byte) 0x0c, (byte) 0x18, (byte) 0x49, (byte) 0x6e, (byte) 0x74, (byte) 0x65,
            (byte) 0x72, (byte) 0x6e, (byte) 0x65, (byte) 0x74, (byte) 0x20, (byte) 0x57,
            (byte) 0x69, (byte) 0x64, (byte) 0x67, (byte) 0x69, (byte) 0x74, (byte) 0x73,
            (byte) 0x20, (byte) 0x50, (byte) 0x74, (byte) 0x79, (byte) 0x20, (byte) 0x4c,
            (byte) 0x74, (byte) 0x64, (byte) 0x30, (byte) 0x1e, (byte) 0x17, (byte) 0x0d,
            (byte) 0x31, (byte) 0x33, (byte) 0x30, (byte) 0x38, (byte) 0x32, (byte) 0x37,
            (byte) 0x31, (byte) 0x36, (byte) 0x32, (byte) 0x38, (byte) 0x32, (byte) 0x38,
            (byte) 0x5a, (byte) 0x17, (byte) 0x0d, (byte) 0x32, (byte) 0x33, (byte) 0x30,
            (byte) 0x38, (byte) 0x32, (byte) 0x35, (byte) 0x31, (byte) 0x36, (byte) 0x32,
            (byte) 0x38, (byte) 0x32, (byte) 0x38, (byte) 0x5a, (byte) 0x30, (byte) 0x45,
            (byte) 0x31, (byte) 0x0b, (byte) 0x30, (byte) 0x09, (byte) 0x06, (byte) 0x03,
            (byte) 0x55, (byte) 0x04, (byte) 0x06, (byte) 0x13, (byte) 0x02, (byte) 0x41,
            (byte) 0x55, (byte) 0x31, (byte) 0x13, (byte) 0x30, (byte) 0x11, (byte) 0x06,
            (byte) 0x03, (byte) 0x55, (byte) 0x04, (byte) 0x08, (byte) 0x0c, (byte) 0x0a,
            (byte) 0x53, (byte) 0x6f, (byte) 0x6d, (byte) 0x65, (byte) 0x2d, (byte) 0x53,
            (byte) 0x74, (byte) 0x61, (byte) 0x74, (byte) 0x65, (byte) 0x31, (byte) 0x21,
            (byte) 0x30, (byte) 0x1f, (byte) 0x06, (byte) 0x03, (byte) 0x55, (byte) 0x04,
            (byte) 0x0a, (byte) 0x0c, (byte) 0x18, (byte) 0x49, (byte) 0x6e, (byte) 0x74,
            (byte) 0x65, (byte) 0x72, (byte) 0x6e, (byte) 0x65, (byte) 0x74, (byte) 0x20,
            (byte) 0x57, (byte) 0x69, (byte) 0x64, (byte) 0x67, (byte) 0x69, (byte) 0x74,
            (byte) 0x73, (byte) 0x20, (byte) 0x50, (byte) 0x74, (byte) 0x79, (byte) 0x20,
            (byte) 0x4c, (byte) 0x74, (byte) 0x64, (byte) 0x30, (byte) 0x81, (byte) 0x9f,
            (byte) 0x30, (byte) 0x0d, (byte) 0x06, (byte) 0x09, (byte) 0x2a, (byte) 0x86,
            (byte) 0x48, (byte) 0x86, (byte) 0xf7, (byte) 0x0d, (byte) 0x01, (byte) 0x01,
            (byte) 0x01, (byte) 0x05, (byte) 0x00, (byte) 0x03, (byte) 0x81, (byte) 0x8d,
            (byte) 0x00, (byte) 0x30, (byte) 0x81, (byte) 0x89, (byte) 0x02, (byte) 0x81,
            (byte) 0x81, (byte) 0x00, (byte) 0xb5, (byte) 0xf6, (byte) 0x08, (byte) 0x0f,
            (byte) 0xc4, (byte) 0x4d, (byte) 0xe4, (byte) 0x0d, (byte) 0x34, (byte) 0x1d,
            (byte) 0xe2, (byte) 0x23, (byte) 0x18, (byte) 0x63, (byte) 0x03, (byte) 0xf7,
            (byte) 0x14, (byte) 0x0e, (byte) 0x98, (byte) 0xcd, (byte) 0x45, (byte) 0x1f,
            (byte) 0xfe, (byte) 0xfb, (byte) 0x09, (byte) 0x3f, (byte) 0x5d, (byte) 0x36,
            (byte) 0x3b, (byte) 0x0f, (byte) 0xf9, (byte) 0x5e, (byte) 0x86, (byte) 0x56,
            (byte) 0x64, (byte) 0xd7, (byte) 0x3f, (byte) 0xae, (byte) 0x33, (byte) 0x09,
            (byte) 0xd3, (byte) 0xdd, (byte) 0x06, (byte) 0x17, (byte) 0x26, (byte) 0xdc,
            (byte) 0xa2, (byte) 0x8c, (byte) 0x3c, (byte) 0x65, (byte) 0xed, (byte) 0x03,
            (byte) 0x82, (byte) 0x78, (byte) 0x9b, (byte) 0xee, (byte) 0xe3, (byte) 0x98,
            (byte) 0x58, (byte) 0xe1, (byte) 0xf1, (byte) 0xa0, (byte) 0x85, (byte) 0xae,
            (byte) 0x63, (byte) 0x84, (byte) 0x41, (byte) 0x46, (byte) 0xa7, (byte) 0x4f,
            (byte) 0xdc, (byte) 0xbb, (byte) 0x1c, (byte) 0x6e, (byte) 0xec, (byte) 0x7b,
            (byte) 0xd5, (byte) 0xab, (byte) 0x3d, (byte) 0x6a, (byte) 0x05, (byte) 0x58,
            (byte) 0x0f, (byte) 0x9b, (byte) 0x6a, (byte) 0x67, (byte) 0x4b, (byte) 0xe9,
            (byte) 0x2a, (byte) 0x6d, (byte) 0x96, (byte) 0x11, (byte) 0x53, (byte) 0x95,
            (byte) 0x78, (byte) 0xaa, (byte) 0xd1, (byte) 0x91, (byte) 0x4a, (byte) 0xf8,
            (byte) 0x54, (byte) 0x52, (byte) 0x6d, (byte) 0xb9, (byte) 0xca, (byte) 0x74,
            (byte) 0x81, (byte) 0xf8, (byte) 0x99, (byte) 0x64, (byte) 0xd1, (byte) 0x4f,
            (byte) 0x01, (byte) 0x38, (byte) 0x4f, (byte) 0x08, (byte) 0x5c, (byte) 0x31,
            (byte) 0xcb, (byte) 0x7c, (byte) 0x5c, (byte) 0x78, (byte) 0x5d, (byte) 0x47,
            (byte) 0xd9, (byte) 0xf0, (byte) 0x1a, (byte) 0xeb, (byte) 0x02, (byte) 0x03,
            (byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0xa3, (byte) 0x50, (byte) 0x30,
            (byte) 0x4e, (byte) 0x30, (byte) 0x1d, (byte) 0x06, (byte) 0x03, (byte) 0x55,
            (byte) 0x1d, (byte) 0x0e, (byte) 0x04, (byte) 0x16, (byte) 0x04, (byte) 0x14,
            (byte) 0x5f, (byte) 0x5b, (byte) 0x5e, (byte) 0xac, (byte) 0x29, (byte) 0xfa,
            (byte) 0xa1, (byte) 0x9f, (byte) 0x9e, (byte) 0xad, (byte) 0x46, (byte) 0xe1,
            (byte) 0xbc, (byte) 0x20, (byte) 0x72, (byte) 0xcf, (byte) 0x4a, (byte) 0xd4,
            (byte) 0xfa, (byte) 0xe3, (byte) 0x30, (byte) 0x1f, (byte) 0x06, (byte) 0x03,
            (byte) 0x55, (byte) 0x1d, (byte) 0x23, (byte) 0x04, (byte) 0x18, (byte) 0x30,
            (byte) 0x16, (byte) 0x80, (byte) 0x14, (byte) 0x5f, (byte) 0x5b, (byte) 0x5e,
            (byte) 0xac, (byte) 0x29, (byte) 0xfa, (byte) 0xa1, (byte) 0x9f, (byte) 0x9e,
            (byte) 0xad, (byte) 0x46, (byte) 0xe1, (byte) 0xbc, (byte) 0x20, (byte) 0x72,
            (byte) 0xcf, (byte) 0x4a, (byte) 0xd4, (byte) 0xfa, (byte) 0xe3, (byte) 0x30,
            (byte) 0x0c, (byte) 0x06, (byte) 0x03, (byte) 0x55, (byte) 0x1d, (byte) 0x13,
            (byte) 0x04, (byte) 0x05, (byte) 0x30, (byte) 0x03, (byte) 0x01, (byte) 0x01,
            (byte) 0xff, (byte) 0x30, (byte) 0x0d, (byte) 0x06, (byte) 0x09, (byte) 0x2a,
            (byte) 0x86, (byte) 0x48, (byte) 0x86, (byte) 0xf7, (byte) 0x0d, (byte) 0x01,
            (byte) 0x01, (byte) 0x05, (byte) 0x05, (byte) 0x00, (byte) 0x03, (byte) 0x81,
            (byte) 0x81, (byte) 0x00, (byte) 0xa1, (byte) 0x4a, (byte) 0xe6, (byte) 0xfc,
            (byte) 0x7f, (byte) 0x17, (byte) 0xaa, (byte) 0x65, (byte) 0x4a, (byte) 0x34,
            (byte) 0xde, (byte) 0x69, (byte) 0x67, (byte) 0x54, (byte) 0x4d, (byte) 0xa2,
            (byte) 0xc2, (byte) 0x98, (byte) 0x02, (byte) 0x43, (byte) 0x6a, (byte) 0x0e,
            (byte) 0x0b, (byte) 0x7f, (byte) 0xa4, (byte) 0x46, (byte) 0xaf, (byte) 0xa4,
            (byte) 0x65, (byte) 0xa0, (byte) 0xdb, (byte) 0xf1, (byte) 0x5b, (byte) 0xd5,
            (byte) 0x09, (byte) 0xbc, (byte) 0xee, (byte) 0x37, (byte) 0x51, (byte) 0x19,
            (byte) 0x36, (byte) 0xc0, (byte) 0x90, (byte) 0xd3, (byte) 0x5f, (byte) 0xf3,
            (byte) 0x4f, (byte) 0xb9, (byte) 0x08, (byte) 0x45, (byte) 0x0e, (byte) 0x01,
            (byte) 0x8a, (byte) 0x95, (byte) 0xef, (byte) 0x92, (byte) 0x95, (byte) 0x33,
            (byte) 0x78, (byte) 0xdd, (byte) 0x90, (byte) 0xbb, (byte) 0xf3, (byte) 0x06,
            (byte) 0x75, (byte) 0xd0, (byte) 0x66, (byte) 0xe6, (byte) 0xd0, (byte) 0x18,
            (byte) 0x6e, (byte) 0xeb, (byte) 0x1c, (byte) 0x52, (byte) 0xc3, (byte) 0x2e,
            (byte) 0x57, (byte) 0x7d, (byte) 0xa9, (byte) 0x03, (byte) 0xdb, (byte) 0xf4,
            (byte) 0x57, (byte) 0x5f, (byte) 0x6c, (byte) 0x7e, (byte) 0x00, (byte) 0x0d,
            (byte) 0x8f, (byte) 0xe8, (byte) 0x91, (byte) 0xf7, (byte) 0xae, (byte) 0x24,
            (byte) 0x35, (byte) 0x07, (byte) 0xb5, (byte) 0x48, (byte) 0x2d, (byte) 0x36,
            (byte) 0x30, (byte) 0x5d, (byte) 0xe9, (byte) 0x49, (byte) 0x2d, (byte) 0xd1,
            (byte) 0x5d, (byte) 0xc5, (byte) 0xf4, (byte) 0x33, (byte) 0x77, (byte) 0x3c,
            (byte) 0x71, (byte) 0xad, (byte) 0x90, (byte) 0x65, (byte) 0xa9, (byte) 0xc1,
            (byte) 0x0b, (byte) 0x5c, (byte) 0x62, (byte) 0x55, (byte) 0x50, (byte) 0x6f,
            (byte) 0x9b, (byte) 0xc9, (byte) 0x0d, (byte) 0xee
    };

    /**
     * Generated from above and converted with:
     *
     * openssl pkcs8 -topk8 -outform d -in userkey.pem -nocrypt | xxd -i | sed 's/0x/(byte) 0x/g'
     */
    private static final byte[] FAKE_EC_KEY_1 = new byte[] {
            (byte) 0x30, (byte) 0x81, (byte) 0x87, (byte) 0x02, (byte) 0x01, (byte) 0x00,
            (byte) 0x30, (byte) 0x13, (byte) 0x06, (byte) 0x07, (byte) 0x2a, (byte) 0x86,
            (byte) 0x48, (byte) 0xce, (byte) 0x3d, (byte) 0x02, (byte) 0x01, (byte) 0x06,
            (byte) 0x08, (byte) 0x2a, (byte) 0x86, (byte) 0x48, (byte) 0xce, (byte) 0x3d,
            (byte) 0x03, (byte) 0x01, (byte) 0x07, (byte) 0x04, (byte) 0x6d, (byte) 0x30,
            (byte) 0x6b, (byte) 0x02, (byte) 0x01, (byte) 0x01, (byte) 0x04, (byte) 0x20,
            (byte) 0x3a, (byte) 0x8a, (byte) 0x02, (byte) 0xdc, (byte) 0xde, (byte) 0x70,
            (byte) 0x84, (byte) 0x45, (byte) 0x34, (byte) 0xaf, (byte) 0xbd, (byte) 0xd5,
            (byte) 0x02, (byte) 0x17, (byte) 0x69, (byte) 0x90, (byte) 0x65, (byte) 0x1e,
            (byte) 0x87, (byte) 0xf1, (byte) 0x3d, (byte) 0x17, (byte) 0xb6, (byte) 0xf4,
            (byte) 0x31, (byte) 0x94, (byte) 0x86, (byte) 0x76, (byte) 0x55, (byte) 0xf7,
            (byte) 0xcc, (byte) 0xba, (byte) 0xa1, (byte) 0x44, (byte) 0x03, (byte) 0x42,
            (byte) 0x00, (byte) 0x04, (byte) 0xd9, (byte) 0xcf, (byte) 0xe7, (byte) 0x9b,
            (byte) 0x23, (byte) 0xc8, (byte) 0xa3, (byte) 0xb8, (byte) 0x33, (byte) 0x14,
            (byte) 0xa4, (byte) 0x4d, (byte) 0x75, (byte) 0x90, (byte) 0xf3, (byte) 0xcd,
            (byte) 0x43, (byte) 0xe5, (byte) 0x1b, (byte) 0x05, (byte) 0x1d, (byte) 0xf3,
            (byte) 0xd0, (byte) 0xa3, (byte) 0xb7, (byte) 0x32, (byte) 0x5f, (byte) 0x79,
            (byte) 0xdc, (byte) 0x88, (byte) 0xb8, (byte) 0x4d, (byte) 0xb3, (byte) 0xd1,
            (byte) 0x6d, (byte) 0xf7, (byte) 0x75, (byte) 0xf3, (byte) 0xbf, (byte) 0x50,
            (byte) 0xa1, (byte) 0xbc, (byte) 0x03, (byte) 0x64, (byte) 0x22, (byte) 0xe6,
            (byte) 0x1a, (byte) 0xa1, (byte) 0xe1, (byte) 0x06, (byte) 0x68, (byte) 0x3b,
            (byte) 0xbc, (byte) 0x9f, (byte) 0xd3, (byte) 0xae, (byte) 0x77, (byte) 0x5e,
            (byte) 0x88, (byte) 0x0c, (byte) 0x5e, (byte) 0x0c, (byte) 0xb2, (byte) 0x38
    };

    /**
     * Generated from above and converted with:
     *
     * openssl x509 -outform d -in usercert.pem | xxd -i | sed 's/0x/(byte) 0x/g'
     */
    private static final byte[] FAKE_EC_USER_1 = new byte[] {
            (byte) 0x30, (byte) 0x82, (byte) 0x02, (byte) 0x51, (byte) 0x30, (byte) 0x82,
            (byte) 0x01, (byte) 0xba, (byte) 0xa0, (byte) 0x03, (byte) 0x02, (byte) 0x01,
            (byte) 0x02, (byte) 0x02, (byte) 0x01, (byte) 0x01, (byte) 0x30, (byte) 0x0d,
            (byte) 0x06, (byte) 0x09, (byte) 0x2a, (byte) 0x86, (byte) 0x48, (byte) 0x86,
            (byte) 0xf7, (byte) 0x0d, (byte) 0x01, (byte) 0x01, (byte) 0x05, (byte) 0x05,
            (byte) 0x00, (byte) 0x30, (byte) 0x45, (byte) 0x31, (byte) 0x0b, (byte) 0x30,
            (byte) 0x09, (byte) 0x06, (byte) 0x03, (byte) 0x55, (byte) 0x04, (byte) 0x06,
            (byte) 0x13, (byte) 0x02, (byte) 0x41, (byte) 0x55, (byte) 0x31, (byte) 0x13,
            (byte) 0x30, (byte) 0x11, (byte) 0x06, (byte) 0x03, (byte) 0x55, (byte) 0x04,
            (byte) 0x08, (byte) 0x0c, (byte) 0x0a, (byte) 0x53, (byte) 0x6f, (byte) 0x6d,
            (byte) 0x65, (byte) 0x2d, (byte) 0x53, (byte) 0x74, (byte) 0x61, (byte) 0x74,
            (byte) 0x65, (byte) 0x31, (byte) 0x21, (byte) 0x30, (byte) 0x1f, (byte) 0x06,
            (byte) 0x03, (byte) 0x55, (byte) 0x04, (byte) 0x0a, (byte) 0x0c, (byte) 0x18,
            (byte) 0x49, (byte) 0x6e, (byte) 0x74, (byte) 0x65, (byte) 0x72, (byte) 0x6e,
            (byte) 0x65, (byte) 0x74, (byte) 0x20, (byte) 0x57, (byte) 0x69, (byte) 0x64,
            (byte) 0x67, (byte) 0x69, (byte) 0x74, (byte) 0x73, (byte) 0x20, (byte) 0x50,
            (byte) 0x74, (byte) 0x79, (byte) 0x20, (byte) 0x4c, (byte) 0x74, (byte) 0x64,
            (byte) 0x30, (byte) 0x1e, (byte) 0x17, (byte) 0x0d, (byte) 0x31, (byte) 0x33,
            (byte) 0x30, (byte) 0x38, (byte) 0x32, (byte) 0x37, (byte) 0x31, (byte) 0x36,
            (byte) 0x33, (byte) 0x30, (byte) 0x30, (byte) 0x38, (byte) 0x5a, (byte) 0x17,
            (byte) 0x0d, (byte) 0x32, (byte) 0x33, (byte) 0x30, (byte) 0x38, (byte) 0x32,
            (byte) 0x35, (byte) 0x31, (byte) 0x36, (byte) 0x33, (byte) 0x30, (byte) 0x30,
            (byte) 0x38, (byte) 0x5a, (byte) 0x30, (byte) 0x62, (byte) 0x31, (byte) 0x0b,
            (byte) 0x30, (byte) 0x09, (byte) 0x06, (byte) 0x03, (byte) 0x55, (byte) 0x04,
            (byte) 0x06, (byte) 0x13, (byte) 0x02, (byte) 0x41, (byte) 0x55, (byte) 0x31,
            (byte) 0x13, (byte) 0x30, (byte) 0x11, (byte) 0x06, (byte) 0x03, (byte) 0x55,
            (byte) 0x04, (byte) 0x08, (byte) 0x0c, (byte) 0x0a, (byte) 0x53, (byte) 0x6f,
            (byte) 0x6d, (byte) 0x65, (byte) 0x2d, (byte) 0x53, (byte) 0x74, (byte) 0x61,
            (byte) 0x74, (byte) 0x65, (byte) 0x31, (byte) 0x21, (byte) 0x30, (byte) 0x1f,
            (byte) 0x06, (byte) 0x03, (byte) 0x55, (byte) 0x04, (byte) 0x0a, (byte) 0x0c,
            (byte) 0x18, (byte) 0x49, (byte) 0x6e, (byte) 0x74, (byte) 0x65, (byte) 0x72,
            (byte) 0x6e, (byte) 0x65, (byte) 0x74, (byte) 0x20, (byte) 0x57, (byte) 0x69,
            (byte) 0x64, (byte) 0x67, (byte) 0x69, (byte) 0x74, (byte) 0x73, (byte) 0x20,
            (byte) 0x50, (byte) 0x74, (byte) 0x79, (byte) 0x20, (byte) 0x4c, (byte) 0x74,
            (byte) 0x64, (byte) 0x31, (byte) 0x1b, (byte) 0x30, (byte) 0x19, (byte) 0x06,
            (byte) 0x03, (byte) 0x55, (byte) 0x04, (byte) 0x03, (byte) 0x0c, (byte) 0x12,
            (byte) 0x73, (byte) 0x65, (byte) 0x72, (byte) 0x76, (byte) 0x65, (byte) 0x72,
            (byte) 0x2e, (byte) 0x65, (byte) 0x78, (byte) 0x61, (byte) 0x6d, (byte) 0x70,
            (byte) 0x6c, (byte) 0x65, (byte) 0x2e, (byte) 0x63, (byte) 0x6f, (byte) 0x6d,
            (byte) 0x30, (byte) 0x59, (byte) 0x30, (byte) 0x13, (byte) 0x06, (byte) 0x07,
            (byte) 0x2a, (byte) 0x86, (byte) 0x48, (byte) 0xce, (byte) 0x3d, (byte) 0x02,
            (byte) 0x01, (byte) 0x06, (byte) 0x08, (byte) 0x2a, (byte) 0x86, (byte) 0x48,
            (byte) 0xce, (byte) 0x3d, (byte) 0x03, (byte) 0x01, (byte) 0x07, (byte) 0x03,
            (byte) 0x42, (byte) 0x00, (byte) 0x04, (byte) 0xd9, (byte) 0xcf, (byte) 0xe7,
            (byte) 0x9b, (byte) 0x23, (byte) 0xc8, (byte) 0xa3, (byte) 0xb8, (byte) 0x33,
            (byte) 0x14, (byte) 0xa4, (byte) 0x4d, (byte) 0x75, (byte) 0x90, (byte) 0xf3,
            (byte) 0xcd, (byte) 0x43, (byte) 0xe5, (byte) 0x1b, (byte) 0x05, (byte) 0x1d,
            (byte) 0xf3, (byte) 0xd0, (byte) 0xa3, (byte) 0xb7, (byte) 0x32, (byte) 0x5f,
            (byte) 0x79, (byte) 0xdc, (byte) 0x88, (byte) 0xb8, (byte) 0x4d, (byte) 0xb3,
            (byte) 0xd1, (byte) 0x6d, (byte) 0xf7, (byte) 0x75, (byte) 0xf3, (byte) 0xbf,
            (byte) 0x50, (byte) 0xa1, (byte) 0xbc, (byte) 0x03, (byte) 0x64, (byte) 0x22,
            (byte) 0xe6, (byte) 0x1a, (byte) 0xa1, (byte) 0xe1, (byte) 0x06, (byte) 0x68,
            (byte) 0x3b, (byte) 0xbc, (byte) 0x9f, (byte) 0xd3, (byte) 0xae, (byte) 0x77,
            (byte) 0x5e, (byte) 0x88, (byte) 0x0c, (byte) 0x5e, (byte) 0x0c, (byte) 0xb2,
            (byte) 0x38, (byte) 0xa3, (byte) 0x7b, (byte) 0x30, (byte) 0x79, (byte) 0x30,
            (byte) 0x09, (byte) 0x06, (byte) 0x03, (byte) 0x55, (byte) 0x1d, (byte) 0x13,
            (byte) 0x04, (byte) 0x02, (byte) 0x30, (byte) 0x00, (byte) 0x30, (byte) 0x2c,
            (byte) 0x06, (byte) 0x09, (byte) 0x60, (byte) 0x86, (byte) 0x48, (byte) 0x01,
            (byte) 0x86, (byte) 0xf8, (byte) 0x42, (byte) 0x01, (byte) 0x0d, (byte) 0x04,
            (byte) 0x1f, (byte) 0x16, (byte) 0x1d, (byte) 0x4f, (byte) 0x70, (byte) 0x65,
            (byte) 0x6e, (byte) 0x53, (byte) 0x53, (byte) 0x4c, (byte) 0x20, (byte) 0x47,
            (byte) 0x65, (byte) 0x6e, (byte) 0x65, (byte) 0x72, (byte) 0x61, (byte) 0x74,
            (byte) 0x65, (byte) 0x64, (byte) 0x20, (byte) 0x43, (byte) 0x65, (byte) 0x72,
            (byte) 0x74, (byte) 0x69, (byte) 0x66, (byte) 0x69, (byte) 0x63, (byte) 0x61,
            (byte) 0x74, (byte) 0x65, (byte) 0x30, (byte) 0x1d, (byte) 0x06, (byte) 0x03,
            (byte) 0x55, (byte) 0x1d, (byte) 0x0e, (byte) 0x04, (byte) 0x16, (byte) 0x04,
            (byte) 0x14, (byte) 0xd5, (byte) 0xc4, (byte) 0x72, (byte) 0xbd, (byte) 0xd2,
            (byte) 0x4e, (byte) 0x90, (byte) 0x1b, (byte) 0x14, (byte) 0x32, (byte) 0xdb,
            (byte) 0x03, (byte) 0xae, (byte) 0xfa, (byte) 0x27, (byte) 0x7d, (byte) 0x8d,
            (byte) 0xe4, (byte) 0x80, (byte) 0x58, (byte) 0x30, (byte) 0x1f, (byte) 0x06,
            (byte) 0x03, (byte) 0x55, (byte) 0x1d, (byte) 0x23, (byte) 0x04, (byte) 0x18,
            (byte) 0x30, (byte) 0x16, (byte) 0x80, (byte) 0x14, (byte) 0x5f, (byte) 0x5b,
            (byte) 0x5e, (byte) 0xac, (byte) 0x29, (byte) 0xfa, (byte) 0xa1, (byte) 0x9f,
            (byte) 0x9e, (byte) 0xad, (byte) 0x46, (byte) 0xe1, (byte) 0xbc, (byte) 0x20,
            (byte) 0x72, (byte) 0xcf, (byte) 0x4a, (byte) 0xd4, (byte) 0xfa, (byte) 0xe3,
            (byte) 0x30, (byte) 0x0d, (byte) 0x06, (byte) 0x09, (byte) 0x2a, (byte) 0x86,
            (byte) 0x48, (byte) 0x86, (byte) 0xf7, (byte) 0x0d, (byte) 0x01, (byte) 0x01,
            (byte) 0x05, (byte) 0x05, (byte) 0x00, (byte) 0x03, (byte) 0x81, (byte) 0x81,
            (byte) 0x00, (byte) 0x43, (byte) 0x99, (byte) 0x9f, (byte) 0x67, (byte) 0x08,
            (byte) 0x43, (byte) 0xd5, (byte) 0x6b, (byte) 0x6f, (byte) 0xd7, (byte) 0x05,
            (byte) 0xd6, (byte) 0x75, (byte) 0x34, (byte) 0x30, (byte) 0xca, (byte) 0x20,
            (byte) 0x47, (byte) 0x61, (byte) 0xa1, (byte) 0x89, (byte) 0xb6, (byte) 0xf1,
            (byte) 0x49, (byte) 0x7b, (byte) 0xd9, (byte) 0xb9, (byte) 0xe8, (byte) 0x1e,
            (byte) 0x29, (byte) 0x74, (byte) 0x0a, (byte) 0x67, (byte) 0xc0, (byte) 0x7d,
            (byte) 0xb8, (byte) 0xe6, (byte) 0x39, (byte) 0xa8, (byte) 0x5e, (byte) 0xc3,
            (byte) 0xb0, (byte) 0xa1, (byte) 0x30, (byte) 0x6a, (byte) 0x1f, (byte) 0x1d,
            (byte) 0xfc, (byte) 0x11, (byte) 0x59, (byte) 0x0b, (byte) 0xb9, (byte) 0xad,
            (byte) 0x3a, (byte) 0x4e, (byte) 0x50, (byte) 0x0a, (byte) 0x61, (byte) 0xdb,
            (byte) 0x75, (byte) 0x6b, (byte) 0xe5, (byte) 0x3f, (byte) 0x8d, (byte) 0xde,
            (byte) 0x28, (byte) 0x68, (byte) 0xb1, (byte) 0x29, (byte) 0x9a, (byte) 0x18,
            (byte) 0x8a, (byte) 0xfc, (byte) 0x3f, (byte) 0x13, (byte) 0x93, (byte) 0x29,
            (byte) 0xed, (byte) 0x22, (byte) 0x7c, (byte) 0xb4, (byte) 0x50, (byte) 0xd5,
            (byte) 0x4d, (byte) 0x32, (byte) 0x4d, (byte) 0x42, (byte) 0x2b, (byte) 0x29,
            (byte) 0x97, (byte) 0x86, (byte) 0xc0, (byte) 0x01, (byte) 0x00, (byte) 0x25,
            (byte) 0xf6, (byte) 0xd3, (byte) 0x2a, (byte) 0xd8, (byte) 0xda, (byte) 0x13,
            (byte) 0x94, (byte) 0x12, (byte) 0x78, (byte) 0x14, (byte) 0x0b, (byte) 0x51,
            (byte) 0xc0, (byte) 0x45, (byte) 0xb4, (byte) 0x02, (byte) 0x37, (byte) 0x98,
            (byte) 0x42, (byte) 0x3c, (byte) 0xcb, (byte) 0x2e, (byte) 0xe4, (byte) 0x38,
            (byte) 0x69, (byte) 0x1b, (byte) 0x72, (byte) 0xf0, (byte) 0xaa, (byte) 0x89,
            (byte) 0x7e, (byte) 0xde, (byte) 0xb2
    };

    /*
     * The keys and certificates below are generated with:
     *
     * openssl req -new -x509 -days 3650 -extensions v3_ca -keyout cakey.pem -out cacert.pem
     * openssl dsaparam -out dsaparam.pem 1024
     * openssl req -newkey dsa:dsaparam.pem -keyout userkey.pem -nodes -days 3650 -out userkey.req
     * mkdir -p demoCA/newcerts
     * touch demoCA/index.txt
     * echo "01" > demoCA/serial
     * openssl ca -out usercert.pem -in userkey.req -cert cacert.pem -keyfile cakey.pem -days 3650
     */

    /**
     * Generated from above and converted with:
     *
     * openssl x509 -outform d -in cacert.pem | xxd -i | sed 's/0x/(byte) 0x/g'
     */
    private static final byte[] FAKE_DSA_CA_1 = new byte[] {
            (byte) 0x30, (byte) 0x82, (byte) 0x02, (byte) 0x8a, (byte) 0x30, (byte) 0x82,
            (byte) 0x01, (byte) 0xf3, (byte) 0xa0, (byte) 0x03, (byte) 0x02, (byte) 0x01,
            (byte) 0x02, (byte) 0x02, (byte) 0x09, (byte) 0x00, (byte) 0x87, (byte) 0xc0,
            (byte) 0x68, (byte) 0x7f, (byte) 0x42, (byte) 0x92, (byte) 0x0b, (byte) 0x7a,
            (byte) 0x30, (byte) 0x0d, (byte) 0x06, (byte) 0x09, (byte) 0x2a, (byte) 0x86,
            (byte) 0x48, (byte) 0x86, (byte) 0xf7, (byte) 0x0d, (byte) 0x01, (byte) 0x01,
            (byte) 0x05, (byte) 0x05, (byte) 0x00, (byte) 0x30, (byte) 0x5e, (byte) 0x31,
            (byte) 0x0b, (byte) 0x30, (byte) 0x09, (byte) 0x06, (byte) 0x03, (byte) 0x55,
            (byte) 0x04, (byte) 0x06, (byte) 0x13, (byte) 0x02, (byte) 0x41, (byte) 0x55,
            (byte) 0x31, (byte) 0x13, (byte) 0x30, (byte) 0x11, (byte) 0x06, (byte) 0x03,
            (byte) 0x55, (byte) 0x04, (byte) 0x08, (byte) 0x0c, (byte) 0x0a, (byte) 0x53,
            (byte) 0x6f, (byte) 0x6d, (byte) 0x65, (byte) 0x2d, (byte) 0x53, (byte) 0x74,
            (byte) 0x61, (byte) 0x74, (byte) 0x65, (byte) 0x31, (byte) 0x21, (byte) 0x30,
            (byte) 0x1f, (byte) 0x06, (byte) 0x03, (byte) 0x55, (byte) 0x04, (byte) 0x0a,
            (byte) 0x0c, (byte) 0x18, (byte) 0x49, (byte) 0x6e, (byte) 0x74, (byte) 0x65,
            (byte) 0x72, (byte) 0x6e, (byte) 0x65, (byte) 0x74, (byte) 0x20, (byte) 0x57,
            (byte) 0x69, (byte) 0x64, (byte) 0x67, (byte) 0x69, (byte) 0x74, (byte) 0x73,
            (byte) 0x20, (byte) 0x50, (byte) 0x74, (byte) 0x79, (byte) 0x20, (byte) 0x4c,
            (byte) 0x74, (byte) 0x64, (byte) 0x31, (byte) 0x17, (byte) 0x30, (byte) 0x15,
            (byte) 0x06, (byte) 0x03, (byte) 0x55, (byte) 0x04, (byte) 0x03, (byte) 0x0c,
            (byte) 0x0e, (byte) 0x63, (byte) 0x61, (byte) 0x2e, (byte) 0x65, (byte) 0x78,
            (byte) 0x61, (byte) 0x6d, (byte) 0x70, (byte) 0x6c, (byte) 0x65, (byte) 0x2e,
            (byte) 0x63, (byte) 0x6f, (byte) 0x6d, (byte) 0x30, (byte) 0x1e, (byte) 0x17,
            (byte) 0x0d, (byte) 0x31, (byte) 0x33, (byte) 0x30, (byte) 0x38, (byte) 0x32,
            (byte) 0x37, (byte) 0x32, (byte) 0x33, (byte) 0x33, (byte) 0x31, (byte) 0x32,
            (byte) 0x39, (byte) 0x5a, (byte) 0x17, (byte) 0x0d, (byte) 0x32, (byte) 0x33,
            (byte) 0x30, (byte) 0x38, (byte) 0x32, (byte) 0x35, (byte) 0x32, (byte) 0x33,
            (byte) 0x33, (byte) 0x31, (byte) 0x32, (byte) 0x39, (byte) 0x5a, (byte) 0x30,
            (byte) 0x5e, (byte) 0x31, (byte) 0x0b, (byte) 0x30, (byte) 0x09, (byte) 0x06,
            (byte) 0x03, (byte) 0x55, (byte) 0x04, (byte) 0x06, (byte) 0x13, (byte) 0x02,
            (byte) 0x41, (byte) 0x55, (byte) 0x31, (byte) 0x13, (byte) 0x30, (byte) 0x11,
            (byte) 0x06, (byte) 0x03, (byte) 0x55, (byte) 0x04, (byte) 0x08, (byte) 0x0c,
            (byte) 0x0a, (byte) 0x53, (byte) 0x6f, (byte) 0x6d, (byte) 0x65, (byte) 0x2d,
            (byte) 0x53, (byte) 0x74, (byte) 0x61, (byte) 0x74, (byte) 0x65, (byte) 0x31,
            (byte) 0x21, (byte) 0x30, (byte) 0x1f, (byte) 0x06, (byte) 0x03, (byte) 0x55,
            (byte) 0x04, (byte) 0x0a, (byte) 0x0c, (byte) 0x18, (byte) 0x49, (byte) 0x6e,
            (byte) 0x74, (byte) 0x65, (byte) 0x72, (byte) 0x6e, (byte) 0x65, (byte) 0x74,
            (byte) 0x20, (byte) 0x57, (byte) 0x69, (byte) 0x64, (byte) 0x67, (byte) 0x69,
            (byte) 0x74, (byte) 0x73, (byte) 0x20, (byte) 0x50, (byte) 0x74, (byte) 0x79,
            (byte) 0x20, (byte) 0x4c, (byte) 0x74, (byte) 0x64, (byte) 0x31, (byte) 0x17,
            (byte) 0x30, (byte) 0x15, (byte) 0x06, (byte) 0x03, (byte) 0x55, (byte) 0x04,
            (byte) 0x03, (byte) 0x0c, (byte) 0x0e, (byte) 0x63, (byte) 0x61, (byte) 0x2e,
            (byte) 0x65, (byte) 0x78, (byte) 0x61, (byte) 0x6d, (byte) 0x70, (byte) 0x6c,
            (byte) 0x65, (byte) 0x2e, (byte) 0x63, (byte) 0x6f, (byte) 0x6d, (byte) 0x30,
            (byte) 0x81, (byte) 0x9f, (byte) 0x30, (byte) 0x0d, (byte) 0x06, (byte) 0x09,
            (byte) 0x2a, (byte) 0x86, (byte) 0x48, (byte) 0x86, (byte) 0xf7, (byte) 0x0d,
            (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x05, (byte) 0x00, (byte) 0x03,
            (byte) 0x81, (byte) 0x8d, (byte) 0x00, (byte) 0x30, (byte) 0x81, (byte) 0x89,
            (byte) 0x02, (byte) 0x81, (byte) 0x81, (byte) 0x00, (byte) 0xa4, (byte) 0xc7,
            (byte) 0x06, (byte) 0xba, (byte) 0xdf, (byte) 0x2b, (byte) 0xee, (byte) 0xd2,
            (byte) 0xb9, (byte) 0xe4, (byte) 0x52, (byte) 0x21, (byte) 0x68, (byte) 0x2b,
            (byte) 0x83, (byte) 0xdf, (byte) 0xe3, (byte) 0x9c, (byte) 0x08, (byte) 0x73,
            (byte) 0xdd, (byte) 0x90, (byte) 0xea, (byte) 0x97, (byte) 0x0c, (byte) 0x96,
            (byte) 0x20, (byte) 0xb1, (byte) 0xee, (byte) 0x11, (byte) 0xd5, (byte) 0xd4,
            (byte) 0x7c, (byte) 0x44, (byte) 0x96, (byte) 0x2e, (byte) 0x6e, (byte) 0xa2,
            (byte) 0xb2, (byte) 0xa3, (byte) 0x4b, (byte) 0x0f, (byte) 0x32, (byte) 0x90,
            (byte) 0xaf, (byte) 0x5c, (byte) 0x6f, (byte) 0x00, (byte) 0x88, (byte) 0x45,
            (byte) 0x4e, (byte) 0x9b, (byte) 0x26, (byte) 0xc1, (byte) 0x94, (byte) 0x3c,
            (byte) 0xfe, (byte) 0x10, (byte) 0xbd, (byte) 0xda, (byte) 0xf2, (byte) 0x8d,
            (byte) 0x03, (byte) 0x52, (byte) 0x32, (byte) 0x11, (byte) 0xff, (byte) 0xf6,
            (byte) 0xf9, (byte) 0x6e, (byte) 0x8f, (byte) 0x0f, (byte) 0xc8, (byte) 0x0a,
            (byte) 0x48, (byte) 0x39, (byte) 0x33, (byte) 0xb9, (byte) 0x0c, (byte) 0xb3,
            (byte) 0x2b, (byte) 0xab, (byte) 0x7d, (byte) 0x79, (byte) 0x6f, (byte) 0x57,
            (byte) 0x5b, (byte) 0xb8, (byte) 0x84, (byte) 0xb6, (byte) 0xcc, (byte) 0xe8,
            (byte) 0x30, (byte) 0x78, (byte) 0xff, (byte) 0x92, (byte) 0xe5, (byte) 0x43,
            (byte) 0x2e, (byte) 0xef, (byte) 0x66, (byte) 0x98, (byte) 0xb4, (byte) 0xfe,
            (byte) 0xa2, (byte) 0x40, (byte) 0xf2, (byte) 0x1f, (byte) 0xd0, (byte) 0x86,
            (byte) 0x16, (byte) 0xc8, (byte) 0x45, (byte) 0xc4, (byte) 0x52, (byte) 0xcb,
            (byte) 0x31, (byte) 0x5c, (byte) 0x9f, (byte) 0x32, (byte) 0x3b, (byte) 0xf7,
            (byte) 0x19, (byte) 0x08, (byte) 0xc7, (byte) 0x00, (byte) 0x21, (byte) 0x7d,
            (byte) 0x02, (byte) 0x03, (byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0xa3,
            (byte) 0x50, (byte) 0x30, (byte) 0x4e, (byte) 0x30, (byte) 0x1d, (byte) 0x06,
            (byte) 0x03, (byte) 0x55, (byte) 0x1d, (byte) 0x0e, (byte) 0x04, (byte) 0x16,
            (byte) 0x04, (byte) 0x14, (byte) 0x47, (byte) 0x82, (byte) 0xa3, (byte) 0xf1,
            (byte) 0xc2, (byte) 0x7e, (byte) 0x3a, (byte) 0xde, (byte) 0x4f, (byte) 0x30,
            (byte) 0x4c, (byte) 0x7f, (byte) 0x72, (byte) 0x81, (byte) 0x15, (byte) 0x32,
            (byte) 0xda, (byte) 0x7f, (byte) 0x58, (byte) 0x18, (byte) 0x30, (byte) 0x1f,
            (byte) 0x06, (byte) 0x03, (byte) 0x55, (byte) 0x1d, (byte) 0x23, (byte) 0x04,
            (byte) 0x18, (byte) 0x30, (byte) 0x16, (byte) 0x80, (byte) 0x14, (byte) 0x47,
            (byte) 0x82, (byte) 0xa3, (byte) 0xf1, (byte) 0xc2, (byte) 0x7e, (byte) 0x3a,
            (byte) 0xde, (byte) 0x4f, (byte) 0x30, (byte) 0x4c, (byte) 0x7f, (byte) 0x72,
            (byte) 0x81, (byte) 0x15, (byte) 0x32, (byte) 0xda, (byte) 0x7f, (byte) 0x58,
            (byte) 0x18, (byte) 0x30, (byte) 0x0c, (byte) 0x06, (byte) 0x03, (byte) 0x55,
            (byte) 0x1d, (byte) 0x13, (byte) 0x04, (byte) 0x05, (byte) 0x30, (byte) 0x03,
            (byte) 0x01, (byte) 0x01, (byte) 0xff, (byte) 0x30, (byte) 0x0d, (byte) 0x06,
            (byte) 0x09, (byte) 0x2a, (byte) 0x86, (byte) 0x48, (byte) 0x86, (byte) 0xf7,
            (byte) 0x0d, (byte) 0x01, (byte) 0x01, (byte) 0x05, (byte) 0x05, (byte) 0x00,
            (byte) 0x03, (byte) 0x81, (byte) 0x81, (byte) 0x00, (byte) 0x08, (byte) 0x7f,
            (byte) 0x6a, (byte) 0x48, (byte) 0x90, (byte) 0x7b, (byte) 0x9b, (byte) 0x72,
            (byte) 0x13, (byte) 0xa7, (byte) 0xef, (byte) 0x6b, (byte) 0x0b, (byte) 0x59,
            (byte) 0xe5, (byte) 0x49, (byte) 0x72, (byte) 0x3a, (byte) 0xc8, (byte) 0x84,
            (byte) 0xcc, (byte) 0x23, (byte) 0x18, (byte) 0x4c, (byte) 0xec, (byte) 0xc7,
            (byte) 0xef, (byte) 0xcb, (byte) 0xa7, (byte) 0xbe, (byte) 0xe4, (byte) 0xef,
            (byte) 0x8f, (byte) 0xc6, (byte) 0x06, (byte) 0x8c, (byte) 0xc0, (byte) 0xe4,
            (byte) 0x2f, (byte) 0x2a, (byte) 0xc0, (byte) 0x35, (byte) 0x7d, (byte) 0x5e,
            (byte) 0x19, (byte) 0x29, (byte) 0x8c, (byte) 0xb9, (byte) 0xf1, (byte) 0x1e,
            (byte) 0xaf, (byte) 0x82, (byte) 0xd8, (byte) 0xe3, (byte) 0x88, (byte) 0xe1,
            (byte) 0x31, (byte) 0xc8, (byte) 0x82, (byte) 0x1f, (byte) 0x83, (byte) 0xa9,
            (byte) 0xde, (byte) 0xfe, (byte) 0x4b, (byte) 0xe2, (byte) 0x78, (byte) 0x64,
            (byte) 0xed, (byte) 0xa4, (byte) 0x7b, (byte) 0xee, (byte) 0x8d, (byte) 0x71,
            (byte) 0x1b, (byte) 0x44, (byte) 0xe6, (byte) 0xb7, (byte) 0xe8, (byte) 0xc5,
            (byte) 0x9a, (byte) 0x93, (byte) 0x92, (byte) 0x6f, (byte) 0x6f, (byte) 0xdb,
            (byte) 0xbd, (byte) 0xd7, (byte) 0x03, (byte) 0x85, (byte) 0xa9, (byte) 0x5f,
            (byte) 0x53, (byte) 0x5f, (byte) 0x5d, (byte) 0x30, (byte) 0xc6, (byte) 0xd9,
            (byte) 0xce, (byte) 0x34, (byte) 0xa8, (byte) 0xbe, (byte) 0x31, (byte) 0x47,
            (byte) 0x1c, (byte) 0xa4, (byte) 0x7f, (byte) 0xc0, (byte) 0x2c, (byte) 0xbc,
            (byte) 0xfe, (byte) 0x1a, (byte) 0x31, (byte) 0xd8, (byte) 0x77, (byte) 0x4d,
            (byte) 0xfc, (byte) 0x45, (byte) 0x84, (byte) 0xfc, (byte) 0x45, (byte) 0x12,
            (byte) 0xab, (byte) 0x50, (byte) 0xe4, (byte) 0x45, (byte) 0xe5, (byte) 0x11
    };

    /**
     * Generated from above and converted with: openssl pkcs8 -topk8 -outform d
     * -in userkey.pem -nocrypt | xxd -i | sed 's/0x/(byte) 0x/g'
     */
    private static final byte[] FAKE_DSA_KEY_1 = new byte[] {
            (byte) 0x30, (byte) 0x82, (byte) 0x01, (byte) 0x4c, (byte) 0x02, (byte) 0x01,
            (byte) 0x00, (byte) 0x30, (byte) 0x82, (byte) 0x01, (byte) 0x2c, (byte) 0x06,
            (byte) 0x07, (byte) 0x2a, (byte) 0x86, (byte) 0x48, (byte) 0xce, (byte) 0x38,
            (byte) 0x04, (byte) 0x01, (byte) 0x30, (byte) 0x82, (byte) 0x01, (byte) 0x1f,
            (byte) 0x02, (byte) 0x81, (byte) 0x81, (byte) 0x00, (byte) 0xb3, (byte) 0x23,
            (byte) 0xf7, (byte) 0x86, (byte) 0xbd, (byte) 0x3b, (byte) 0x86, (byte) 0xcc,
            (byte) 0xc3, (byte) 0x91, (byte) 0xc0, (byte) 0x30, (byte) 0x32, (byte) 0x02,
            (byte) 0x47, (byte) 0x35, (byte) 0x01, (byte) 0xef, (byte) 0xee, (byte) 0x98,
            (byte) 0x13, (byte) 0x56, (byte) 0x49, (byte) 0x47, (byte) 0xb5, (byte) 0x20,
            (byte) 0xa8, (byte) 0x60, (byte) 0xcb, (byte) 0xc0, (byte) 0xd5, (byte) 0x77,
            (byte) 0xc1, (byte) 0x69, (byte) 0xcd, (byte) 0x18, (byte) 0x34, (byte) 0x92,
            (byte) 0xf2, (byte) 0x6a, (byte) 0x2a, (byte) 0x10, (byte) 0x59, (byte) 0x1c,
            (byte) 0x91, (byte) 0x20, (byte) 0x51, (byte) 0xca, (byte) 0x37, (byte) 0xb2,
            (byte) 0x87, (byte) 0xa6, (byte) 0x8a, (byte) 0x02, (byte) 0xfd, (byte) 0x45,
            (byte) 0x46, (byte) 0xf9, (byte) 0x76, (byte) 0xb1, (byte) 0x35, (byte) 0x38,
            (byte) 0x8d, (byte) 0xff, (byte) 0x4c, (byte) 0x5d, (byte) 0x75, (byte) 0x8f,
            (byte) 0x66, (byte) 0x15, (byte) 0x7d, (byte) 0x7b, (byte) 0xda, (byte) 0xdb,
            (byte) 0x57, (byte) 0x39, (byte) 0xff, (byte) 0x91, (byte) 0x3f, (byte) 0xdd,
            (byte) 0xe2, (byte) 0xb4, (byte) 0x22, (byte) 0x60, (byte) 0x4c, (byte) 0x32,
            (byte) 0x3b, (byte) 0x9d, (byte) 0x34, (byte) 0x9f, (byte) 0xb9, (byte) 0x5d,
            (byte) 0x75, (byte) 0xb9, (byte) 0xd3, (byte) 0x7f, (byte) 0x11, (byte) 0xba,
            (byte) 0xb7, (byte) 0xc8, (byte) 0x32, (byte) 0xc6, (byte) 0xce, (byte) 0x71,
            (byte) 0x91, (byte) 0xd3, (byte) 0x32, (byte) 0xaf, (byte) 0x4d, (byte) 0x7e,
            (byte) 0x7c, (byte) 0x15, (byte) 0xf7, (byte) 0x71, (byte) 0x2c, (byte) 0x52,
            (byte) 0x65, (byte) 0x4d, (byte) 0xa9, (byte) 0x81, (byte) 0x25, (byte) 0x35,
            (byte) 0xce, (byte) 0x0b, (byte) 0x5b, (byte) 0x56, (byte) 0xfe, (byte) 0xf1,
            (byte) 0x02, (byte) 0x15, (byte) 0x00, (byte) 0xeb, (byte) 0x4e, (byte) 0x7f,
            (byte) 0x7a, (byte) 0x31, (byte) 0xb3, (byte) 0x7d, (byte) 0x8d, (byte) 0xb2,
            (byte) 0xf7, (byte) 0xaf, (byte) 0xad, (byte) 0xb1, (byte) 0x42, (byte) 0x92,
            (byte) 0xf3, (byte) 0x6c, (byte) 0xe4, (byte) 0xed, (byte) 0x8b, (byte) 0x02,
            (byte) 0x81, (byte) 0x81, (byte) 0x00, (byte) 0x81, (byte) 0xc8, (byte) 0x36,
            (byte) 0x48, (byte) 0xdb, (byte) 0x71, (byte) 0x2b, (byte) 0x91, (byte) 0xce,
            (byte) 0x6d, (byte) 0xbc, (byte) 0xb8, (byte) 0xf9, (byte) 0xcb, (byte) 0x50,
            (byte) 0x91, (byte) 0x10, (byte) 0x8a, (byte) 0xf8, (byte) 0x37, (byte) 0x50,
            (byte) 0xda, (byte) 0x4f, (byte) 0xc8, (byte) 0x4d, (byte) 0x73, (byte) 0xcb,
            (byte) 0x4d, (byte) 0xb0, (byte) 0x19, (byte) 0x54, (byte) 0x5a, (byte) 0xf3,
            (byte) 0x6c, (byte) 0xc9, (byte) 0xd8, (byte) 0x96, (byte) 0xd9, (byte) 0xb0,
            (byte) 0x54, (byte) 0x7e, (byte) 0x7d, (byte) 0xe2, (byte) 0x58, (byte) 0x0e,
            (byte) 0x5f, (byte) 0xc0, (byte) 0xce, (byte) 0xb9, (byte) 0x5c, (byte) 0xe3,
            (byte) 0xd3, (byte) 0xdf, (byte) 0xcf, (byte) 0x45, (byte) 0x74, (byte) 0xfb,
            (byte) 0xe6, (byte) 0x20, (byte) 0xe7, (byte) 0xfc, (byte) 0x0f, (byte) 0xca,
            (byte) 0xdb, (byte) 0xc0, (byte) 0x0b, (byte) 0xe1, (byte) 0x5a, (byte) 0x16,
            (byte) 0x1d, (byte) 0xb3, (byte) 0x2e, (byte) 0xe5, (byte) 0x5f, (byte) 0x89,
            (byte) 0x17, (byte) 0x73, (byte) 0x50, (byte) 0xd1, (byte) 0x4a, (byte) 0x60,
            (byte) 0xb7, (byte) 0xaa, (byte) 0xf0, (byte) 0xc7, (byte) 0xc5, (byte) 0x03,
            (byte) 0x4e, (byte) 0x36, (byte) 0x51, (byte) 0x9e, (byte) 0x2f, (byte) 0xfa,
            (byte) 0xf3, (byte) 0xd6, (byte) 0x58, (byte) 0x14, (byte) 0x02, (byte) 0xb4,
            (byte) 0x41, (byte) 0xd6, (byte) 0x72, (byte) 0x6f, (byte) 0x58, (byte) 0x5b,
            (byte) 0x2d, (byte) 0x23, (byte) 0xc0, (byte) 0x75, (byte) 0x4f, (byte) 0x39,
            (byte) 0xa8, (byte) 0x6a, (byte) 0xdf, (byte) 0x79, (byte) 0x21, (byte) 0xf2,
            (byte) 0x77, (byte) 0x91, (byte) 0x3f, (byte) 0x1c, (byte) 0x4d, (byte) 0x48,
            (byte) 0x78, (byte) 0xcd, (byte) 0xed, (byte) 0x79, (byte) 0x23, (byte) 0x04,
            (byte) 0x17, (byte) 0x02, (byte) 0x15, (byte) 0x00, (byte) 0xc7, (byte) 0xe7,
            (byte) 0xe2, (byte) 0x6b, (byte) 0x14, (byte) 0xe6, (byte) 0x31, (byte) 0x12,
            (byte) 0xb2, (byte) 0x1e, (byte) 0xd4, (byte) 0xf2, (byte) 0x9b, (byte) 0x2c,
            (byte) 0xf6, (byte) 0x54, (byte) 0x4c, (byte) 0x12, (byte) 0xe8, (byte) 0x22
    };

    /**
     * Generated from above and converted with: openssl x509 -outform d -in
     * usercert.pem | xxd -i | sed 's/0x/(byte) 0x/g'
     */
    private static final byte[] FAKE_DSA_USER_1 = new byte[] {
            (byte) 0x30, (byte) 0x82, (byte) 0x03, (byte) 0xca, (byte) 0x30, (byte) 0x82,
            (byte) 0x03, (byte) 0x33, (byte) 0xa0, (byte) 0x03, (byte) 0x02, (byte) 0x01,
            (byte) 0x02, (byte) 0x02, (byte) 0x01, (byte) 0x01, (byte) 0x30, (byte) 0x0d,
            (byte) 0x06, (byte) 0x09, (byte) 0x2a, (byte) 0x86, (byte) 0x48, (byte) 0x86,
            (byte) 0xf7, (byte) 0x0d, (byte) 0x01, (byte) 0x01, (byte) 0x05, (byte) 0x05,
            (byte) 0x00, (byte) 0x30, (byte) 0x5e, (byte) 0x31, (byte) 0x0b, (byte) 0x30,
            (byte) 0x09, (byte) 0x06, (byte) 0x03, (byte) 0x55, (byte) 0x04, (byte) 0x06,
            (byte) 0x13, (byte) 0x02, (byte) 0x41, (byte) 0x55, (byte) 0x31, (byte) 0x13,
            (byte) 0x30, (byte) 0x11, (byte) 0x06, (byte) 0x03, (byte) 0x55, (byte) 0x04,
            (byte) 0x08, (byte) 0x0c, (byte) 0x0a, (byte) 0x53, (byte) 0x6f, (byte) 0x6d,
            (byte) 0x65, (byte) 0x2d, (byte) 0x53, (byte) 0x74, (byte) 0x61, (byte) 0x74,
            (byte) 0x65, (byte) 0x31, (byte) 0x21, (byte) 0x30, (byte) 0x1f, (byte) 0x06,
            (byte) 0x03, (byte) 0x55, (byte) 0x04, (byte) 0x0a, (byte) 0x0c, (byte) 0x18,
            (byte) 0x49, (byte) 0x6e, (byte) 0x74, (byte) 0x65, (byte) 0x72, (byte) 0x6e,
            (byte) 0x65, (byte) 0x74, (byte) 0x20, (byte) 0x57, (byte) 0x69, (byte) 0x64,
            (byte) 0x67, (byte) 0x69, (byte) 0x74, (byte) 0x73, (byte) 0x20, (byte) 0x50,
            (byte) 0x74, (byte) 0x79, (byte) 0x20, (byte) 0x4c, (byte) 0x74, (byte) 0x64,
            (byte) 0x31, (byte) 0x17, (byte) 0x30, (byte) 0x15, (byte) 0x06, (byte) 0x03,
            (byte) 0x55, (byte) 0x04, (byte) 0x03, (byte) 0x0c, (byte) 0x0e, (byte) 0x63,
            (byte) 0x61, (byte) 0x2e, (byte) 0x65, (byte) 0x78, (byte) 0x61, (byte) 0x6d,
            (byte) 0x70, (byte) 0x6c, (byte) 0x65, (byte) 0x2e, (byte) 0x63, (byte) 0x6f,
            (byte) 0x6d, (byte) 0x30, (byte) 0x1e, (byte) 0x17, (byte) 0x0d, (byte) 0x31,
            (byte) 0x33, (byte) 0x30, (byte) 0x38, (byte) 0x32, (byte) 0x37, (byte) 0x32,
            (byte) 0x33, (byte) 0x33, (byte) 0x34, (byte) 0x32, (byte) 0x32, (byte) 0x5a,
            (byte) 0x17, (byte) 0x0d, (byte) 0x32, (byte) 0x33, (byte) 0x30, (byte) 0x38,
            (byte) 0x32, (byte) 0x35, (byte) 0x32, (byte) 0x33, (byte) 0x33, (byte) 0x34,
            (byte) 0x32, (byte) 0x32, (byte) 0x5a, (byte) 0x30, (byte) 0x62, (byte) 0x31,
            (byte) 0x0b, (byte) 0x30, (byte) 0x09, (byte) 0x06, (byte) 0x03, (byte) 0x55,
            (byte) 0x04, (byte) 0x06, (byte) 0x13, (byte) 0x02, (byte) 0x41, (byte) 0x55,
            (byte) 0x31, (byte) 0x13, (byte) 0x30, (byte) 0x11, (byte) 0x06, (byte) 0x03,
            (byte) 0x55, (byte) 0x04, (byte) 0x08, (byte) 0x0c, (byte) 0x0a, (byte) 0x53,
            (byte) 0x6f, (byte) 0x6d, (byte) 0x65, (byte) 0x2d, (byte) 0x53, (byte) 0x74,
            (byte) 0x61, (byte) 0x74, (byte) 0x65, (byte) 0x31, (byte) 0x21, (byte) 0x30,
            (byte) 0x1f, (byte) 0x06, (byte) 0x03, (byte) 0x55, (byte) 0x04, (byte) 0x0a,
            (byte) 0x0c, (byte) 0x18, (byte) 0x49, (byte) 0x6e, (byte) 0x74, (byte) 0x65,
            (byte) 0x72, (byte) 0x6e, (byte) 0x65, (byte) 0x74, (byte) 0x20, (byte) 0x57,
            (byte) 0x69, (byte) 0x64, (byte) 0x67, (byte) 0x69, (byte) 0x74, (byte) 0x73,
            (byte) 0x20, (byte) 0x50, (byte) 0x74, (byte) 0x79, (byte) 0x20, (byte) 0x4c,
            (byte) 0x74, (byte) 0x64, (byte) 0x31, (byte) 0x1b, (byte) 0x30, (byte) 0x19,
            (byte) 0x06, (byte) 0x03, (byte) 0x55, (byte) 0x04, (byte) 0x03, (byte) 0x0c,
            (byte) 0x12, (byte) 0x73, (byte) 0x65, (byte) 0x72, (byte) 0x76, (byte) 0x65,
            (byte) 0x72, (byte) 0x2e, (byte) 0x65, (byte) 0x78, (byte) 0x61, (byte) 0x6d,
            (byte) 0x70, (byte) 0x6c, (byte) 0x65, (byte) 0x2e, (byte) 0x63, (byte) 0x6f,
            (byte) 0x6d, (byte) 0x30, (byte) 0x82, (byte) 0x01, (byte) 0xb7, (byte) 0x30,
            (byte) 0x82, (byte) 0x01, (byte) 0x2c, (byte) 0x06, (byte) 0x07, (byte) 0x2a,
            (byte) 0x86, (byte) 0x48, (byte) 0xce, (byte) 0x38, (byte) 0x04, (byte) 0x01,
            (byte) 0x30, (byte) 0x82, (byte) 0x01, (byte) 0x1f, (byte) 0x02, (byte) 0x81,
            (byte) 0x81, (byte) 0x00, (byte) 0xb3, (byte) 0x23, (byte) 0xf7, (byte) 0x86,
            (byte) 0xbd, (byte) 0x3b, (byte) 0x86, (byte) 0xcc, (byte) 0xc3, (byte) 0x91,
            (byte) 0xc0, (byte) 0x30, (byte) 0x32, (byte) 0x02, (byte) 0x47, (byte) 0x35,
            (byte) 0x01, (byte) 0xef, (byte) 0xee, (byte) 0x98, (byte) 0x13, (byte) 0x56,
            (byte) 0x49, (byte) 0x47, (byte) 0xb5, (byte) 0x20, (byte) 0xa8, (byte) 0x60,
            (byte) 0xcb, (byte) 0xc0, (byte) 0xd5, (byte) 0x77, (byte) 0xc1, (byte) 0x69,
            (byte) 0xcd, (byte) 0x18, (byte) 0x34, (byte) 0x92, (byte) 0xf2, (byte) 0x6a,
            (byte) 0x2a, (byte) 0x10, (byte) 0x59, (byte) 0x1c, (byte) 0x91, (byte) 0x20,
            (byte) 0x51, (byte) 0xca, (byte) 0x37, (byte) 0xb2, (byte) 0x87, (byte) 0xa6,
            (byte) 0x8a, (byte) 0x02, (byte) 0xfd, (byte) 0x45, (byte) 0x46, (byte) 0xf9,
            (byte) 0x76, (byte) 0xb1, (byte) 0x35, (byte) 0x38, (byte) 0x8d, (byte) 0xff,
            (byte) 0x4c, (byte) 0x5d, (byte) 0x75, (byte) 0x8f, (byte) 0x66, (byte) 0x15,
            (byte) 0x7d, (byte) 0x7b, (byte) 0xda, (byte) 0xdb, (byte) 0x57, (byte) 0x39,
            (byte) 0xff, (byte) 0x91, (byte) 0x3f, (byte) 0xdd, (byte) 0xe2, (byte) 0xb4,
            (byte) 0x22, (byte) 0x60, (byte) 0x4c, (byte) 0x32, (byte) 0x3b, (byte) 0x9d,
            (byte) 0x34, (byte) 0x9f, (byte) 0xb9, (byte) 0x5d, (byte) 0x75, (byte) 0xb9,
            (byte) 0xd3, (byte) 0x7f, (byte) 0x11, (byte) 0xba, (byte) 0xb7, (byte) 0xc8,
            (byte) 0x32, (byte) 0xc6, (byte) 0xce, (byte) 0x71, (byte) 0x91, (byte) 0xd3,
            (byte) 0x32, (byte) 0xaf, (byte) 0x4d, (byte) 0x7e, (byte) 0x7c, (byte) 0x15,
            (byte) 0xf7, (byte) 0x71, (byte) 0x2c, (byte) 0x52, (byte) 0x65, (byte) 0x4d,
            (byte) 0xa9, (byte) 0x81, (byte) 0x25, (byte) 0x35, (byte) 0xce, (byte) 0x0b,
            (byte) 0x5b, (byte) 0x56, (byte) 0xfe, (byte) 0xf1, (byte) 0x02, (byte) 0x15,
            (byte) 0x00, (byte) 0xeb, (byte) 0x4e, (byte) 0x7f, (byte) 0x7a, (byte) 0x31,
            (byte) 0xb3, (byte) 0x7d, (byte) 0x8d, (byte) 0xb2, (byte) 0xf7, (byte) 0xaf,
            (byte) 0xad, (byte) 0xb1, (byte) 0x42, (byte) 0x92, (byte) 0xf3, (byte) 0x6c,
            (byte) 0xe4, (byte) 0xed, (byte) 0x8b, (byte) 0x02, (byte) 0x81, (byte) 0x81,
            (byte) 0x00, (byte) 0x81, (byte) 0xc8, (byte) 0x36, (byte) 0x48, (byte) 0xdb,
            (byte) 0x71, (byte) 0x2b, (byte) 0x91, (byte) 0xce, (byte) 0x6d, (byte) 0xbc,
            (byte) 0xb8, (byte) 0xf9, (byte) 0xcb, (byte) 0x50, (byte) 0x91, (byte) 0x10,
            (byte) 0x8a, (byte) 0xf8, (byte) 0x37, (byte) 0x50, (byte) 0xda, (byte) 0x4f,
            (byte) 0xc8, (byte) 0x4d, (byte) 0x73, (byte) 0xcb, (byte) 0x4d, (byte) 0xb0,
            (byte) 0x19, (byte) 0x54, (byte) 0x5a, (byte) 0xf3, (byte) 0x6c, (byte) 0xc9,
            (byte) 0xd8, (byte) 0x96, (byte) 0xd9, (byte) 0xb0, (byte) 0x54, (byte) 0x7e,
            (byte) 0x7d, (byte) 0xe2, (byte) 0x58, (byte) 0x0e, (byte) 0x5f, (byte) 0xc0,
            (byte) 0xce, (byte) 0xb9, (byte) 0x5c, (byte) 0xe3, (byte) 0xd3, (byte) 0xdf,
            (byte) 0xcf, (byte) 0x45, (byte) 0x74, (byte) 0xfb, (byte) 0xe6, (byte) 0x20,
            (byte) 0xe7, (byte) 0xfc, (byte) 0x0f, (byte) 0xca, (byte) 0xdb, (byte) 0xc0,
            (byte) 0x0b, (byte) 0xe1, (byte) 0x5a, (byte) 0x16, (byte) 0x1d, (byte) 0xb3,
            (byte) 0x2e, (byte) 0xe5, (byte) 0x5f, (byte) 0x89, (byte) 0x17, (byte) 0x73,
            (byte) 0x50, (byte) 0xd1, (byte) 0x4a, (byte) 0x60, (byte) 0xb7, (byte) 0xaa,
            (byte) 0xf0, (byte) 0xc7, (byte) 0xc5, (byte) 0x03, (byte) 0x4e, (byte) 0x36,
            (byte) 0x51, (byte) 0x9e, (byte) 0x2f, (byte) 0xfa, (byte) 0xf3, (byte) 0xd6,
            (byte) 0x58, (byte) 0x14, (byte) 0x02, (byte) 0xb4, (byte) 0x41, (byte) 0xd6,
            (byte) 0x72, (byte) 0x6f, (byte) 0x58, (byte) 0x5b, (byte) 0x2d, (byte) 0x23,
            (byte) 0xc0, (byte) 0x75, (byte) 0x4f, (byte) 0x39, (byte) 0xa8, (byte) 0x6a,
            (byte) 0xdf, (byte) 0x79, (byte) 0x21, (byte) 0xf2, (byte) 0x77, (byte) 0x91,
            (byte) 0x3f, (byte) 0x1c, (byte) 0x4d, (byte) 0x48, (byte) 0x78, (byte) 0xcd,
            (byte) 0xed, (byte) 0x79, (byte) 0x23, (byte) 0x03, (byte) 0x81, (byte) 0x84,
            (byte) 0x00, (byte) 0x02, (byte) 0x81, (byte) 0x80, (byte) 0x1a, (byte) 0x50,
            (byte) 0x9d, (byte) 0x3e, (byte) 0xa1, (byte) 0x6c, (byte) 0x99, (byte) 0x35,
            (byte) 0x36, (byte) 0x26, (byte) 0x22, (byte) 0x6b, (byte) 0x47, (byte) 0x45,
            (byte) 0x80, (byte) 0x5b, (byte) 0xd5, (byte) 0xc1, (byte) 0xc5, (byte) 0x70,
            (byte) 0x75, (byte) 0x55, (byte) 0x66, (byte) 0x33, (byte) 0x1d, (byte) 0xae,
            (byte) 0xd0, (byte) 0x01, (byte) 0x64, (byte) 0x8b, (byte) 0xae, (byte) 0x9d,
            (byte) 0x66, (byte) 0x58, (byte) 0xf9, (byte) 0x42, (byte) 0x74, (byte) 0x3a,
            (byte) 0x32, (byte) 0xc7, (byte) 0x7f, (byte) 0x25, (byte) 0x64, (byte) 0x7d,
            (byte) 0x08, (byte) 0x26, (byte) 0xbf, (byte) 0x21, (byte) 0x3a, (byte) 0x84,
            (byte) 0xcc, (byte) 0x2c, (byte) 0x66, (byte) 0x7d, (byte) 0xc7, (byte) 0xd6,
            (byte) 0xb1, (byte) 0x69, (byte) 0x57, (byte) 0x67, (byte) 0x52, (byte) 0x73,
            (byte) 0x3f, (byte) 0x79, (byte) 0x60, (byte) 0xaa, (byte) 0xf4, (byte) 0x8a,
            (byte) 0x48, (byte) 0x42, (byte) 0x46, (byte) 0x41, (byte) 0xd0, (byte) 0x50,
            (byte) 0x9b, (byte) 0xa2, (byte) 0x4e, (byte) 0xa5, (byte) 0x88, (byte) 0x10,
            (byte) 0xf7, (byte) 0x61, (byte) 0xa2, (byte) 0xfa, (byte) 0x8d, (byte) 0xa6,
            (byte) 0x13, (byte) 0x9e, (byte) 0x36, (byte) 0x86, (byte) 0x62, (byte) 0xf0,
            (byte) 0x97, (byte) 0xef, (byte) 0x11, (byte) 0xc6, (byte) 0x35, (byte) 0xd3,
            (byte) 0x79, (byte) 0x30, (byte) 0xde, (byte) 0xf2, (byte) 0x7f, (byte) 0x7a,
            (byte) 0x3c, (byte) 0x03, (byte) 0xa3, (byte) 0xc5, (byte) 0xbc, (byte) 0xb1,
            (byte) 0xbc, (byte) 0x2f, (byte) 0x10, (byte) 0xf4, (byte) 0x51, (byte) 0x89,
            (byte) 0xe2, (byte) 0xaf, (byte) 0xf7, (byte) 0x61, (byte) 0x1a, (byte) 0xf0,
            (byte) 0x87, (byte) 0x5e, (byte) 0xa5, (byte) 0x02, (byte) 0xd2, (byte) 0xe4,
            (byte) 0xa3, (byte) 0x7b, (byte) 0x30, (byte) 0x79, (byte) 0x30, (byte) 0x09,
            (byte) 0x06, (byte) 0x03, (byte) 0x55, (byte) 0x1d, (byte) 0x13, (byte) 0x04,
            (byte) 0x02, (byte) 0x30, (byte) 0x00, (byte) 0x30, (byte) 0x2c, (byte) 0x06,
            (byte) 0x09, (byte) 0x60, (byte) 0x86, (byte) 0x48, (byte) 0x01, (byte) 0x86,
            (byte) 0xf8, (byte) 0x42, (byte) 0x01, (byte) 0x0d, (byte) 0x04, (byte) 0x1f,
            (byte) 0x16, (byte) 0x1d, (byte) 0x4f, (byte) 0x70, (byte) 0x65, (byte) 0x6e,
            (byte) 0x53, (byte) 0x53, (byte) 0x4c, (byte) 0x20, (byte) 0x47, (byte) 0x65,
            (byte) 0x6e, (byte) 0x65, (byte) 0x72, (byte) 0x61, (byte) 0x74, (byte) 0x65,
            (byte) 0x64, (byte) 0x20, (byte) 0x43, (byte) 0x65, (byte) 0x72, (byte) 0x74,
            (byte) 0x69, (byte) 0x66, (byte) 0x69, (byte) 0x63, (byte) 0x61, (byte) 0x74,
            (byte) 0x65, (byte) 0x30, (byte) 0x1d, (byte) 0x06, (byte) 0x03, (byte) 0x55,
            (byte) 0x1d, (byte) 0x0e, (byte) 0x04, (byte) 0x16, (byte) 0x04, (byte) 0x14,
            (byte) 0xd1, (byte) 0x6c, (byte) 0x36, (byte) 0x36, (byte) 0x61, (byte) 0x6c,
            (byte) 0xf6, (byte) 0x90, (byte) 0x82, (byte) 0x82, (byte) 0x87, (byte) 0x93,
            (byte) 0xbe, (byte) 0x99, (byte) 0x60, (byte) 0x1b, (byte) 0x03, (byte) 0x58,
            (byte) 0x36, (byte) 0x63, (byte) 0x30, (byte) 0x1f, (byte) 0x06, (byte) 0x03,
            (byte) 0x55, (byte) 0x1d, (byte) 0x23, (byte) 0x04, (byte) 0x18, (byte) 0x30,
            (byte) 0x16, (byte) 0x80, (byte) 0x14, (byte) 0x47, (byte) 0x82, (byte) 0xa3,
            (byte) 0xf1, (byte) 0xc2, (byte) 0x7e, (byte) 0x3a, (byte) 0xde, (byte) 0x4f,
            (byte) 0x30, (byte) 0x4c, (byte) 0x7f, (byte) 0x72, (byte) 0x81, (byte) 0x15,
            (byte) 0x32, (byte) 0xda, (byte) 0x7f, (byte) 0x58, (byte) 0x18, (byte) 0x30,
            (byte) 0x0d, (byte) 0x06, (byte) 0x09, (byte) 0x2a, (byte) 0x86, (byte) 0x48,
            (byte) 0x86, (byte) 0xf7, (byte) 0x0d, (byte) 0x01, (byte) 0x01, (byte) 0x05,
            (byte) 0x05, (byte) 0x00, (byte) 0x03, (byte) 0x81, (byte) 0x81, (byte) 0x00,
            (byte) 0x81, (byte) 0xde, (byte) 0x20, (byte) 0xa1, (byte) 0xb2, (byte) 0x50,
            (byte) 0x03, (byte) 0xcd, (byte) 0x90, (byte) 0x4f, (byte) 0x2b, (byte) 0x47,
            (byte) 0x1d, (byte) 0xac, (byte) 0x6e, (byte) 0xb4, (byte) 0xc7, (byte) 0x14,
            (byte) 0xc6, (byte) 0x4f, (byte) 0x45, (byte) 0xaf, (byte) 0x81, (byte) 0x5d,
            (byte) 0x5a, (byte) 0x31, (byte) 0xff, (byte) 0x9c, (byte) 0x4d, (byte) 0xdc,
            (byte) 0x9e, (byte) 0x36, (byte) 0x9f, (byte) 0x9b, (byte) 0xb1, (byte) 0xc9,
            (byte) 0x50, (byte) 0xa3, (byte) 0xf6, (byte) 0x9c, (byte) 0x68, (byte) 0x6f,
            (byte) 0x68, (byte) 0xd9, (byte) 0x56, (byte) 0x1b, (byte) 0xe5, (byte) 0x1b,
            (byte) 0x41, (byte) 0xd4, (byte) 0xcc, (byte) 0xb6, (byte) 0x37, (byte) 0xd5,
            (byte) 0x69, (byte) 0x6b, (byte) 0x39, (byte) 0xaf, (byte) 0xc6, (byte) 0xb8,
            (byte) 0x39, (byte) 0x76, (byte) 0xe3, (byte) 0xf7, (byte) 0x97, (byte) 0x74,
            (byte) 0x31, (byte) 0xc4, (byte) 0x2d, (byte) 0xb7, (byte) 0x9a, (byte) 0xa4,
            (byte) 0xfa, (byte) 0x9f, (byte) 0xa8, (byte) 0xe3, (byte) 0x41, (byte) 0xda,
            (byte) 0x2f, (byte) 0x0c, (byte) 0x9d, (byte) 0x83, (byte) 0xdc, (byte) 0x86,
            (byte) 0x1f, (byte) 0x5c, (byte) 0x0f, (byte) 0x87, (byte) 0x05, (byte) 0xc9,
            (byte) 0xb0, (byte) 0x63, (byte) 0xca, (byte) 0x9b, (byte) 0xdb, (byte) 0xe6,
            (byte) 0x3c, (byte) 0xe9, (byte) 0x23, (byte) 0x9e, (byte) 0x23, (byte) 0x44,
            (byte) 0x1d, (byte) 0x5b, (byte) 0x60, (byte) 0x66, (byte) 0xb6, (byte) 0x72,
            (byte) 0x8c, (byte) 0x87, (byte) 0x86, (byte) 0xe8, (byte) 0xdb, (byte) 0x29,
            (byte) 0x67, (byte) 0x9c, (byte) 0x33, (byte) 0x5c, (byte) 0x39, (byte) 0xf1,
            (byte) 0xb5, (byte) 0x9b, (byte) 0xb8, (byte) 0xe1, (byte) 0x42, (byte) 0x51,
            (byte) 0xed, (byte) 0x2c
    };

    /**
     * The amount of time to allow before and after expected time for variance
     * in timing tests.
     */
    private static final long SLOP_TIME_MILLIS = 15000L;

    @Override
    protected void setUp() throws Exception {
        // Wipe any existing entries in the KeyStore
        KeyStore ksTemp = KeyStore.getInstance("AndroidKeyStore");
        ksTemp.load(null, null);
        Enumeration<String> aliases = ksTemp.aliases();
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            ksTemp.deleteEntry(alias);
        }

        // Get a new instance because some tests need it uninitialized
        mKeyStore = KeyStore.getInstance("AndroidKeyStore");
    }

    private PrivateKey generatePrivateKey(String keyType, byte[] fakeKey1) throws Exception {
        KeyFactory kf = KeyFactory.getInstance(keyType);
        return kf.generatePrivate(new PKCS8EncodedKeySpec(fakeKey1));
    }

    private Certificate generateCertificate(byte[] fakeUser1) throws Exception {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        return cf.generateCertificate(new ByteArrayInputStream(fakeUser1));
    }

    private PrivateKeyEntry makeUserDsaKey1() throws Exception {
        return new KeyStore.PrivateKeyEntry(generatePrivateKey("DSA", FAKE_DSA_KEY_1),
                new Certificate[] {
                generateCertificate(FAKE_DSA_USER_1), generateCertificate(FAKE_DSA_CA_1)
        });
    }

    private PrivateKeyEntry makeUserEcKey1() throws Exception {
        return new KeyStore.PrivateKeyEntry(generatePrivateKey("EC", FAKE_EC_KEY_1),
                new Certificate[] {
                        generateCertificate(FAKE_EC_USER_1), generateCertificate(FAKE_EC_CA_1)
                });
    }

    private PrivateKeyEntry makeUserRsaKey1() throws Exception {
        return new KeyStore.PrivateKeyEntry(generatePrivateKey("RSA", FAKE_RSA_KEY_1),
                new Certificate[] {
                        generateCertificate(FAKE_RSA_USER_1), generateCertificate(FAKE_RSA_CA_1)
                });
    }

    private Entry makeCa1() throws Exception {
        return new KeyStore.TrustedCertificateEntry(generateCertificate(FAKE_RSA_CA_1));
    }

    private void assertAliases(final String[] expectedAliases) throws KeyStoreException {
        final Enumeration<String> aliases = mKeyStore.aliases();
        int count = 0;

        final Set<String> expectedSet = new HashSet<String>();
        expectedSet.addAll(Arrays.asList(expectedAliases));

        while (aliases.hasMoreElements()) {
            count++;
            final String alias = aliases.nextElement();
            assertTrue("The alias should be in the expected set", expectedSet.contains(alias));
            expectedSet.remove(alias);
        }
        assertTrue("The expected set and actual set should be exactly equal", expectedSet.isEmpty());
        assertEquals("There should be the correct number of keystore entries",
                expectedAliases.length, count);
    }

    public void testKeyStore_Aliases_Unencrypted_Success() throws Exception {
        mKeyStore.load(null, null);

        assertAliases(new String[] {});

        mKeyStore.setEntry(TEST_ALIAS_1, makeUserRsaKey1(), null);

        assertAliases(new String[] { TEST_ALIAS_1 });

        mKeyStore.setEntry(TEST_ALIAS_2, makeCa1(), null);

        assertAliases(new String[] { TEST_ALIAS_1, TEST_ALIAS_2 });
    }

    public void testKeyStore_Aliases_NotInitialized_Unencrypted_Failure() throws Exception {
        try {
            mKeyStore.aliases();
            fail("KeyStore should throw exception when not initialized");
        } catch (KeyStoreException success) {
        }
    }

    public void testKeyStore_ContainsAliases_PrivateAndCA_Unencrypted_Success() throws Exception {
        mKeyStore.load(null, null);

        assertAliases(new String[] {});

        mKeyStore.setEntry(TEST_ALIAS_1, makeUserRsaKey1(), null);

        assertTrue("Should contain generated private key", mKeyStore.containsAlias(TEST_ALIAS_1));

        mKeyStore.setEntry(TEST_ALIAS_2, makeCa1(), null);

        assertTrue("Should contain added CA certificate", mKeyStore.containsAlias(TEST_ALIAS_2));

        assertFalse("Should not contain unadded certificate alias",
                mKeyStore.containsAlias(TEST_ALIAS_3));
    }

    public void testKeyStore_ContainsAliases_CAOnly_Unencrypted_Success() throws Exception {
        mKeyStore.load(null, null);

        mKeyStore.setEntry(TEST_ALIAS_2, makeCa1(), null);

        assertTrue("Should contain added CA certificate", mKeyStore.containsAlias(TEST_ALIAS_2));
    }

    public void testKeyStore_ContainsAliases_NonExistent_Unencrypted_Failure() throws Exception {
        mKeyStore.load(null, null);

        assertFalse("Should contain added CA certificate", mKeyStore.containsAlias(TEST_ALIAS_1));
    }

    public void testKeyStore_DeleteEntry_Unencrypted_Success() throws Exception {
        mKeyStore.load(null, null);

        // TEST_ALIAS_1
        mKeyStore.setEntry(TEST_ALIAS_1, makeUserRsaKey1(), null);

        // TEST_ALIAS_2
        mKeyStore.setCertificateEntry(TEST_ALIAS_2, generateCertificate(FAKE_RSA_CA_1));

        // TEST_ALIAS_3
        mKeyStore.setCertificateEntry(TEST_ALIAS_3, generateCertificate(FAKE_RSA_CA_1));

        assertAliases(new String[] { TEST_ALIAS_1, TEST_ALIAS_2, TEST_ALIAS_3 });

        mKeyStore.deleteEntry(TEST_ALIAS_1);

        assertAliases(new String[] { TEST_ALIAS_2, TEST_ALIAS_3 });

        mKeyStore.deleteEntry(TEST_ALIAS_3);

        assertAliases(new String[] { TEST_ALIAS_2 });

        mKeyStore.deleteEntry(TEST_ALIAS_2);

        assertAliases(new String[] { });
    }

    public void testKeyStore_DeleteEntry_EmptyStore_Unencrypted_Success() throws Exception {
        mKeyStore.load(null, null);

        // Should not throw when a non-existent entry is requested for delete.
        mKeyStore.deleteEntry(TEST_ALIAS_1);
    }

    public void testKeyStore_DeleteEntry_NonExistent_Unencrypted_Success() throws Exception {
        mKeyStore.load(null, null);

        // TEST_ALIAS_1
        mKeyStore.setEntry(TEST_ALIAS_1, makeUserRsaKey1(), null);

        // Should not throw when a non-existent entry is requested for delete.
        mKeyStore.deleteEntry(TEST_ALIAS_2);
    }

    public void testKeyStore_GetCertificate_Single_Unencrypted_Success() throws Exception {
        mKeyStore.load(null, null);

        mKeyStore.setCertificateEntry(TEST_ALIAS_1, generateCertificate(FAKE_RSA_CA_1));

        assertAliases(new String[] { TEST_ALIAS_1 });

        assertNull("Certificate should not exist in keystore",
                mKeyStore.getCertificate(TEST_ALIAS_2));

        Certificate retrieved = mKeyStore.getCertificate(TEST_ALIAS_1);

        assertNotNull("Retrieved certificate should not be null", retrieved);

        CertificateFactory f = CertificateFactory.getInstance("X.509");
        Certificate actual = f.generateCertificate(new ByteArrayInputStream(FAKE_RSA_CA_1));

        assertEquals("Actual and retrieved certificates should be the same", actual, retrieved);
    }

    public void testKeyStore_GetCertificate_NonExist_Unencrypted_Failure() throws Exception {
        mKeyStore.load(null, null);

        assertNull("Certificate should not exist in keystore",
                mKeyStore.getCertificate(TEST_ALIAS_1));
    }

    public void testKeyStore_GetCertificateAlias_CAEntry_Unencrypted_Success() throws Exception {
        mKeyStore.load(null, null);

        Certificate cert = generateCertificate(FAKE_RSA_CA_1);
        mKeyStore.setCertificateEntry(TEST_ALIAS_1, cert);

        assertEquals("Stored certificate alias should be found", TEST_ALIAS_1,
                mKeyStore.getCertificateAlias(cert));
    }

    public void testKeyStore_GetCertificateAlias_PrivateKeyEntry_Unencrypted_Success()
            throws Exception {
        mKeyStore.load(null, null);

        mKeyStore.setEntry(TEST_ALIAS_1, makeUserRsaKey1(), null);

        CertificateFactory f = CertificateFactory.getInstance("X.509");
        Certificate actual = f.generateCertificate(new ByteArrayInputStream(FAKE_RSA_USER_1));

        assertEquals("Stored certificate alias should be found", TEST_ALIAS_1,
                mKeyStore.getCertificateAlias(actual));
    }

    public void testKeyStore_GetCertificateAlias_CAEntry_WithPrivateKeyUsingCA_Unencrypted_Success()
            throws Exception {
        mKeyStore.load(null, null);

        Certificate actual = generateCertificate(FAKE_RSA_CA_1);

        // Insert TrustedCertificateEntry with CA name
        mKeyStore.setCertificateEntry(TEST_ALIAS_2, actual);

        // Insert PrivateKeyEntry that uses the same CA
        mKeyStore.setEntry(TEST_ALIAS_1, makeUserRsaKey1(), null);

        assertEquals("Stored certificate alias should be found", TEST_ALIAS_2,
                mKeyStore.getCertificateAlias(actual));
    }

    public void testKeyStore_GetCertificateAlias_NonExist_Empty_Unencrypted_Failure()
            throws Exception {
        mKeyStore.load(null, null);

        CertificateFactory f = CertificateFactory.getInstance("X.509");
        Certificate actual = f.generateCertificate(new ByteArrayInputStream(FAKE_RSA_CA_1));

        assertNull("Stored certificate alias should not be found",
                mKeyStore.getCertificateAlias(actual));
    }

    public void testKeyStore_GetCertificateAlias_NonExist_Unencrypted_Failure() throws Exception {
        mKeyStore.load(null, null);

        Certificate ca = generateCertificate(FAKE_RSA_CA_1);

        // Insert TrustedCertificateEntry with CA name
        mKeyStore.setCertificateEntry(TEST_ALIAS_1, ca);

        Certificate userCert = generateCertificate(FAKE_RSA_USER_1);

        assertNull("Stored certificate alias should be found",
                mKeyStore.getCertificateAlias(userCert));
    }

    public void testKeyStore_GetCertificateChain_SingleLength_Unencrypted_Success() throws Exception {
        mKeyStore.load(null, null);

        // TEST_ALIAS_1
        mKeyStore.setEntry(TEST_ALIAS_1, makeUserRsaKey1(), null);

        Certificate[] expected = new Certificate[2];
        expected[0] = generateCertificate(FAKE_RSA_USER_1);
        expected[1] = generateCertificate(FAKE_RSA_CA_1);

        Certificate[] actual = mKeyStore.getCertificateChain(TEST_ALIAS_1);

        assertNotNull("Returned certificate chain should not be null", actual);
        assertEquals("Returned certificate chain should be correct size", expected.length,
                actual.length);
        assertEquals("First certificate should be user certificate", expected[0], actual[0]);
        assertEquals("Second certificate should be CA certificate", expected[1], actual[1]);

        // Negative test when keystore is populated.
        assertNull("Stored certificate alias should not be found",
                mKeyStore.getCertificateChain(TEST_ALIAS_2));
    }

    public void testKeyStore_GetCertificateChain_NonExist_Unencrypted_Failure() throws Exception {
        mKeyStore.load(null, null);

        assertNull("Stored certificate alias should not be found",
                mKeyStore.getCertificateChain(TEST_ALIAS_1));
    }

    public void testKeyStore_GetCreationDate_PrivateKeyEntry_Unencrypted_Success() throws Exception {
        mKeyStore.load(null, null);

        // TEST_ALIAS_1
        mKeyStore.setEntry(TEST_ALIAS_1, makeUserRsaKey1(), null);

        Date now = new Date();
        Date actual = mKeyStore.getCreationDate(TEST_ALIAS_1);

        Date expectedAfter = new Date(now.getTime() - SLOP_TIME_MILLIS);
        Date expectedBefore = new Date(now.getTime() + SLOP_TIME_MILLIS);

        assertTrue("Time should be close to current time", actual.before(expectedBefore));
        assertTrue("Time should be close to current time", actual.after(expectedAfter));
    }

    public void testKeyStore_GetCreationDate_CAEntry_Unencrypted_Success() throws Exception {
        mKeyStore.load(null, null);

        // Insert TrustedCertificateEntry with CA name
        mKeyStore.setCertificateEntry(TEST_ALIAS_1, generateCertificate(FAKE_RSA_CA_1));

        Date now = new Date();
        Date actual = mKeyStore.getCreationDate(TEST_ALIAS_1);
        assertNotNull("Certificate should be found", actual);

        Date expectedAfter = new Date(now.getTime() - SLOP_TIME_MILLIS);
        Date expectedBefore = new Date(now.getTime() + SLOP_TIME_MILLIS);

        assertTrue("Time should be close to current time", actual.before(expectedBefore));
        assertTrue("Time should be close to current time", actual.after(expectedAfter));
    }

    public void testKeyStore_GetEntry_NullParams_Unencrypted_Success() throws Exception {
        mKeyStore.load(null, null);

        // TEST_ALIAS_1
        mKeyStore.setEntry(TEST_ALIAS_1, makeUserRsaKey1(), null);

        Entry entry = mKeyStore.getEntry(TEST_ALIAS_1, null);
        assertNotNull("Entry should exist", entry);

        assertTrue("Should be a PrivateKeyEntry", entry instanceof PrivateKeyEntry);

        PrivateKeyEntry keyEntry = (PrivateKeyEntry) entry;

        assertPrivateKeyEntryEquals(keyEntry, "RSA", FAKE_RSA_KEY_1, FAKE_RSA_USER_1, FAKE_RSA_CA_1);
    }

    public void testKeyStore_GetEntry_DSA_NullParams_Unencrypted_Success() throws Exception {
        mKeyStore.load(null, null);

        // TEST_ALIAS_1
        mKeyStore.setEntry(TEST_ALIAS_1, makeUserDsaKey1(), null);

        Entry entry = mKeyStore.getEntry(TEST_ALIAS_1, null);
        assertNotNull("Entry should exist", entry);

        assertTrue("Should be a PrivateKeyEntry", entry instanceof PrivateKeyEntry);

        PrivateKeyEntry keyEntry = (PrivateKeyEntry) entry;

        assertPrivateKeyEntryEquals(keyEntry, "DSA", FAKE_DSA_KEY_1, FAKE_DSA_USER_1, FAKE_DSA_CA_1);
    }

    public void testKeyStore_GetEntry_EC_NullParams_Unencrypted_Success() throws Exception {
        mKeyStore.load(null, null);

        // TEST_ALIAS_1
        mKeyStore.setEntry(TEST_ALIAS_1, makeUserEcKey1(), null);

        Entry entry = mKeyStore.getEntry(TEST_ALIAS_1, null);
        assertNotNull("Entry should exist", entry);

        assertTrue("Should be a PrivateKeyEntry", entry instanceof PrivateKeyEntry);

        PrivateKeyEntry keyEntry = (PrivateKeyEntry) entry;

        assertPrivateKeyEntryEquals(keyEntry, "EC", FAKE_EC_KEY_1, FAKE_EC_USER_1, FAKE_EC_CA_1);
    }

    public void testKeyStore_GetEntry_RSA_NullParams_Unencrypted_Success() throws Exception {
        mKeyStore.load(null, null);

        // TEST_ALIAS_1
        mKeyStore.setEntry(TEST_ALIAS_1, makeUserRsaKey1(), null);

        Entry entry = mKeyStore.getEntry(TEST_ALIAS_1, null);
        assertNotNull("Entry should exist", entry);

        assertTrue("Should be a PrivateKeyEntry", entry instanceof PrivateKeyEntry);

        PrivateKeyEntry keyEntry = (PrivateKeyEntry) entry;

        assertPrivateKeyEntryEquals(keyEntry, "RSA", FAKE_RSA_KEY_1, FAKE_RSA_USER_1,
                FAKE_RSA_CA_1);
    }

    @SuppressWarnings("unchecked")
    private void assertPrivateKeyEntryEquals(PrivateKeyEntry keyEntry, String keyType, byte[] key,
            byte[] cert, byte[] ca) throws Exception {
        KeyFactory keyFact = KeyFactory.getInstance(keyType);
        PrivateKey expectedKey = keyFact.generatePrivate(new PKCS8EncodedKeySpec(key));

        CertificateFactory certFact = CertificateFactory.getInstance("X.509");
        Certificate expectedCert = certFact.generateCertificate(new ByteArrayInputStream(cert));

        final Collection<Certificate> expectedChain;
        if (ca != null) {
            expectedChain = (Collection<Certificate>) certFact
                    .generateCertificates(new ByteArrayInputStream(ca));
        } else {
            expectedChain = null;
        }

        assertPrivateKeyEntryEquals(keyEntry, expectedKey, expectedCert, expectedChain);
    }

    private void assertPrivateKeyEntryEquals(PrivateKeyEntry keyEntry, PrivateKey expectedKey,
            Certificate expectedCert, Collection<Certificate> expectedChain) throws Exception {
        final PrivateKey privKey = keyEntry.getPrivateKey();
        final PublicKey pubKey = keyEntry.getCertificate().getPublicKey();

        if (expectedKey instanceof DSAPrivateKey) {
            assertEquals("Returned PrivateKey should be what we inserted",
                    ((DSAPrivateKey) expectedKey).getParams(),
                    ((DSAPublicKey) pubKey).getParams());
        } else if (expectedKey instanceof ECPrivateKey) {
            assertEquals("Returned PrivateKey should be what we inserted",
                    ((ECPrivateKey) expectedKey).getParams().getCurve(),
                    ((ECPublicKey) pubKey).getParams().getCurve());
        } else if (expectedKey instanceof RSAPrivateKey) {
            assertEquals("Returned PrivateKey should be what we inserted",
                    ((RSAPrivateKey) expectedKey).getModulus(),
                    ((RSAPrivateKey) privKey).getModulus());
        }

        assertNull("getFormat() should return null", privKey.getFormat());
        assertNull("getEncoded() should return null", privKey.getEncoded());

        assertEquals("Public keys should be in X.509 format", "X.509", pubKey.getFormat());
        assertNotNull("Public keys should be encodable", pubKey.getEncoded());

        assertEquals("Returned Certificate should be what we inserted", expectedCert,
                keyEntry.getCertificate());

        Certificate[] actualChain = keyEntry.getCertificateChain();

        assertEquals("First certificate in chain should be user cert", expectedCert, actualChain[0]);

        if (expectedChain == null) {
            assertEquals("Certificate chain should not include CAs", 1, actualChain.length);
        } else {
            assertEquals("Chains should be the same size", expectedChain.size() + 1,
                    actualChain.length);
            int i = 1;
            final Iterator<Certificate> it = expectedChain.iterator();
            while (it.hasNext() && i < actualChain.length) {
                assertEquals("CA chain certificate should equal what we put in", it.next(),
                        actualChain[i++]);
            }
        }
    }

    public void testKeyStore_GetEntry_Nonexistent_NullParams_Unencrypted_Failure() throws Exception {
        mKeyStore.load(null, null);

        assertNull("A non-existent entry should return null",
                mKeyStore.getEntry(TEST_ALIAS_1, null));
    }

    public void testKeyStore_GetKey_NoPassword_Unencrypted_Success() throws Exception {
        mKeyStore.load(null, null);

        // TEST_ALIAS_1
        mKeyStore.setEntry(TEST_ALIAS_1, makeUserRsaKey1(), null);

        Key key = mKeyStore.getKey(TEST_ALIAS_1, null);
        assertNotNull("Key should exist", key);

        assertTrue("Should be a RSAPrivateKey", key instanceof RSAPrivateKey);

        RSAPrivateKey actualKey = (RSAPrivateKey) key;

        KeyFactory keyFact = KeyFactory.getInstance("RSA");
        PrivateKey expectedKey = keyFact.generatePrivate(new PKCS8EncodedKeySpec(FAKE_RSA_KEY_1));

        assertEquals("Inserted key should be same as retrieved key",
                ((RSAPrivateKey) expectedKey).getModulus(), actualKey.getModulus());
    }

    public void testKeyStore_GetKey_Certificate_Unencrypted_Failure() throws Exception {
        mKeyStore.load(null, null);

        // Insert TrustedCertificateEntry with CA name
        mKeyStore.setCertificateEntry(TEST_ALIAS_1, generateCertificate(FAKE_RSA_CA_1));

        assertNull("Certificate entries should return null", mKeyStore.getKey(TEST_ALIAS_1, null));
    }

    public void testKeyStore_GetKey_NonExistent_Unencrypted_Failure() throws Exception {
        mKeyStore.load(null, null);

        assertNull("A non-existent entry should return null", mKeyStore.getKey(TEST_ALIAS_1, null));
    }

    public void testKeyStore_GetProvider_Unencrypted_Success() throws Exception {
        assertEquals("AndroidKeyStore", mKeyStore.getProvider().getName());
    }

    public void testKeyStore_GetType_Unencrypted_Success() throws Exception {
        assertEquals("AndroidKeyStore", mKeyStore.getType());
    }

    public void testKeyStore_IsCertificateEntry_CA_Unencrypted_Success() throws Exception {
        mKeyStore.load(null, null);

        // Insert TrustedCertificateEntry with CA name
        mKeyStore.setCertificateEntry(TEST_ALIAS_1, generateCertificate(FAKE_RSA_CA_1));

        assertTrue("Should return true for CA certificate",
                mKeyStore.isCertificateEntry(TEST_ALIAS_1));
    }

    public void testKeyStore_IsCertificateEntry_PrivateKey_Unencrypted_Failure() throws Exception {
        mKeyStore.load(null, null);

        // TEST_ALIAS_1
        mKeyStore.setEntry(TEST_ALIAS_1, makeUserRsaKey1(), null);

        assertFalse("Should return false for PrivateKeyEntry",
                mKeyStore.isCertificateEntry(TEST_ALIAS_1));
    }

    public void testKeyStore_IsCertificateEntry_NonExist_Unencrypted_Failure() throws Exception {
        mKeyStore.load(null, null);

        assertFalse("Should return false for non-existent entry",
                mKeyStore.isCertificateEntry(TEST_ALIAS_1));
    }

    public void testKeyStore_IsKeyEntry_PrivateKey_Unencrypted_Success() throws Exception {
        mKeyStore.load(null, null);

        // TEST_ALIAS_1
        mKeyStore.setEntry(TEST_ALIAS_1, makeUserRsaKey1(), null);

        assertTrue("Should return true for PrivateKeyEntry", mKeyStore.isKeyEntry(TEST_ALIAS_1));
    }

    public void testKeyStore_IsKeyEntry_CA_Unencrypted_Failure() throws Exception {
        mKeyStore.load(null, null);

        mKeyStore.setCertificateEntry(TEST_ALIAS_1, generateCertificate(FAKE_RSA_CA_1));

        assertFalse("Should return false for CA certificate", mKeyStore.isKeyEntry(TEST_ALIAS_1));
    }

    public void testKeyStore_IsKeyEntry_NonExist_Unencrypted_Failure() throws Exception {
        mKeyStore.load(null, null);

        assertFalse("Should return false for non-existent entry",
                mKeyStore.isKeyEntry(TEST_ALIAS_1));
    }

    public void testKeyStore_SetCertificate_CA_Unencrypted_Success() throws Exception {
        final Certificate actual = generateCertificate(FAKE_RSA_CA_1);

        mKeyStore.load(null, null);

        mKeyStore.setCertificateEntry(TEST_ALIAS_1, actual);
        assertAliases(new String[] { TEST_ALIAS_1 });

        Certificate retrieved = mKeyStore.getCertificate(TEST_ALIAS_1);

        assertEquals("Retrieved certificate should be the same as the one inserted", actual,
                retrieved);
    }

    public void testKeyStore_SetCertificate_CAExists_Overwrite_Unencrypted_Success()
            throws Exception {
        mKeyStore.load(null, null);

        mKeyStore.setCertificateEntry(TEST_ALIAS_1, generateCertificate(FAKE_RSA_CA_1));

        assertAliases(new String[] { TEST_ALIAS_1 });

        final Certificate cert = generateCertificate(FAKE_RSA_CA_1);

        // TODO have separate FAKE_CA for second test
        mKeyStore.setCertificateEntry(TEST_ALIAS_1, cert);

        assertAliases(new String[] { TEST_ALIAS_1 });
    }

    public void testKeyStore_SetCertificate_PrivateKeyExists_Unencrypted_Failure() throws Exception {
        mKeyStore.load(null, null);

        mKeyStore.setEntry(TEST_ALIAS_1, makeUserRsaKey1(), null);

        assertAliases(new String[] { TEST_ALIAS_1 });

        final Certificate cert = generateCertificate(FAKE_RSA_CA_1);

        try {
            mKeyStore.setCertificateEntry(TEST_ALIAS_1, cert);
            fail("Should throw when trying to overwrite a PrivateKey entry with a Certificate");
        } catch (KeyStoreException success) {
        }
    }

    public void testKeyStore_SetEntry_PrivateKeyEntry_Unencrypted_Success() throws Exception {
        mKeyStore.load(null, null);

        KeyFactory keyFact = KeyFactory.getInstance("RSA");
        PrivateKey expectedKey = keyFact.generatePrivate(new PKCS8EncodedKeySpec(FAKE_RSA_KEY_1));

        final CertificateFactory f = CertificateFactory.getInstance("X.509");

        final Certificate[] expectedChain = new Certificate[2];
        expectedChain[0] = f.generateCertificate(new ByteArrayInputStream(FAKE_RSA_USER_1));
        expectedChain[1] = f.generateCertificate(new ByteArrayInputStream(FAKE_RSA_CA_1));

        PrivateKeyEntry expected = new PrivateKeyEntry(expectedKey, expectedChain);

        mKeyStore.setEntry(TEST_ALIAS_1, expected, null);

        Entry actualEntry = mKeyStore.getEntry(TEST_ALIAS_1, null);
        assertNotNull("Retrieved entry should exist", actualEntry);

        assertTrue("Retrieved entry should be of type PrivateKeyEntry",
                actualEntry instanceof PrivateKeyEntry);

        PrivateKeyEntry actual = (PrivateKeyEntry) actualEntry;

        assertPrivateKeyEntryEquals(actual, "RSA", FAKE_RSA_KEY_1, FAKE_RSA_USER_1, FAKE_RSA_CA_1);
    }

    public void testKeyStore_SetEntry_PrivateKeyEntry_Params_Unencrypted_Failure() throws Exception {
        mKeyStore.load(null, null);

        KeyFactory keyFact = KeyFactory.getInstance("RSA");
        PrivateKey expectedKey = keyFact.generatePrivate(new PKCS8EncodedKeySpec(FAKE_RSA_KEY_1));

        final CertificateFactory f = CertificateFactory.getInstance("X.509");

        final Certificate[] expectedChain = new Certificate[2];
        expectedChain[0] = f.generateCertificate(new ByteArrayInputStream(FAKE_RSA_USER_1));
        expectedChain[1] = f.generateCertificate(new ByteArrayInputStream(FAKE_RSA_CA_1));

        PrivateKeyEntry entry = new PrivateKeyEntry(expectedKey, expectedChain);

        try {
            mKeyStore.setEntry(TEST_ALIAS_1, entry,
                    new KeyStoreParameter.Builder(getContext())
                    .setEncryptionRequired(true)
                    .build());
            fail("Shouldn't be able to insert encrypted entry when KeyStore uninitialized");
        } catch (KeyStoreException expected) {
        }

        assertNull(mKeyStore.getEntry(TEST_ALIAS_1, null));
    }

    public void testKeyStore_SetEntry_PrivateKeyEntry_Overwrites_PrivateKeyEntry_Unencrypted_Success()
            throws Exception {
        mKeyStore.load(null, null);

        final KeyFactory keyFact = KeyFactory.getInstance("RSA");
        final CertificateFactory f = CertificateFactory.getInstance("X.509");

        // Start with PrivateKeyEntry
        {
            PrivateKey expectedKey = keyFact
                    .generatePrivate(new PKCS8EncodedKeySpec(FAKE_RSA_KEY_1));

            final Certificate[] expectedChain = new Certificate[2];
            expectedChain[0] = f.generateCertificate(new ByteArrayInputStream(FAKE_RSA_USER_1));
            expectedChain[1] = f.generateCertificate(new ByteArrayInputStream(FAKE_RSA_CA_1));

            PrivateKeyEntry expected = new PrivateKeyEntry(expectedKey, expectedChain);

            mKeyStore.setEntry(TEST_ALIAS_1, expected, null);

            Entry actualEntry = mKeyStore.getEntry(TEST_ALIAS_1, null);
            assertNotNull("Retrieved entry should exist", actualEntry);

            assertTrue("Retrieved entry should be of type PrivateKeyEntry",
                    actualEntry instanceof PrivateKeyEntry);

            PrivateKeyEntry actual = (PrivateKeyEntry) actualEntry;

            assertPrivateKeyEntryEquals(actual, "RSA", FAKE_RSA_KEY_1, FAKE_RSA_USER_1,
                    FAKE_RSA_CA_1);
        }

        // TODO make entirely new test vector for the overwrite
        // Replace with PrivateKeyEntry
        {
            PrivateKey expectedKey = keyFact
                    .generatePrivate(new PKCS8EncodedKeySpec(FAKE_RSA_KEY_1));

            final Certificate[] expectedChain = new Certificate[2];
            expectedChain[0] = f.generateCertificate(new ByteArrayInputStream(FAKE_RSA_USER_1));
            expectedChain[1] = f.generateCertificate(new ByteArrayInputStream(FAKE_RSA_CA_1));

            PrivateKeyEntry expected = new PrivateKeyEntry(expectedKey, expectedChain);

            mKeyStore.setEntry(TEST_ALIAS_1, expected, null);

            Entry actualEntry = mKeyStore.getEntry(TEST_ALIAS_1, null);
            assertNotNull("Retrieved entry should exist", actualEntry);

            assertTrue("Retrieved entry should be of type PrivateKeyEntry",
                    actualEntry instanceof PrivateKeyEntry);

            PrivateKeyEntry actual = (PrivateKeyEntry) actualEntry;

            assertPrivateKeyEntryEquals(actual, "RSA", FAKE_RSA_KEY_1, FAKE_RSA_USER_1,
                    FAKE_RSA_CA_1);
        }
    }

    public void testKeyStore_SetEntry_CAEntry_Overwrites_PrivateKeyEntry_Unencrypted_Success()
            throws Exception {
        mKeyStore.load(null, null);

        final CertificateFactory f = CertificateFactory.getInstance("X.509");

        // Start with TrustedCertificateEntry
        {
            final Certificate caCert = f
                    .generateCertificate(new ByteArrayInputStream(FAKE_RSA_CA_1));

            TrustedCertificateEntry expectedCertEntry = new TrustedCertificateEntry(caCert);
            mKeyStore.setEntry(TEST_ALIAS_1, expectedCertEntry, null);

            Entry actualEntry = mKeyStore.getEntry(TEST_ALIAS_1, null);
            assertNotNull("Retrieved entry should exist", actualEntry);
            assertTrue("Retrieved entry should be of type TrustedCertificateEntry",
                    actualEntry instanceof TrustedCertificateEntry);
            TrustedCertificateEntry actualCertEntry = (TrustedCertificateEntry) actualEntry;
            assertEquals("Stored and retrieved certificates should be the same",
                    expectedCertEntry.getTrustedCertificate(),
                    actualCertEntry.getTrustedCertificate());
        }

        // Replace with PrivateKeyEntry
        {
            KeyFactory keyFact = KeyFactory.getInstance("RSA");
            PrivateKey expectedKey = keyFact
                    .generatePrivate(new PKCS8EncodedKeySpec(FAKE_RSA_KEY_1));
            final Certificate[] expectedChain = new Certificate[2];
            expectedChain[0] = f.generateCertificate(new ByteArrayInputStream(FAKE_RSA_USER_1));
            expectedChain[1] = f.generateCertificate(new ByteArrayInputStream(FAKE_RSA_CA_1));

            PrivateKeyEntry expectedPrivEntry = new PrivateKeyEntry(expectedKey, expectedChain);

            mKeyStore.setEntry(TEST_ALIAS_1, expectedPrivEntry, null);

            Entry actualEntry = mKeyStore.getEntry(TEST_ALIAS_1, null);
            assertNotNull("Retrieved entry should exist", actualEntry);
            assertTrue("Retrieved entry should be of type PrivateKeyEntry",
                    actualEntry instanceof PrivateKeyEntry);

            PrivateKeyEntry actualPrivEntry = (PrivateKeyEntry) actualEntry;
            assertPrivateKeyEntryEquals(actualPrivEntry, "RSA", FAKE_RSA_KEY_1, FAKE_RSA_USER_1,
                    FAKE_RSA_CA_1);
        }
    }

    public void testKeyStore_SetEntry_PrivateKeyEntry_Overwrites_CAEntry_Unencrypted_Success()
            throws Exception {
        mKeyStore.load(null, null);

        final CertificateFactory f = CertificateFactory.getInstance("X.509");

        final Certificate caCert = f.generateCertificate(new ByteArrayInputStream(FAKE_RSA_CA_1));

        // Start with PrivateKeyEntry
        {
            KeyFactory keyFact = KeyFactory.getInstance("RSA");
            PrivateKey expectedKey = keyFact
                    .generatePrivate(new PKCS8EncodedKeySpec(FAKE_RSA_KEY_1));
            final Certificate[] expectedChain = new Certificate[2];
            expectedChain[0] = f.generateCertificate(new ByteArrayInputStream(FAKE_RSA_USER_1));
            expectedChain[1] = caCert;

            PrivateKeyEntry expectedPrivEntry = new PrivateKeyEntry(expectedKey, expectedChain);

            mKeyStore.setEntry(TEST_ALIAS_1, expectedPrivEntry, null);

            Entry actualEntry = mKeyStore.getEntry(TEST_ALIAS_1, null);
            assertNotNull("Retrieved entry should exist", actualEntry);
            assertTrue("Retrieved entry should be of type PrivateKeyEntry",
                    actualEntry instanceof PrivateKeyEntry);

            PrivateKeyEntry actualPrivEntry = (PrivateKeyEntry) actualEntry;
            assertPrivateKeyEntryEquals(actualPrivEntry, "RSA", FAKE_RSA_KEY_1, FAKE_RSA_USER_1,
                    FAKE_RSA_CA_1);
        }

        // Replace with TrustedCertificateEntry
        {
            TrustedCertificateEntry expectedCertEntry = new TrustedCertificateEntry(caCert);
            mKeyStore.setEntry(TEST_ALIAS_1, expectedCertEntry, null);

            Entry actualEntry = mKeyStore.getEntry(TEST_ALIAS_1, null);
            assertNotNull("Retrieved entry should exist", actualEntry);
            assertTrue("Retrieved entry should be of type TrustedCertificateEntry",
                    actualEntry instanceof TrustedCertificateEntry);
            TrustedCertificateEntry actualCertEntry = (TrustedCertificateEntry) actualEntry;
            assertEquals("Stored and retrieved certificates should be the same",
                    expectedCertEntry.getTrustedCertificate(),
                    actualCertEntry.getTrustedCertificate());
        }
    }

    public void testKeyStore_SetEntry_PrivateKeyEntry_Overwrites_ShortPrivateKeyEntry_Unencrypted_Success()
            throws Exception {
        mKeyStore.load(null, null);

        final CertificateFactory f = CertificateFactory.getInstance("X.509");

        final Certificate caCert = f.generateCertificate(new ByteArrayInputStream(FAKE_RSA_CA_1));

        // Start with PrivateKeyEntry
        {
            KeyFactory keyFact = KeyFactory.getInstance("RSA");
            PrivateKey expectedKey = keyFact
                    .generatePrivate(new PKCS8EncodedKeySpec(FAKE_RSA_KEY_1));
            final Certificate[] expectedChain = new Certificate[2];
            expectedChain[0] = f.generateCertificate(new ByteArrayInputStream(FAKE_RSA_USER_1));
            expectedChain[1] = caCert;

            PrivateKeyEntry expectedPrivEntry = new PrivateKeyEntry(expectedKey, expectedChain);

            mKeyStore.setEntry(TEST_ALIAS_1, expectedPrivEntry, null);

            Entry actualEntry = mKeyStore.getEntry(TEST_ALIAS_1, null);
            assertNotNull("Retrieved entry should exist", actualEntry);
            assertTrue("Retrieved entry should be of type PrivateKeyEntry",
                    actualEntry instanceof PrivateKeyEntry);

            PrivateKeyEntry actualPrivEntry = (PrivateKeyEntry) actualEntry;
            assertPrivateKeyEntryEquals(actualPrivEntry, "RSA", FAKE_RSA_KEY_1, FAKE_RSA_USER_1,
                    FAKE_RSA_CA_1);
        }

        // Replace with PrivateKeyEntry that has no chain
        {
            KeyFactory keyFact = KeyFactory.getInstance("RSA");
            PrivateKey expectedKey = keyFact
                    .generatePrivate(new PKCS8EncodedKeySpec(FAKE_RSA_KEY_1));
            final Certificate[] expectedChain = new Certificate[1];
            expectedChain[0] = f.generateCertificate(new ByteArrayInputStream(FAKE_RSA_USER_1));

            PrivateKeyEntry expectedPrivEntry = new PrivateKeyEntry(expectedKey, expectedChain);

            mKeyStore.setEntry(TEST_ALIAS_1, expectedPrivEntry, null);

            Entry actualEntry = mKeyStore.getEntry(TEST_ALIAS_1, null);
            assertNotNull("Retrieved entry should exist", actualEntry);
            assertTrue("Retrieved entry should be of type PrivateKeyEntry",
                    actualEntry instanceof PrivateKeyEntry);

            PrivateKeyEntry actualPrivEntry = (PrivateKeyEntry) actualEntry;
            assertPrivateKeyEntryEquals(actualPrivEntry, "RSA", FAKE_RSA_KEY_1, FAKE_RSA_USER_1,
                    null);
        }
    }

    public void testKeyStore_SetEntry_CAEntry_Overwrites_CAEntry_Unencrypted_Success()
            throws Exception {
        mKeyStore.load(null, null);

        final CertificateFactory f = CertificateFactory.getInstance("X.509");

        // Insert TrustedCertificateEntry
        {
            final Certificate caCert = f
                    .generateCertificate(new ByteArrayInputStream(FAKE_RSA_CA_1));

            TrustedCertificateEntry expectedCertEntry = new TrustedCertificateEntry(caCert);
            mKeyStore.setEntry(TEST_ALIAS_1, expectedCertEntry, null);

            Entry actualEntry = mKeyStore.getEntry(TEST_ALIAS_1, null);
            assertNotNull("Retrieved entry should exist", actualEntry);
            assertTrue("Retrieved entry should be of type TrustedCertificateEntry",
                    actualEntry instanceof TrustedCertificateEntry);
            TrustedCertificateEntry actualCertEntry = (TrustedCertificateEntry) actualEntry;
            assertEquals("Stored and retrieved certificates should be the same",
                    expectedCertEntry.getTrustedCertificate(),
                    actualCertEntry.getTrustedCertificate());
        }

        // Replace with TrustedCertificateEntry of USER
        {
            final Certificate userCert = f.generateCertificate(new ByteArrayInputStream(
                    FAKE_RSA_USER_1));

            TrustedCertificateEntry expectedUserEntry = new TrustedCertificateEntry(userCert);
            mKeyStore.setEntry(TEST_ALIAS_1, expectedUserEntry, null);

            Entry actualEntry = mKeyStore.getEntry(TEST_ALIAS_1, null);
            assertNotNull("Retrieved entry should exist", actualEntry);
            assertTrue("Retrieved entry should be of type TrustedCertificateEntry",
                    actualEntry instanceof TrustedCertificateEntry);
            TrustedCertificateEntry actualUserEntry = (TrustedCertificateEntry) actualEntry;
            assertEquals("Stored and retrieved certificates should be the same",
                    expectedUserEntry.getTrustedCertificate(),
                    actualUserEntry.getTrustedCertificate());
        }
    }

    public void testKeyStore_SetKeyEntry_ProtectedKey_Unencrypted_Failure() throws Exception {
        mKeyStore.load(null, null);

        final CertificateFactory f = CertificateFactory.getInstance("X.509");

        final Certificate caCert = f.generateCertificate(new ByteArrayInputStream(FAKE_RSA_CA_1));

        KeyFactory keyFact = KeyFactory.getInstance("RSA");
        PrivateKey privKey = keyFact.generatePrivate(new PKCS8EncodedKeySpec(FAKE_RSA_KEY_1));
        final Certificate[] chain = new Certificate[2];
        chain[0] = f.generateCertificate(new ByteArrayInputStream(FAKE_RSA_USER_1));
        chain[1] = caCert;

        try {
            mKeyStore.setKeyEntry(TEST_ALIAS_1, privKey, "foo".toCharArray(), chain);
            fail("Should fail when a password is specified");
        } catch (KeyStoreException success) {
        }
    }

    public void testKeyStore_SetKeyEntry_Unencrypted_Success() throws Exception {
        mKeyStore.load(null, null);

        final CertificateFactory f = CertificateFactory.getInstance("X.509");

        final Certificate caCert = f.generateCertificate(new ByteArrayInputStream(FAKE_RSA_CA_1));

        KeyFactory keyFact = KeyFactory.getInstance("RSA");
        PrivateKey privKey = keyFact.generatePrivate(new PKCS8EncodedKeySpec(FAKE_RSA_KEY_1));
        final Certificate[] chain = new Certificate[2];
        chain[0] = f.generateCertificate(new ByteArrayInputStream(FAKE_RSA_USER_1));
        chain[1] = caCert;

        mKeyStore.setKeyEntry(TEST_ALIAS_1, privKey, null, chain);

        Entry actualEntry = mKeyStore.getEntry(TEST_ALIAS_1, null);
        assertNotNull("Retrieved entry should exist", actualEntry);

        assertTrue("Retrieved entry should be of type PrivateKeyEntry",
                actualEntry instanceof PrivateKeyEntry);

        PrivateKeyEntry actual = (PrivateKeyEntry) actualEntry;

        assertPrivateKeyEntryEquals(actual, "RSA", FAKE_RSA_KEY_1, FAKE_RSA_USER_1, FAKE_RSA_CA_1);
    }

    public void testKeyStore_SetKeyEntry_Replaced_Unencrypted_Success() throws Exception {
        mKeyStore.load(null, null);

        final CertificateFactory f = CertificateFactory.getInstance("X.509");

        final Certificate caCert = f.generateCertificate(new ByteArrayInputStream(FAKE_RSA_CA_1));

        // Insert initial key
        {
            KeyFactory keyFact = KeyFactory.getInstance("RSA");
            PrivateKey privKey = keyFact.generatePrivate(new PKCS8EncodedKeySpec(FAKE_RSA_KEY_1));
            final Certificate[] chain = new Certificate[2];
            chain[0] = f.generateCertificate(new ByteArrayInputStream(FAKE_RSA_USER_1));
            chain[1] = caCert;

            mKeyStore.setKeyEntry(TEST_ALIAS_1, privKey, null, chain);

            Entry actualEntry = mKeyStore.getEntry(TEST_ALIAS_1, null);
            assertNotNull("Retrieved entry should exist", actualEntry);

            assertTrue("Retrieved entry should be of type PrivateKeyEntry",
                    actualEntry instanceof PrivateKeyEntry);

            PrivateKeyEntry actual = (PrivateKeyEntry) actualEntry;

            assertPrivateKeyEntryEquals(actual, "RSA", FAKE_RSA_KEY_1, FAKE_RSA_USER_1,
                    FAKE_RSA_CA_1);
        }

        // TODO make a separate key
        // Replace key
        {
            KeyFactory keyFact = KeyFactory.getInstance("RSA");
            PrivateKey privKey = keyFact.generatePrivate(new PKCS8EncodedKeySpec(FAKE_RSA_KEY_1));
            final Certificate[] chain = new Certificate[2];
            chain[0] = f.generateCertificate(new ByteArrayInputStream(FAKE_RSA_USER_1));
            chain[1] = caCert;

            mKeyStore.setKeyEntry(TEST_ALIAS_1, privKey, null, chain);

            Entry actualEntry = mKeyStore.getEntry(TEST_ALIAS_1, null);
            assertNotNull("Retrieved entry should exist", actualEntry);

            assertTrue("Retrieved entry should be of type PrivateKeyEntry",
                    actualEntry instanceof PrivateKeyEntry);

            PrivateKeyEntry actual = (PrivateKeyEntry) actualEntry;

            assertPrivateKeyEntryEquals(actual, "RSA", FAKE_RSA_KEY_1, FAKE_RSA_USER_1,
                    FAKE_RSA_CA_1);
        }
    }

    public void testKeyStore_SetKeyEntry_ReplacedChain_Unencrypted_Success() throws Exception {
        mKeyStore.load(null, null);

        // Create key #1
        {
            KeyStore.PrivateKeyEntry privEntry = makeUserRsaKey1();
            mKeyStore.setEntry(TEST_ALIAS_1, privEntry, null);

            Entry entry = mKeyStore.getEntry(TEST_ALIAS_1, null);

            assertTrue(entry instanceof PrivateKeyEntry);

            PrivateKeyEntry keyEntry = (PrivateKeyEntry) entry;

            ArrayList<Certificate> chain = new ArrayList<Certificate>();
            chain.add(generateCertificate(FAKE_RSA_CA_1));
            assertPrivateKeyEntryEquals(keyEntry, privEntry.getPrivateKey(),
                    privEntry.getCertificate(), chain);
        }

        // Replace key #1 with new chain
        {
            Key key = mKeyStore.getKey(TEST_ALIAS_1, null);

            assertTrue(key instanceof PrivateKey);

            PrivateKey expectedKey = (PrivateKey) key;

            Certificate expectedCert = generateCertificate(FAKE_RSA_USER_1);

            mKeyStore.setKeyEntry(TEST_ALIAS_1, expectedKey, null,
                    new Certificate[] { expectedCert });

            Entry entry = mKeyStore.getEntry(TEST_ALIAS_1, null);

            assertTrue(entry instanceof PrivateKeyEntry);

            PrivateKeyEntry keyEntry = (PrivateKeyEntry) entry;

            assertPrivateKeyEntryEquals(keyEntry, expectedKey, expectedCert, null);
        }
    }

    public void testKeyStore_SetKeyEntry_ReplacedChain_DifferentPrivateKey_Unencrypted_Failure()
            throws Exception {
        mKeyStore.load(null, null);

        // Create key #1
        mKeyStore.setEntry(TEST_ALIAS_1, makeUserRsaKey1(), null);

        // Create key #2
        mKeyStore.setEntry(TEST_ALIAS_2, makeUserRsaKey1(), null);


        // Replace key #1 with key #2
        {
            Key key1 = mKeyStore.getKey(TEST_ALIAS_2, null);

            Certificate cert = generateCertificate(FAKE_RSA_USER_1);

            try {
                mKeyStore.setKeyEntry(TEST_ALIAS_1, key1, null, new Certificate[] { cert });
                fail("Should not allow setting of KeyEntry with wrong PrivaetKey");
            } catch (KeyStoreException success) {
            }
        }
    }

    public void testKeyStore_SetKeyEntry_ReplacedWithSame_UnencryptedToUnencrypted_Failure()
            throws Exception {
        mKeyStore.load(null, null);

        // Create key #1
        mKeyStore.setEntry(TEST_ALIAS_1, makeUserRsaKey1(), null);

        // Replace with same
        Entry entry = mKeyStore.getEntry(TEST_ALIAS_1, null);
        mKeyStore.setEntry(TEST_ALIAS_1, entry, null);
    }

    public void testKeyStore_Size_Unencrypted_Success() throws Exception {
        mKeyStore.load(null, null);

        mKeyStore.setCertificateEntry(TEST_ALIAS_1, generateCertificate(FAKE_RSA_CA_1));

        assertEquals("The keystore size should match expected", 1, mKeyStore.size());
        assertAliases(new String[] { TEST_ALIAS_1 });

        mKeyStore.setCertificateEntry(TEST_ALIAS_2, generateCertificate(FAKE_RSA_CA_1));

        assertEquals("The keystore size should match expected", 2, mKeyStore.size());
        assertAliases(new String[] { TEST_ALIAS_1, TEST_ALIAS_2 });

        mKeyStore.setEntry(TEST_ALIAS_3, makeUserRsaKey1(), null);

        assertEquals("The keystore size should match expected", 3, mKeyStore.size());
        assertAliases(new String[] { TEST_ALIAS_1, TEST_ALIAS_2, TEST_ALIAS_3 });

        mKeyStore.deleteEntry(TEST_ALIAS_1);

        assertEquals("The keystore size should match expected", 2, mKeyStore.size());
        assertAliases(new String[] { TEST_ALIAS_2, TEST_ALIAS_3 });

        mKeyStore.deleteEntry(TEST_ALIAS_3);

        assertEquals("The keystore size should match expected", 1, mKeyStore.size());
        assertAliases(new String[] { TEST_ALIAS_2 });
    }

    public void testKeyStore_Store_LoadStoreParam_Unencrypted_Failure() throws Exception {
        mKeyStore.load(null, null);

        try {
            mKeyStore.store(null);
            fail("Should throw UnsupportedOperationException when trying to store");
        } catch (UnsupportedOperationException success) {
        }
    }

    public void testKeyStore_Load_InputStreamSupplied_Unencrypted_Failure() throws Exception {
        byte[] buf = "FAKE KEYSTORE".getBytes();
        ByteArrayInputStream is = new ByteArrayInputStream(buf);

        try {
            mKeyStore.load(is, null);
            fail("Should throw IllegalArgumentException when InputStream is supplied");
        } catch (IllegalArgumentException success) {
        }
    }

    public void testKeyStore_Load_PasswordSupplied_Unencrypted_Failure() throws Exception {
        try {
            mKeyStore.load(null, "password".toCharArray());
            fail("Should throw IllegalArgumentException when password is supplied");
        } catch (IllegalArgumentException success) {
        }
    }

    public void testKeyStore_Store_OutputStream_Unencrypted_Failure() throws Exception {
        mKeyStore.load(null, null);

        OutputStream sink = new ByteArrayOutputStream();
        try {
            mKeyStore.store(sink, null);
            fail("Should throw UnsupportedOperationException when trying to store");
        } catch (UnsupportedOperationException success) {
        }

        try {
            mKeyStore.store(sink, "blah".toCharArray());
            fail("Should throw UnsupportedOperationException when trying to store");
        } catch (UnsupportedOperationException success) {
        }
    }

    public void testKeyStore_KeyOperations_Wrap_Unencrypted_Success() throws Exception {
        mKeyStore.load(null, null);

        mKeyStore.setEntry(TEST_ALIAS_1, makeUserRsaKey1(), null);

        // Test key usage
        Entry e = mKeyStore.getEntry(TEST_ALIAS_1, null);
        assertNotNull(e);
        assertTrue(e instanceof PrivateKeyEntry);

        PrivateKeyEntry privEntry = (PrivateKeyEntry) e;
        PrivateKey privKey = privEntry.getPrivateKey();
        assertNotNull(privKey);

        PublicKey pubKey = privEntry.getCertificate().getPublicKey();

        Cipher c = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        c.init(Cipher.WRAP_MODE, pubKey);

        byte[] expectedKey = new byte[] {
                0x00, 0x05, (byte) 0xAA, (byte) 0x0A5, (byte) 0xFF, 0x55, 0x0A
        };

        SecretKey expectedSecret = new SecretKeySpec(expectedKey, "AES");

        byte[] wrappedExpected = c.wrap(expectedSecret);

        c.init(Cipher.UNWRAP_MODE, privKey);
        SecretKey actualSecret = (SecretKey) c.unwrap(wrappedExpected, "AES", Cipher.SECRET_KEY);

        assertEquals(Arrays.toString(expectedSecret.getEncoded()),
                Arrays.toString(actualSecret.getEncoded()));
    }

    public void testKeyStore_Encrypting_RSA_NONE_NOPADDING() throws Exception {

        String alias = "MyKey";
        KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
        assertNotNull(ks);
        ks.load(null);

        Calendar cal = Calendar.getInstance();
        cal.set(1944, 5, 6);
        Date now = cal.getTime();
        cal.clear();

        cal.set(1945, 8, 2);
        Date end = cal.getTime();

        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
        assertNotNull(kpg);
        kpg.initialize(new KeyPairGeneratorSpec.Builder(mContext)
                .setAlias(alias)
                .setStartDate(now)
                .setEndDate(end)
                .setSerialNumber(BigInteger.valueOf(1))
                .setSubject(new X500Principal("CN=test1"))
                .build());

        kpg.generateKeyPair();

        RSAPrivateKey key = (RSAPrivateKey) ks.getKey(alias, null);
        assertNotNull(key);
        String cipher = key.getAlgorithm() + "/NONE/NOPADDING";
        Cipher encrypt = Cipher.getInstance(cipher);
        assertNotNull(encrypt);
        encrypt.init(Cipher.ENCRYPT_MODE, key);

        byte[] plainText = new byte[encrypt.getBlockSize()];
        Arrays.fill(plainText, (byte) 0xFF);

        // We expect a BadPaddingException here as the message size (plaintext)
        // is bigger than the modulus.
        try {
            encrypt.doFinal(plainText);
            fail("Expected BadPaddingException");
        } catch (BadPaddingException e) {
            // pass on exception as it is expected
        }
    }
}
