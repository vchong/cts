/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_android_ndkaudio_AudioPlayer */

#ifndef _Included_com_android_ndkaudio_AudioPlayer
#define _Included_com_android_ndkaudio_AudioPlayer
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_android_ndkaudio_AudioPlayer
 * Method:    InitN
 * Signature: ()V
 */

/*
 * Class:     com_android_ndkaudio_AudioPlayer
 * Method:    Create
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_android_ndkaudio_AudioPlayer_Create
  (JNIEnv *, jobject);

/*
 * Class:     com_android_ndkaudio_AudioPlayer
 * Method:    Destroy
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_android_ndkaudio_AudioPlayer_Destroy
  (JNIEnv*, jobject);

/*
 * Class:     com_android_ndkaudio_AudioPlayer
 * Method:    RealizePlayer
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_android_ndkaudio_AudioPlayer_RealizePlayer
  (JNIEnv*, jobject);

/*
 * Class:     com_android_ndkaudio_AudioPlayer
 * Method:    RealizeRoutingProxy
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_android_ndkaudio_AudioPlayer_RealizeRoutingProxy
  (JNIEnv*, jobject);

/*
 * Class:     com_android_ndkaudio_AudioPlayer
 * Method:    Start
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_android_ndkaudio_AudioPlayer_Start
  (JNIEnv *, jobject);

/*
 * Class:     com_android_ndkaudio_AudioPlayer
 * Method:    Stop
 * Signature: ()android/
 */
JNIEXPORT void JNICALL Java_com_android_ndkaudio_AudioPlayer_Stop
  (JNIEnv *, jobject);

/*
 * Class:     com_android_ndkaudio_AudioPlayer
 * Method:    GetRoutingInterface
 * Signature: ()Landroid/media/AudioRouting;
 */
JNIEXPORT jobject JNICALL Java_com_android_ndkaudio_AudioPlayer_GetRoutingInterface
  (JNIEnv*, jobject);

/*
 * Class:     com_android_ndkaudio_AudioPlayer
 * Method:    GetRoutingInterface
 * Signature: (Landroid/media/AudioRouting;)V
 */
JNIEXPORT void JNICALL Java_com_android_ndkaudio_AudioPlayer_ReleaseRoutingInterface
  (JNIEnv*, jobject, jobject);

/*
 * Class:     com_android_ndkaudio_AudioPlayer
 * Method:    GetLastSLResult
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_com_android_ndkaudio_AudioPlayer_GetLastSLResult
  (JNIEnv*, jobject);

/*
 * Class:     com_android_ndkaudio_AudioPlayer
 * Method:    ClearLastSLResult
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_android_ndkaudio_AudioPlayer_ClearLastSLResult
  (JNIEnv*, jobject);

#ifdef __cplusplus
}
#endif
#endif
