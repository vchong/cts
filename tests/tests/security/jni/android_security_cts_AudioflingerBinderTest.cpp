/*
 * Copyright (C) 2015 The Android Open Source Project
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

#define LOG_TAG "AudioFlingerBinderTest-JNI"

#include <jni.h>
#include <binder/IServiceManager.h>
#include <media/IAudioFlinger.h>
#include <media/AudioSystem.h>
#include <system/audio.h>
#include <utils/Log.h>
#include <utils/SystemClock.h>

using namespace android;

/*
 * Native methods used by
 * cts/tests/tests/security/src/android/security/cts/AudioFlingerBinderTest.java
 */


class MyDeathClient: public IBinder::DeathRecipient
{
public:
    MyDeathClient() :
        mAfIsDead(false) {
    }

    bool afIsDead() const { return mAfIsDead; }

    // DeathRecipient
    virtual void binderDied(const wp<IBinder>& who __unused) { mAfIsDead = true; }

private:
    bool mAfIsDead;
};


static bool connectAudioFlinger(sp<IAudioFlinger>& af, sp<MyDeathClient> &dr)
{
    int64_t startTime = 0;
    while (af == 0) {
        sp<IBinder> binder = defaultServiceManager()->checkService(String16("media.audio_flinger"));
        if (binder == 0) {
            if (startTime == 0) {
                startTime = uptimeMillis();
            } else if ((uptimeMillis()-startTime) > 10000) {
                ALOGE("timeout while getting audio flinger service");
                return false;
            }
            sleep(1);
        } else {
            af = interface_cast<IAudioFlinger>(binder);
            dr = new MyDeathClient();
            binder->linkToDeath(dr);
        }
    }
    return true;
}

/*
 * Checks that AudioSystem::setMasterMute() does not crash mediaserver if a duplicated output
 * is opened.
 */
jboolean android_security_cts_AudioFlinger_test_setMasterMute(JNIEnv* env __unused,
                                                           jobject thiz __unused)
{
    sp<IAudioFlinger> af;
    sp<MyDeathClient> dr;

    if (!connectAudioFlinger(af, dr)) {
        return false;
    }

    // force opening of a duplicating output
    status_t status = AudioSystem::setDeviceConnectionState(AUDIO_DEVICE_OUT_REMOTE_SUBMIX,
                                          AUDIO_POLICY_DEVICE_STATE_AVAILABLE,
                                          "0");
    if (status != NO_ERROR) {
        return false;
    }

    bool mute;
    status = AudioSystem::getMasterMute(&mute);
    if (status != NO_ERROR) {
        return false;
    }

    AudioSystem::setMasterMute(!mute);

    sleep(1);

    // Check that mediaserver did not crash
    if (dr->afIsDead()) {
        return false;
    }

    AudioSystem::setMasterMute(mute);

    AudioSystem::setDeviceConnectionState(AUDIO_DEVICE_OUT_REMOTE_SUBMIX,
                                          AUDIO_POLICY_DEVICE_STATE_UNAVAILABLE,
                                          "0");

    AudioSystem::setMasterMute(false);

    return true;
}

jboolean android_security_cts_AudioFlinger_test_setMasterVolume(JNIEnv* env __unused,
                                                           jobject thiz __unused)
{
    sp<IAudioFlinger> af;
    sp<MyDeathClient> dr;

    if (!connectAudioFlinger(af, dr)) {
        return false;
    }

    // force opening of a duplicating output
    status_t status = AudioSystem::setDeviceConnectionState(AUDIO_DEVICE_OUT_REMOTE_SUBMIX,
                                          AUDIO_POLICY_DEVICE_STATE_AVAILABLE,
                                          "0");
    if (status != NO_ERROR) {
        return false;
    }

    float vol;
    status = AudioSystem::getMasterVolume(&vol);
    if (status != NO_ERROR) {
        return false;
    }

    AudioSystem::setMasterVolume(vol < 0.5 ? 1.0 : 0.0);

    sleep(1);

    // Check that mediaserver did not crash
    if (dr->afIsDead()) {
        return false;
    }

    AudioSystem::setMasterMute(vol);

    AudioSystem::setDeviceConnectionState(AUDIO_DEVICE_OUT_REMOTE_SUBMIX,
                                          AUDIO_POLICY_DEVICE_STATE_UNAVAILABLE,
                                          "0");

    return true;
}


static JNINativeMethod gMethods[] = {
    {  "native_test_setMasterMute", "()Z",
            (void *) android_security_cts_AudioFlinger_test_setMasterMute },
    {  "native_test_setMasterVolume", "()Z",
            (void *) android_security_cts_AudioFlinger_test_setMasterVolume },
};

int register_android_security_cts_AudioFlingerBinderTest(JNIEnv* env)
{
    jclass clazz = env->FindClass("android/security/cts/AudioFlingerBinderTest");
    return env->RegisterNatives(clazz, gMethods,
            sizeof(gMethods) / sizeof(JNINativeMethod));
}
